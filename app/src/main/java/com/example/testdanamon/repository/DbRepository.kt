package com.example.testdanamon.repository

import com.example.testdanamon.database.UserDao
import com.example.testdanamon.database.UserEntity
import javax.inject.Inject

class DbRepository
@Inject constructor(
    private val dao: UserDao
) {
    suspend fun registerUser(userEntity: UserEntity) = dao.insertUser(userEntity)


    suspend fun getUser(email: String, pass: String) = dao.getUser(email = email, password = pass)

    suspend fun getUserById(id: Int) = dao.getUserById(userId = id)

    suspend fun deleteUserById(userId: Int) =dao.deleteUserById(userId=userId)

    suspend fun updateUserById(userId: Int, name: String, email: String, role: String,pasword : String) =
        dao.updateUserById(
            userId,
            name, email, role,pasword
        )

    suspend fun getallUser() = dao.getAllUser()

    suspend fun checkUserAndPassword(username: String, password: String): Boolean {
        val count = dao.checkUserAndPassword(username, password)
        return count > 0
    }




}