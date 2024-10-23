package com.example.mekato_tessst

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mekato_tessst.databinding.FragmentProductsBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class ProductsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout using View Binding
        val binding: FragmentProductsBinding =
            FragmentProductsBinding.inflate(inflater, container, false)

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().popBackStack()
        }

        // ViewModel initialization
        val vmodel = ViewModelProvider(this)[ProductsViewModel::class.java]

        // Fetch products
        vmodel.getProducts()

        // Setup RecyclerView
        val recyclerView: RecyclerView = binding.recyclerView
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)
        val adapter = AdapterProducts { selectedItem -> onItemClick(selectedItem) }
        recyclerView.adapter = adapter

        // Observe the product list
        vmodel.productMutable.observe(viewLifecycleOwner) {
            adapter.updateProductList(it)
        }


        // Handle BottomNavigationView interactions
        val bottomNavigationView = binding.bottomNavigationView
        bottomNavigationView.selectedItemId = R.id.menu_products
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_products -> {
                    // Stay on the current fragment
                    true
                }
                R.id.menu_cart -> {
                    // Navigate to Cart Fragment
                    findNavController().navigate(R.id.action_productsFragment_to_cartFragment)
                    true
                }
                R.id.menu_user -> {
                    // Navigate to User Fragment
                    findNavController().navigate(R.id.action_productsFragment_to_userFragment)
                    true
                }
                else -> false
            }
        }

        // Return the root view
        return binding.root
    }

    // Handle item click in RecyclerView
    private fun onItemClick(item: Products) {
        val action = ProductsFragmentDirections.actionProductsFragmentToProductDetailsFragment(item.id)
        findNavController().navigate(action)
    }
}
