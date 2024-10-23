package com.example.mekato_tessst

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mekato_tessst.databinding.ProductsDetailsDesignBinding

class adapter_details : RecyclerView.Adapter<Holder_details>() {
    private var productList: List<Products> = listOf()

    @SuppressLint("NotifyDataSetChanged")
    fun setProductList(products: List<Products>) {
        productList = products
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder_details {
        val binding: ProductsDetailsDesignBinding = ProductsDetailsDesignBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return Holder_details(binding)
    }

    override fun getItemCount(): Int {
        return productList.size
    }

    override fun onBindViewHolder(holder: Holder_details, position: Int) {
        val item = productList[position]
        holder.bind(item)
    }

}
class Holder_details(val binding: ProductsDetailsDesignBinding) : RecyclerView.ViewHolder(binding.root) {

    @SuppressLint("SetTextI18n")
    fun bind(data: Products) { // Updated method name for clarity
        // Load image
        Glide.with(binding.root.context)
            .load(data.image)
            .into(binding.productImage)


        binding.productDescription.text = data.description
        binding.productPrice.text = "Price: ${data.price} EGP"
        binding.productTitle.text = data.title


    }
}