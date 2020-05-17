package com.dsantano.damkeepapp.ui.auth

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.dsantano.damkeepapp.MainActivity
import com.dsantano.damkeepapp.R
import com.dsantano.damkeepapp.api.ApiService
import com.dsantano.damkeepapp.api.generators.ServiceGeneratorPostLoginAndRegister
import com.dsantano.damkeepapp.api.response.login.LoginResponse
import com.dsantano.damkeepapp.api.response.login.SendToLogin
import com.dsantano.damkeepapp.common.Constants
import com.dsantano.damkeepapp.common.MyApp
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val edUsername: EditText = findViewById(R.id.editTextUsernameLogin)
        val edPassword: EditText = findViewById(R.id.editTextPasswordLogin)
        val btnLogin: Button = findViewById(R.id.buttonLogin)
        val btnRegister: Button = findViewById(R.id.buttonRegister)

        val serviceGenerator: ServiceGeneratorPostLoginAndRegister = ServiceGeneratorPostLoginAndRegister()
        val apiService: ApiService = serviceGenerator.getLoginAndRegisterService()

        btnLogin.setOnClickListener { v ->
            val username: String = edUsername.text.toString()
            val password: String = edPassword.text.toString()
            val sendToLogin: SendToLogin
            if(username.isEmpty() || password.isEmpty()){
                if (username.isEmpty()) {
                    edUsername.error = "Username Empty"
                }
                if (password.isEmpty()) {
                    edPassword.error = "Password Empty"
                }
            } else {
                sendToLogin = SendToLogin(username, password)
                val call: Call<LoginResponse> = apiService.postLogin(sendToLogin)
                call.enqueue(object: Callback<LoginResponse> {
                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {
                        if(response.isSuccessful) {

                            val sharedPreference =  getSharedPreferences(Constants.SHARED_PREFS_FILE, Context.MODE_PRIVATE)
                            var editor = sharedPreference.edit()
                            editor.putString(Constants.AUTH_TOKEN,response.body()?.token)
                            editor.putString(Constants.REFRESH_TOKEN,response.body()?.refreshToken)
                            editor.apply()

                            Toast.makeText(MyApp.instance, "Login succesfull: ${response.body()?.user?.username}", Toast.LENGTH_LONG).show()

                            val mainActivityIntent = Intent(MyApp.instance, MainActivity::class.java)
                            mainActivityIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            ContextCompat.startActivity(MyApp.instance, mainActivityIntent, null)

                        } else {
                            Toast.makeText(MyApp.instance, "Error at Login, check your data", Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                        Toast.makeText(MyApp.instance, "Error at Login", Toast.LENGTH_LONG).show()
                    }
                })
            }
        }

        btnRegister.setOnClickListener { v ->
            val registerIntent = Intent(MyApp.instance, RegisterActivity::class.java)
            registerIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            ContextCompat.startActivity(MyApp.instance, registerIntent, null)
        }
    }
}
