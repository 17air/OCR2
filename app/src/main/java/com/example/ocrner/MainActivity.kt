package com.example.ocrner

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {

    private lateinit var selectButton: Button
    private lateinit var imageView: ImageView
    private lateinit var ocrTextView: TextView
    private lateinit var nerTextView: TextView

    private val ocrProcessor = OCRProcessor()
    private val nerProcessor by lazy { NERProcessor(this) }

    private val imagePicker = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val data: Intent? = result.data
            val uri: Uri? = data?.data
            if (uri != null) {
                val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                imageView.setImageBitmap(bitmap)
                processImage(bitmap)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        selectButton = findViewById(R.id.btn_select)
        imageView = findViewById(R.id.image_view)
        ocrTextView = findViewById(R.id.text_ocr)
        nerTextView = findViewById(R.id.text_ner)

        selectButton.setOnClickListener {
            if (checkPermissions()) {
                openGallery()
            }
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        imagePicker.launch(intent)
    }

    private fun checkPermissions(): Boolean {
        val permission = Manifest.permission.READ_EXTERNAL_STORAGE
        return if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(permission), 0)
            false
        } else {
            true
        }
    }

    private fun processImage(bitmap: Bitmap) {
        ocrProcessor.process(bitmap) { text ->
            ocrTextView.text = text
            val entities = nerProcessor.predict(text)
            nerTextView.text = entities.joinToString("\n") { "${it.first}: ${it.second}" }
        }
    }
}
