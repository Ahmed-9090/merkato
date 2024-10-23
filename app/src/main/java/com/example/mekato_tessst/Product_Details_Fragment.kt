package com.example.mekato_tessst

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import androidx.activity.addCallback
import androidx.databinding.DataBindingUtil

import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mekato_tessst.databinding.FragmentProductDetailsBinding


class Product_Details_Fragment : Fragment() {

    lateinit var binding: FragmentProductDetailsBinding
    lateinit var viewModel: ProductsViewModel
    private val args: Product_Details_FragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {

        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_product__details_, container, false
        )

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            findNavController().popBackStack()
        }

        viewModel = ViewModelProvider(this)[ProductsViewModel::class.java]
        binding.detailsViewmodel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        val adapter = adapter_details()
        binding.productDetailsRecycler.adapter = adapter
        binding.productDetailsRecycler.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        val productId = args.productId
        viewModel.getProductById(productId).observe(viewLifecycleOwner) { product ->
            product?.let {
                // Update adapter with the single product
                adapter.setProductList(listOf(it))
            }

        }

        // Handle BottomNavigationView interactions
        val bottomNavigationView = binding.bottomNavigationView
        bottomNavigationView.selectedItemId = R.id.menu_products
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.menu_products -> {
                    findNavController().navigate(R.id.action_product_Details_Fragment_to_productsFragment)
                    true
                }
                R.id.menu_cart -> {
                    // Navigate to Cart Fragment
                    findNavController().navigate(R.id.action_product_Details_Fragment_to_cartFragment)
                    true
                }
                R.id.menu_user -> {
                    // Navigate to User Fragment
                    findNavController().navigate(R.id.action_product_Details_Fragment_to_userFragment)
                    true
                }
                else -> false
            }
        }


        return binding.root
    }


}