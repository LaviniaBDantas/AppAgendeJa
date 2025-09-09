package com.example.aula17.data_layer

class PacienteRepository(private val pacienteDao: PacienteDao) {
    val allItems = pacienteDao.getAllItems()

    suspend fun insert(paciente: Paciente) {
        pacienteDao.insert(paciente)
    }
//
//    suspend fun update(item: Item) {
//        itemDao.update(item)
//    }
//
//    /* ... */
//    suspend fun deleteById(id: Int) {
//        itemDao.deleteById(id)
//    }
//    suspend fun clearAll(){
//        itemDao.clearAll()
//    }
//    suspend fun updateComprado(id: Int, comprado: Boolean){
//        itemDao.updateComprado(id,comprado)
//    }

}
