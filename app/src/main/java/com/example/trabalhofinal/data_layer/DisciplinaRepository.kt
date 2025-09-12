package com.example.trabalhofinal.data_layer

import kotlinx.coroutines.flow.Flow

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

    fun getNotesByDisciplina(disciplinaId: Int): Flow<List<DisciplinaComNotas>> {
        return disciplinaDao.getNotasByDisciplinaId(disciplinaId)
    }

    fun getNameDisciplinaById(disciplinaId: Int): Flow<String> {
        return disciplinaDao.getNomeDisciplina(disciplinaId)
    }

    fun searchDisciplina(nomeBuscado: String): Flow<List<Disciplina>> {
        return disciplinaDao.searchDisciplina(nomeBuscado)
    }
}
