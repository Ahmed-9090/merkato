package com.example.mekato_tessst

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductsViewModel : ViewModel() {
    val productMutable: MutableLiveData<List<Products>> = MutableLiveData()
    val productError: MutableLiveData<String> = MutableLiveData()

    fun getProducts() {
        ApiClient.getInstance().getProducts().enqueue(object : Callback<List<Products>> {
            override fun onResponse(call: Call<List<Products>>, response: Response<List<Products>>) {
                if (response.isSuccessful) {
                    productMutable.value = response.body()
                } else {
                    productError.value = "Error: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<List<Products>>, t: Throwable) {
                productError.value = "Error: ${t.message}"
            }
        })
    }

    fun getProductById(productId: Int): LiveData<Products> {
        val singleProductLiveData = MutableLiveData<Products>()
        ApiClient.getInstance().getProductById(productId).enqueue(object : Callback<Products> {
            override fun onResponse(call: Call<Products>, response: Response<Products>) {
                if (response.isSuccessful) {
                    singleProductLiveData.value = response.body()
                } else {
                    productError.value = "Error: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<Products>, t: Throwable) {
                productError.value = "Error: ${t.message}"
            }
        })
        return singleProductLiveData
    }
}
