export const validateEmail = (email) => {
  const re = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  return re.test(email);
};

export const validatePassword = (password) => {
  // At least 8 characters, 1 uppercase, 1 lowercase, 1 digit
  const re = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d).{8,}$/;
  return re.test(password);
};

export const validatePin = (pin) => {
  // 4-6 digits
  const re = /^\d{4,6}$/;
  return re.test(pin);
};

export const validatePhone = (phone) => {
  // Vietnamese phone format
  const re = /^0\d{9}$/;
  return re.test(phone);
};

export const validateAmount = (amount) => {
  const num = parseFloat(amount);
  return !isNaN(num) && num > 0;
};

export default {
  validateEmail,
  validatePassword,
  validatePin,
  validatePhone,
  validateAmount
};