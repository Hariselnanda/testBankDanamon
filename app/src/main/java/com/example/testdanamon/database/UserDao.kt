package com.example.testdanamon.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.testdanamon.utils.Constant.APP_TABLE


@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user: UserEntity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateUser(user: UserEntity)

    @Delete
    suspend fun deleteUser(userEntity: UserEntity)

    @Query("SELECT * FROM $APP_TABLE ORDER BY id ASC")
    suspend fun getAllUser() :MutableList<UserEntity>

    @Query("SELECT * FROM $APP_TABLE WHERE email = :email AND password = :password")
    suspend fun getUser(email: String, password: String): UserEntity

    @Query("SELECT * FROM $APP_TABLE WHERE id = :userId")
    suspend fun getUserById(userId: Int): UserEntity?

    @Query("UPDATE $APP_TABLE SET username = :name, email = :email, password= :password, role = :role WHERE id = :userId")
    suspend fun updateUserById(userId: Int, name: String, email: String, role: String,password: String)

    @Query("DELETE FROM $APP_TABLE WHERE id = :userId")
    suspend fun deleteUserById(userId: Int) :Int

    @Query("SELECT COUNT(*) FROM $APP_TABLE WHERE username = :username AND password = :password")
    suspend fun checkUserAndPassword(username: String, password: String): Int

}