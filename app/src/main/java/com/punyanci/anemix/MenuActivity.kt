package com.punyanci.anemix

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.Profile
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MenuActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu)

        val menu_anemix = findViewById<CardView>(R.id.menu_anemix)
        menu_anemix.setOnClickListener {
            val intent = Intent(this, Info::class.java)
            startActivity(intent)
        }
        val menu_dataset = findViewById<CardView>(R.id.menu_dataset)
        menu_dataset.setOnClickListener {
            val intent = Intent(this, Dataset::class.java)
            startActivity(intent)
        }
        val menu_fitur = findViewById<CardView>(R.id.menu_fitur)
        menu_fitur.setOnClickListener {
            val intent = Intent(this, Fitur::class.java)
            startActivity(intent)
        }
        val menu_model = findViewById<CardView>(R.id.menu_model)
        menu_model.setOnClickListener {
            val intent = Intent(this, Model::class.java)
            startActivity(intent)
        }
        val menu_simulasi = findViewById<CardView>(R.id.menu_simulasi)
        menu_simulasi.setOnClickListener {
            val intent = Intent(this, Simulasi::class.java)
            startActivity(intent)
        }
        val profile_nci = findViewById<CardView>(R.id.profile_nci)
        profile_nci.setOnClickListener {
            val intent = Intent(this, ProfileNci::class.java)
            startActivity(intent)
        }
    }
}