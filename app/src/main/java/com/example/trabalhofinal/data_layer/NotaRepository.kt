package com.example.trabalhofinal.data_layer

import com.example.trabalhofinal.data_layer.Nota
import com.example.trabalhofinal.data_layer.NotaDao
import kotlinx.coroutines.flow.Flow


class NotaRepository (private val notaDao: NotaDao){

    val allItems = notaDao.getAllItems()

    suspend fun insert(nota: Nota) {
        notaDao.insert(nota)
    }

//    suspend fun update(item: Item) {
//        itemDao.update(item)
//    }

    suspend fun deleteById(id: Int) {
        notaDao.deleteById(id)
    }

    suspend fun deleteAll(){
        notaDao.deleteAllNotes()
    }

    fun getNoteById(id: Int): Flow<Nota> {
        return  notaDao.getNoteById(id)
    }
}