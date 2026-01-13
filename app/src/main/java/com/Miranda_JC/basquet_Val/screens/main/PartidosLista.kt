package com.Miranda_JC.Basquet_Val.screens.main

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.Miranda_JC.Basquet_Val.screens.common.PartidoCard
import com.Miranda_JC.Basquet_Val.viewmodels.PartidosViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun PartidosScreen(navController: NavHostController) {
    val viewModel: PartidosViewModel = viewModel()

    LaunchedEffect(key1 = true) {
        viewModel.cargarPartidosEnDirecto()
        viewModel.cargarPartidosProgramados()
        viewModel.cargarPartidosFinalizados()
    }


    val partidosEnDirecto by viewModel.partidosEnDirecto.collectAsStateWithLifecycle()
    val partidosProgramados by viewModel.partidosProgramados.collectAsStateWithLifecycle()
    val partidosFinalizados by viewModel.partidosFinalizados.collectAsStateWithLifecycle()

    val isLoading by viewModel.isLoading.collectAsStateWithLifecycle()
    val error by viewModel.error.collectAsStateWithLifecycle()
    val selectedTab by viewModel.selectedTab.collectAsStateWithLifecycle()
    val searchQuery by viewModel.searchQuery.collectAsStateWithLifecycle()

    Column(modifier = Modifier.fillMaxSize()) {
        //Barra de búsqueda
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { viewModel.setSearchQuery(it) },
            label = { Text("Buscar partidos...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Buscar") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            singleLine = true
        )

        //Pestañas
        TabRow(
            selectedTabIndex = selectedTab,
            containerColor = Color.White,
            contentColor = Color(0xFF1D428A),
            modifier = Modifier.fillMaxWidth()
        ) {
            Tab(
                selected = selectedTab == 0,
                onClick = { viewModel.setSelectedTab(0) },
                text = { Text("PROGRAMADOS") }
            )
            Tab(
                selected = selectedTab == 1,
                onClick = { viewModel.setSelectedTab(1) },
                text = { Text("EN DIRECTO") }
            )
            Tab(
                selected = selectedTab == 2,
                onClick = { viewModel.setSelectedTab(2) },
                text = { Text("FINALIZADOS") }
            )
        }

        //Contenido
        Box(modifier = Modifier.fillMaxSize()) {
            when (selectedTab) {
                0 -> PartidosLista(
                    partidos = viewModel.partidosFiltrados(partidosProgramados),
                    navController = navController,
                    emptyMessage = "No hay partidos programados"
                )
                1 -> PartidosLista(
                    partidos = viewModel.partidosFiltrados(partidosEnDirecto),
                    navController = navController,
                    emptyMessage = "No hay partidos en directo"
                )
                2 -> PartidosLista(
                    partidos = viewModel.partidosFiltrados(partidosFinalizados),
                    navController = navController,
                    emptyMessage = "No hay partidos finalizados"
                )
            }

            //Loading
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

@Composable
private fun PartidosLista(
    partidos: List<com.Miranda_JC.Basquet_Val.data.Partido>,
    navController: NavHostController,
    emptyMessage: String
) {
    if (partidos.isEmpty()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = emptyMessage,
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
            items(partidos) { partido ->
                PartidoCard(
                    partido = partido,
                    navController = navController
                )
            }
        }
    }
}