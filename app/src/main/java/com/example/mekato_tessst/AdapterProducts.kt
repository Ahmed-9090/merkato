package com.example.mekato_tessst

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mekato_tessst.databinding.CategoriesviewBinding


class AdapterProducts(private val onItemClick: (Products) -> Unit) :
    RecyclerView.Adapter<AdapterProducts.ProductViewHolder>() {

    private var products: List<Products> = listOf()

    @SuppressLint("NotifyDataSetChanged")
    fun updateProductList(apiProducts: List<Products>) {
        products = apiProducts
        notifyDataSetChanged()
    }


    class ProductViewHolder(
        val binding: CategoriesviewBinding,
        private val onItemClick: (Products) -> Unit,
    ) : RecyclerView.ViewHolder(binding.root) {


        @SuppressLint("SetTextI18n")
        fun bind(data: Products) { // Updated method name for clarity
            // Load image
            Glide.with(binding.root.context)
                .load(data.image)
                .into(binding.productImage)

            binding.productTitle.text = data.title
            binding.productPrice.text = data.price.toString()
            binding.productCategory.text = data.category
            binding.productRating.text = data.rating.toString()

            binding.productsDesign.setOnClickListener {
                onItemClick(data)
            }

        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val binding: CategoriesviewBinding =
            CategoriesviewBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ProductViewHolder(binding, onItemClick)

    }

    override fun getItemCount() = products.size

    @SuppressLint("SetTextI18n") // bttl3 warning law feh moshkla fe elserver we (I18n) version zy utf8 bid3m kza lo8a 3shan ytl3ly elwarning bkza lo8a
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]

        holder.bind(product)

    }

}