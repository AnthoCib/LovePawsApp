package com.cibertec.applovepaws.feature_mascota.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.cibertec.applovepaws.feature_mascota.MascotaViewModel
import com.cibertec.applovepaws.feature_mascota.data.dto.MascotaDto

@Composable
fun MascotaScreen(
    viewModel: MascotaViewModel,
    esGestor: Boolean,
    mensajeRol: String,
    onIrARegistro: () -> Unit = {},
    onIrAHome: () -> Unit = {},
    onIrALogin: () -> Unit = {}
) {
    LaunchedEffect(Unit) {
        viewModel.cargarMascotasLocales()
    }

    Scaffold(
        floatingActionButton = {
            if (esGestor) {
                FloatingActionButton(
                    onClick = onIrARegistro,
                    containerColor = Color(0xFF1565C0)
                ) {
                    Text("+", color = Color.White, fontSize = 24.sp)
                }
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
                LazyColumn {
                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            TextButton(onClick = onIrAHome) { Text("Inicio") }
                            TextButton(onClick = onIrALogin) { Text("Login") }
                        }

                        Text(
                            text = mensajeRol,
                            color = if (esGestor) Color(0xFF15803D) else Color(0xFF1D4ED8),
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    items(viewModel.mascotas) { mascota ->
                        MascotaItem(mascota)
                    }
                }
            }
        }
    }
}

@Composable
fun MascotaItem(m: MascotaDto) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(Modifier.padding(12.dp)) {
            AsyncImage(
                model = m.fotoUrl,
                contentDescription = m.nombre,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            )

            Text(m.nombre, style = MaterialTheme.typography.titleMedium)

            Text("Edad: ${m.edad}")
            Text("Raza: ${m.razaNombre}")
            Text("Estado: ${m.estadoDescripcion}")
        }
    }
}
