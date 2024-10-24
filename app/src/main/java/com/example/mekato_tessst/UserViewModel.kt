package com.example.mekato_tessst

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class UserViewModel(private val userDao: UserDao) : ViewModel() {

    // MutableLiveData to hold the list of user details
    private val _userDetails = MutableLiveData<UserEntity?>()
    val userDetails: LiveData<UserEntity?>
        get() = _userDetails

    // LiveData to hold any error messages
    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?>
        get() = _errorMessage

    // LiveData to indicate loading state
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    // Function to fetch user details from the database
    fun fetchUserDetails(emailList: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                Log.d("UserViewModel", "Fetching users for emails: $emailList")
                val users = userDao.getUserByEmail(emailList)
                Log.d("UserViewModel", "Fetched users: $users")
                _userDetails.value = users

                if (users == null) {
                    _errorMessage.value = "No users found."
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error fetching user details: ${e.message}"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun loginUser(email: String, password: String) {
        // Check if the user exists in the database
        viewModelScope.launch {
            val user = userDao.getUserByEmail(email)
            if (user != null && user.password == password) { // Check password
                // User exists, fetch user details using ViewModel
                _userDetails.value = user // Use the instance here
                } else {
                _userDetails.value = null
            }
        }
    }

    // Function to update user details in the database
    fun updateUserDetails(users: List<UserEntity>) {
        viewModelScope.launch {
            _isLoading.value = true // Set loading state to true
            try {
                for (user in users) {
                    userDao.updateUser(user) // Update each user in the database
                }
            } catch (e: Exception) {
                // Handle any exceptions that occur
                _errorMessage.value = "Error updating user details: ${e.message}"
            } finally {
                _isLoading.value = false // Set loading state to false
            }
        }
    }

    // Optional: You can add a method to clear error messages
    fun clearErrorMessage() {
        _errorMessage.value = null
    }
}
