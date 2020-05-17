package com.dsantano.damkeepapp.ui.auth

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.dsantano.damkeepapp.R
import com.dsantano.damkeepapp.api.ApiService
import com.dsantano.damkeepapp.api.generators.ServiceGeneratorPostLoginAndRegister
import com.dsantano.damkeepapp.api.response.login.LoginResponse
import com.dsantano.damkeepapp.api.response.login.SendToLogin
import com.dsantano.damkeepapp.api.response.register.RegisterResponse
import com.dsantano.damkeepapp.api.response.register.SendToRegister
import com.dsantano.damkeepapp.common.Constants
import com.dsantano.damkeepapp.common.MyApp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val edUsername: EditText = findViewById(R.id.editTextUsernameRegister)
        val edUFullName: EditText = findViewById(R.id.editTextFullNameRegister)
        val edPassword: EditText = findViewById(R.id.editTextPasswordRegiser)
        val edPasswordConfirm: EditText = findViewById(R.id.editTextPasswordSecondRegister)
        val btnRegister: Button = findViewById(R.id.buttonDoRegister)

        val serviceGenerator: ServiceGeneratorPostLoginAndRegister = ServiceGeneratorPostLoginAndRegister()
        val apiService: ApiService = serviceGenerator.getLoginAndRegisterService()

        btnRegister.setOnClickListener { v ->
            val username: String = edUsername.text.toString()
            val password: String = edPassword.text.toString()
            val fullName: String = edUFullName.text.toString()
            val passwordConfirm: String = edPasswordConfirm.text.toString()
            val sendToRegister: SendToRegister
            if(username.isEmpty() || password.isEmpty() || fullName.isEmpty() || passwordConfirm.isEmpty()){
                if (username.isEmpty()) {
                    edUsername.error = "Username Empty"
                }
                if (password.isEmpty()) {
                    edPassword.error = "Password Empty"
                }
                if (fullName.isEmpty()) {
                    edPassword.error = "Full Name Empty"
                }
                if (passwordConfirm.isEmpty()) {
                    edPassword.error = "Password Empty"
                }
            } else {
                sendToRegister = SendToRegister(username, fullName, password, passwordConfirm)
                val call: Call<RegisterResponse> = apiService.postRegister(sendToRegister)
                call.enqueue(object: Callback<RegisterResponse> {
                    override fun onResponse(
                        call: Call<RegisterResponse>,
                        response: Response<RegisterResponse>
                    ) {
                        if(response.isSuccessful) {
                            Toast.makeText(MyApp.instance, "Register succesfull now you can login", Toast.LENGTH_LONG).show()
                            val intent = Intent(MyApp.instance, LoginActivity::class.java)
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            ContextCompat.startActivity(MyApp.instance, intent, null)
                            finish()
                        } else {
                            Toast.makeText(MyApp.instance, "Error at Register", Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                        Toast.makeText(MyApp.instance, "Error at Register", Toast.LENGTH_LONG).show()
                    }
                })
            }
        }
    }
}
