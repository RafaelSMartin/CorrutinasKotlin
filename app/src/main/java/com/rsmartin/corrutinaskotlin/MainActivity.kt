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

        //blokingExample()
        //suspendExample()
        //runBlockingExample()

        //dispatcher()
        //launch()
        //exampleJob()
        //println(measureTimeMillis { asyncAwait() }.toString())
        //println(measureTimeMillis { asyncAwaitDeferred() }.toString())
        //println(measureTimeMillis { withContextExample() }.toString())
        cancelCoroutine()
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

    /*
      Dispatchers en que hilo se va a ejecutar una corrutina
    */
    fun dispatcher() {
        runBlocking {
            Log.d(TAG, "Hilo en el que se ejecuta 1: ${Thread.currentThread().name}")
        }
        runBlocking(Dispatchers.Unconfined) {//nos da igual
            Log.d(TAG, "Hilo en el que se ejecuta 2: ${Thread.currentThread().name}")
        }
        runBlocking(Dispatchers.Default) { //uso intensivo de CPU
            Log.d(TAG, "Hilo en el que se ejecuta 3: ${Thread.currentThread().name}")
        }
        runBlocking(Dispatchers.IO) { //operaciones entrada-salida de datos, webServices, BBDD
            Log.d(TAG, "Hilo en el que se ejecuta 4: ${Thread.currentThread().name}")
        }
        runBlocking(newSingleThreadContext("MiHilo")) { //creando un nuevo hilo
            Log.d(TAG, "Hilo en el que se ejecuta 5: ${Thread.currentThread().name}")
        }
        runBlocking(Dispatchers.Main) { //Hilo principal
            Log.d(TAG, "Hilo en el que se ejecuta 6: ${Thread.currentThread().name}")
        }
    }

    /*
    Launch lanza una corrutina sin bloquear el hilo y nos olvidamos de lo q devuelva
    Devuelve una referencia de la corrutina como Job
    La corrutina es cancelada cuando el resultado del Job es cancelado
    */
    fun launch() {
        Log.d(TAG, "launch")
        Log.d(TAG, "Tarea1 " + Thread.currentThread().name + Instant.now())
        GlobalScope.launch {
            delay(timeMillis = 4000)
            Log.d(TAG, "Tarea2 " + Thread.currentThread().name + Instant.now())
        }
        Log.d(TAG, "Tarea3 " + Thread.currentThread().name + Instant.now())
    }

    /*
    Scope nos ayuda a definir el ciclo de vida de las corrutinas
    Puede durar toda la app o estar vinculado a activity o viewmodel
    Toda corrutina tiene q tener asociado un scope
    GlobalScope esta asociado a la vida util de toda la app, no recomendable
    Job elemento cancelable con un ciclo de vida cancelable a su finalizacion
    Job no produce ningun valor como resultado, representa una pieza de trabajo q necesita ser realizada
    Job se puede cancelar y finalizar su ejecucion
    */

    fun exampleJob() {
        Log.d(TAG, "Job")
        Log.d(TAG, "Tarea1 " + Thread.currentThread().name + Instant.now())
        val job = GlobalScope.launch {
            delay(timeMillis = 4000)
            Log.d(TAG, "Tarea2 " + Thread.currentThread().name + Instant.now())
        }
        Log.d(TAG, "Tarea3 " + Thread.currentThread().name + Instant.now())
        job.cancel() // no sale Tarea2 porque se cancela antes de terminar
    }

    /*
    Async permite ejecutar de forma asincrona y con await esperar el resultado del metodo dentro de awayt
    Permite escribir de forma sincrona codigo que se ejecuta asincronamente
    */
    fun asyncAwait() = runBlocking {
        val num1 = async { calculateHard() }.await()
        val num2 = async { calculateHard() }.await()
        println("resultado: ${num1 + num2}")
    }

    suspend fun calculateHard(): Int {
        delay(2000)
        return 15
    }

    /*
    Deferred es devuelto por Async-await, es un futuro cancelable sin bloqueo.
    Deferred es como un Job, la forma de obtenerlo es por el await
    */
    fun asyncAwaitDeferred() = runBlocking {
        val num1: Deferred<Int> = async { calculateHard() }
        val num2: Deferred<Int> = async { calculateHard() }
        val result: Int = num1.await() + num2.await()
        println("resultado: ${result}")
    }

    /*
    WithContext ejecuta la linea y hasta no obtener resultado se detiene.
    */
    fun withContextExample() = runBlocking {
        val num1 = withContext(Dispatchers.IO) { calculateHard() }
        val num2 = withContext(Dispatchers.IO) { calculateHard() }
        val result: Int = num1 + num2
        println("resultado: ${result}")
    }

    /*
    Cancelar corrutinas
    */
    fun cancelCoroutine() {
        runBlocking {
            val job: Job = launch {
                repeat(1000) { i ->
                    println("job: $i")
                    delay(500L)
                }
            }
            delay(1400)
            job.cancel()
            println("main cansado de esperar")
        }
    }
}