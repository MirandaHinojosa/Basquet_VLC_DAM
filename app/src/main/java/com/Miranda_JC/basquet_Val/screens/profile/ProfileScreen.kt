package com.Miranda_JC.Basquet_Val.screens.profile

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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.Miranda_JC.Basquet_Val.viewmodels.AuthViewModel
import com.Miranda_JC.Basquet_Val.viewmodels.ViewModelFactory
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(navController: NavController) {
    val context = LocalContext.current
    val authViewModel: AuthViewModel = viewModel(
        factory = ViewModelFactory.provideFactory(context)
    )
    val currentUser by authViewModel.currentUser.collectAsState()
    val currentPerfil by authViewModel.currentPerfil.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    var nombreCompleto by remember { mutableStateOf(currentPerfil?.nombreCompleto ?: "") }
    var fechaNacimiento by remember { mutableStateOf(currentPerfil?.fechaNacimiento ?: "") }
    var posicion by remember { mutableStateOf(currentPerfil?.posicion ?: "") }
    var equipoFavorito by remember { mutableStateOf(currentPerfil?.equipoFavorito ?: "") }
    var bio by remember { mutableStateOf(currentPerfil?.bio ?: "") }
    var isEditing by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        // Header
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF1D428A))
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Perfil",
                    tint = Color.White,
                    modifier = Modifier.size(80.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = currentUser?.username ?: "Usuario",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Text(
                    text = currentUser?.email ?: "",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        //Formulario de perfil
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Mi Perfil",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )

                    IconButton(
                        onClick = { isEditing = !isEditing }
                    ) {
                        Icon(
                            imageVector = if (isEditing) Icons.Default.Save else Icons.Default.Edit,
                            contentDescription = if (isEditing) "Guardar" else "Editar"
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedTextField(
                    value = nombreCompleto,
                    onValueChange = { nombreCompleto = it },
                    label = { Text("Nombre completo") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = isEditing,
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = fechaNacimiento,
                    onValueChange = { fechaNacimiento = it },
                    label = { Text("Fecha de nacimiento (DD/MM/AAAA)") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = isEditing,
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = posicion,
                    onValueChange = { posicion = it },
                    label = { Text("Posición favorita") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = isEditing,
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = equipoFavorito,
                    onValueChange = { equipoFavorito = it },
                    label = { Text("Equipo favorito") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = isEditing,
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedTextField(
                    value = bio,
                    onValueChange = { bio = it },
                    label = { Text("Biografía") },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = isEditing,
                    maxLines = 3
                )

                if (isEditing) {
                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = {
                            coroutineScope.launch {
                                val perfil = currentPerfil?.copy(
                                    nombreCompleto = nombreCompleto,
                                    fechaNacimiento = fechaNacimiento,
                                    posicion = posicion,
                                    equipoFavorito = equipoFavorito,
                                    bio = bio
                                ) ?: com.Miranda_JC.Basquet_Val.data.local.PerfilJugador(
                                    usuarioId = currentUser?.id ?: 0,
                                    nombreCompleto = nombreCompleto,
                                    fechaNacimiento = fechaNacimiento,
                                    posicion = posicion,
                                    equipoFavorito = equipoFavorito,
                                    bio = bio
                                )

                                val success = authViewModel.updatePerfil(perfil)
                                if (success) {
                                    isEditing = false
                                }
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Guardar Cambios")
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        //Botón de cerrar sesión
        Button(
            onClick = {
                authViewModel.logout()
                navController.navigate("login") {
                    popUpTo("main") { inclusive = true }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = Color.Red)
        ) {
            Text("Cerrar Sesión", color = Color.White)
        }
    }
}