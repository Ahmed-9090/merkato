package com.example.mekato_tessst

import android.os.Bundle
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.example.mekato_tessst.databinding.FragmentLoginBinding

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var userDB: UserDB // Reference to the Room database
    private val userViewModel: UserViewModel by lazy {
        ViewModelProvider(
            requireActivity(),
            UserViewModelFactory(userDB.userDao())
        )[UserViewModel::class.java]
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_login, container, false
        )
        userDB = UserDB.getDatabase(requireContext()) // Initialize the database


        binding.loginButton.setOnClickListener {
            val email = binding.username.text.toString().trim()
            val password = binding.password.text.toString().trim()
            // Validate input
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(
                    requireContext(),
                    "Please enter email and password",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                userViewModel.loginUser(email, password)
            }
        }

        binding.signupButton.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_sign_Up_Fragment)
        }

        userViewModel.userDetails.observe(viewLifecycleOwner) {
            if (it != null) {
                // Navigate to productsFragment
                Toast.makeText(requireContext(), "Login successful", Toast.LENGTH_SHORT).show()
                findNavController().navigate(R.id.action_loginFragment_to_productsFragment)
            } else {
                // User not found
                Toast.makeText(requireContext(), "Invalid email or password", Toast.LENGTH_SHORT)
                    .show()
            }
        }

        return binding.root
    }
}
