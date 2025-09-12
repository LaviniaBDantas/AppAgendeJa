package com.example.trabalhofinal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.trabalhofinal.screens.Home
import com.example.trabalhofinal.screens.SearchScreen
import com.example.trabalhofinal.screens.TelaInsertNote
import com.example.trabalhofinal.screens.TelaShowNotes
import com.example.trabalhofinal.screens.TelaViewNote
import com.example.trabalhofinal.view_model.MeuViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()

            NavHost(navController = navController, startDestination = "home") {
                composable("home") {
                    Home(
                        navToNotas = { disciplinaId ->
                            navController.navigate("notas/$disciplinaId")
                        },
                        navToSearch = { // 🔹 passa a navegação de pesquisa
                            navController.navigate("pesquisa")
                        }
                    )
                }
                composable("notas/{disciplinaId}") { backStackEntry ->
                    val disciplinaId = backStackEntry.arguments?.getString("disciplinaId")?.toInt() ?: 0
                    TelaShowNotes(
                        disciplinaId = disciplinaId,
                        navToNote = { notaId -> navController.navigate("viewnote/$notaId") },
                        // Linha corrigida abaixo:
                        navToInsert = { idDaDisciplina -> navController.navigate("telaInsertNote/$idDaDisciplina") }
                    )
                }
                composable("viewnote/{notaId}") { backStackEntry ->
                    val notaId = backStackEntry.arguments?.getString("notaId")?.toInt() ?: 0
                    TelaViewNote(notaId = notaId)
                }

                composable("telaInsertNote/{disciplinaId}") { backStackEntry ->
                    val disciplinaId = backStackEntry.arguments?.getString("disciplinaId")?.toInt() ?: -1
                    val meuViewModel: MeuViewModel = viewModel()

                    TelaInsertNote(
                        disciplinaId = disciplinaId,
                        meuViewModel = meuViewModel,
                        onNoteSaved = {
                            navController.popBackStack()
                        }
                    )
                }
                composable("pesquisa") {
                    val meuViewModel: MeuViewModel = viewModel()

                    SearchScreen(
                        viewModel = meuViewModel,
                        onClickDisciplina = { id -> navController.navigate("notas/$id") },
                        onFavoritar = { disciplina -> meuViewModel.toggleFavoritado(disciplina) },
                        onDeletar = { disciplina -> meuViewModel.deletaDisciplina(disciplina.id) },
                    )
                }
            }
        }
    }
}
