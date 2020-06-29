package com.example.visahackathon

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem

class Splash : AppCompatActivity() {

    lateinit var handler: Handler
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash)

        handler = Handler()
        handler.postDelayed({
            val intent = Intent(this, Register::class.java)
            startActivity(intent)
            finish()
        }, 3000)
    }
}