# Smart E-Wallet Application

Ứng dụng ví điện tử thông minh với xác thực OTP (Smart OTP - TOTP), nạp tiền qua VNPay, chuyển tiền và quản lý giao dịch.

## 🏗️ Kiến trúc Hệ Thống

```
┌─────────────────────────┐
│   Android App           │
│   (Kotlin)              │
└────────────┬────────────┘
             │ REST API (HTTP/HTTPS)
             ↓
┌─────────────────────────┐
│   Backend Server        │
│   Express.js + Node.js  │
└────────────┬────────────┘
             │
             ↓
┌─────────────────────────┐
│   Firebase Firestore    │
│   (Database + Auth)     │
└─────────────────────────┘
             
             ↓
┌─────────────────────────┐
│   VNPay Payment         │
│   Gateway               │
└─────────────────────────┘
```

## 📱 Công Nghệ Sử Dụng

### Frontend (Mobile)
- **Android Studio** - IDE
- **Kotlin** - Ngôn ngữ lập trình
- **Retrofit** - REST API client
- **OkHttp** - HTTP client
- **TOTP** - Smart OTP authentication
- **SharedPreferences** - Local storage

### Backend
- **Node.js** - Runtime
- **Express.js** - Web framework
- **Firebase Admin SDK** - Database & Auth
- **Firebase Firestore** - NoSQL Database
- **JWT** - Token authentication
- **bcrypt** - Password encryption
- **TOTP** - OTP generation

### External Services
- **Firebase Firestore** - Cloud Database
- **VNPay** - Payment Gateway

## ✨ Tính Năng Chính

### Người Dùng (User)
- ✅ Đăng ký tài khoản
- ✅ Đăng nhập với mật khẩu
- ✅ Xác thực 2 lớp (2FA) với Smart OTP (TOTP)
- ✅ Cập nhật thông tin cá nhân
- ✅ Thay đổi mật khẩu
- ✅ Thiết lập mã PIN
- ✅ Thay đổi mã PIN
- ✅ Nạp tiền (VNPay)
- ✅ Chuyển tiền cho người dùng khác
- ✅ Xem lịch sử giao dịch
- ✅ Xem số dư ví
- ✅ Quên mật khẩu (Recovery)

### Người Quản Trị (Admin)
- ✅ Quản lý người dùng
- ✅ Quản lý dịch vụ
- ✅ Xem thống kê giao dịch
- ✅ Khóa/Mở tài khoản người dùng

## 📁 Cấu Trúc Thư Mục

```
smart-ewallet-app/
├── server/                          # Backend Express.js
│   ├── config/
│   │   └── firebase.js             # Firebase config
│   ├── controllers/
│   │   ├── auth.js                 # Auth logic
│   │   ├── users.js                # User management
│   │   ├── wallet.js               # Wallet operations
│   │   ├── transactions.js         # Transactions
│   │   ├── otp.js                  # OTP TOTP logic
│   │   └── vnpay.js                # VNPay integration
│   ├── middleware/
│   │   ├── auth.js                 # JWT verification
│   │   └── errorHandler.js         # Error handling
│   ├── routes/
│   │   ├── auth.js
│   │   ├── users.js
│   │   ├── wallet.js
│   │   ├── transactions.js
│   │   └── vnpay.js
│   ├── utils/
│   │   ├── validators.js           # Input validation
│   │   ├── totp.js                 # TOTP utilities
│   │   └── helpers.js              # Helper functions
│   ├── .env                        # Environment variables
│   ├── .env.example                # Example env
│   ├── package.json
│   └── index.js                    # Entry point
│
└── android/                         # Android App (Kotlin)
    ├── app/src/main/
    │   ├── java/com/ewallet/
    │   │   ├── activities/
    │   │   │   ├── LoginActivity.kt
    │   │   │   ├── RegisterActivity.kt
    │   │   │   ├── OtpSetupActivity.kt
    │   │   │   ├── OtpVerifyActivity.kt
    │   │   │   ├── DashboardActivity.kt
    │   │   │   ├── TransferActivity.kt
    │   │   │   ├── TopupActivity.kt
    │   │   │   ├── TransactionHistoryActivity.kt
    │   │   │   ├── ProfileActivity.kt
    │   │   │   └── SettingsActivity.kt
    │   │   ├── fragments/
    │   │   ├── models/
    │   │   │   ├── User.kt
    │   │   │   ├── Wallet.kt
    │   │   │   ├── Transaction.kt
    │   │   │   └── OtpSecret.kt
    │   │   ├── api/
    │   │   │   ├── ApiClient.kt
    │   │   │   └── ApiService.kt
    │   │   ├── repositories/
    │   │   │   ├── AuthRepository.kt
    │   │   │   ├── WalletRepository.kt
    │   │   │   └── TransactionRepository.kt
    │   │   ├── viewmodels/
    │   │   ├── utils/
    │   │   │   ├── TotpManager.kt
    │   │   │   ├── TokenManager.kt
    │   │   │   └── ValidationUtils.kt
    │   │   └── App.kt
    │   └── res/
    │       ├── layout/
    │       ├── values/
    │       └── drawable/
    └── build.gradle
```

