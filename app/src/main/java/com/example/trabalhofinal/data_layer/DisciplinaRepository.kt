package com.example.trabalhofinal.data_layer

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
    suspend fun updateFavoritado(id: Int, favoritado: Boolean){
        disciplinaDao.updateFavoritado(id,favoritado)
    }

}
