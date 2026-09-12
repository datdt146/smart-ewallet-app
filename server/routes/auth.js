import express from 'express';
import bcrypt from 'bcrypt';
import { validateEmail, validatePassword } from '../utils/validators.js';
import { generateToken, generateOtpSecret, generateBackupCodes } from '../utils/totp.js';
import QRCode from 'qrcode';

const router = express.Router();

// Register
router.post('/register', async (req, res) => {
  try {
    const { email, password, firstName, lastName, phone } = req.body;
    const db = req.app.locals.db;
    
    // Validation
    if (!email || !password || !firstName || !lastName || !phone) {
      return res.status(400).json({
        success: false,
        message: 'Missing required fields'
      });
    }
    
    if (!validateEmail(email)) {
      return res.status(400).json({
        success: false,
        message: 'Invalid email format'
      });
    }
    
    if (!validatePassword(password)) {
      return res.status(400).json({
        success: false,
        message: 'Password must be at least 8 characters with uppercase, lowercase and digit'
      });
    }
    
    // Check if user exists
    const userSnapshot = await db.collection('users').where('email', '==', email).get();
    if (!userSnapshot.empty) {
      return res.status(409).json({
        success: false,
        message: 'User already exists'
      });
    }
    
    // Hash password
    const hashedPassword = await bcrypt.hash(password, 10);
    
    // Create user
    const newUser = {
      email,
      password: hashedPassword,
      firstName,
      lastName,
      phone,
      isOtpEnabled: false,
      isPinSet: false,
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString()
    };
    
    const userRef = await db.collection('users').add(newUser);
    
    // Create wallet
    await db.collection('wallets').add({
      userId: userRef.id,
      balance: 0,
      currency: 'VND',
      isActive: true,
      createdAt: new Date().toISOString(),
      updatedAt: new Date().toISOString()
    });
    
    res.status(201).json({
      success: true,
      message: 'User registered successfully',
      user: {
        id: userRef.id,
        email,
        firstName,
        lastName,
        phone
      }
    });
  } catch (error) {
    console.error('Register error:', error);
    res.status(500).json({
      success: false,
      message: 'Registration failed',
      error: error.message
    });
  }
});

// Login
router.post('/login', async (req, res) => {
  try {
    const { email, password } = req.body;
    const db = req.app.locals.db;
    
    if (!email || !password) {
      return res.status(400).json({
        success: false,
        message: 'Email and password are required'
      });
    }
    
    // Find user
    const userSnapshot = await db.collection('users').where('email', '==', email).get();
    
    if (userSnapshot.empty) {
      return res.status(401).json({
        success: false,
        message: 'Invalid credentials'
      });
    }
    
    const userDoc = userSnapshot.docs[0];
    const user = userDoc.data();
    
    // Verify password
    const passwordMatch = await bcrypt.compare(password, user.password);
    
    if (!passwordMatch) {
      return res.status(401).json({
        success: false,
        message: 'Invalid credentials'
      });
    }
    
    // Generate token
    const token = generateToken(userDoc.id, email);
    
    // Check if OTP is enabled
    if (user.isOtpEnabled) {
      return res.status(200).json({
        success: true,
        message: 'Please verify OTP',
        token: token,
        requiresOtp: true
      });
    }
    
    res.status(200).json({
      success: true,
      message: 'Login successful',
      token: token,
      user: {
        id: userDoc.id,
        email: user.email,
        firstName: user.firstName,
        lastName: user.lastName,
        phone: user.phone
      },
      requiresOtp: false
    });
  } catch (error) {
    console.error('Login error:', error);
    res.status(500).json({
      success: false,
      message: 'Login failed',
      error: error.message
    });
  }
});

// Setup OTP
router.post('/setup-otp', async (req, res) => {
  try {
    // TODO: Get user from JWT token
    const userId = req.body.userId || 'test-user';
    
    // Generate OTP secret
    const secret = generateOtpSecret();
    const backupCodes = generateBackupCodes();
    
    // Generate QR code
    const otpauthUrl = `otpauth://totp/SmartEWallet:${userId}?secret=${secret}&issuer=SmartEWallet`;
    const qrCode = await QRCode.toDataURL(otpauthUrl);
    
    res.status(200).json({
      success: true,
      message: 'OTP setup initiated',
      otpSecret: {
        secret: secret,
        qrCode: qrCode,
        backupCodes: backupCodes
      }
    });
  } catch (error) {
    console.error('Setup OTP error:', error);
    res.status(500).json({
      success: false,
      message: 'OTP setup failed',
      error: error.message
    });
  }
});

// Verify OTP
router.post('/verify-otp', async (req, res) => {
  try {
    const { code, token } = req.body;
    
    if (!code || !token) {
      return res.status(400).json({
        success: false,
        message: 'Code and token are required'
      });
    }
    
    // TODO: Verify TOTP code
    // For now, accept any 6-digit code
    if (code.length === 6 && !isNaN(code)) {
      res.status(200).json({
        success: true,
        message: 'OTP verified successfully',
        token: token
      });
    } else {
      res.status(401).json({
        success: false,
        message: 'Invalid OTP code'
      });
    }
  } catch (error) {
    console.error('Verify OTP error:', error);
    res.status(500).json({
      success: false,
      message: 'OTP verification failed',
      error: error.message
    });
  }
});

// Refresh Token
router.post('/refresh-token', (req, res) => {
  try {
    const token = req.headers.authorization?.split(' ')[1];
    
    if (!token) {
      return res.status(401).json({
        success: false,
        message: 'No token provided'
      });
    }
    
    // TODO: Verify and refresh token
    res.status(200).json({
      success: true,
      message: 'Token refreshed',
      token: token
    });
  } catch (error) {
    console.error('Refresh token error:', error);
    res.status(500).json({
      success: false,
      message: 'Token refresh failed',
      error: error.message
    });
  }
});

export default router;