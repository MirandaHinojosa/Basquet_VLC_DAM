package com.Miranda_JC.Basquet_Val.screens.detail

import androidx.compose.foundation.background
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
import com.Miranda_JC.Basquet_Val.data.Clasificacion
import com.Miranda_JC.Basquet_Val.data.Competicion
import com.Miranda_JC.Basquet_Val.network.RetrofitInstance
import kotlinx.coroutines.launch
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CompeticionDetalleScreen(
    navController: NavHostController,
    competicionId: Int
) {
    var competicion by remember { mutableStateOf<Competicion?>(null) }
    var clasificacion by remember { mutableStateOf<List<Clasificacion>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(key1 = competicionId) {
        scope.launch {
            isLoading = true
            try {
                // 1. Obtener competición
                val competicionResponse = RetrofitInstance.api.getCompeticionDetalle(competicionId)
                if (competicionResponse.isSuccessful) {
                    val body = competicionResponse.body()
                    if (body?.success == true) {
                        competicion = body.data?.competicion
                        // IMPORTANTE: La clasificación ya viene aquí
                        clasificacion = body.data?.clasificacion ?: emptyList()
                    } else {
                        error = body?.message ?: "Error al cargar la competición"
                    }
                } else {
                    error = "Error HTTP: ${competicionResponse.code()}"
                }


            } catch (e: Exception) {
                error = "Error de conexión: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle de Competición") },
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
                        Icon(
                            imageVector = Icons.Default.Error,
                            contentDescription = "Error",
                            tint = Color.Red,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = error!!,
                            color = Color.Red,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                            modifier = Modifier.padding(horizontal = 32.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { navController.popBackStack() }) {
                            Text("Volver")
                        }
                    }
                }

                competicion != null -> {
                    CompeticionDetalleContent(
                        competicion = competicion!!,
                        clasificacion = clasificacion
                    )
                }
            }
        }
    }
}

@Composable
private fun CompeticionDetalleContent(
    competicion: Competicion,
    clasificacion: List<Clasificacion>
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        //Encabezado de la competición
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
                    Text(
                        text = competicion.nombre,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1D428A),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    //Inform
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        FilterChip(
                            selected = true,
                            onClick = {},
                            label = { Text(competicion.tipo) }
                        )

                        FilterChip(
                            selected = true,
                            onClick = {},
                            label = { Text(competicion.categoria) }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    //Temporada
                    Text(
                        text = "Temporada ${competicion.temporada}",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray
                    )
                }
            }
        }

        //Clasificación
        if (clasificacion.isNotEmpty()) {
            item {
                Text(
                    text = "Clasificación",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1D428A),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }

            item {
                ClasificacionTableHeader()
            }

            items(clasificacion) { equipo ->
                ClasificacionRow(clasificacion = equipo)
            }
        } else {
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFF5F5F5)
                    )
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Leaderboard,
                            contentDescription = "Sin datos",
                            tint = Color.Gray,
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = "No hay clasificación disponible",
                            fontSize = 16.sp,
                            color = Color.Gray,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }
        }

        //Información adicional
        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Información de la Competición",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1D428A),
                        modifier = Modifier.padding(bottom = 12.dp)
                    )

                    InfoRow(
                        icon = Icons.Default.Sports,
                        label = "Tipo",
                        value = competicion.tipo
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    InfoRow(
                        icon = Icons.Default.Category,
                        label = "Categoría",
                        value = competicion.categoria
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    InfoRow(
                        icon = Icons.Default.DateRange,
                        label = "Temporada",
                        value = competicion.temporada
                    )
                }
            }
        }
    }
}
//clasificacion tabla de puntos
@Composable
private fun ClasificacionTableHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF1D428A))
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {

        Text(
            text = "Pos",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            modifier = Modifier.width(40.dp)
        )

        //Equipo
        Text(
            text = "Equipo",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            modifier = Modifier.weight(1f)
        )

        // PJ
        Text(
            text = "PJ",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            modifier = Modifier.width(40.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        // PG
        Text(
            text = "PG",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            modifier = Modifier.width(40.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        // PP
        Text(
            text = "PP",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            modifier = Modifier.width(40.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        // Pts
        Text(
            text = "Pts",
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            modifier = Modifier.width(50.dp),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}

@Composable
private fun ClasificacionRow(clasificacion: Clasificacion) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Posición
            Box(
                modifier = Modifier.width(40.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = clasificacion.posicion.toString(),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color(0xFF1D428A)
                )
            }

            //Nombre del equipo
            Column(
                modifier = Modifier.weight(1f).padding(horizontal = 8.dp)
            ) {
                Text(
                    text = clasificacion.equipoNombre ?: "Equipo",
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
                Text(
                    text = clasificacion.clubNombre ?: "Club",
                    fontSize = 12.sp,
                    color = Color.Gray,
                    maxLines = 1,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
            }

            // Partidos jugados
            Text(
                text = clasificacion.partidosJugados.toString(),
                fontSize = 14.sp,
                modifier = Modifier.width(40.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            // Partidos ganados
            Text(
                text = clasificacion.partidosGanados.toString(),
                fontSize = 14.sp,
                color = Color(0xFF2E7D32),
                fontWeight = FontWeight.Medium,
                modifier = Modifier.width(40.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            // Partidos perdidos
            Text(
                text = clasificacion.partidosPerdidos.toString(),
                fontSize = 14.sp,
                color = Color(0xFFC62828),
                fontWeight = FontWeight.Medium,
                modifier = Modifier.width(40.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            // Puntos
            Text(
                text = clasificacion.puntosClasificacion.toString(),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1D428A),
                modifier = Modifier.width(50.dp),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )
        }
    }
}

@Composable
private fun InfoRow(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color(0xFF1D428A),
            modifier = Modifier.size(20.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = label,
                fontSize = 14.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = value,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}