package com.example.testdanamon.ui.login

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.testdanamon.databinding.ActivityLoginUserBinding
import com.example.testdanamon.MainActivity
import com.example.testdanamon.adapter.PhotoAdapter
import com.example.testdanamon.utils.Resource
import com.example.testdanamon.utils.gone
import com.example.testdanamon.utils.visible
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch


@AndroidEntryPoint
class LoginUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginUserBinding
    private val viewModel by viewModels<LoginUserViewModel>()
    private lateinit var photoAdapter: PhotoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginUserBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        viewModel.fetchPhotos()
        photoAdapter = PhotoAdapter()
        // setup list data
        binding.rvContent.apply {
            adapter = photoAdapter
            layoutManager = LinearLayoutManager(this@LoginUserActivity)
            setHasFixedSize(true)
        }
        //saat load data
        photoAdapter.addLoadStateListener { loadState ->
            if (loadState.refresh is LoadState.Loading ||
                (loadState.append is LoadState.Loading && loadState.append.endOfPaginationReached)
            ) {
                binding.progressBar.isVisible = true
            } else {
                binding.progressBar.isVisible = false

                // Toast error state
                val errorState = when {
                    loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                    loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                    loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                    else -> null
                }
                errorState?.let {
                    Toast.makeText(this, it.error.toString(), Toast.LENGTH_LONG).show()
                }


                if (loadState.refresh is LoadState.Loading ||
                    loadState.append is LoadState.Loading ||
                    loadState.prepend is LoadState.Loading
                ) {
                    // Show progress bar when loading is in progress
                    binding.progressBar.visible()
                } else {
                    // Hide progress bar when loading is not in progress
                    binding.progressBar.gone()

                    // If we have an error, show a toast
                    val errorState = when {
                        loadState.append is LoadState.Error -> loadState.append as LoadState.Error
                        loadState.prepend is LoadState.Error -> loadState.prepend as LoadState.Error
                        loadState.refresh is LoadState.Error -> loadState.refresh as LoadState.Error
                        else -> null
                    }
                    errorState?.let {
                        Toast.makeText(this, it.error.toString(), Toast.LENGTH_LONG).show()
                    }
                }

            }
        }

        viewModel.photosLiveData.observe(this) { resource ->
            when (resource) {
                is Resource.Loading -> {
                    // Show loading progress
                    binding.progressBar.visible()
                }

                is Resource.Success -> {
                    // Update UI with the data
                    lifecycleScope.launch {
                        resource.data?.let { photoAdapter.submitData(it) }
                    }
                }

                is Resource.Error -> {
                    // Show error message
                    val errorMessage = resource.message ?: "An error occurred"
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                }

                else -> {}
            }
        }
        initClick()
        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                val intent = Intent(this@LoginUserActivity, MainActivity::class.java)
                startActivity(intent)
                finish() // F
            }
        })
    }

    private fun initClick() {
        binding.apply {
            logoutButton.setOnClickListener {
                val intent = Intent(this@LoginUserActivity, MainActivity::class.java)
                startActivity(intent)
                finish() // F
            }
        }
    }


}