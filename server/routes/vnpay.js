import express from 'express';
import crypto from 'crypto';

const router = express.Router();

// Create VNPay Payment
router.post('/create-payment', async (req, res) => {
  try {
    const { amount } = req.body;
    const db = req.app.locals.db;
    
    if (!amount || amount <= 0) {
      return res.status(400).json({
        success: false,
        message: 'Invalid amount'
      });
    }
    
    const tmnCode = process.env.VNPAY_TMN_CODE;
    const secretKey = process.env.VNPAY_SECRET_KEY;
    const vnpUrl = process.env.VNPAY_API_URL;
    const returnUrl = process.env.VNPAY_RETURN_URL;
    
    const date = new Date();
    const createDate = date.getFullYear() +
      String(date.getMonth() + 1).padStart(2, '0') +
      String(date.getDate()).padStart(2, '0') +
      String(date.getHours()).padStart(2, '0') +
      String(date.getMinutes()).padStart(2, '0') +
      String(date.getSeconds()).padStart(2, '0');
    
    const orderId = `ORD-${Date.now()}`;
    
    let vnp_Params = {};
    vnp_Params['vnp_Version'] = '2.1.0';
    vnp_Params['vnp_Command'] = 'pay';
    vnp_Params['vnp_TmnCode'] = tmnCode;
    vnp_Params['vnp_Locale'] = 'vn';
    vnp_Params['vnp_CurrCode'] = 'VND';
    vnp_Params['vnp_TxnRef'] = orderId;
    vnp_Params['vnp_OrderInfo'] = 'Nap tien vi dien tu';
    vnp_Params['vnp_OrderType'] = 'other';
    vnp_Params['vnp_Amount'] = amount * 100;
    vnp_Params['vnp_ReturnUrl'] = returnUrl;
    vnp_Params['vnp_IpAddr'] = '127.0.0.1';
    vnp_Params['vnp_CreateDate'] = createDate;
    
    // Sort parameters
    vnp_Params = Object.keys(vnp_Params)
      .sort()
      .reduce((result, key) => {
        result[key] = vnp_Params[key];
        return result;
      }, {});
    
    // Create HMAC
    let query = new URLSearchParams(vnp_Params).toString();
    let hmac = crypto
      .createHmac('sha512', secretKey)
      .update(query)
      .digest('hex');
    
    const paymentUrl = `${vnpUrl}/paygate?${query}&vnp_SecureHash=${hmac}`;
    
    res.status(200).json({
      success: true,
      message: 'Payment link created',
      data: {
        paymentUrl,
        transactionId: orderId
      }
    });
  } catch (error) {
    console.error('Create payment error:', error);
    res.status(500).json({
      success: false,
      message: 'Failed to create payment',
      error: error.message
    });
  }
});

// VNPay Callback
router.get('/callback', async (req, res) => {
  try {
    const db = req.app.locals.db;
    const vnp_Params = req.query;
    const secureHash = vnp_Params['vnp_SecureHash'];
    
    delete vnp_Params['vnp_SecureHash'];
    delete vnp_Params['vnp_SecureHashType'];
    
    const secretKey = process.env.VNPAY_SECRET_KEY;
    
    // Sort and create HMAC
    let sortedParams = Object.keys(vnp_Params)
      .sort()
      .reduce((result, key) => {
        result[key] = vnp_Params[key];
        return result;
      }, {});
    
    let query = new URLSearchParams(sortedParams).toString();
    let hmac = crypto
      .createHmac('sha512', secretKey)
      .update(query)
      .digest('hex');
    
    if (secureHash === hmac && vnp_Params['vnp_ResponseCode'] === '00') {
      // Payment successful
      const transactionId = vnp_Params['vnp_TxnRef'];
      const amount = vnp_Params['vnp_Amount'] / 100;
      const userId = req.body.userId || 'test-user';
      
      // Create topup transaction
      const transaction = {
        fromUserId: userId,
        toUserId: null,
        amount,
        type: 'topup',
        status: 'completed',
        description: 'Top up via VNPay',
        referenceNo: transactionId,
        createdAt: new Date().toISOString(),
        updatedAt: new Date().toISOString()
      };
      
      await db.collection('transactions').add(transaction);
      
      // Update wallet balance
      const walletSnapshot = await db.collection('wallets')
        .where('userId', '==', userId)
        .get();
      
      if (!walletSnapshot.empty) {
        const wallet = walletSnapshot.docs[0].data();
        await db.collection('wallets').doc(walletSnapshot.docs[0].id).update({
          balance: wallet.balance + amount,
          updatedAt: new Date().toISOString()
        });
      }
      
      res.status(200).json({
        success: true,
        message: 'Payment completed successfully'
      });
    } else {
      res.status(400).json({
        success: false,
        message: 'Payment verification failed'
      });
    }
  } catch (error) {
    console.error('VNPay callback error:', error);
    res.status(500).json({
      success: false,
      message: 'Callback processing failed',
      error: error.message
    });
  }
});

export default router;