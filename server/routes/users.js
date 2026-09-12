import express from 'express';
import bcrypt from 'bcrypt';

const router = express.Router();

// Get Profile
router.get('/profile', async (req, res) => {
  try {
    const userId = req.body.userId || 'test-user';
    const db = req.app.locals.db;
    
    const userDoc = await db.collection('users').doc(userId).get();
    
    if (!userDoc.exists) {
      return res.status(404).json({
        success: false,
        message: 'User not found'
      });
    }
    
    const user = userDoc.data();
    delete user.password;
    
    res.status(200).json({
      success: true,
      data: {
        id: userDoc.id,
        ...user
      }
    });
  } catch (error) {
    console.error('Get profile error:', error);
    res.status(500).json({
      success: false,
      message: 'Failed to get profile',
      error: error.message
    });
  }
});

// Update Profile
router.put('/profile', async (req, res) => {
  try {
    const userId = req.body.userId || 'test-user';
    const { firstName, lastName, phone, address, dateOfBirth } = req.body;
    const db = req.app.locals.db;
    
    await db.collection('users').doc(userId).update({
      firstName,
      lastName,
      phone,
      address,
      dateOfBirth,
      updatedAt: new Date().toISOString()
    });
    
    const updatedUser = await db.collection('users').doc(userId).get();
    const user = updatedUser.data();
    delete user.password;
    
    res.status(200).json({
      success: true,
      message: 'Profile updated successfully',
      data: {
        id: userId,
        ...user
      }
    });
  } catch (error) {
    console.error('Update profile error:', error);
    res.status(500).json({
      success: false,
      message: 'Failed to update profile',
      error: error.message
    });
  }
});

// Change Password
router.put('/change-password', async (req, res) => {
  try {
    const userId = req.body.userId || 'test-user';
    const { oldPassword, newPassword } = req.body;
    const db = req.app.locals.db;
    
    if (!oldPassword || !newPassword) {
      return res.status(400).json({
        success: false,
        message: 'Old password and new password are required'
      });
    }
    
    const userDoc = await db.collection('users').doc(userId).get();
    const user = userDoc.data();
    
    // Verify old password
    const passwordMatch = await bcrypt.compare(oldPassword, user.password);
    if (!passwordMatch) {
      return res.status(401).json({
        success: false,
        message: 'Old password is incorrect'
      });
    }
    
    // Hash new password
    const hashedPassword = await bcrypt.hash(newPassword, 10);
    
    await db.collection('users').doc(userId).update({
      password: hashedPassword,
      updatedAt: new Date().toISOString()
    });
    
    res.status(200).json({
      success: true,
      message: 'Password changed successfully'
    });
  } catch (error) {
    console.error('Change password error:', error);
    res.status(500).json({
      success: false,
      message: 'Failed to change password',
      error: error.message
    });
  }
});

// Set PIN
router.put('/pin', async (req, res) => {
  try {
    const userId = req.body.userId || 'test-user';
    const { pin, otp } = req.body;
    const db = req.app.locals.db;
    
    if (!pin || !otp) {
      return res.status(400).json({
        success: false,
        message: 'PIN and OTP are required'
      });
    }
    
    // TODO: Verify OTP
    
    const hashedPin = await bcrypt.hash(pin, 10);
    
    await db.collection('users').doc(userId).update({
      pin: hashedPin,
      isPinSet: true,
      updatedAt: new Date().toISOString()
    });
    
    res.status(200).json({
      success: true,
      message: 'PIN set successfully'
    });
  } catch (error) {
    console.error('Set PIN error:', error);
    res.status(500).json({
      success: false,
      message: 'Failed to set PIN',
      error: error.message
    });
  }
});

// Change PIN
router.put('/change-pin', async (req, res) => {
  try {
    const userId = req.body.userId || 'test-user';
    const { oldPin, newPin, otp } = req.body;
    const db = req.app.locals.db;
    
    if (!oldPin || !newPin || !otp) {
      return res.status(400).json({
        success: false,
        message: 'Old PIN, new PIN, and OTP are required'
      });
    }
    
    // TODO: Verify OTP
    
    const userDoc = await db.collection('users').doc(userId).get();
    const user = userDoc.data();
    
    // Verify old PIN
    const pinMatch = await bcrypt.compare(oldPin, user.pin || '');
    if (!pinMatch) {
      return res.status(401).json({
        success: false,
        message: 'Old PIN is incorrect'
      });
    }
    
    // Hash new PIN
    const hashedPin = await bcrypt.hash(newPin, 10);
    
    await db.collection('users').doc(userId).update({
      pin: hashedPin,
      updatedAt: new Date().toISOString()
    });
    
    res.status(200).json({
      success: true,
      message: 'PIN changed successfully'
    });
  } catch (error) {
    console.error('Change PIN error:', error);
    res.status(500).json({
      success: false,
      message: 'Failed to change PIN',
      error: error.message
    });
  }
});

export default router;