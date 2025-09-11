package com.example.trabalhofinal.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.trabalhofinal.view_model.MeuViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaInsertNote(
    disciplinaId: Int,
    meuViewModel: MeuViewModel,
    onNoteSaved: () -> Unit
) {
    var comentario by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Adicionar Anotação") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.primary,
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (comentario.isNotBlank()) {
                        meuViewModel.insertNote(
                            texto = comentario,
                            idDisciplina = disciplinaId
                        )
                        onNoteSaved()
                    }
                }
            ) {
                Icon(Icons.Default.Done, contentDescription = "Salvar Anotação")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize()
        ) {
            OutlinedTextField(
                value = comentario,
                onValueChange = { comentario = it },
                label = { Text("Sua observação") },
                placeholder = { Text("Digite os detalhes da sua anotação aqui...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                singleLine = false
            )
        }
    }
}