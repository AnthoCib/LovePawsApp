package com.cibertec.applovepaws.feature_mascota.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cibertec.applovepaws.feature_mascota.MascotaViewModel
import com.cibertec.applovepaws.feature_mascota.data.dto.MascotaDto
import coil.compose.AsyncImage



@Composable
fun MascotaScreen(
    viewModel: MascotaViewModel,
    onIrARegistro: () -> Unit = {}
) {
    LaunchedEffect(Unit) {
        viewModel.cargarMascotasLocales()
    }

    Scaffold (
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
                LazyColumn {
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
