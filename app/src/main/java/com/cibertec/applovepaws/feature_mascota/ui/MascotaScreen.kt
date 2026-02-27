package com.cibertec.applovepaws.feature_mascota.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.cibertec.applovepaws.feature_mascota.MascotaViewModel

@Composable
fun MascotaScreen(
    viewModel: MascotaViewModel,
    onIrARegistro: () -> Unit = {}
) {
    LaunchedEffect(Unit) {
        viewModel.cargarMascotas()
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onIrARegistro,
                containerColor = Color(0xFF1565C0)
            ) {
                Text("+", color = Color.White, fontSize = 24.sp)
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (viewModel.loading) {
                CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            } else {
                Column {
                    Button(
                        onClick = { viewModel.cargarMascotas() },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text("Sincronizar")
                    }

                    viewModel.errorMessage?.let { message ->
                        Text(
                            text = message,
                            color = Color.Red,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }

                    LazyColumn {
                        items(viewModel.mascotas) { mascota ->
                            MascotaItem(mascota)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MascotaItem(m: com.cibertec.applovepaws.feature_mascota.data.dto.MascotaDto) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(Modifier.padding(12.dp)) {
            val fotoValida = m.fotoUrl?.takeIf {
                it.startsWith("http://", ignoreCase = true) || it.startsWith("https://", ignoreCase = true)
            }

            AsyncImage(
                model = fotoValida,
                contentDescription = m.nombre,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            )

            if (fotoValida == null) {
                Text(
                    text = "Imagen no disponible",
                    color = Color.Gray,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            Text(m.nombre, style = MaterialTheme.typography.titleMedium)
            Text("Edad: ${m.edad}")
            Text("Raza: ${m.razaNombre ?: "No especificada"}")
            Text("Estado: ${m.estadoDescripcion ?: "Sin estado"}")
        }
    }
}
