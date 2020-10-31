package com.rsmartin.corrutinaskotlin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainViewModel : ViewModel() {

    fun doSomething() {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                //mi llamada a bbdd o retrofit
            }
            //Hacer lo q quisieramos incluso actualizar la ui
            //aqui ya estamos en el hilo principal de nuevo
        }
    }

}