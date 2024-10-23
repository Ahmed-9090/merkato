package com.example.mekato_tessst

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface IFunction {

    @GET("products")
    fun getProducts(): Call<List<Products>>

    @GET("products/{id}")
    fun getProductById(@retrofit2.http.Path("id") id: Int): retrofit2.Call<Products>

    @GET("carts")
    fun getCart() : retrofit2.Call<List<Api_Cart>>
    @GET("carts/{id}")
    fun getCartById(@retrofit2.http.Path("id") id: Int): retrofit2.Call<Api_Cart>
}



class ApiClient {
    private val baseURL = "https://fakestoreapi.com/"

    private val productInterface: IFunction

    init {
        //initiate connection
        //convert Json data from api to Java
        val retrofit =
            Retrofit.Builder().baseUrl(baseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        productInterface = retrofit.create(IFunction::class.java)
    }
    fun getProducts(): Call<List<Products>> {
        return productInterface.getProducts()
    }

    fun getProductById(productId: Int): retrofit2.Call<Products> {
        return productInterface.getProductById(productId)
    }
    fun getCart(): retrofit2.Call<List<Api_Cart>> {
        return productInterface.getCart()
    }

    fun getCartById(id: Int): retrofit2.Call<Api_Cart> {
        return productInterface.getCartById(id)
    }

    companion object {
        private var instance: ApiClient? = null
        fun getInstance(): ApiClient{
            return instance ?: kotlin.synchronized(this){
                instance ?: ApiClient().also {
                    instance = it
                }

            }
        }
    }




}