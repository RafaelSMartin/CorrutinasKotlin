package com.rsmartin.corrutinaskotlin

import android.app.Application
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/*
* Scope-> el ambito donde se va a poder ejecutar esas corrutinas
* un ambito muy amplio es el de application se usa GlobalScope q estara vivo
* durante el tiempo de vida de la app, lo q se ejecute se seguira ejecutando
* mientras siga viva la app.
*
* Builder de corrutina-> es el q nos permite crear esa corrutina para poder ejecutar
* codigo dentro. hay varios, launch crea el contructor por defecto para crear la
* corrutina.
*
* Funciones de suspension-> van dentro del scope, funcoines que bloquean la ejecucion
* de la corrutina pero si se hacen bien no bloquean el hilo en el q se estan ejecutando.
*
* Dispatchers-> que se vaya a bloquear o no el hilo depende del dispatcher que declaremos
* en las corrutinas y funciones de suspension. El por defecto en globalscope es
* Dispatchers.default que lo que hace es salirse a otro hilo.
* Lo normal es q la corrutina se ejecute en el hilo principal y las tareas en otro sitio,
* de tal forma que todas las actualuzaciones que hagamos con los resultados de esas tareas
* las podamos utilizar en el hilo principal.
* Dispatchers.Main -> el hilo principal de android.
* Dispatchers.Default-> para ejecutar tareas pesadas que requieres uso intensivo de cpu,
* como algoritmos, etc...
* Dispatchers.IO -> para tareas bloqueantes, de tipo llamar bbdd y servicios, no estamos
* usando la cepu pero bloqueamos el hilo esperando una respuesta.
* Usamos withContext para cambiar a otro hilo y modificar contexto para ciertas lineas
* de codigo, identificando un dispatchers.
*
*
* */


class App : Application() {

    override fun onCreate() {
        super.onCreate()
    }
}