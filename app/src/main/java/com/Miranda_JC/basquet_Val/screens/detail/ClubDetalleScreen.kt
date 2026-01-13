package com.Miranda_JC.Basquet_Val.screens.detail

import androidx.compose.foundation.Image
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.Miranda_JC.Basquet_Val.data.Club
import com.Miranda_JC.Basquet_Val.network.RetrofitInstance
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClubDetalleScreen(
    navController: NavHostController,
    clubId: Int
) {
    //estados para manejar la carga de datos.
    val scope = rememberCoroutineScope()
    val context = LocalContext.current
    var club by remember { mutableStateOf<Club?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    //carga los datos del club cuando cambia el clubid
    LaunchedEffect(key1 = clubId) {
        scope.launch {
            isLoading = true
            error = null

            try {
                val response = RetrofitInstance.api.getClubDetalle(clubId)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody?.success == true) {
                        club = responseBody.data?.club
                        if (club == null) {
                            error = "No se encontró el club"
                        }
                    } else {
                        error = responseBody?.message ?: "Error al cargar el club"
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
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Club") },
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

                club != null -> {
                    ClubDetalleContent(club = club!!, clubId = clubId)
                }
            }
        }
    }
}

@Composable
private fun ClubDetalleContent(club: Club, clubId: Int) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //lo hacemos por el ID de la tabla, ya que no añadi la columna del nombre de la imagen
        //es lo que más rapido se me ocurrió
        val context = LocalContext.current
        val imageName = "club_$clubId"

        //Lista de extensiones a probar
        val extensions = listOf("png", "jpg", "jpeg")
        var localImageResourceId by remember { mutableStateOf<Int?>(null) }

        //Buscamos el recurso de imagen local
        LaunchedEffect(clubId) {
            localImageResourceId = findLocalImageResource(context, imageName, extensions)
        }

        //Mostrar imagen local si existe
        if (localImageResourceId != null) {
            Image(
                painter = painterResource(id = localImageResourceId!!),
                contentDescription = "Logo del club ${club.nombre}",
                modifier = Modifier
                    .size(150.dp)
                    .padding(bottom = 16.dp)
            )
        }  else {
            //si creamos un club u nos olvidamos añdir la imagen
            Box(
                modifier = Modifier
                    .size(150.dp)
                    .background(
                        color = Color(0xFF1D428A),
                        shape = androidx.compose.foundation.shape.CircleShape
                    )
                    .padding(bottom = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = getInitials(club.nombre),
                    fontSize = 42.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        // Nombre del club
        Text(
            text = club.nombre,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1D428A),
            textAlign = androidx.compose.ui.text.style.TextAlign.Center,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Información de contacto
                InfoSection(
                    title = "Información de Contacto",
                    items = listOfNotNull(
                        club.direccion?.let {
                            InfoItem(
                                icon = Icons.Default.LocationOn,
                                label = "Dirección",
                                value = it
                            )
                        },
                        club.telefono?.let {
                            InfoItem(
                                icon = Icons.Default.Phone,
                                label = "Teléfono",
                                value = it
                            )
                        },
                        club.email?.let {
                            InfoItem(
                                icon = Icons.Default.Email,
                                label = "Email",
                                value = it
                            )
                        }
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Presidente
                if (!club.presidente.isNullOrBlank()) {
                    Divider(modifier = Modifier.padding(vertical = 8.dp))
                    InfoSection(
                        title = "Directiva",
                        items = listOf(
                            InfoItem(
                                icon = Icons.Default.Person,
                                label = "Presidente",
                                value = club.presidente
                            )
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

/**
 * Función auxiliar para buscar recursos de imagen local
 */
private fun findLocalImageResource(context: android.content.Context, baseName: String, extensions: List<String>): Int? {
    for (extension in extensions) {
        try {
            // Intentar con diferentes nombres
            val possibleNames = listOf(
                baseName,
                "club_$baseName",
                "club$baseName",
                baseName.replace("_", "") // club1 en lugar de club_1
            )

            for (name in possibleNames) {
                val resourceId = context.resources.getIdentifier(name, "drawable", context.packageName)
                if (resourceId != 0) {
                    return resourceId
                }
            }
        } catch (e: Exception) {
            // Continuar con la siguiente extensión
        }
    }
    return null
}

/**
 * Obtener iniciales del nombre del club
 */
private fun getInitials(clubName: String): String {
    val words = clubName.split(" ")
    return when {
        words.size >= 2 -> "${words[0].first()}${words[1].first()}"
        words.isNotEmpty() -> words[0].take(2)
        else -> "CB"
    }.uppercase()
}

@Composable
private fun InfoSection(
    title: String,
    items: List<InfoItem>
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF1D428A),
            modifier = Modifier.padding(bottom = 12.dp)
        )

        Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items.forEach { item -> InfoRow(item = item) }
        }
    }
}

@Composable
private fun InfoRow(item: InfoItem) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = item.label,
            tint = Color(0xFF1D428A),
            modifier = Modifier
                .size(24.dp)
                .padding(top = 2.dp)
        )

        Spacer(modifier = Modifier.width(12.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = item.label,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = item.value,
                fontSize = 16.sp,
                color = Color.Black
            )
        }
    }
}

private data class InfoItem(
    val icon: androidx.compose.ui.graphics.vector.ImageVector,
    val label: String,
    val value: String
)