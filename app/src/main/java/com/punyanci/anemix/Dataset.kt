package com.punyanci.anemix

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.viewpager2.widget.ViewPager2

class Dataset : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_dataset)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        val viewPager: ViewPager2 = findViewById(R.id.viewPager)

        val images = intArrayOf(
            R.drawable.img_dataset,
            R.drawable.img_dataset2,
        )

        val adapter = ImageSliderAdapter(this, images)
        viewPager.adapter = adapter

        val backButton: ImageButton = findViewById(R.id.back_button)
        backButton.setOnClickListener {
            val intent = Intent(this@Dataset, MenuActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    fun openKaggleLink(view: View) {
        val url = "https://www.kaggle.com/datasets/ehababoelnaga/anemia-types-classification"
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = Uri.parse(url)
        startActivity(intent)
    }

}