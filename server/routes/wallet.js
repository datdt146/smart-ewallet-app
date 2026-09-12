import express from 'express';

const router = express.Router();

// Get Balance
router.get('/balance', async (req, res) => {
  try {
    const userId = req.body.userId || 'test-user';
    const db = req.app.locals.db;
    
    const walletSnapshot = await db.collection('wallets')
      .where('userId', '==', userId)
      .get();
    
    if (walletSnapshot.empty) {
      return res.status(404).json({
        success: false,
        message: 'Wallet not found'
      });
    }
    
    const wallet = walletSnapshot.docs[0].data();
    
    res.status(200).json({
      success: true,
      data: {
        id: walletSnapshot.docs[0].id,
        ...wallet
      }
    });
  } catch (error) {
    console.error('Get balance error:', error);
    res.status(500).json({
      success: false,
      message: 'Failed to get balance',
      error: error.message
    });
  }
});

// Transfer Money
router.post('/transfer', async (req, res) => {
  try {
    const fromUserId = req.body.userId || 'test-user';
    const { toEmail, amount, description, pin, otp } = req.body;
    const db = req.app.locals.db;
    
    if (!toEmail || !amount || !pin || !otp) {
      return res.status(400).json({
        success: false,
        message: 'Missing required fields'
      });
    }
    
    // TODO: Verify PIN and OTP
    
    // Find recipient
    const recipientSnapshot = await db.collection('users')
      .where('email', '==', toEmail)
      .get();
    
    if (recipientSnapshot.empty) {
      return res.status(404).json({
        success: false,
        message: 'Recipient not found'
      });
    }
    
    const toUserId = recipientSnapshot.docs[0].id;
    
    // Get sender's wallet
    const senderWalletSnapshot = await db.collection('wallets')
      .where('userId', '==', fromUserId)
      .get();
    
    if (senderWalletSnapshot.empty) {
      return res.status(400).json({
        success: false,
        message: 'Sender wallet not found'
      });
    }
    
    const senderWallet = senderWalletSnapshot.docs[0].data();
    
    if (senderWallet.balance < amount) {
      return res.status(400).json({
        success: false,
        message: 'Insufficient balance'
      });
    }
    
    // Get recipient's wallet
    const recipientWalletSnapshot = await db.collection('wallets')
      .where('userId', '==', toUserId)
      .get();
    
    if (recipientWalletSnapshot.empty) {
      return res.status(400).json({
        success: false,
        message: 'Recipient wallet not found'
      });
    }
    
    const recipientWallet = recipientWalletSnapshot.docs[0].data();
    
    // Create transaction
    const transaction = {
      fromUserId,
      toUserId,
      amount,
      type: 'transfer',
      status: 'completed',
      description,
      referenceNo: `TRF-${Date.now()}`,
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString()
    };
    
    const transactionRef = await db.collection('transactions').add(transaction);
    
    // Update wallets
    await db.collection('wallets').doc(senderWalletSnapshot.docs[0].id).update({
      balance: senderWallet.balance - amount,
      updatedAt: new Date().toISOString()
    });
    
    await db.collection('wallets').doc(recipientWalletSnapshot.docs[0].id).update({
      balance: recipientWallet.balance + amount,
      updatedAt: new Date().toISOString()
    });
    
    res.status(201).json({
      success: true,
      message: 'Transfer successful',
      data: {
        id: transactionRef.id,
        ...transaction
      }
    });
  } catch (error) {
    console.error('Transfer error:', error);
    res.status(500).json({
      success: false,
      message: 'Transfer failed',
      error: error.message
    });
  }
});

export default router;