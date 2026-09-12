import jwt from 'jsonwebtoken';

export const generateToken = (userId, email) => {
  return jwt.sign(
    { userId, email },
    process.env.JWT_SECRET,
    { expiresIn: process.env.JWT_EXPIRE || '7d' }
  );
};

export const verifyToken = (token) => {
  try {
    return jwt.verify(token, process.env.JWT_SECRET);
  } catch (error) {
    return null;
  }
};

export const generateOtpSecret = () => {
  // Generate random base32 encoded secret
  const chars = 'ABCDEFGHIJKLMNOPQRSTUVWXYZ234567';
  let secret = '';
  for (let i = 0; i < 32; i++) {
    secret += chars[Math.floor(Math.random() * chars.length)];
  }
  return secret;
};

export const generateBackupCodes = (count = 10) => {
  const codes = [];
  for (let i = 0; i < count; i++) {
    let code = '';
    for (let j = 0; j < 8; j++) {
      code += Math.floor(Math.random() * 10);
    }
    codes.push(code);
  }
  return codes;
};

export const hashPin = async (pin, bcrypt) => {
  return await bcrypt.hash(pin, 10);
};

export const verifyPin = async (pin, hashedPin, bcrypt) => {
  return await bcrypt.compare(pin, hashedPin);
};

export default {
  generateToken,
  verifyToken,
  generateOtpSecret,
  generateBackupCodes,
  hashPin,
  verifyPin
};