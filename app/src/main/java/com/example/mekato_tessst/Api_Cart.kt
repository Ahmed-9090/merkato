package com.example.mekato_tessst

/*[{"id":1,"userId":1,"date":"2020-03-02T00:00:00.000Z","products":
[{"productId":1,"quantity":4},{"productId":2,"quantity":1},{"productId":3,"quantity":6}],"__v":0}

*/

data class Api_Cart(
    val id: Int,
    val userId: Int,
    val date: String,
    val products: MutableList<Product_cart>,
)

data class Product_cart(
    val productId: Int,
    var quantity: Int,
)