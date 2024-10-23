package com.example.mekato_tessst

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class UserViewModel(private val userDao: UserDao) : ViewModel() {

    // MutableLiveData to hold the list of user details
    private val _userDetails = MutableLiveData<List<UserEntity>?>()
    val userDetails: LiveData<List<UserEntity>?>
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
    fun fetchUserDetails(emailList: List<String>) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                Log.d("UserViewModel", "Fetching users for emails: $emailList")
                val users = userDao.getUsersByEmails(emailList)
                Log.d("UserViewModel", "Fetched users: $users")
                _userDetails.value = users

                if (users.isEmpty()) {
                    _errorMessage.value = "No users found."
                }
            } catch (e: Exception) {
                _errorMessage.value = "Error fetching user details: ${e.message}"
            } finally {
                _isLoading.value = false
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
