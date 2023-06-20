package com.rsmartin.corrutinaskotlin

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.lang.StringBuilder
import java.time.Instant
import kotlin.system.measureTimeMillis

class MainActivity : AppCompatActivity() {

    private val TAG = "CoroutinesTAG"
    private lateinit var mText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coroutine_main)
        setUpView()

    }



    private fun setUpView() {
        mText = findViewById(R.id.mainTextView)
    }

    private fun updateTextView(message: String = "") {
        mText.text = StringBuilder(mText.text.toString()).append(message).append("\n")
    }
}