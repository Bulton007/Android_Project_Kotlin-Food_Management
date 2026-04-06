package com.example.food_retrofit.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.food_retrofit.R
import com.example.food_retrofit.data.model.Cart.CartResponse.CartResponse
import com.example.food_retrofit.ui.detaill.DetailActivity

class CartAdapter(
    private val list: MutableList<CartResponse>,
    private val onIncrease: (CartResponse) -> Unit,
    private val onDecrease: (CartResponse) -> Unit,
    private val onDelete: (CartResponse) -> Unit
) : RecyclerView.Adapter<CartAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val img : ImageView = view.findViewById(R.id.imgProduct)
        val txtTitle: TextView = view.findViewById(R.id.txtTitle)
        val txtPrice: TextView = view.findViewById(R.id.txtPrice)
        val txtQty: TextView = view.findViewById(R.id.txtQty)
        val btnClick : ImageView = view.findViewById(R.id.imgProduct)
        val btnPlus: ImageButton = view.findViewById(R.id.btnPlus)
        val btnMinus: ImageButton = view.findViewById(R.id.btnMinus)
        val btnDelete: ImageButton = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_cart, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cart = list[position]

        holder.txtTitle.text = cart.product.title
        holder.txtPrice.text = "$${cart.product.price}"
        holder.txtQty.text = cart.quantity.toString()
        Glide.with(holder.itemView.context)
            .load(cart.product.images[0].image_url)
            .placeholder(R.drawable.plus)
            .into(holder.img)

        holder.btnPlus.setOnClickListener {
            onIncrease(cart)
        }

        holder.btnMinus.setOnClickListener {
            if (cart.quantity > 1) {
                onDecrease(cart)
            }
        }

        holder.btnDelete.setOnClickListener {
            onDelete(cart)
        }
        holder.btnClick.setOnClickListener {
            val intent = Intent(holder.itemView.context, DetailActivity::class.java)
            intent.putExtra("product", cart.product)
            holder.itemView.context.startActivity(intent)

        }
    }
}