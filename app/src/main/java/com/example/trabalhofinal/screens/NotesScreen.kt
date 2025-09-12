package com.example.trabalhofinal.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.trabalhofinal.data_layer.DisciplinaComNotas
import com.example.trabalhofinal.view_model.MeuViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


@Composable
fun ListNotes(
    listaNotas: List<DisciplinaComNotas>,
    onDeleteNota: (Int) -> Unit,
    onClickNote: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        val disciplina = listaNotas.firstOrNull()

        if (disciplina == null || disciplina.notas.isEmpty()) {
            Text("Sem anotações dessa disciplina")
        } else {
            disciplina.notas.forEach { nota ->
                Card(
                    modifier = Modifier
                        .padding(bottom = 16.dp)
                        .fillMaxWidth()
                        .clickable { onClickNote(nota.id) }

                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        val formatter = SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                        val dataFormatada = formatter.format(Date(nota.dataCriacao))

                        Column {
                            Text(
                                text = dataFormatada,
                                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = nota.texto,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        IconButton(onClick = { onDeleteNota(nota.id) }) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Delete"
                            )
                        }
                    }
                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaShowNotes(
    meuViewModel: MeuViewModel = viewModel(),
    navToNote: (Int) -> Unit,
    navToInsert: (Int) -> Unit,
    disciplinaId: Int
) {

    val disciplinaNome by meuViewModel.getNameDisciplina(disciplinaId)
        .collectAsStateWithLifecycle(initialValue = "")

    val notas by meuViewModel.getNotesByDisciplina(disciplinaId)
        .collectAsStateWithLifecycle(initialValue = emptyList())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Notas de $disciplinaNome") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                ),
                actions = {
                    IconButton(onClick = { meuViewModel.deleteAllNotesByDisciplina(disciplinaId) }) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Apagar todas as notas de $disciplinaNome",
                            tint = Color.Red
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
//                    text = "Barra de navegacao",
//                )
//            }
//        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navToInsert(disciplinaId) }
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
            ListNotes(
                listaNotas = notas,
                onDeleteNota = { id -> meuViewModel.deleteNote(id) },
                onClickNote = { id -> navToNote(id) }
            )
        }
    }
}


