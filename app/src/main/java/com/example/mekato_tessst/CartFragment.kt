package com.example.mekato_tessst

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mekato_tessst.databinding.FragmentCartBinding

class CartFragment : Fragment() {
    private lateinit var binding: FragmentCartBinding
    private lateinit var viewModel: P_ViewModel
    private lateinit var productViewModel: ProductsViewModel
    private lateinit var adapter: Cart_adapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_cart,container, false)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().popBackStack()
        }

        viewModel = ViewModelProvider(this)[P_ViewModel::class.java]
        productViewModel = ViewModelProvider(this)[ProductsViewModel::class.java]

        // Initialize adapter with an empty product list and empty lambda functions
        adapter = Cart_adapter(emptyList(), { _, _ -> }, { _ -> }) // Provide empty lambdas (as a placeholders )

        binding.cartRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.cartRecyclerView.adapter = adapter

        // Observe cart items and update adapter
        viewModel.cartItems.observe(viewLifecycleOwner) { cartItems ->
            adapter.setCartList(cartItems)
        }

        // Observe product data from ProductsViewModel and update the product list in the adapter
        productViewModel.productMutable.observe(viewLifecycleOwner) { products ->
            adapter.updateProducts(products)
        }

        // Handle bottom navigation
        setupBottomNavigation()

        return binding.root
    }

    private fun setupBottomNavigation() {
        val bottomNavigationView = binding.bottomNavigationView
        bottomNavigationView.selectedItemId = R.id.menu_cart
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_cart -> true
                R.id.menu_products -> {
                    findNavController().navigate(R.id.action_cartFragment_to_productsFragment)
                    true
                }
                R.id.menu_user -> {
                    findNavController().navigate(R.id.action_cartFragment_to_userFragment)
                    true
                }
                else -> false
            }
        }
    }
}

