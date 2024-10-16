package com.example.asm_kot_ph45484

import android.app.Activity
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class Profile : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ProfileScreen()
        }
    }
}

@Composable
fun ProfileScreen() {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var ward by remember { mutableStateOf("") }
    var street by remember { mutableStateOf("") }
    var houseNumber by remember { mutableStateOf("") }

    var showDialog by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.White // Màu nền
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Nút Edit và Signout
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween, // Đặt khoảng cách đều giữa các nút
                verticalAlignment = Alignment.CenterVertically // Căn chỉnh theo chiều dọc
            ) {
                Text(
                    text = "Edit",
                    color = Color.Black,
                    fontSize = 20.sp, // Tăng kích thước chữ
                    modifier = Modifier
                        .clickable { showDialog = true } // Mở dialog khi nhấn Edit
                )
                TextButton(onClick = { logoutUser(context) }) {
                    Text(
                        text = "Signout",
                        color = Color.Red, fontSize = 20.sp
                    )

                }
            }

            Spacer(modifier = Modifier.height(16.dp)) // Khoảng cách

            // Ảnh đại diện
            Image(
                painter = painterResource(id = R.drawable.lo_go), // Thay thế bằng ID hình ảnh thực tế
                contentDescription = "Profile Picture",
                modifier = Modifier
                    .size(150.dp)
                    .clip(CircleShape) // Bo góc hình ảnh thành hình tròn
            )

            Spacer(modifier = Modifier.height(16.dp)) // Khoảng cách giữa ảnh và tên

            // Thông tin cá nhân với các khung
            ProfileInfo(label = "Họ Tên", value = name)
            ProfileInfo(label = "Số điện thoại", value = phone)
            ProfileInfo(label = "Phường", value = ward)
            ProfileInfo(label = "Đường", value = street)
            ProfileInfo(label = "Số nhà", value = houseNumber)

            // Hiển thị dialog nếu showDialog là true
            if (showDialog) {
                EditProfileDialog(
                    currentName = name,
                    currentPhone = phone,
                    currentWard = ward,
                    currentStreet = street,
                    currentHouseNumber = houseNumber,
                    onDismiss = { showDialog = false },
                    onSave = { newName, newPhone, newWard, newStreet, newHouseNumber ->
                        name = newName
                        phone = newPhone
                        ward = newWard
                        street = newStreet
                        houseNumber = newHouseNumber
                        showDialog = false
                    }
                )
            }
        }
    }
}

@Composable
fun ProfileInfo(label: String, value: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp) // Khoảng cách giữa các trường
            .background(Color.LightGray, shape = RoundedCornerShape(8.dp)) // Khung
            .padding(16.dp) // Padding bên trong khung
    ) {
        Text(text = label, color = Color.Gray) // Nhãn
        Text(text = value, color = Color.Black) // Giá trị
    }
}

@Composable
fun EditProfileDialog(
    currentName: String,
    currentPhone: String,
    currentWard: String,
    currentStreet: String,
    currentHouseNumber: String,
    onDismiss: () -> Unit,
    onSave: (String, String, String, String, String) -> Unit
) {
    var name by remember { mutableStateOf(TextFieldValue(currentName)) }
    var phone by remember { mutableStateOf(TextFieldValue(currentPhone)) }
    var ward by remember { mutableStateOf(TextFieldValue(currentWard)) }
    var street by remember { mutableStateOf(TextFieldValue(currentStreet)) }
    var houseNumber by remember { mutableStateOf(TextFieldValue(currentHouseNumber)) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Sửa hồ sơ") },
        confirmButton = {
            TextButton(onClick = {
                onSave(name.text, phone.text, ward.text, street.text, houseNumber.text)
            }) {
                Text("Lưu")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Hủy")
            }
        },
        text = {
            Column {
                TextField(value = name, onValueChange = { name = it }, label = { Text("Họ Tên") })
                TextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Số điện thoại") })
                TextField(value = ward, onValueChange = { ward = it }, label = { Text("Phường") })
                TextField(
                    value = street,
                    onValueChange = { street = it },
                    label = { Text("Đường") })
                TextField(
                    value = houseNumber,
                    onValueChange = { houseNumber = it },
                    label = { Text("Số nhà") })
            }
        }
    )
}

fun logoutUser(context: Context) {
    // Lấy SharedPreferences
    val sharedPref = context.getSharedPreferences("MY_APP_PREFS", MODE_PRIVATE)
    val editor = sharedPref.edit()

    // Xóa token khỏi SharedPreferences
    editor.remove("auth_token")
    RetrofitClient.token = null
    editor.apply()

    // Chuyển hướng về LoginScreen
    val intent = Intent(context, LoginScreen::class.java)
    intent.flags =
        Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK // Xóa stack activity trước đó
    context.startActivity(intent)

    // Kết thúc Activity hiện tại (nếu có)
    if (context is Activity) {
        context.finish()
    }
}


@Preview
@Composable
fun PreviewProfileScreen() {
    ProfileScreen()
}
