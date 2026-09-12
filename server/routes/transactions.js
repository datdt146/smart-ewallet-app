import express from 'express';

const router = express.Router();

// Get Transaction History
router.get('/history', async (req, res) => {
  try {
    const userId = req.body.userId || 'test-user';
    const page = parseInt(req.query.page) || 1;
    const limit = parseInt(req.query.limit) || 20;
    const skip = (page - 1) * limit;
    const db = req.app.locals.db;
    
    const transactionSnapshot = await db.collection('transactions')
      .where('fromUserId', '==', userId)
      .orderBy('createdAt', 'desc')
      .limit(limit)
      .get();
    
    const transactions = transactionSnapshot.docs.map(doc => ({
      id: doc.id,
      ...doc.data()
    }));
    
    res.status(200).json({
      success: true,
      data: transactions,
      pagination: {
        page,
        limit,
        total: transactionSnapshot.size
      }
    });
  } catch (error) {
    console.error('Get transaction history error:', error);
    res.status(500).json({
      success: false,
      message: 'Failed to get transaction history',
      error: error.message
    });
  }
});

// Get Single Transaction
router.get('/:id', async (req, res) => {
  try {
    const transactionId = req.params.id;
    const db = req.app.locals.db;
    
    const transactionDoc = await db.collection('transactions').doc(transactionId).get();
    
    if (!transactionDoc.exists) {
      return res.status(404).json({
        success: false,
        message: 'Transaction not found'
      });
    }
    
    res.status(200).json({
      success: true,
      data: {
        id: transactionDoc.id,
        ...transactionDoc.data()
      }
    });
  } catch (error) {
    console.error('Get transaction error:', error);
    res.status(500).json({
      success: false,
      message: 'Failed to get transaction',
      error: error.message
    });
  }
});

export default router;