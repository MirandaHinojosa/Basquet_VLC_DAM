package com.Miranda_JC.Basquet_Val.screens.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.navigation.NavHostController
import com.Miranda_JC.Basquet_Val.screens.common.JugadorCard
import com.Miranda_JC.Basquet_Val.viewmodels.JugadoresViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun JugadoresScreen(navController: NavHostController) {
    val viewModel: JugadoresViewModel = viewModel()

    LaunchedEffect(key1 = true) {
        viewModel.cargarJugadores()
    }

    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val selectedEquipo by viewModel.selectedEquipo.collectAsState()
    val selectedPosicion by viewModel.selectedPosicion.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    // Filtros
    var showFilters by remember { mutableStateOf(false) }
    val equipos = viewModel.getEquiposUnicos()
    val posiciones = viewModel.getPosicionesUnicas()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Jugadores") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1D428A),
                    titleContentColor = Color.White
                ),
                actions = {
                    IconButton(onClick = { viewModel.refresh() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Actualizar", tint = Color.White)
                    }
                    IconButton(onClick = { showFilters = !showFilters }) {
                        Icon(Icons.Default.FilterList, contentDescription = "Filtros", tint = Color.White)
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            //Barra de búsqueda
            OutlinedTextField(
                value = searchQuery, // CAMBIA AQUÍ
                onValueChange = { viewModel.setSearchQuery(it) },
                label = { Text("Buscar jugadores...") },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                singleLine = true
            )

            // Filtros
            if (showFilters) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    elevation = CardDefaults.cardElevation(4.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Filtros",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1D428A),
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        // Filtro por equipo
                        Text(
                            text = "Equipo:",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )


                        androidx.compose.foundation.layout.FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.padding(bottom = 12.dp)
                        ) {
                            FilterChip(
                                selected = selectedEquipo == null,
                                onClick = { viewModel.setSelectedEquipo(null) },
                                label = { Text("Todos") }
                            )
                            equipos.forEach { equipo ->
                                FilterChip(
                                    selected = selectedEquipo == equipo,
                                    onClick = { viewModel.setSelectedEquipo(equipo) },
                                    label = { Text(equipo) }
                                )
                            }
                        }

                        //Filtro por posición
                        Text(
                            text = "Posición:",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )


                        androidx.compose.foundation.layout.FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            FilterChip(
                                selected = selectedPosicion == null,
                                onClick = { viewModel.setSelectedPosicion(null) },
                                label = { Text("Todas") }
                            )
                            posiciones.forEach { posicion ->
                                FilterChip(
                                    selected = selectedPosicion == posicion,
                                    onClick = { viewModel.setSelectedPosicion(posicion) },
                                    label = { Text(posicion) }
                                )
                            }
                        }
                    }
                }
            }

            //Lista de jugadores
            Box(modifier = Modifier.fillMaxSize()) {
                val jugadoresFiltrados = viewModel.jugadoresFiltrados()

                if (jugadoresFiltrados.isEmpty() && !isLoading) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = if (selectedEquipo != null || selectedPosicion != null ||
                                searchQuery.isNotBlank()) {
                                "No se encontraron jugadores con los filtros seleccionados"
                            } else {
                                "No hay jugadores disponibles"
                            },
                            fontSize = 16.sp,
                            color = Color.Gray,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(jugadoresFiltrados) { jugador ->
                            JugadorCard(
                                jugador = jugador,
                                navController = navController
                            )
                        }
                    }
                }

                // Loading
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                // Error
                if (error != null) {
                    Column(
                        modifier = Modifier.align(Alignment.Center),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = error!!,
                            color = Color.Red,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.refresh() }) {
                            Text("Reintentar")
                        }
                    }
                }
            }
        }
    }
}