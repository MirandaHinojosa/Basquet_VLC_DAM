package com.Miranda_JC.Basquet_Val.screens.common

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
import com.Miranda_JC.Basquet_Val.data.Partido

@Composable
fun PartidoCard(
    partido: Partido,
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable {
                //Navegar al detalle del partido
                navController.navigate("partido_detalle/${partido.id}")
            },
        colors = CardDefaults.cardColors(
            containerColor = if (partido.estado == "EN_CURSO") Color(0xFFFFF8E1) else Color.White
        ),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            //Encabezado
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = when (partido.estado) {
                        "EN_CURSO" -> "EN DIRECTO"
                        "FINALIZADO" -> "FINALIZADO"
                        else -> "PROGRAMADO"
                    },
                    color = when (partido.estado) {
                        "EN_CURSO" -> Color.Red
                        "FINALIZADO" -> Color.Gray
                        else -> Color(0xFF1D428A)
                    },
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )

                Text(
                    text = if (partido.fechaHora.length >= 16) {
                        partido.fechaHora.substring(11, 16)
                    } else {
                        partido.fechaHora
                    },
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

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
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                        fontSize = 14.sp
                    )
                    Text(
                        text = partido.resultadoLocal?.toString() ?: "-",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1D428A)
                    )
                }

                Text("VS", fontWeight = FontWeight.Bold, fontSize = 16.sp)

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = partido.equipoVisitanteNombre ?: "Equipo Visitante",
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        maxLines = 2,
                        fontSize = 14.sp
                    )
                    Text(
                        text = partido.resultadoVisitante?.toString() ?: "-",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1D428A)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            //Información adicional
            Text(
                text = partido.competicionNombre ?: "",
                fontSize = 12.sp,
                color = Color.Gray
            )

            partido.pabellon?.let { pabellon ->
                if (pabellon.isNotBlank()) {
                    Text(
                        text = pabellon,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}