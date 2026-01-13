package com.Miranda_JC.Basquet_Val.screens.common

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.Miranda_JC.Basquet_Val.data.Jugador
import com.Miranda_JC.Basquet_Val.navigation.navigateToJugadorDetail

@Composable
fun JugadorCard(
    jugador: Jugador,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                //navega al detall del jugador
                navController.navigateToJugadorDetail(jugador.id)
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Número de camiseta en criculo azl
            if (jugador.numeroCamiseta != null) {
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(40.dp)
                        .padding(end = 16.dp)
                ) {
                    Text(
                        text = jugador.numeroCamiseta.toString(),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                color = Color(0xFF1D428A),
                                shape = androidx.compose.foundation.shape.CircleShape
                            )
                            .padding(8.dp)
                    )
                }
            }

            //Información del jugador
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "${jugador.nombre} ${jugador.apellidos}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                //Posicion y equipo
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    if (!jugador.posicion.isNullOrBlank()) {
                        Text(
                            text = jugador.posicion!!,
                            fontSize = 14.sp,
                            color = Color(0xFF1D428A),
                            fontWeight = FontWeight.Medium
                        )
                    }

                    if (!jugador.equipoActual.isNullOrBlank()) {
                        Text(
                            text = "•",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                        Text(
                            text = jugador.equipoActual!!,
                            fontSize = 14.sp,
                            color = Color.Gray,
                            maxLines = 1,
                            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                        )
                    }
                }

                // Altura y Peso
                if (jugador.altura != null || jugador.peso != null) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = buildString {
                            if (jugador.altura != null) {
                                append("${jugador.altura}m")
                            }
                            if (jugador.peso != null) {
                                if (jugador.altura != null) append(" • ")
                                append("${jugador.peso}kg")
                            }
                        },
                        fontSize = 13.sp,
                        color = Color.DarkGray
                    )
                }
            }
        }
    }
}