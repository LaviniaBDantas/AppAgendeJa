package com.example.trabalhofinal
import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import com.example.aula17.data_layer.Disciplina
import com.example.aula17.data_layer.DisciplinaComNotas
import com.example.aula17.data_layer.DisciplinaRepository
import com.example.aula17.data_layer.Nota
import com.example.aula17.data_layer.TrabFinalDatabase
import com.example.trabalhofinal.data_layer.NotaRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Home()
        }
    }
}

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

    fun insereNota (texto: String, idDisciplina: Int){
        viewModelScope.launch {
            val novaNota = Nota(
                texto = texto,
                idDisciplina = idDisciplina
            )
            notaRepository.insert(novaNota)
        }
    }

    fun deletaNota(id: Int){
        viewModelScope.launch {
            notaRepository.deleteById(id)
        }
    }

    fun deletaTodasAsNotas(){
        viewModelScope.launch {
            notaRepository.deleteAll()
        }
    }
}


@Composable
fun AlertDialogExample(
    onDismissRequest: () -> Unit,
    onConfirmation: (String) -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
) {
    val userInput = remember { mutableStateOf("") }

    AlertDialog(
        icon = {
            Icon(icon, contentDescription = "Example Icon")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
            TextField(value = userInput.value, onValueChange = { userInput.value = it })
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation(userInput.value)
                }
            ) {
                Text("Confirmar")
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text("Sair")
            }
        }
    )
}

@Composable
fun ListaDisciplinas(
    listaDisciplinas: List<DisciplinaComNotas>,     onDeleteDisciplina: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp), verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        if (listaDisciplinas.isEmpty()) {
            Text("Lista vazia")
        } else {
            listaDisciplinas.forEach { element ->
                Card(
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .fillMaxWidth()
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
//                            .background(color = MaterialTheme.colorScheme.primaryContainer)
                            .fillMaxWidth()
                            .padding(start = 10.dp)
                        ,
                    ) {
                        Text(text = element.disciplina.nome, style = MaterialTheme.typography.titleMedium, modifier = Modifier.padding(10.dp))
                        IconButton(onClick = {  onDeleteDisciplina(element.disciplina.id)  }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete")
                        }
                    }
                }
            }
        }
    }
}



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(meuViewModel: MeuViewModel = viewModel()) {
    val uiState by meuViewModel.uiState.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("Lista de Disciplinas")
                }
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.primary,
            ) {
                Text(
                    modifier = Modifier
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    text = "Aq vai ficar nossa barra de navegacao",
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { meuViewModel.botaoAddDisciplina() }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) {  innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(20.dp)
        ) {

            ListaDisciplinas(listaDisciplinas = uiState.lista, onDeleteDisciplina = { id -> meuViewModel.deletaDisciplina(id) })
        }
        if (uiState.showDialog){
            AlertDialogExample(
                onDismissRequest = {  meuViewModel.botaoSair()},
                onConfirmation = { disciplinaInserida->meuViewModel.insereDisciplina(disciplinaInserida) },
                dialogTitle = "Nova Disciplina",
                dialogText = "Deseja inserir qual disciplina?",
                icon = Icons.Default.Add
            )
        }
    }
}