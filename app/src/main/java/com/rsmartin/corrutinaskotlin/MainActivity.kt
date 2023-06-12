package com.rsmartin.corrutinaskotlin

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import java.lang.StringBuilder
import java.time.Instant

class MainActivity : AppCompatActivity() {

    private val TAG = "CoroutinesTAG"
    private lateinit var mText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coroutine_main)
        setUpView()

        blokingExample()
        suspendExample()
        runBlockingExample()
    }

    private fun setUpView() {
        mText = findViewById(R.id.mainTextView)
    }

    private fun updateTextView(message: String = "") {
        mText.text = StringBuilder(mText.text.toString()).append(message).append("\n")
    }

    /*
    Bloquea el hilo principal lo que tarde longTaskWithMessage
     */
    fun blokingExample() {
        updateTextView("blokingExample")
        updateTextView("Tarea1 " + Thread.currentThread().name + Instant.now())
        longTaskWithMessage("Tarea2 ")
        updateTextView("Tarea3 " + Thread.currentThread().name + Instant.now())
    }

    private fun longTaskWithMessage(message: String) {
        Thread.sleep(4000)
        updateTextView(message + Thread.currentThread().name + Instant.now())
    }

    /*
    Suspend no nos va a bloquear el hilo principal, sirve para corrutinas que estaran en el centro de ella
    Se pueden pausar y reanudar mas adelante,
    Se puede ejecutar cualquier operacion de larga duracion sin bloquear
    Necesitan una corrutina para ser lanzadas
    *//*
    RunBlocking nos permite ejecutar corrutina y bloquea hasta la finalizacion
    Bloquea el hilo pero se ejecuta en orden
    Se va a ejecutar una corrutina pero en el hilo en el q estamos bloqueando
    */
    fun suspendExample() {
        updateTextView("suspendExample&runBlocking")
        updateTextView("Tarea1 " + Thread.currentThread().name + Instant.now())
        runBlocking {
            delayCoroutine("Tarea2 ")
        }
        updateTextView("Tarea3 " + Thread.currentThread().name + Instant.now())
    }

    private suspend fun delayCoroutine(message: String) {
        delay(timeMillis = 4000)
        updateTextView(message + Thread.currentThread().name + Instant.now())
    }

    /*
        RunBlocking se jecuta de otra manera en codigo
    */
    fun runBlockingExample() = runBlocking {
        updateTextView("runBlocking")
        updateTextView("Tarea1 " + Thread.currentThread().name + Instant.now())
        delayCoroutine("Tarea2 ")
        updateTextView("Tarea3 " + Thread.currentThread().name + Instant.now())
    }


}