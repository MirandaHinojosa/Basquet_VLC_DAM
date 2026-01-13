package com.Miranda_JC.Basquet_Val.screens.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.Miranda_JC.Basquet_Val.data.Partido
import com.Miranda_JC.Basquet_Val.network.RetrofitInstance


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartidoDetalleScreen(
    navController: NavHostController,
    partidoId: Int
) {
    var isLoading by remember { mutableStateOf(true) }
    var partido by remember { mutableStateOf<Partido?>(null) }
    var error by remember { mutableStateOf<String?>(null) }

    //carga datos del partido cuando cambia el id
    LaunchedEffect(key1 = partidoId) {
        isLoading = true
        error = null

        try {
            val response = RetrofitInstance.api.getPartidoDetalle(partidoId)
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody?.success == true) {
                    partido = responseBody.data?.partido
                    if (partido == null) {
                        error = "No se encontró el partido"
                    }
                } else {
                    error = responseBody?.message ?: "Error al cargar el partido"
                }
            } else {
                error = "Error HTTP: ${response.code()}"
            }
        } catch (e: Exception) {
            error = "Error de conexión: ${e.message}"
        } finally {
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Partido") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Volver")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1D428A),
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                error != null -> {
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
                        Button(onClick = { navController.popBackStack() }) {
                            Text("Volver")
                        }
                    }
                }

                partido != null -> {
                    PartidoDetalleContent(partido = partido!!)
                }
            }
        }
    }
}

@Composable
private fun PartidoDetalleContent(partido: Partido) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        //Encabezado principal del partido
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    //Estado y hora
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = when (partido.estado) {
                                "EN_CURSO" -> "EN DIRECTO"   //roja partido en vivo
                                "FINALIZADO" -> "FINALIZADO"  // gris finalizado
                                else -> "PROGRAMADO"        //azul par programados
                            },
                            color = when (partido.estado) {
                                "EN_CURSO" -> Color.Red
                                "FINALIZADO" -> Color.Gray
                                else -> Color(0xFF1D428A)
                            },
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = if (partido.fechaHora.length >= 16) {
                                partido.fechaHora.substring(11, 16)   //capturamos HH:mm
                            } else {
                                partido.fechaHora
                            },
                            fontWeight = FontWeight.Medium
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    //Equipos y marcador
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = partido.equipoLocalNombre ?: "Equipo Local",
                                fontWeight = FontWeight.Bold,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                            Text(
                                text = partido.resultadoLocal?.toString() ?: "-",
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1D428A)
                            )
                        }

                        Text(
                            text = "VS",
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp,
                            modifier = Modifier.padding(horizontal = 16.dp)
                        )

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.weight(1f)
                        ) {
                            Text(
                                text = partido.equipoVisitanteNombre ?: "Equipo Visitante",
                                fontWeight = FontWeight.Bold,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                            Text(
                                text = partido.resultadoVisitante?.toString() ?: "-",
                                fontSize = 36.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF1D428A)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Información adicional
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = partido.competicionNombre ?: "",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium
                        )

                        partido.fase?.let { fase ->
                            if (fase.isNotBlank()) {
                                Text(
                                    text = fase,
                                    fontSize = 14.sp,
                                    color = Color.Gray
                                )
                            }
                        }

                        partido.pabellon?.let { pabellon ->
                            if (pabellon.isNotBlank()) {
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = pabellon,
                                    fontSize = 14.sp,
                                    color = Color.Gray,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }

                        partido.direccionPabellon?.let { direccion ->
                            if (direccion.isNotBlank()) {
                                Text(
                                    text = direccion,
                                    fontSize = 12.sp,
                                    color = Color.Gray,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}