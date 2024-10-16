package com.example.asm_kot_ph45484

data class RegisterResponse(
    val userId: String,     // ID của người dùng sau khi đăng ký thành công
    val message: String     // Thông báo kết quả đăng ký
)
data class LoginResponse(
    val userId: String,     // ID của người dùng
    val username: String,   // Tên đăng nhập
    val token: String,      // Token xác thực (JWT)
    val message: String     // Thông báo kết quả đăng nhập
)

