package com.rsmartin.corrutinaskotlin

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //por defecto usa Dispatchers.Main
        lifecycleScope.launch() {
            val result = withContext(Dispatchers.IO) {
                //mi llamada a bbdd o retrofit
            }
            updateTextView("MainActivity")
        }
    }

    fun updateTextView(message: String) {
        mainTextView.text = "desde la $message con corrutina IO"
    }

}