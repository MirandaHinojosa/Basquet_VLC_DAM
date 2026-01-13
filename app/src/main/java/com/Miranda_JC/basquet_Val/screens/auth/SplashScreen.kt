package com.Miranda_JC.Basquet_Val.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.SportsBasketball
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay
@Composable
fun SplashScreen(navController: NavController) {

    //espera de 2 sec y navega al login
    LaunchedEffect(key1 = true) {
        delay(2000)
        navController.navigate("login") {
            popUpTo("splash") { inclusive = true }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1D428A)), //fondo azul
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            //icono baloncesto
            imageVector = Icons.Default.SportsBasketball,
            contentDescription = "Baloncesto",
            tint = Color.White,
            modifier = Modifier.size(120.dp)
        )

        Spacer(modifier = Modifier.height(24.dp))

        //nombre de la aplicacion
        Text(
            text = "FBCV App",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(8.dp))
        //subtitulo
        Text(
            text = "Baloncesto Valenciano",
            fontSize = 14.sp,
            color = Color.White.copy(alpha = 0.8f)
        )
    }
}