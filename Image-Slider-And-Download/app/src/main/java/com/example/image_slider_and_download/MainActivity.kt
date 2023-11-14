package com.example.image_slider_and_download

import android.Manifest
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream

class MainActivity : AppCompatActivity() {
    private lateinit var viewPager: ViewPager2
    private lateinit var downloadButton: Button
    private lateinit var itemCountTextView: TextView

    private val images = listOf(
        R.drawable.image1,
        R.drawable.image2,
        R.drawable.image3,
        R.drawable.image4
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewPager = findViewById(R.id.viewPager)
        downloadButton = findViewById(R.id.downloadButton)
        itemCountTextView = findViewById(R.id.itemCountTextView)

        val adapter = ImageSliderAdapter(images)
        viewPager.adapter = adapter

        val itemText = "1 / ${images.size}"
        itemCountTextView.text = itemText

        downloadButton.setOnClickListener {
            // 현재 보고 있는 이미지 다운로드
            val currentImage = images[viewPager.currentItem]
            downloadImage(currentImage)
        }

        // ViewPager2 페이지 변경 이벤트 처리
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)

                // 현재 아이템의 번호와 전체 아이템 수 표시
                val currentItemNumber = position + 1
                val totalItemCount = images.size
                val itemText = "$currentItemNumber / $totalItemCount"
                itemCountTextView.text = itemText
            }
        })

        // 저장소에 쓰기 권한 확인 및 요청
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) !=
            PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                1
            )
        }
    }

    private fun downloadImage(imageResId: Int) {
        val bitmap = BitmapFactory.decodeResource(resources, imageResId)

        val fileOutputStream: OutputStream
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            val resolver = contentResolver
            val contentValues = ContentValues().apply {
                put(MediaStore.Images.Media.DISPLAY_NAME, "image_${System.currentTimeMillis()}.jpg")
                put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg")
                put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
            }
            val imageUri =
                resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)
            fileOutputStream = imageUri?.let { resolver.openOutputStream(it) } ?: return
        } else {
            val imagesDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image = File(imagesDir, "image_${System.currentTimeMillis()}.jpg")
            fileOutputStream = FileOutputStream(image)
        }

        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream)
        fileOutputStream.close()

        Toast.makeText(this, "이미지가 저장되었습니다.", Toast.LENGTH_SHORT).show()
    }
}