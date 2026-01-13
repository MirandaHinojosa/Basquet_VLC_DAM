package com.Miranda_JC.Basquet_Val.screens.detail

import androidx.compose.foundation.background
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
import androidx.navigation.NavHostController
import com.Miranda_JC.Basquet_Val.data.Jugador
import com.Miranda_JC.Basquet_Val.network.RetrofitInstance
import com.Miranda_JC.Basquet_Val.utils.calcularEdad
import com.Miranda_JC.Basquet_Val.utils.formatearFecha
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JugadorDetalleScreen(
    navController: NavHostController,
    jugadorId: Int
) {
    var jugador by remember { mutableStateOf<Jugador?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(key1 = jugadorId) {
        isLoading = true
        error = null

        try {
            val response = RetrofitInstance.api.getJugadorDetalle(jugadorId)
            if (response.isSuccessful) {
                val responseBody = response.body()
                if (responseBody?.success == true) {
                    jugador = responseBody.data?.jugador
                    if (jugador == null) {
                        error = "No se encontró el jugador"
                    }
                } else {
                    error = responseBody?.message ?: "Error al cargar el jugador"
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
                title = { Text("Ficha del Jugador") },
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
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            Icons.Default.Error,
                            contentDescription = "Error",
                            tint = Color.Red,
                            modifier = Modifier.size(64.dp)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
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

                jugador != null -> {
                    JugadorDetalleContent(jugador = jugador!!)
                }
            }
        }
    }
}

@Composable
private fun JugadorDetalleContent(jugador: Jugador) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        //Cabecera
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(Color(0xFF1D428A))
        ) {
            if (jugador.numeroCamiseta != null) {
                Text(
                    text = jugador.numeroCamiseta.toString(),
                    fontSize = 80.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White.copy(alpha = 0.2f),
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            Box(
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.Center)
                    .background(Color.White, androidx.compose.foundation.shape.CircleShape),
                contentAlignment = Alignment.Center
            ) {
                val iniciales = if (jugador.nombre.isNotEmpty() && jugador.apellidos.isNotEmpty()) {
                    "${jugador.nombre.first()}${jugador.apellidos.first()}"
                } else {
                    "J"
                }

                Text(
                    text = iniciales.uppercase(),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF1D428A)
                )
            }
        }

        // Información
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = "${jugador.nombre} ${jugador.apellidos}",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF1D428A),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // Posición y equipo - USANDO FilterChip de Material 3
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                jugador.posicion?.let { posicion ->
                    if (posicion.isNotBlank()) {
                        FilterChip(
                            selected = true,
                            onClick = {},
                            label = { Text(posicion) },
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = Color(0xFFE3F2FD),
                                labelColor = Color(0xFF0D47A1)
                            )
                        )
                    }
                }

                jugador.equipoActual?.let { equipo ->
                    if (equipo.isNotBlank()) {
                        FilterChip(
                            selected = true,
                            onClick = {},
                            label = { Text(equipo) },
                            colors = FilterChipDefaults.filterChipColors(
                                containerColor = Color(0xFFF3E5F5),
                                labelColor = Color(0xFF4A148C)
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Información personal
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Información Personal",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1D428A),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    // Fecha de nacimiento y edad
                    jugador.fechaNacimiento?.let { fechaNac ->
                        if (fechaNac.isNotBlank()) {
                            val edad = calcularEdad(fechaNac)
                            val fechaFormateada = formatearFecha(fechaNac)

                            InfoRow(
                                icon = Icons.Default.Cake,
                                label = "Fecha de Nacimiento",
                                value = buildString {
                                    append(fechaFormateada)
                                    edad?.let { append(" ($it años)") }
                                }
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }

                    // Licencia federativa
                    jugador.licenciaFederativa?.let { licencia ->
                        if (licencia.isNotBlank()) {
                            InfoRow(
                                icon = Icons.Default.Badge,
                                label = "Licencia Federativa",
                                value = licencia
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                        }
                    }

                    // Altura
                    jugador.altura?.let { altura ->
                        InfoRow(
                            icon = Icons.Default.Height,
                            label = "Altura",
                            value = "$altura m"
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }

                    // Peso
                    jugador.peso?.let { peso ->
                        InfoRow(
                            icon = Icons.Default.MonitorWeight,
                            label = "Peso",
                            value = "$peso kg"
                        )
                    }
                }
            }
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
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
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