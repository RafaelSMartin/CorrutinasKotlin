package com.rsmartin.corrutinaskotlin

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull
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
        /*runBlocking {
            launch {
                for(j in 1..3) {
                    println("No estoy bloqueado $j")
                    delay(1000)
                }
            }
            firstFlow().collect { value -> println(value) }
        }*/

        //Cuando ejecutamos collect se empieza a ejecutar la funcion de emision cold flow
        /*runBlocking {
            println("Llmando Flow...")
            val flow = firstFlow()
            println("Collect...")
            flow.collect { value -> println(value) }
            println("Collect again...")
            flow.collect { value -> println(value) }
        }*/

        // Cancelamos sin necesitar de terminar la operacion
        /*runBlocking {
            withTimeoutOrNull(2500){
                firstFlow().collect { value -> println(value) }
            }
            println("Finalizado")
        }*/

        /*runBlocking {
            //secondFlow().collect { value -> println(value) }
            thirdFlow().collect { value -> println(value) }
        }*/

        // Operador Map, convierte un flow en otro flow
        /*runBlocking {
            (1..3).asFlow()
                .map { request -> performRequest(request) }
                .collect { response -> println(response) }
        }*/

        // Operador Filter, indicamos una expresion logica para filtrar y
        // devolverlo como flow, se puede compaginar con map
        runBlocking {
            (1..3).asFlow()
                .filter { request -> request > 1 }
                .map { request -> performRequest(request) }
                .collect { response -> println(response) }
        }

        //Operador Transforme, es el mas general y mas opciones nos da, podemos imitar otros operadores
        runBlocking {
            (1..3).asFlow()
                .transform { request ->
                    emit("Making request $request")
                    emit(performRequest(request))
                }
                .collect { response -> println(response) }
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
    Flow es un call asincrono data stream que se ejecuta de manera secuencial en la misma
    corrutina y emite valores y finaliza normalmente con el estado completado o lanzando
    una excepcion, se focaliza en un data stream que emite valores secuencialmente.
    Flow necesita siempre un constructor, si no se llama a collect no se ejecuta nada del flow
    */

    // Emite un numero cada segundo
    fun firstFlow(): Flow<Int> = flow {
        for (i in 1..3) {
            delay(1000)
            emit(i)
        }
    }

    // Un segundo constructor para Flow
    fun secondFlow(): Flow<Int> {
        return flowOf(1, 2, 3)
    }

    // Una tercera constructor para Flow
    fun thirdFlow(): Flow<Int> {
        return (1..3).asFlow()
    }

    /*
    Devuelve un string con el numero entero de request
     */
    suspend fun performRequest(request: Int): String {
        delay(1000)
        return "response $request"
    }

}