package com.example.trabalhofinal

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.trabalhofinal.screens.Home
import com.example.trabalhofinal.screens.TelaNotas
import com.example.trabalhofinal.screens.ViewNote


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
                        }
                    )
                }
                composable("notas/{disciplinaId}") { backStackEntry ->
                    val disciplinaId = backStackEntry.arguments?.getString("disciplinaId")?.toInt() ?: 0
                    TelaNotas(
                        disciplinaId = disciplinaId,
                        navToNote = { notaId -> navController.navigate("viewnote/$notaId") }
                    )
                }
                composable("viewnote/{notaId}") { backStackEntry ->
                    val notaId = backStackEntry.arguments?.getString("notaId")?.toInt() ?: 0
                    ViewNote(notaId = notaId)
                }
            }
        }
    }
}
