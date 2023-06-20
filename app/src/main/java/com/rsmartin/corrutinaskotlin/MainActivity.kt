package com.rsmartin.corrutinaskotlin

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.concurrent.*

class MainActivity : AppCompatActivity() {

    private val TAG = "CoroutinesTAG"
    private lateinit var mText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coroutine_main)

        //show()
        /*runBlocking {
            runAsynchronous().forEach { i -> println(i) }
        }*/

        //Lanza una corrutina asincrona y luego llama al flow con collect
        runBlocking {
            launch {
                for(j in 1..3) {
                    println("No estoy bloqueado $j")
                    delay(1000)
                }
            }
            firstFlow().collect { value -> println(value) }
        }
    }

    fun show() {
        //listar().forEach { i->println(i) }
        secuencia().forEach { i -> println(i) }
    }

    /*
    Listas y secuencias son sincronas y bloquean
    */
    fun listar(): List<Int> = listOf(3, 78, 98)

    fun secuencia(): Sequence<Int> = sequence {
        for (i in 1..3) {
            Thread.sleep(1000)
            yield(i)
        }
    }

    /*
    Listas con corrutinas de manera asincrona
    */
    suspend fun runAsynchronous(): List<Int> {
        return runBlocking {
            delay(1000)
            return@runBlocking listOf(1, 2, 3)
        }
    }

    /*
    Flow es un call asincrono data string que se ejecuta de manera secuencial en la misma
    corrutina y emite valores y finaliza normalmente con el estado completado o lanzando
    una excepcion, se focaliza en un data string que emite valores secuencialmente.
    Flow necesita siempre un constructor
    */

    // Emite un numero cada segundo
    fun firstFlow(): Flow<Int> = flow {
        for (i in 1..3) {
            delay(1000)
            emit(i)
        }
    }
}