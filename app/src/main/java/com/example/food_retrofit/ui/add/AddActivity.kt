package com.example.food_retrofit.ui.add

import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.food_retrofit.R
import com.example.food_retrofit.data.model.Category.Category
import com.example.food_retrofit.data.model.Product.Product
import com.example.food_retrofit.data.model.Product.ProductRequest
import com.example.food_retrofit.data.remote.RetrofitClient
import com.example.food_retrofit.utils.toBody
import com.example.food_retrofit.viewmodel.ProductViewModel
import com.github.dhaval2404.imagepicker.ImagePicker
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class AddActivity : AppCompatActivity() {

    private lateinit var imgPreview: ImageView
    private lateinit var etName: EditText
    private lateinit var etPrice: EditText
    private lateinit var etDesc: EditText
    private lateinit var etRating: EditText
    private lateinit var spCategory: Spinner
    private lateinit var btnSave: Button

    private var imageUri: Uri? = null
    private var categoryList: List<Category> = listOf()
    private var product: Product? = null
    private lateinit var viewModel : ProductViewModel


    private val imagePickerLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->

            if (result.resultCode == RESULT_OK) {

                val uri = result.data?.data

                if (uri != null) {
                    imageUri = uri

                    Glide.with(this)
                        .load(uri)
                        .placeholder(R.drawable.plus)
                        .into(imgPreview)

                } else {
                    Toast.makeText(this, "Image not found ", Toast.LENGTH_SHORT).show()
                }

            } else {
                Toast.makeText(this, "Cancelled ", Toast.LENGTH_SHORT).show()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        viewModel = ViewModelProvider(this)[ProductViewModel::class.java]

        imgPreview = findViewById(R.id.imgProductPreview)
        etName = findViewById(R.id.etProductName)
        etPrice = findViewById(R.id.etProductPrice)
        etDesc = findViewById(R.id.etProductDesc)
        etRating = findViewById(R.id.etProductRating)
        spCategory = findViewById(R.id.spCategory)
        btnSave = findViewById(R.id.btnSaveProduct)

        product = intent.getSerializableExtra("product") as? Product


        product?.let {
            etName.setText(it.title)
            etDesc.setText(it.description)
            etPrice.setText(it.price.toString())
            etRating.setText(it.rating.toString())

            if (it.images.isNotEmpty()) {
                Glide.with(this)
                    .load(it.images[0].image_url)
                    .into(imgPreview)
            }

            btnSave.text = "Update Product "
        }

        loadCategories()

        // 🔥 PICK IMAGE
        imgPreview.setOnClickListener {
            ImagePicker.with(this)
                .galleryOnly()
                .compress(1024)
                .createIntent { intent ->
                    imagePickerLauncher.launch(intent)
                }
        }

        btnSave.setOnClickListener {
            saveProduct()
        }
    }

    private fun loadCategories() {
        viewModel.getCategoriesSuspend {

            if (it.isSuccessful && it.body() != null) {

                categoryList = it.body()!!

                val adapter = ArrayAdapter(
                    this@AddActivity,
                    android.R.layout.simple_spinner_dropdown_item,
                    categoryList.map { it.name }
                )

                spCategory.adapter = adapter

                product?.let {
                    val index = categoryList.indexOfFirst { c ->
                        c.id == it.category.id
                    }
                    if (index != -1) spCategory.setSelection(index)
                }
            }
        }
    }

    private fun saveProduct() {


        if (product == null && imageUri == null) {
            Toast.makeText(this, "Select image first ❗", Toast.LENGTH_SHORT).show()
            return
        }


        val nameText = etName.text.toString().trim()
        val descText = etDesc.text.toString().trim()
        val priceText = etPrice.text.toString().trim()
        val ratingText = etRating.text.toString().trim()

        if (nameText.isEmpty() || priceText.isEmpty() || ratingText.isEmpty()) {
            Toast.makeText(this, "Please fill all required fields ❗", Toast.LENGTH_SHORT).show()
            return
        }

        if (categoryList.isEmpty()) {
            Toast.makeText(this, "Category not loaded ❗", Toast.LENGTH_SHORT).show()
            return
        }


        val title = nameText.toRequestBody("text/plain".toMediaTypeOrNull())
        val subtitle = descText.toRequestBody("text/plain".toMediaTypeOrNull())
        val price = priceText.toRequestBody("text/plain".toMediaTypeOrNull())
        val rating = ratingText.toRequestBody("text/plain".toMediaTypeOrNull())

        val categoryId = categoryList[spCategory.selectedItemPosition].id
            .toString()
            .toRequestBody("text/plain".toMediaTypeOrNull())


        val imagePart = if (imageUri != null) {
            val file = try {
                uriToFile(imageUri!!)
            } catch (e: Exception) {
                e.printStackTrace()
                Toast.makeText(this, "Failed to read image ❌", Toast.LENGTH_SHORT).show()
                return
            }

            if (!file.exists() || file.length() == 0L) {
                Toast.makeText(this, "Invalid image file ❌", Toast.LENGTH_SHORT).show()
                return
            }

            Log.d("UPLOAD", "FILE PATH: ${file.absolutePath}")
            Log.d("UPLOAD", "FILE SIZE: ${file.length()}")

            val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())

            MultipartBody.Part.createFormData(
                "images[]",
                file.name,
                requestFile
            )
        } else null


        if (product == null) {

            viewModel.addProductsSuspend(
                title,
                subtitle,
                price,
                rating,
                categoryId,
                listOf(imagePart!!)
            ) {

                if (it.isSuccessful) {
                    Toast.makeText(this, "Success ", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Failed ", Toast.LENGTH_SHORT).show()
                }
            }

        } else {

            val imageList = if (imagePart != null) listOf(imagePart) else emptyList()

            viewModel.updateProductsSuspend(
                product!!.id,
                title,
                subtitle,
                price,
                rating,
                categoryId,
                imageList
            ) {

                if (it.isSuccessful) {
                    Toast.makeText(this, "Updated ✅", Toast.LENGTH_SHORT).show()
                    finish()
                } else {
                    Toast.makeText(this, "Update failed ❌", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private fun uriToFile(uri: Uri): File {
        val inputStream = contentResolver.openInputStream(uri)
        val file = File(cacheDir, "upload_${System.currentTimeMillis()}.jpg")
        val outputStream = file.outputStream()

        inputStream!!.copyTo(outputStream)

        inputStream.close()
        outputStream.close()

        return file
    }
    }
