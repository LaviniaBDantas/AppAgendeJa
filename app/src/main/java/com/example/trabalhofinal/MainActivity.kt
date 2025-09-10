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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import com.example.aula17.data_layer.Disciplina
import com.example.aula17.data_layer.DisciplinaComNotas
import com.example.aula17.data_layer.DisciplinaRepository
import com.example.aula17.data_layer.TrabFinalDatabase
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
    val showDialog: Boolean = false // State to control dialog visibility
)

class MeuViewModel(application: Application) : AndroidViewModel(application) {
    private val _uiState = MutableStateFlow(MeuUiState())
    val uiState = _uiState.asStateFlow()

    private val disciplinaRepository: DisciplinaRepository

    init {
        val db = Room.databaseBuilder(
            application.applicationContext,
            TrabFinalDatabase::class.java,
            "meu_app_database"
        ).build()
        disciplinaRepository = DisciplinaRepository(db.disciplinaDao())
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
    fun insereDisciplina() {
        viewModelScope.launch {
            disciplinaRepository.insert(
                disciplina = Disciplina(
                    nome = "PDM",
                    favoritada = false
                )
            )
        }
        botaoSair()
    }

    fun botaoSair(){
        _uiState.update { it.copy(showDialog = false) }
    }
}


@Composable
fun AlertDialogExample(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
) {
    AlertDialog(
        icon = {
            Icon(icon, contentDescription = "Example Icon")
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
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
    listaDisciplinas: List<DisciplinaComNotas>,
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

            ListaDisciplinas(listaDisciplinas = uiState.lista)
        }
        if (uiState.showDialog){
            AlertDialogExample(
                onDismissRequest = {  meuViewModel.botaoSair()},
                onConfirmation = { meuViewModel.insereDisciplina() },
                dialogTitle = "Nova Disciplina",
                dialogText = "Deseja inserir qual disciplina?",
                icon = Icons.Default.Add
            )
        }
    }
}