package com.example.testdanamon.ui.login

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testdanamon.MainActivity
import com.example.testdanamon.adapter.LoginAdminAdapter
import com.example.testdanamon.database.UserEntity
import com.example.testdanamon.databinding.ActivityLoginAdminBinding
import com.example.testdanamon.databinding.DialogPasswordBinding
import com.example.testdanamon.ui.update.UpdateActivitity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber


@AndroidEntryPoint
class LoginAdminActivity : AppCompatActivity(),
    LoginAdminAdapter.OnUpdateClickListener,
    LoginAdminAdapter.OnDeleteClickListener {
    private lateinit var binding: ActivityLoginAdminBinding

    private lateinit var userAdapter: LoginAdminAdapter
    private lateinit var updateDataLauncher: ActivityResultLauncher<Intent>
    private val viewModel: LoginAdminViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginAdminBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        updateDataLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    viewModel.loadUsers()
                }
            }
        initClick()
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(this@LoginAdminActivity, MainActivity::class.java)
                startActivity(intent)
                finish() // F
            }
        })

        userAdapter = LoginAdminAdapter(this, this)

        binding.rvContent.apply {
            layoutManager = LinearLayoutManager(this@LoginAdminActivity)
            adapter = userAdapter
        }

        viewModel.userList.observe(this) { userList ->
            userAdapter.submitList(userList)
        }
        viewModel.loadUsers()
    }

    private fun initClick() {
        binding.apply {
            logoutButton.setOnClickListener {
                val intent = Intent(this@LoginAdminActivity, MainActivity::class.java)
                startActivity(intent)
                finish() // F
            }
        }
    }

    @SuppressLint("SuspiciousIndentation")
    private fun showPasswordDialog(user: UserEntity, position: Int) {
        val dialogBinding = DialogPasswordBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(this)

        val alerrtDialog = builder.setView(dialogBinding.root)
            .setPositiveButton("Submit") { _, _ ->
                val password = dialogBinding.usernameEditText.text.toString()
                val username = user.username

                viewModel.verifyPassword(username, password)
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
        alerrtDialog.show()

        // Observe the password verification result
        viewModel.isPasswordValid.observe(this) { isValid ->
            if (isValid) {
                val id = user.id
                Timber.d(id.toString() + position.toString())
                viewModel.removeUser(position)
                alerrtDialog.dismiss()
                viewModel.deleteUserById(id)
                Toast.makeText(this, "Berhasil Hapus Data", Toast.LENGTH_SHORT).show()
                viewModel.deleteUserById
            } else {
                // Show error pesan
                Toast.makeText(this, "Salah password", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onUpdateClick(user: UserEntity, position: Int) {
        val intent = Intent(this, UpdateActivitity::class.java)
        intent.putExtra("userId", user.id)
        updateDataLauncher.launch(intent)
    }

    override fun onDeleteClick(user: UserEntity, position: Int) {
        showPasswordDialog(user, position)
    }

    override fun onBackPressed() {
        super.onBackPressed()
    }


}