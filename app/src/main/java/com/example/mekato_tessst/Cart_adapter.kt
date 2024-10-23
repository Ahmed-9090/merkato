package com.example.mekato_tessst


import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.mekato_tessst.databinding.CartDesignBinding

class Cart_adapter(
    private var productList: List<Products>, // Change from val to var
    private val onQuantityChanged: (Api_Cart, Int) -> Unit,
    private val onRemoveProduct: (Api_Cart) -> Unit
) : RecyclerView.Adapter<CartHolder>() {

    private var cartList: List<Api_Cart> = listOf()

    @SuppressLint("NotifyDataSetChanged")
    fun setCartList(cartItems: List<Api_Cart>) {
        cartList = cartItems
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateProducts(newProducts: List<Products>) {
        this.productList = newProducts // Update the internal product list
        notifyDataSetChanged() // Notify the adapter that the data set has changed
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartHolder {
        val binding = CartDesignBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return CartHolder(binding, productList, onQuantityChanged, onRemoveProduct)
    }

    override fun getItemCount(): Int = cartList.size

    override fun onBindViewHolder(holder: CartHolder, position: Int) {
        val cartItem = cartList[position]
        holder.bindCart(cartItem)
    }
}



class CartHolder(
    private val binding: CartDesignBinding,
    private val productList: List<Products>,
    private val onQuantityChanged: (Api_Cart, Int) -> Unit,
    private val onRemoveProduct: (Api_Cart) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    private fun findProduct(productId: Int): Products? {
        return productList.find { it.id == productId }
    }

    @SuppressLint("DefaultLocale")
    fun bindCart(cartData: Api_Cart) {
        var totalPrice = 0.0
        val product = cartData.products.firstOrNull() // Handling one product for simplicity

        product?.let { cartProduct ->
            val productData = findProduct(cartProduct.productId)
            productData?.let { data ->
                totalPrice += data.price * cartProduct.quantity

                // Bind product details to views
                Glide.with(binding.root.context)
                    .load(data.image)
                    .into(binding.cartProductImage)

                binding.cartProductName.text = data.title
                binding.cartProductPrice.text = String.format("%.2f €", data.price)
                binding.productQuantityValue.text = cartProduct.quantity.toString()

                // Handle quantity increase
                binding.increaseQuantityButton.setOnClickListener {
                    val newQuantity = cartProduct.quantity + 1
                    binding.productQuantityValue.text = newQuantity.toString()
                    onQuantityChanged(cartData, newQuantity)
                }

                // Handle quantity decrease with validation
                binding.decreaseQuantityButton.setOnClickListener {
                    if (cartProduct.quantity > 1) {
                        val newQuantity = cartProduct.quantity - 1
                        binding.productQuantityValue.text = newQuantity.toString()
                        onQuantityChanged(cartData, newQuantity)
                    }
                }

                // Handle product removal from cart
                binding.removeFromCartButton.setOnClickListener {
                    onRemoveProduct(cartData)
                }
            }
        }

        // Update total price display
        binding.cartProductPrice.text = String.format("TOTAL %.2f €", totalPrice)
        binding.cartProductQuantity.text = cartData.products.sumOf { it.quantity }.toString()
    }
}
