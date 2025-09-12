package com.example.trabalhofinal.screens

import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.trabalhofinal.components.AlertDialogExample
import com.example.trabalhofinal.data_layer.Disciplina
import com.example.trabalhofinal.data_layer.DisciplinaComNotas
import com.example.trabalhofinal.view_model.MeuViewModel


@Composable
fun ListaDisciplinas(
    listaDisciplinas: List<DisciplinaComNotas>,
    onDeleteDisciplina: (Int) -> Unit,
    onFavoritarDisciplina: (Disciplina) -> Unit,
    onClickDisciplina: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        if (listaDisciplinas.isEmpty()) {
            Text("Lista vazia")
        } else {
            listaDisciplinas.forEach { element ->
                Card(
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .fillMaxWidth()
                        .clickable { onClickDisciplina(element.disciplina.id) }
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 10.dp)
                    ) {
                        Text(
                            text = element.disciplina.nome,
                            style = MaterialTheme.typography.titleMedium,
                            modifier = Modifier.padding(10.dp)
                        )

                        Row(horizontalArrangement = Arrangement.End) {
                            IconButton(onClick = { onDeleteDisciplina(element.disciplina.id) }) {
                                Icon(
                                    Icons.Default.Delete,
                                    contentDescription = "Delete"
                                )
                            }
                            IconButton(onClick = { onFavoritarDisciplina(element.disciplina) }) {
                                Icon(
                                    imageVector = if (element.disciplina.favoritada)
                                        Icons.Default.Favorite
                                    else
                                        Icons.Default.FavoriteBorder,
                                    contentDescription = "Favorite",
                                    tint = if (element.disciplina.favoritada) Color.Red else Color.Black
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Home(
    meuViewModel: MeuViewModel = viewModel(),
    navToNotas: (Int) -> Unit,
    navToSearch: () -> Unit
) {
    val uiState by meuViewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Lista de Disciplinas") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                actions = {
                    IconButton(onClick = { navToSearch() }) { // 🔹 chama a navegação
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Pesquisar",
                            tint = Color.Black
                        )
                    }
                }
            )
        },

//        bottomBar = {
//            BottomAppBar(
//                containerColor = MaterialTheme.colorScheme.primaryContainer,
//                contentColor = MaterialTheme.colorScheme.primary,
//            ) {
//                Text(
//                    modifier = Modifier.fillMaxWidth(),
//                    textAlign = TextAlign.Center,
//                    text = "Aq vai ficar nossa barra de navegacao",
//                )
//            }
//        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { meuViewModel.botaoAddDisciplina() }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(20.dp)
        ) {
            ListaDisciplinas(
                listaDisciplinas = uiState.lista,
                onDeleteDisciplina = { id -> meuViewModel.deletaDisciplina(id) },
                onFavoritarDisciplina = { disciplina -> meuViewModel.toggleFavoritado(disciplina) },
                onClickDisciplina = { id -> navToNotas(id) }
            )
        }

        if (uiState.showDialog) {
            AlertDialogExample(
                onDismissRequest = { meuViewModel.botaoSair() },
                onConfirmation = { disciplinaInserida ->
                    meuViewModel.insereDisciplina(disciplinaInserida)
                },
                dialogTitle = "Nova Disciplina",
                dialogText = "Deseja inserir qual disciplina?",
                icon = Icons.Default.Add
            )
        }
    }
}
