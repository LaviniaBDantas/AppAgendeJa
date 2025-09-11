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
                    TelaNotas(disciplinaId = disciplinaId)
                }
            }
        }
    }
}
