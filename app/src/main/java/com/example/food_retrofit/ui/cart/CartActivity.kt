package com.example.food_retrofit.ui.cart

import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.food_retrofit.R
import com.example.food_retrofit.adapter.CartAdapter
import com.example.food_retrofit.data.model.Cart.CartResponse.CartResponse
import com.example.food_retrofit.data.model.Cart.CartRequest.CartRequest
import com.example.food_retrofit.data.remote.RetrofitClient
import com.example.food_retrofit.viewmodel.CartViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CartActivity : AppCompatActivity() {

    private lateinit var recyclerCart: RecyclerView
    private lateinit var txtTotal: TextView
    private lateinit var viewModel: CartViewModel

    // ✅ ONE SOURCE OF TRUTH
    private var cartList: MutableList<CartResponse> = mutableListOf()
    private lateinit var adapter: CartAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)
        viewModel = ViewModelProvider(this)[CartViewModel::class.java]
        recyclerCart = findViewById(R.id.recyclerCart)
        txtTotal = findViewById(R.id.txtTotal)

        recyclerCart.layoutManager = LinearLayoutManager(this)

        // ✅ SET ADAPTER
        adapter = CartAdapter(
            cartList,

            // ➕ INCREASE
            onIncrease = { cart ->
                cart.quantity += 1
                updateQuantity(cart)
            },

            // ➖ DECREASE
            onDecrease = { cart ->
                if (cart.quantity > 1) {
                    cart.quantity -= 1
                    updateQuantity(cart)
                }
            },

            // 🗑 DELETE
            onDelete = { cart ->
                deleteItem(cart)
            }
        )

        recyclerCart.adapter = adapter

        // 🔥 LOAD DATA
        loadCart()
        val btnPay = findViewById<Button>(R.id.btnPay)

        btnPay.setOnClickListener {
            payNow()
        }
    }

    // 🔥 GET CART FROM API
    private fun loadCart() {
        viewModel.getCartSuspend(1) {

            if (it.isSuccessful && it.body() != null) {

                cartList.clear()
                cartList.addAll(it.body()!!)

                adapter.notifyDataSetChanged()
                updateTotal()
            }
        }
    }

    // 🔥 UPDATE QUANTITY (PUT)
    private fun updateQuantity(cart: CartResponse) {

        val request = CartRequest(
            user_id = cart.user_id,
            product_id = cart.product.id,
            quantity = cart.quantity
        )

        viewModel.updateCartSuspend(cart.id, request) {

            if (it.isSuccessful) {
                adapter.notifyDataSetChanged()
                updateTotal()
            }
        }
    }

    // 🔥 DELETE ITEM
    private fun deleteItem(cart: CartResponse) {

        viewModel.deleteCartSuspend(cart.id) {

            if (it.isSuccessful) {
                cartList.remove(cart)
                adapter.notifyDataSetChanged()
                updateTotal()
            }
        }
    }

    // 🔥 CALCULATE TOTAL
    private fun updateTotal() {
        var total = 0.0

        cartList.forEach {
            total += it.product.price * it.quantity
        }

        txtTotal.text = "Total: $$total"
    }
    private fun payNow() {

        if (cartList.isEmpty()) {
            Toast.makeText(this, "Cart is empty ❌", Toast.LENGTH_SHORT).show()
            return
        }

        AlertDialog.Builder(this)
            .setTitle("Confirm Payment")
            .setMessage("Pay $${
                cartList.sumOf { it.product.price * it.quantity }
            } ?")
            .setPositiveButton("Pay") { _, _ ->

                // 🔥 CALL API TO CLEAR CART
                viewModel.clearCartSuspend(1) {

                    if (it.isSuccessful) {

                        cartList.clear()
                        adapter.notifyDataSetChanged()
                        updateTotal()

                        showSuccess()

                    } else {
                        Toast.makeText(
                            this@CartActivity,
                            "Payment failed ❌",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
    private fun showSuccess() {
        AlertDialog.Builder(this)
            .setTitle("Payment Successful 🎉")
            .setMessage("Your order has been placed!")
            .setPositiveButton("OK", null)
            .show()
    }
}