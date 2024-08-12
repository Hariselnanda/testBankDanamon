package com.example.testdanamon.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.testdanamon.databinding.ItemUserBinding
import com.example.testdanamon.database.UserEntity

class LoginAdminAdapter(
    private val onUpdateClickListener: OnUpdateClickListener,
    private val onDeleteClickListener: OnDeleteClickListener
) : ListAdapter<UserEntity, LoginAdminAdapter.UserViewHolder>(UserDiffCallback()) {

    interface OnUpdateClickListener {
        fun onUpdateClick(user: UserEntity,position: Int)
    }

    interface OnDeleteClickListener {
        fun onDeleteClick(user: UserEntity,position: Int)
    }

    inner class UserViewHolder(private val binding: ItemUserBinding) :
        RecyclerView.ViewHolder(binding.root) {

        init {
            // Set click listeners for the update and delete images
            binding.ivUpdate.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onUpdateClickListener.onUpdateClick(getItem(position),position)
                }
            }

            binding.ivDelete.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onDeleteClickListener.onDeleteClick(getItem(position),position)
                }
            }
        }

        fun bind(user: UserEntity) {
            binding.apply {
                // Bind user data to views
                idTextView.text = user.id.toString()
                usernameTextView.text = user.username
                emailTextView.text = user.email
                roleTextView.text = user.role
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemUserBinding.inflate(inflater, parent, false)
        return UserViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = getItem(position)
        holder.bind(user)
    }

    class UserDiffCallback : DiffUtil.ItemCallback<UserEntity>() {
        override fun areItemsTheSame(oldItem: UserEntity, newItem: UserEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: UserEntity, newItem: UserEntity): Boolean {
            return oldItem == newItem
        }
    }
}