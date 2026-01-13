package com.Miranda_JC.Basquet_Val.screens.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import com.Miranda_JC.Basquet_Val.screens.common.PartidoCard
import com.Miranda_JC.Basquet_Val.screens.common.QuickAccessButton
import com.Miranda_JC.Basquet_Val.viewmodels.PartidosViewModel

@Composable
fun HomeScreen(navController: NavHostController) {
    val partidosViewModel: PartidosViewModel = viewModel()

    LaunchedEffect(key1 = true) {
        partidosViewModel.cargarPartidosEnDirecto()
        partidosViewModel.cargarPartidosProgramados()
    }

    val partidosEnDirecto by partidosViewModel.partidosEnDirecto.collectAsState()
    val partidosProgramados by partidosViewModel.partidosProgramados.collectAsState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        //Sección de partidos en directo
        if (partidosEnDirecto.isNotEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "PARTIDOS EN DIRECTO",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Red
                        )

                        Text(
                            text = "${LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))}",
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    partidosEnDirecto.forEach { partido ->
                        PartidoCard(partido = partido, navController = navController) // Aquí está bien
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            }
        }

        //sección de próximos partidos
        if (partidosProgramados.isNotEmpty()) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "PRÓXIMOS PARTIDOS",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1D428A)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    partidosProgramados.take(3).forEach { partido ->

                        PartidoCard(partido = partido, navController = navController)
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    if (partidosProgramados.size > 3) {
                        TextButton(
                            onClick = { navController.navigate("partidos") },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Ver todos los partidos")
                        }
                    }
                }
            }
        }

        // Sección de acceso rápido
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = "ACCESO RÁPIDO",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1D428A)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    QuickAccessButton(
                        icon = Icons.Default.Search,
                        text = "Buscar Club",
                        onClick = { navController.navigate("clubes") }
                    )

                    QuickAccessButton(
                        icon = Icons.Default.CalendarToday,
                        text = "Calendario",
                        onClick = { navController.navigate("partidos") }
                    )

                    QuickAccessButton(
                        icon = Icons.Default.Leaderboard,
                        text = "Competiciones",
                        onClick = { navController.navigate("competiciones") }
                    )
                }
            }
        }
    }
}