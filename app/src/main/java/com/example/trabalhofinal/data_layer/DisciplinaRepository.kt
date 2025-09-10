package com.example.aula17.data_layer

class DisciplinaRepository(private val disciplinaDao: DisciplinaDao) {
    val allItems = disciplinaDao.getAllItems()

    suspend fun insert(disciplina: Disciplina) {
        disciplinaDao.insert(disciplina)
    }
//
//    suspend fun update(item: Item) {
//        itemDao.update(item)
//    }
//
    /* ... */
    suspend fun deleteById(id: Int) {
        disciplinaDao.deleteById(id)
    }
//    suspend fun clearAll(){
//        itemDao.clearAll()
//    }
//    suspend fun updateComprado(id: Int, comprado: Boolean){
//        itemDao.updateComprado(id,comprado)
//    }

}
