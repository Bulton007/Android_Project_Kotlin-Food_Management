package com.example.food_retrofit.ui.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.food_retrofit.R
import com.example.food_retrofit.adapter.CategoryAdapter
import com.example.food_retrofit.adapter.ProductAdapter
import com.example.food_retrofit.data.model.Category.Category
import com.example.food_retrofit.data.model.Product.Product
import com.example.food_retrofit.data.remote.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.util.Log
import android.widget.EditText
import com.example.food_retrofit.ui.search.SearchActivity
import android.content.Intent
import android.os.Handler
import android.os.Looper
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.example.food_retrofit.adapter.SliderAdapter
import com.example.food_retrofit.ui.add.AddActivity
import com.example.food_retrofit.ui.cart.CartActivity
import com.example.food_retrofit.ui.detaill.DetailActivity
import com.example.food_retrofit.viewmodel.ProductViewModel

class HomePageActivity : AppCompatActivity() {

    private var allProduct : List<Product> = listOf()
    private lateinit var recyclerCategory: RecyclerView
    private lateinit var recyclerPopular: RecyclerView
    private lateinit var viewModel : ProductViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_page)
        val viewPager = findViewById<ViewPager2>(R.id.viewPager)

        val imageList = listOf(
            R.drawable.img_1,
            R.drawable.img_2,
            R.drawable.img_3
        )

        val adapter = SliderAdapter(imageList)
        viewPager.adapter = adapter
        val handler = Handler(Looper.getMainLooper())

        val runnable = object : Runnable {
            override fun run() {
                val next = (viewPager.currentItem + 1) % imageList.size
                viewPager.currentItem = next
                handler.postDelayed(this, 3000)
            }
        }

        handler.postDelayed(runnable, 3000)
        viewModel = ViewModelProvider(this)[ProductViewModel::class.java]
        val etSearch = findViewById<RelativeLayout>(R.id.txtSearch)
        etSearch.setOnClickListener {
            startActivity(Intent(this, SearchActivity::class.java))
        }
        val btnCart = findViewById<ImageView>(R.id.btnCart)
        btnCart.setOnClickListener {
            val intent = Intent(this, CartActivity::class.java)
            startActivity(intent)
        }
        val btnAdd = findViewById<ImageView>(R.id.btnAdd)

        btnAdd.setOnClickListener {
            startActivity(Intent(this, AddActivity::class.java))
        }
        // 🔹 Bind views
        recyclerCategory = findViewById(R.id.recyclerCategory)
        recyclerPopular = findViewById(R.id.recyclerPopular)

        // 🔹 Layout
        recyclerCategory.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        recyclerPopular.layoutManager =
            GridLayoutManager(this, 2)
        recyclerPopular.setHasFixedSize(true)
        recyclerCategory.setHasFixedSize(true)
        // 🔹 Load data

        loadProductsSuspend()
        loadCategoriesSuspend()
    }

    private fun loadProductsSuspend() {
        viewModel.getProductsSuspend {
            if (it.isSuccessful && it.body() != null) {

                val data = it.body()!!
                allProduct = data

                recyclerPopular.adapter = ProductAdapter(
                    list = data,

                    onClick = { product ->
                        val intent = Intent(this@HomePageActivity, DetailActivity::class.java)
                        intent.putExtra("product", product)
                        startActivity(intent)
                    },

                    onEdit = { product ->
                        val intent = Intent(this@HomePageActivity, AddActivity::class.java)
                        intent.putExtra("product", product)
                        startActivity(intent)
                    },

                    onDelete = { product ->
                        deleteProductSuspend(product.id)
                    }
                )
            }
        }
    }
    private fun loadProducts() {
        viewModel.getProducts()
            .enqueue(object : Callback<List<Product>> {

                override fun onResponse(
                    call: Call<List<Product>>,
                    response: Response<List<Product>>
                ) {

                    if (response.isSuccessful && response.body() != null) {

                        val data = response.body()!!
                        allProduct = data

                        recyclerPopular.adapter = ProductAdapter(
                            list = data,

                            onClick = { product ->
                                val intent = Intent(this@HomePageActivity, DetailActivity::class.java)
                                intent.putExtra("product", product)
                                startActivity(intent)
                            },

                            onEdit = { product ->
                                val intent = Intent(this@HomePageActivity, AddActivity::class.java)
                                intent.putExtra("product", product)
                                startActivity(intent)
                            },

                            onDelete = { product ->
                                deleteProductSuspend(product.id)
                            }
                        )
                    }
                }

                override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                    t.printStackTrace()
                }
            })
    }
    private fun loadCategoriesSuspend() {
        viewModel.getCategoriesSuspend {
            if (it.isSuccessful && it.body() != null) {

                val categoryList = it.body()!!
                recyclerCategory.adapter =
                    CategoryAdapter(categoryList) { category ->
                        filterProduct(category)
                    }
            }
        }
    }
    private fun filterProduct(category: Category) {

        val filteredList = allProduct.filter {
            it.category.id == category.id
        }

        recyclerPopular.adapter = ProductAdapter(
            list = filteredList,

            // 🔹 DETAIL
            onClick = { product ->
                val intent = Intent(this@HomePageActivity, DetailActivity::class.java)
                intent.putExtra("product", product)
                startActivity(intent)
            },

            // 🔹 EDIT
            onEdit = { product ->
                val intent = Intent(this@HomePageActivity, AddActivity::class.java)
                intent.putExtra("product", product)
                startActivity(intent)
            },

            // 🔹 DELETE
            onDelete = { product ->
                deleteProductSuspend(product.id)
            }
        )
    }
    private fun deleteProductSuspend(id: Int) {
        viewModel.deleteProductSuspend(id) {
            if (it.isSuccessful) {
                Toast.makeText(this, "Deleted ", Toast.LENGTH_SHORT).show()
                loadProductsSuspend()
            } else {
                Toast.makeText(this, "Delete failed ", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        loadProductsSuspend()
    }
}