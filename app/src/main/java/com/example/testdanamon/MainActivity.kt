package com.example.testdanamon

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.testdanamon.databinding.ActivityMainBinding
import com.example.testdanamon.ui.login.LoginAdminActivity
import com.example.testdanamon.ui.login.LoginUserActivity
import com.example.testdanamon.ui.register.RegisterActivity
import com.example.testdanamon.utils.condition1Check
import com.example.testdanamon.utils.condition2Check
import com.example.testdanamon.utils.condition3Check
import com.example.testdanamon.utils.isValidEmail
import com.example.testdanamon.utils.observeNonNull
import com.example.testdanamon.utils.observeNull
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

//    val settings = getSharedPreferences("Login", 0)
    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
//        val email: String = settings.getString("email", "").toString()
//        val role: String = settings.getString("role", "").toString()
//        if (email.isEmpty()) {
//            Toast.makeText(
//                this@MainActivity,
//                "Belum Login, Silahkan Login terlebih dahulu",
//                Toast.LENGTH_SHORT
//            ).show()
//        } else {
//            dashboard(role)
//        }
        initClick()

        viewModel.loginResult.observe(this) { success ->
            if (success) {
//                settings.edit().putString("email", viewModel.userEmail.toString()).apply()
//                settings.edit().putString("role", viewModel.userRole.toString()).apply()

                viewModel.userRole.observeNonNull(this) { role ->
                    //masuk ke dashboard
                    dashboard(role)
                }
                viewModel.userRole.observeNull(this) {
                   Timber.d("User tidak ditemukan")
                }
            } else {
                Toast.makeText(
                    this@MainActivity,
                    "username atau password salah",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun dashboard(role: String?) {
        val intent = when (role) {
            "Admin" -> Intent(this@MainActivity, LoginAdminActivity::class.java)
            "Normal User" -> Intent(this@MainActivity, LoginUserActivity::class.java)
            else -> throw IllegalStateException(" user role tidak ditemukan")
        }
        startActivity(intent)
    }

    private fun initClick() {
        binding.apply {
            loginButton.setOnClickListener {
                validateLogin()
            }
            registerButton.setOnClickListener {
                //click register untuk login
                initCleanData()
                // halaman register
                val intent = Intent(this@MainActivity, RegisterActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun initCleanData() {
        //clear data saat masuk ke halaman login
        binding.apply {
            passwordEditText.text?.clear()
            emailEditText.text?.clear()
            passwordTextInputLayout.clearFocus()
            emailTextInputLayout.clearFocus()
            passwordEditText.clearFocus()
            emailTextInputLayout.clearFocus()

        }
    }


    private fun validateLogin() {
        val password = binding.passwordEditText.text.toString()
        val email = binding.emailEditText.text.toString()

        if (!email.isValidEmail()) {
            binding.emailEditText.error = "Invalid email address"
            binding.emailEditText.requestFocus()
            return
        }

        // Check which condition the password doesn't meet
        val conditionNotMet = when {
            !password.condition1Check -> "Password must be at least 6 characters long."
            !password.condition2Check -> "Password must be at least 6 characters long, contain at least one digit, and have mixed case."
            !password.condition3Check -> "Password must be at least 6 characters long, contain at least one digit, have mixed case, and contain a special character."
            else -> null // Password meets all conditions
        }

        // Show error message if conditionNotMet is not null
        conditionNotMet?.let {
            binding.passwordEditText.error = it
            binding.passwordEditText.requestFocus()
            binding.passwordTextInputLayout.endIconMode = TextInputLayout.END_ICON_NONE
            return
        }

        // Check if two are meet then
        //Navigate to LoginAdminActivity
        binding.passwordTextInputLayout.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE
        initCleanData()
        viewModel.login(email, password)


    }
}