package com.example.mekato_tessst

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.android.gms.analytics.ecommerce.Product
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class P_ViewModel : ViewModel() {
    // LiveData for observing product data and error messages
    val dataMutable: MutableLiveData<List<Products>> = MutableLiveData()
    val err: MutableLiveData<String> = MutableLiveData()
    private val _cartItems = MutableLiveData<MutableList<Api_Cart>>()
    val cartItems: LiveData<MutableList<Api_Cart>> get() = _cartItems

    init {
        _cartItems.value = mutableListOf() // Initialize with an empty cart
    }

    // Function to fetch all products
    fun getAllProducts() {
        ApiClient.getInstance().getProducts().enqueue(object : Callback<List<Products>> {
            override fun onResponse(call: Call<List<Products>>, response: Response<List<Products>>) {
                if (response.isSuccessful) {
                    dataMutable.value = response.body()
                } else {
                    err.value = "Error: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<List<Products>>, t: Throwable) {
                err.value = "Error: ${t.message}"
            }
        })
    }
    fun getProductById(productId: Int): LiveData<Products> {
        val singleProductLiveData = MutableLiveData<Products>()
        ApiClient.getInstance().getProductById(productId).enqueue(object : Callback<Products> {
            override fun onResponse(call: Call<Products>, response: Response<Products>) {
                if (response.isSuccessful) {
                    singleProductLiveData.value = response.body() // Post the single product
                } else {
                    err.value = "Error: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<Products>, t: Throwable) {
                err.value = "Error: ${t.message}"
            }
        })
        return singleProductLiveData // Return LiveData for observing
    }

    // Function to fetch a product by ID
    fun addProductToCart(product: Products) {
        fun generateCartId(): Int {
            return Random().nextInt(1000) // Just a random ID for example
        }

        fun getUserId(): Int {
            return 1 // Assuming a static user ID for testing
        }

        fun getCurrentDate(): String {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            return sdf.format(Date()) // Current date in String format
        }

        val currentCartItems = _cartItems.value ?: mutableListOf()

        // Assuming you have only one cart for simplicity
        var currentCart: Api_Cart? = currentCartItems.firstOrNull()

        // Create a new cart if it doesn't exist
        if (currentCart == null) {
            currentCart = Api_Cart(
                id = generateCartId(),
                userId = getUserId(),
                date = getCurrentDate(),
                products = mutableListOf()
            )
            currentCartItems.add(currentCart)
        }

        // Check if the product already exists in the cart
        val existingProduct = currentCart.products.find { it.productId == product.id }
        if (existingProduct != null) {
            existingProduct.quantity += 1 // Increment quantity
        } else {
            currentCart.products.add(Product_cart(productId = product.id, quantity = 1)) // Add new product
        }

        // Update LiveData
        _cartItems.value = currentCartItems
    }

}
