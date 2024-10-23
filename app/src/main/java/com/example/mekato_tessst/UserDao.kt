package com.example.mekato_tessst

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE) // Avoid duplicates
    suspend fun insertUser(user: UserEntity)

    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<UserEntity> // Fetch all users

    @Query("SELECT * FROM users WHERE email = :email LIMIT 1")
    suspend fun getUserByEmail(email: String): UserEntity? // Fetch a specific user by email


    @Query("SELECT * FROM users WHERE email IN (:emailList)")
    suspend fun getUsersByEmails(emailList: List<String>): List<UserEntity>


    @Update
    suspend fun updateUser(user: UserEntity): Int // Return number of rows affected

    @Query("DELETE FROM users WHERE email = :email")
    suspend fun deleteUser(email: String)

    @Query("SELECT * FROM users")
    fun getAllUsersLiveData(): LiveData<List<UserEntity>>


    @Query("SELECT * FROM users WHERE email = :email AND password = :password LIMIT 1")
    suspend fun getUserByEmailAndPassword(email: String, password: String): UserEntity?
}
