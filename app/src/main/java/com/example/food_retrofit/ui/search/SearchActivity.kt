package com.example.food_retrofit.ui.search

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.food_retrofit.R
import com.example.food_retrofit.adapter.CategoryAdapter
import com.example.food_retrofit.adapter.ProductAdapter
import com.example.food_retrofit.data.model.Category.Category
import com.example.food_retrofit.data.model.Product.Product
import com.example.food_retrofit.data.remote.RetrofitClient
import com.example.food_retrofit.ui.add.AddActivity
import com.example.food_retrofit.ui.detaill.DetailActivity
import com.example.food_retrofit.viewmodel.CartViewModel
import com.example.food_retrofit.viewmodel.ProductViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class SearchActivity : AppCompatActivity() {
    private lateinit var recycleSearch : RecyclerView
    private lateinit var viewModel : ProductViewModel
    private lateinit var etSearch : EditText
    private lateinit var recyclerCategory: RecyclerView
    private var allProducts : List<Product> = listOf()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_search)
        viewModel = ViewModelProvider(this)[ProductViewModel::class.java]
        recycleSearch = findViewById(R.id.recyclerSearch)
        recyclerCategory = findViewById(R.id.recyclerCategory)
        etSearch = findViewById(R.id.etSearch)
        recycleSearch.layoutManager = LinearLayoutManager(this)
        recyclerCategory.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        loadCategories()
        loadProducts()

        etSearch.addTextChangedListener(object : TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                val query = s.toString().lowercase()
                val filtered  = allProducts.filter {
                    it.title.lowercase().contains(query)
                }

                recycleSearch.adapter = ProductAdapter(
                    list = filtered,

                    // 🔹 DETAIL
                    onClick = { product ->
                        val intent = Intent(this@SearchActivity, DetailActivity::class.java)
                        intent.putExtra("product", product)
                        startActivity(intent)
                    },

                    // 🔹 EDIT
                    onEdit = { product ->
                        val intent = Intent(this@SearchActivity, AddActivity::class.java)
                        intent.putExtra("product", product)
                        startActivity(intent)
                    },

                    // 🔹 DELETE
                    onDelete = { product ->
                        Toast.makeText(this@SearchActivity,"Delete from search", Toast.LENGTH_SHORT).show()
                    }
                )
            }
            override fun beforeTextChanged(
                s: CharSequence?,
                start: Int,
                count: Int,
                after: Int
            ) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

            }
        })

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }
    private fun loadCategories() {
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
    private fun loadProducts() {
        viewModel.getProductsSuspend {

            if (it.isSuccessful && it.body() != null) {

                allProducts = it.body()!!

                recycleSearch.adapter = ProductAdapter(
                    list = allProducts,

                    onClick = { product ->
                        val intent = Intent(this, DetailActivity::class.java)
                        intent.putExtra("product", product)
                        startActivity(intent)
                    },

                    onEdit = { product ->
                        val intent = Intent(this, AddActivity::class.java)
                        intent.putExtra("product", product)
                        startActivity(intent)
                    },

                    onDelete = {
                        Toast.makeText(this, "Delete from search", Toast.LENGTH_SHORT).show()
                    }
                )
            }
        }
    }
    private fun filterProduct(category: Category) {

        val filteredList = allProducts.filter {
            it.category.id == category.id
        }

        recycleSearch.adapter = ProductAdapter(
            list = filteredList,

            // 🔹 DETAIL
            onClick = { product ->
                val intent = Intent(this@SearchActivity, DetailActivity::class.java)
                intent.putExtra("product", product)
                startActivity(intent)
            },

            // 🔹 EDIT
            onEdit = { product ->
                val intent = Intent(this@SearchActivity, AddActivity::class.java)
                intent.putExtra("product", product)
                startActivity(intent)
            },

            // 🔹 DELETE
            onDelete = { product ->
                deleteProduct(product.id)
            }
        )
    }
    private fun deleteProduct(id: Int) {

        viewModel.deleteProductSuspend(id) {

            if (it.isSuccessful) {
                Toast.makeText(this, "Deleted ✅", Toast.LENGTH_SHORT).show()
                loadProducts()
            } else {
                Toast.makeText(this, "Delete failed ❌", Toast.LENGTH_SHORT).show()
            }
        }
    }
}