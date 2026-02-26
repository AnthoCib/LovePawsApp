package com.cibertec.applovepaws.feature_mascota.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.cibertec.applovepaws.feature_mascota.MascotaViewModel
import com.cibertec.applovepaws.feature_mascota.data.dto.MascotaResponseDTO

@Composable
fun MascotaScreen(
    viewModel: MascotaViewModel = viewModel(),
    onVerDetalle: (Long) -> Unit,
    onAdoptar: (Long) -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.cargarMascotas()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when {
            viewModel.loading -> CircularProgressIndicator(modifier = Modifier.padding(16.dp))
            viewModel.error != null -> Text(
                text = viewModel.error ?: "Error",
                modifier = Modifier.padding(16.dp)
            )
            else -> LazyColumn {
                items(viewModel.mascotas) { mascota ->
                    MascotaCard(
                        mascota = mascota,
                        onVerDetalle = { onVerDetalle(mascota.id.toLong()) },
                        onAdoptar = { onAdoptar(mascota.id.toLong()) }
                    )
                }
            }
        }
    }
}

@Composable
fun MascotaCard(
    mascota: MascotaResponseDTO,
    onVerDetalle: () -> Unit,
    onAdoptar: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(Modifier.padding(12.dp)) {
            AsyncImage(
                model = mascota.fotoUrl,
                contentDescription = mascota.nombre,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            )
            Text(mascota.nombre, style = MaterialTheme.typography.titleMedium)
            Text("Edad: ${mascota.edad}")
            Text("Raza: ${mascota.raza}")
            Text("Estado: ${mascota.estado}")

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(onClick = onVerDetalle, modifier = Modifier.weight(1f)) {
                    Text("Ver detalle")
                }
                Button(onClick = onAdoptar, modifier = Modifier.weight(1f)) {
                    Text("Adoptar")
                }
            }
        }
    }
}