## 🚀 Hướng Dẫn Cài Đặt

### Backend Setup

```bash
cd server
npm install
cp .env.example .env
# Cấu hình Firebase credentials trong .env
npm start
```

### Android Setup

1. Mở Android Studio
2. Mở folder `android/`
3. Cấu hình API base URL trong `ApiClient.kt`
4. Build & Run trên emulator/device

## 📝 API Endpoints

### Authentication
- `POST /auth/register` - Đăng ký
- `POST /auth/login` - Đăng nhập
- `POST /auth/verify-otp` - Xác thực OTP
- `POST /auth/setup-otp` - Thiết lập OTP
- `POST /auth/refresh-token` - Làm mới token

### User Management
- `GET /users/profile` - Lấy thông tin cá nhân
- `PUT /users/profile` - Cập nhật thông tin
- `PUT /users/change-password` - Thay đổi mật khẩu
- `PUT /users/pin` - Thiết lập/thay đổi PIN

### Wallet Operations
- `GET /wallet/balance` - Xem số dư
- `POST /wallet/transfer` - Chuyển tiền

### Transactions
- `GET /transactions/history` - Lịch sử giao dịch
- `GET /transactions/:id` - Chi tiết giao dịch

### VNPay
- `POST /vnpay/create-payment` - Tạo link thanh toán
- `GET /vnpay/callback` - Xử lý callback từ VNPay

## 🔐 Bảo Mật

- ✅ JWT Token authentication
- ✅ Password hashing (bcrypt)
- ✅ Smart OTP (TOTP) 2FA
- ✅ PIN protection cho giao dịch
- ✅ HTTPS/SSL encryption
- ✅ Input validation & sanitization
- ✅ Rate limiting
- ✅ CORS protection

## 📦 Dependencies

### Backend
```json
{
  "express": "^4.18.2",
  "firebase-admin": "^11.11.0",
  "firebase": "^10.6.0",
  "bcrypt": "^5.1.0",
  "jsonwebtoken": "^9.0.0",
  "dotenv": "^16.0.3",
  "body-parser": "^1.20.2",
  "cors": "^2.8.5"
}
```

### Android
```gradle
implementation 'com.squareup.retrofit2:retrofit:2.9.0'
implementation 'com.squareup.retrofit2:converter-gson:2.9.0'
implementation 'com.google.firebase:firebase-auth:21.3.0'
implementation 'com.google.firebase:firebase-firestore:24.8.1'
implementation 'androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.1'
implementation 'dev.turingcomplete:kotlin-otp:2.4.0'
```

## 🔄 Quy Trình Giao Dịch

### 1. Đăng Ký & Đăng Nhập
```
User Input → Validate → Hash Password → Save Firebase → Return Token
```

### 2. Thiết Lập OTP
```
Generate Secret Key → Display QR Code → User Scan → Verify Code → Save Secret
```

### 3. Đăng Nhập với OTP
```
Enter Username/Password → Verify → Request OTP Code → Verify TOTP → Issue JWT Token
```

### 4. Nạp Tiền
```
Select Amount → Create VNPay Link → Redirect to VNPay → Callback → Update Balance
```

### 5. Chuyển Tiền
```
Select Recipient → Enter Amount → Verify PIN → Verify OTP → Deduct & Add Balance → Log Transaction
```

## 🧪 Testing

- Unit tests cho backend logic
- Integration tests cho API endpoints
- UI tests cho Android app

## 📄 Liên Hệ & Support

Nếu có vấn đề, vui lòng tạo issue trên GitHub.

---

**Tác giả:** Đạt Trần  
**Năm:** 2024  
**License:** MIT
