package com.example.mekato_tessst

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.mekato_tessst.databinding.FragmentSignUpBinding
import kotlinx.coroutines.launch

class Sign_Up_Fragment : Fragment() {
    private lateinit var binding: FragmentSignUpBinding
    private lateinit var userDB: UserDB // Reference to the Room database

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_sign__up_, container, false
        )

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().popBackStack()
        }

        userDB = UserDB.getDatabase(requireContext()) // Initialize the database
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.SignButton.setOnClickListener {
            signUpUser() // Call the sign-up function
        }
    }

    private fun signUpUser() {
        // Get user input
        val firstname = binding.FirstnameSignup.text.toString().trim()
        val lastname = binding.LastnameSignup.text.toString().trim()
        val email = binding.username.text.toString().trim()
        val password = binding.password.text.toString().trim()

        // Validate input
        if (firstname.isEmpty() || lastname.isEmpty() || email.isEmpty() || password.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        // Create a new UserEntity
        val newUser = UserEntity(
            firstname = firstname,
            lastname = lastname,
            email = email,
            password = password,
            profileImageUrl = R.drawable.cart_blue.toString() // or a default URL if you have one
        )

        // Insert the user into the database
        lifecycleScope.launch {
            try {
                // Check if the user already exists
                val existingUser = userDB.userDao().getUserByEmail(email)
                if (existingUser != null) {
                    Toast.makeText(requireContext(), "User already exists with this email.", Toast.LENGTH_SHORT).show()
                    return@launch // Exit the function if the user already exists
                }

                // Insert new user if no existing user found
                userDB.userDao().insertUser(newUser)
                Toast.makeText(requireContext(), "User signed up successfully", Toast.LENGTH_SHORT).show()

                // Optionally, log user count for debugging
                val userCount = userDB.userDao().getAllUsers().size
                Log.d("SignUp", "Current user count: $userCount")

                // Navigate to login
                findNavController().navigate(R.id.action_sign_Up_Fragment_to_loginFragment)

            } catch (e: Exception) {
                Toast.makeText(requireContext(), "Error signing up: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
