package com.example.trabalhofinal.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.trabalhofinal.data_layer.Disciplina
import com.example.trabalhofinal.view_model.MeuViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    viewModel: MeuViewModel = viewModel(),
    onClickDisciplina: (Int) -> Unit,
    onFavoritar: (Disciplina) -> Unit,
    onDeletar: (Disciplina) -> Unit
) {
    var query by remember { mutableStateOf("") }
    val resultados by viewModel.searchResults.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Pesquisar Disciplina") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = query,
                onValueChange = {
                    query = it
                    viewModel.searchDisciplina(it)
                },
                label = { Text("Pesquisar disciplina") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            when {
                query.isBlank() -> {
                    // Mensagem inicial
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Digite para pesquisar")
                    }
                }

                resultados.isEmpty() -> {
                    // Mensagem quando não encontra nada
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("Nenhuma disciplina encontrada")
                    }
                }

                else -> {
                    LazyColumn {
                        items(resultados) { disciplinaComNotas ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 8.dp)
                                    .clickable { onClickDisciplina(disciplinaComNotas.id) },
                                shape = RoundedCornerShape(16.dp),
                                elevation = CardDefaults.cardElevation(6.dp)
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = disciplinaComNotas.nome,
                                        style = MaterialTheme.typography.titleMedium
                                    )

                                    Row {
                                        IconButton(onClick = { onFavoritar(disciplinaComNotas) }) {
                                            Icon(
                                                imageVector = if (disciplinaComNotas.favoritada)
                                                    Icons.Default.Favorite
                                                else
                                                    Icons.Default.FavoriteBorder,
                                                contentDescription = "Favoritar",
                                                tint = if (disciplinaComNotas.favoritada) Color.Red else Color.Black

                                            )
                                        }
                                        IconButton(onClick = { onDeletar(disciplinaComNotas) }) {
                                            Icon(
                                                imageVector = Icons.Default.Delete,
                                                contentDescription = "Deletar"
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

