package com.Miranda_JC.Basquet_Val.screens.main


import androidx.compose.foundation.clickable
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
import com.Miranda_JC.Basquet_Val.viewmodels.CompeticionesViewModel
import androidx.lifecycle.viewmodel.compose.viewModel

import com.Miranda_JC.Basquet_Val.navigation.navigateToCompeticionDetail
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults

@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun CompeticionesScreen(navController: NavHostController) {
    val viewModel: CompeticionesViewModel = viewModel()

    LaunchedEffect(key1 = true) {
        viewModel.cargarCompeticiones()
    }

    val competiciones by viewModel.competiciones.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val selectedTipo by viewModel.selectedTipo.collectAsState()
    val selectedCategoria by viewModel.selectedCategoria.collectAsState()

    //Filtros
    var showFilters by remember { mutableStateOf(false) }
    val tipos = viewModel.getTiposUnicos()
    val categorias = viewModel.getCategoriasUnicas()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Competiciones") },
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
            // Filtros
            if (showFilters) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
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

                        // Filtro por tipo
                        Text(
                            text = "Tipo de competición:",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.padding(bottom = 12.dp)
                        ) {
                            FilterChip(
                                selected = selectedTipo == null,
                                onClick = { viewModel.setSelectedTipo(null) },
                                label = { Text("Todos") }
                            )
                            tipos.forEach { tipo ->
                                FilterChip(
                                    selected = selectedTipo == tipo,
                                    onClick = { viewModel.setSelectedTipo(tipo) },
                                    label = { Text(tipo) }
                                )
                            }
                        }

                        //Filtro por categoría
                        Text(
                            text = "Categoría:",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(bottom = 4.dp)
                        )

                        FlowRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.padding(bottom = 8.dp)
                        ) {
                            FilterChip(
                                selected = selectedCategoria == null,
                                onClick = { viewModel.setSelectedCategoria(null) },
                                label = { Text("Todas") }
                            )
                            categorias.forEach { categoria ->
                                FilterChip(
                                    selected = selectedCategoria == categoria,
                                    onClick = { viewModel.setSelectedCategoria(categoria) },
                                    label = { Text(categoria) }
                                )
                            }
                        }
                    }
                }
            }

            // Lista de competiciones
            Box(modifier = Modifier.fillMaxSize()) {
                val competicionesFiltradas = viewModel.competicionesFiltradas()

                if (competicionesFiltradas.isEmpty() && !isLoading) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = if (selectedTipo != null || selectedCategoria != null) {
                                "No hay competiciones con los filtros seleccionados"
                            } else {
                                "No hay competiciones disponibles"
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
                        items(competicionesFiltradas) { competicion ->
                            CompeticionCard(
                                competicion = competicion,
                                onClick = {

                                    navController.navigateToCompeticionDetail(competicion.id)

                                }
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

@Composable
fun CompeticionCard(
    competicion: com.Miranda_JC.Basquet_Val.data.Competicion,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = competicion.nombre,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1D428A)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                //Tipo
                FilterChip(
                    selected = true,
                    onClick = { },
                    label = { Text(competicion.tipo) },
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = Color(0xFFE3F2FD)
                    )
                )

                // Categoría
                FilterChip(
                    selected = true,
                    onClick = { },
                    label = { Text(competicion.categoria) },
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = Color(0xFFF3E5F5)
                    )
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Temporada: ${competicion.temporada}",
                fontSize = 14.sp,
                color = Color.Gray
            )
        }
    }
}