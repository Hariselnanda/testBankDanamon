package com.example.testdanamon.ui.update

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.testdanamon.MainActivity
import com.example.testdanamon.R
import com.example.testdanamon.databinding.ActivityUpdateBinding
import com.example.testdanamon.utils.condition1Check
import com.example.testdanamon.utils.condition2Check
import com.example.testdanamon.utils.condition3Check
import com.example.testdanamon.utils.isValidEmail
import com.google.android.material.textfield.TextInputLayout
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class UpdateActivitity : AppCompatActivity() {

    private lateinit var binding: ActivityUpdateBinding

    private val viewModel: UpdateViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityUpdateBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val userId = intent.getIntExtra("userId", 0)
        viewModel.userById.observe(this) { user ->
            // Update UI with user details
            binding.apply {
                usernameEditText.setText(user?.username)
                emailEditText.setText(user?.email)
                passwordEditText.setText(user?.password)
                val roleArray = resources.getStringArray(R.array.roles_array)
                val roleIndex = roleArray.indexOf(user?.role)
                roleSpinner.setSelection(roleIndex)
            }
        }

        viewModel.allUsers.observe(this) { user ->
            //showToastAndFinish(user.username, user.email, user.password, user.role)
            Toast.makeText(this, "Berhasil Update", Toast.LENGTH_LONG).show()
            setResult(Activity.RESULT_OK)
            finish()
        }
        initRole()
        initClick(userId)
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(this@UpdateActivitity, MainActivity::class.java)
                startActivity(intent)
                finish() // F
            }
        })
        viewModel.getUserById(userId)
    }



    private fun initClick(userId: Int) {
        binding.apply {
            submitButton.setOnClickListener {
                validateUpdate(userId)
            }
        }
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


    private fun validateUpdate(userId: Int) {
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
        viewModel.update(userId,username, password, selectedSpinnerText, email)


    }
}