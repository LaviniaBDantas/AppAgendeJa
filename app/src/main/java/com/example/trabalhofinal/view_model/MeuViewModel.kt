package com.example.trabalhofinal.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.trabalhofinal.data_layer.Disciplina
import com.example.trabalhofinal.data_layer.DisciplinaComNotas
import com.example.trabalhofinal.data_layer.DisciplinaRepository
import com.example.trabalhofinal.data_layer.Nota
import com.example.trabalhofinal.data_layer.TrabFinalDatabase
import com.example.trabalhofinal.data_layer.NotaRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MeuUiState(
val lista: List<DisciplinaComNotas> = listOf(),
val showDialog: Boolean = false, // State to control dialog visibility
)

class MeuViewModel(application: Application) : AndroidViewModel(application) {
private val _uiState = MutableStateFlow(MeuUiState())
val uiState = _uiState.asStateFlow()

private val disciplinaRepository: DisciplinaRepository

private val notaRepository: NotaRepository

init {
    val db = Room.databaseBuilder(
        application.applicationContext,
        TrabFinalDatabase::class.java,
        "meu_app_database"
    ).build()
    disciplinaRepository = DisciplinaRepository(db.disciplinaDao())
    notaRepository = NotaRepository(db.notaDao())
    viewModelScope.launch {
        disciplinaRepository.allItems.collect { listaDB ->
            _uiState.update {
                it.copy(lista = listaDB)
            }
        }
    }
}

fun botaoAddDisciplina(){
    _uiState.update { it.copy(showDialog = true) }
}
fun insereDisciplina(disciplinaInserida : String    ) {
    viewModelScope.launch {
        disciplinaRepository.insert(
            disciplina = Disciplina(
                nome = disciplinaInserida,
                favoritada = false
            )
        )
    }
    botaoSair()
}

fun botaoSair(){
    _uiState.update { it.copy(showDialog = false) }
}
fun deletaDisciplina(id: Int){
    viewModelScope.launch {
        disciplinaRepository.deleteById(id)
    }
}

fun toggleFavoritado(disciplina: Disciplina){
    viewModelScope.launch {
        disciplinaRepository.updateFavoritado(disciplina.id, !disciplina.favoritada)
    }
}

fun insertNote (texto: String, idDisciplina: Int){
    viewModelScope.launch {
        val novaNota = Nota(
            texto = texto,
            idDisciplina = idDisciplina
        )
        notaRepository.insert(novaNota)
    }
}

fun deleteNote(id: Int){
    viewModelScope.launch {
        notaRepository.deleteById(id)
    }
}

fun deleteAllNotesByDisciplina(idDisciplina: Int){
    viewModelScope.launch {
        notaRepository.deleteAllNotesByDisciplina(idDisciplina)
    }
}
fun getNotesByDisciplina(idDisciplina: Int): Flow<List<DisciplinaComNotas>> {
    return disciplinaRepository.getNotesByDisciplina(idDisciplina)
}

fun getNameDisciplina(idDisciplina: Int): Flow<String> {
    return disciplinaRepository.getNameDisciplinaById(idDisciplina)
}

fun getNoteById(id :Int): Flow<Nota>{
    return notaRepository.getNoteById(id)
}

private val _searchResults = MutableStateFlow<List<Disciplina>>(emptyList())
val searchResults = _searchResults.asStateFlow()

fun searchDisciplina(query: String) {
    viewModelScope.launch {
        disciplinaRepository.searchDisciplina(query).collect { lista ->
            _searchResults.value = lista
        }
    }
}
}
