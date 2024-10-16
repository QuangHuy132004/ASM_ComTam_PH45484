package com.example.asm_kot_ph45484

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.asm_kot_ph45484.Model.AuthViewModel
import com.example.asm_kot_ph45484.ui.theme.ASM_KOT_PH45484Theme

class LoginScreen : ComponentActivity() {

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ASM_KOT_PH45484Theme {
                LoginScreenUI()
            }
        }
    }

    val String.toColor: Color
        get() = Color(android.graphics.Color.parseColor(this))

    @Preview(showBackground = true, showSystemUi = true)
    @Composable
    fun LoginScreenUI() {
        val username = remember { mutableStateOf("") }
        val password = remember { mutableStateOf("") }
        val context = LocalContext.current

        // Lắng nghe các thay đổi từ LiveData
        LaunchedEffect(authViewModel.loginResponse) {
            authViewModel.loginResponse.observeForever {
                it?.let {
                    // Xử lý khi đăng nhập thành công
                    Toast.makeText(context, "Đăng nhập thành công", Toast.LENGTH_SHORT).show()
                    // Chuyển đến màn hình chính
                    val intent = Intent(context, MainActivity::class.java)
                    context.startActivity(intent)
                }
            }
        }

        // Lắng nghe lỗi
        LaunchedEffect(authViewModel.errorMessage) {
            authViewModel.errorMessage.observeForever { errorMessage ->
                errorMessage?.let {
                    Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = "#F6F6F6".toColor),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.lo_go),
                contentDescription = "Logo",
                modifier = Modifier
                    .size(190.dp)
                    .padding(bottom = 16.dp),
                contentScale = ContentScale.Fit
            )

            Text(
                text = "Login",
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(start = 20.dp, bottom = 8.dp),
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
            )

            TextField2(
                title = "UserName",
                placeholder = "Enter your email",
                value = username.value,
                onValueChange = { username.value = it }
            )

            TextField2(
                title = "Password",
                placeholder = "Enter your password",
                value = password.value,
                onValueChange = { password.value = it },
                isPassword = true
            )

            Button(
                onClick = {
                    authViewModel.login(username.value, password.value)
                },
                modifier = Modifier
                    .padding(16.dp)
                    .height(45.dp)
                    .width(140.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = "#FE724C".toColor,
                    contentColor = "#FFFFFF".toColor
                )
            ) {
                Text(text = "LOGIN")
            }
            Row(
                modifier = Modifier.padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Don't have an account? ",
                    color = Color.Black,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal
                )
                TextButton(
                    onClick = {
                        val intent = Intent(context, RegisterScreen::class.java)
                        context.startActivity(intent)
                    }
                ) {
                    Text(
                        text = "Sign Up",
                        color = "#FE724C".toColor,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun TextField2(
    title: String,
    placeholder: String,
    value: String,
    onValueChange: (String) -> Unit,
    trailingIcon: @Composable (() -> Unit)? = null, // Thêm tham số trailingIcon

    isPassword: Boolean = false
) {
    Column(modifier = Modifier.padding(20.dp)) {
        Text(text = title, modifier = Modifier.padding(bottom = 8.dp))
        OutlinedTextField(
            value = value,
            modifier = Modifier.width(400.dp),
            onValueChange = onValueChange,
            placeholder = { Text(placeholder) },
            trailingIcon = trailingIcon, // Gán biểu tượng vào TextField

            visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None
        )
    }
}
