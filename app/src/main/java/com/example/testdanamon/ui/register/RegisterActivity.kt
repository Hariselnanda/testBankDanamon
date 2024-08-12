package com.example.testdanamon.ui.register


import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.testdanamon.R
import com.example.testdanamon.databinding.ActivityRegisterBinding
import com.example.testdanamon.utils.condition1Check
import com.example.testdanamon.utils.condition2Check
import com.example.testdanamon.utils.condition3Check
import com.example.testdanamon.utils.isValidEmail
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import timber.log.Timber

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {
    private val coroutineScope = CoroutineScope(Dispatchers.Main)
    private lateinit var binding: ActivityRegisterBinding

    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        initRole()
        initClick()
        viewModel.registrationResult.observe(this) { success ->
            if (success) {
                viewModel.registeredUser.observe(this) { user ->
                    showToastAndFinish(user.username, user.email, user.password, user.role)
                }
            } else {
                // Display error message
                Timber.d("Something Bad Happen")
            }
        }
    }

    private fun initClick() {
        binding.apply {

            submitButton.setOnClickListener {
                validateRegister()
            }
            cancelButton.setOnClickListener {
                finish()
            }
        }
    }

    private fun showToastAndFinish(
        username: String,
        email: String,
        password: String,
        role: String
    ) {
        Timber.d("Logged in with Username: $username\nEmail:$email\nPassword: $password\nRole:$role")
        Toast.makeText(this, "Berhasil Register", Toast.LENGTH_LONG).show()
        finish()
    }

    private fun initRole() {
        // Create an ArrayAdapter using the string array and a default spinner layout
        val adapter = ArrayAdapter.createFromResource(
            this,
            R.array.roles_array,
            android.R.layout.simple_spinner_item
        )
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        // Apply the adapter to the spinner
        binding.roleSpinner.adapter = adapter
    }

    private fun validateRegister() {
        val password = binding.passwordEditText.text.toString()
        val email = binding.emailEditText.text.toString()
        val username = binding.usernameEditText.text.toString()

        val spinnerArray = resources.getStringArray(R.array.roles_array)
        val selectedSpinnerItem = binding.roleSpinner.selectedItemPosition
        val selectedSpinnerText = spinnerArray[selectedSpinnerItem]

        if (username.isEmpty()) {
            binding.usernameEditText.error = "Username cannot empty"
            binding.usernameEditText.requestFocus()
            return
        }

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

        binding.passwordTextInputLayout.endIconMode = TextInputLayout.END_ICON_PASSWORD_TOGGLE

        //Back To Finish
        viewModel.register(username, password, selectedSpinnerText, email)
    }

    override fun onDestroy() {
        super.onDestroy()
        coroutineScope.cancel()
        viewModel.registeredUser.removeObservers(this)
        viewModel.registrationResult.removeObservers(this)
    }
}