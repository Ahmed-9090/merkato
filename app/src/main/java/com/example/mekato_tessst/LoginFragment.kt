package com.example.mekato_tessst

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.mekato_tessst.databinding.FragmentLoginBinding
import kotlinx.coroutines.launch

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var userDB: UserDB // Reference to the Room database
    private lateinit var userViewModel: UserViewModel // Reference to UserViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_login, container, false
        )
        userDB = UserDB.getDatabase(requireContext()) // Initialize the database

        // Initialize the ViewModel
        userViewModel = ViewModelProvider(this, UserViewModelFactory(userDB.userDao())).get(UserViewModel::class.java)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.loginButton.setOnClickListener {
            loginUser()
        }

        binding.signupButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_sign_Up_Fragment)
        }
    }

    private fun loginUser() {
        // Get user input
        val email = binding.username.text.toString().trim()
        val password = binding.password.text.toString().trim()

        // Validate input
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter email and password", Toast.LENGTH_SHORT).show()
            return
        }

        // Check if the user exists in the database
        lifecycleScope.launch {
            val user = userDB.userDao().getUserByEmail(email)
            if (user != null && user.password == password) { // Check password
                // User exists, fetch user details using ViewModel
                userViewModel.fetchUserDetails(listOf(email)) // Use the instance here
                // Navigate to productsFragment
                Toast.makeText(requireContext(), "Login successful", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_loginFragment_to_productsFragment)
            } else {
                // User not found
                Toast.makeText(requireContext(), "Invalid email or password", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
