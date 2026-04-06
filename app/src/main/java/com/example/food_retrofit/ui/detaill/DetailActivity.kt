package com.example.food_retrofit.ui.detaill

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.food_retrofit.R
import com.example.food_retrofit.data.model.Cart.CartRequest.CartRequest
import com.example.food_retrofit.data.model.Cart.CartResponse.CartResponse
import com.example.food_retrofit.data.model.Product.Product
import com.example.food_retrofit.data.remote.RetrofitClient
import com.example.food_retrofit.viewmodel.CartViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailActivity : AppCompatActivity() {

    private var quantity = 1
    private lateinit var viewModel: CartViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        viewModel = ViewModelProvider(this)[CartViewModel::class.java]

        // 🔥 GET PRODUCT
        val product = intent.getSerializableExtra("product") as Product

        // 🔥 VIEWS
        val imgProduct = findViewById<ImageView>(R.id.imgProduct)
        val tvQuantity = findViewById<TextView>(R.id.tvQuantity)
        val btnPlus = findViewById<ImageButton>(R.id.btnPlus)
        val btnMinus = findViewById<ImageButton>(R.id.btnMinus)
        val btnAddCart = findViewById<Button>(R.id.btnAddCart)

        // 🔥 LOAD IMAGE (FIXED)
        if (product.images.isNotEmpty()) {
            Glide.with(this)
                .load(product.images[0].image_url)
                .placeholder(R.drawable.beef_burger)
                .into(imgProduct)
        }

        // 🔥 QUANTITY
        tvQuantity.text = quantity.toString()

        btnPlus.setOnClickListener {
            quantity++
            tvQuantity.text = quantity.toString()
        }

        btnMinus.setOnClickListener {
            if (quantity > 1) {
                quantity--
                tvQuantity.text = quantity.toString()
            }
        }

        // 🔥 ADD TO CART
        btnAddCart.setOnClickListener {

            val request = CartRequest(
                user_id = 1,
                product_id = product.id,
                quantity = quantity
            )

            viewModel.addToCartSuspend(request) {

                if (it.isSuccessful) {
                    Toast.makeText(
                        this@DetailActivity,
                        "Added to cart ✅",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Toast.makeText(
                        this@DetailActivity,
                        "Failed ❌",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        }
    }
}