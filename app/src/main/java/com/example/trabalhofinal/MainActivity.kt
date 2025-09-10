package com.example.trabalhofinal
import android.R.attr.onClick
import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.example.trabalhofinal.ui.theme.TrabalhoFinalTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MinhaTela()
        }
    }
}

data class MeuUiState(
    val lista: List<DisciplinaComNotas> = listOf(),
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

    fun insereDisciplina() {
        viewModelScope.launch {
            disciplinaRepository.insert(
                disciplina = Disciplina(
                    nome = "PDM",
                    favoritada = false
                )
            )
        }
    }
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
                        .fillMaxWidth()
                        .padding(bottom = 10.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp),
                    ) {
                        Text(text = element.disciplina.nome)
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun MinhaTela(meuViewModel: MeuViewModel = viewModel()) {
    val uiState by meuViewModel.uiState.collectAsStateWithLifecycle()

    TrabalhoFinalTheme {
        Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
            Column(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(16.dp)
            ) {
                Text(
                    text = "Lista de Disciplinas",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Botão para inserir um novo paciente

                    FloatingActionButton(
                        onClick = { meuViewModel.insereDisciplina() },
                    ) {
                        Icon(Icons.Filled.Add, "Floating action button.")
                    }

//                Button(
//                    onClick = { meuViewModel.insereDisciplina() },
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    Text(text = "Adicionar Nova Disciplina")
//                }

                Spacer(modifier = Modifier.height(16.dp))

                // Exibe a lista de pacientes
                ListaDisciplinas(listaDisciplinas = uiState.lista)
            }
        }
    }
}