package com.rsmartin.corrutinaskotlin

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.buffer
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.conflate
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.reduce
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.flow.zip
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull
import java.util.concurrent.*
import kotlin.system.measureTimeMillis

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
        /*runBlocking {
            (1..3).asFlow()
                .filter { request -> request > 1 }
                .map { request -> performRequest(request) }
                .collect { response -> println(response) }
        }*/

        //Operador Transforme, es el mas general y mas opciones nos da, podemos imitar otros operadores
        /*runBlocking {
            (1..3).asFlow()
                .transform { request ->
                    emit("Making request $request")
                    emit(performRequest(request))
                }
                .collect { response -> println(response) }
        }*/

        //Operador Take, cancela el flow cuando alcanza el limite que le indicamos
        /*runBlocking {
            (1..3).asFlow()
                .take(2)
                .collect { response -> println(response) }
        }*/

        //Operador terminal toList, pasar a una lista el flow que recibimos
        /*runBlocking {
            var list: List<Int> = (1..3).asFlow().toList()
            println(list)
        }*/

        //Operador terminal first, coge el primer elemento del flow y el resto los desecha
        /*runBlocking {
            var num = (6..90).asFlow().first()
            println(num)
        }*/

        //Operador terminal reduce, opera con los datos de los flow
        // Acumula valor comenzando con el primer elemento y aplicando la operación al valor acumulador actual y cada elemento.
        // en este caso coge el primer flow y lo suma con el sig
        /*runBlocking {
            var num = (1..3).asFlow()
                .reduce { a, b -> a+b }
            println(num)
        }*/

        //Flow es secuencial, si no pasa filter no llega a map ni collect
        /*runBlocking {
            (1..5).asFlow()
                .filter { i->
                    println("Filtrado $i")
                    i%2 == 0
                }
                .map { i->
                    println("Map $i")
                    "String $i"
                }
                .collect { i->
                    println("Collect $i")
                }
        }*/

        //Buffer
        /*runBlocking {
            val time = measureTimeMillis {
                firstFlow()
                    .buffer()
                    .collect { value->
                        delay(3000)
                        println(value)
                    }
            }
            println("$time ms")
        }*/

        //Conflate, procesa el primer y ultimo valor
        /*runBlocking {
            val time = measureTimeMillis {
                firstFlow()
                    .conflate()
                    .collect { value->
                        delay(3000)
                        println(value)
                    }
            }
            println("$time ms")
        }*/

        //CollectLatest, procesa el ultimo valor
        /*runBlocking {
            val time = measureTimeMillis {
                firstFlow()
                    .collectLatest { value->
                        println("Collecting $value")
                        delay(3000)
                        println("Finalizado $value")
                    }
            }
            println("$time ms")
        }*/

        // Zip, combina los valores correspondientes de dos flujos
        /*val nums: Flow<Int> = (1..3).asFlow()
        val strs: Flow<String> = flowOf("Uno","Dos","Tres")
        runBlocking {
            nums.zip(strs) {
                a,b -> "Zip $a -> $b"
            }.collect { println(it) }
        }*/

        /*runBlocking {
            var ejemplo = (1..3).asFlow().map { requestFlow(it) }
        }*/

        //FlatMapConcat, espera que se complete el flujo interno antes de comenzar a recopilar el siguiente
        /*runBlocking {
            val startTime = System.currentTimeMillis()
            (1..3).asFlow()
                .onEach { delay(100) }
                .flatMapConcat { requestFlow(it) }
                .collect { value -> println("$value at ${System.currentTimeMillis() - startTime} ms from start") }
        }*/

        //FlatMapMerge,
        runBlocking {
            val startTime = System.currentTimeMillis()
            (1..3).asFlow()
                .onEach { delay(100) }
                .flatMapMerge { requestFlow(it) }
                .collect { value -> println("$value at ${System.currentTimeMillis() - startTime} ms from start") }
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

    fun requestFlow(i : Int): Flow<String> = flow {
        emit("$i: First")
        delay(500)
        emit("$i: Second")
    }

}