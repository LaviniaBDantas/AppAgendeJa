package com.example.trabalhofinal.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.aula17.data_layer.DisciplinaComNotas
import com.example.trabalhofinal.AlertDialogExample
import com.example.trabalhofinal.ListaDisciplinas
import com.example.trabalhofinal.MeuViewModel
import kotlin.collections.forEach


@Composable
fun ListaNotas(
    listaNotas: List<DisciplinaComNotas>, onDeleteNota: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .padding(16.dp), verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        if (listaNotas.isEmpty()) {
            Text("Sem anotações dessa disciplina")
        } else {
            listaNotas.forEach { element ->
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

                        LazyColumn {
                            items(element.notas) { nota ->
                                Text(
                                    text = "${nota.texto} - ${nota.dataCriacao}",
                                    style = MaterialTheme.typography.titleMedium,
                                    modifier = Modifier.padding(10.dp)
                                )
                            }
                        }
                        IconButton(onClick = {  onDeleteNota(element.disciplina.id)  }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete")
                        }
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaNotas(meuViewModel: MeuViewModel = viewModel()) {
    val uiState by meuViewModel.uiState.collectAsStateWithLifecycle()
    Scaffold(
        topBar = {
            TopAppBar(
                colors = topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                title = {
                    Text("Notas da disciplina")
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
            FloatingActionButton(onClick = { meuViewModel.insereNota(texto = "Interessante", idDisciplina = 1) }) {
                Icon(Icons.Default.Add, contentDescription = "Add")
            }
        }
    ) {  innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(20.dp)
        ) {

            ListaNotas(listaNotas = uiState.lista, onDeleteNota = { id -> meuViewModel.deletaDisciplina(id) })
        }
    }
}