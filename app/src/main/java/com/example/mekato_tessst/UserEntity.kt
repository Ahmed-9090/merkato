package com.example.mekato_tessst

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val firstname: String,
    val lastname: String,
    val password: String,
    val email: String,
    val profileImageUrl: String?
)


