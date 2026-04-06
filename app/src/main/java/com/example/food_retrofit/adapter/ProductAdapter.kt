    package com.example.food_retrofit.adapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.food_retrofit.R
import com.example.food_retrofit.data.model.Category.Category
import com.example.food_retrofit.data.model.Product.Product
import kotlin.collections.isNotEmpty
class ProductAdapter(
    private val list: List<Product>,
    private val onClick: (Product) -> Unit,
    private val onEdit: (Product) -> Unit,
    private val onDelete: (Product) -> Unit
) : RecyclerView.Adapter<ProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtTitle: TextView = view.findViewById(R.id.txtTitle)
        val txtPrice: TextView = view.findViewById(R.id.txtPrice)
        val imgProduct: ImageView = view.findViewById(R.id.imgProduct)

        val btnEdit: ImageView = view.findViewById(R.id.btnEdit)
        val btnDelete: ImageView = view.findViewById(R.id.btnDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_product, parent, false)
        return ProductViewHolder(view)
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = list[position]

        holder.txtTitle.text = product.title
        holder.txtPrice.text = "$${product.price}"


        if (product.images.isNotEmpty()) {
            val imageUrl = product.images[0].image_url

            android.util.Log.d("IMAGE_URL", imageUrl)

            Glide.with(holder.itemView.context)
                .load(imageUrl)
                .placeholder(R.drawable.pluss)   // loading image
                .error(R.drawable.pluss)         // if fail
                .into(holder.imgProduct)
        } else {
            holder.imgProduct.setImageResource(R.drawable.pluss)
        }

        holder.itemView.setOnClickListener { onClick(product) }
        holder.btnEdit.setOnClickListener { onEdit(product) }
        holder.btnDelete.setOnClickListener { onDelete(product) }
    }
}