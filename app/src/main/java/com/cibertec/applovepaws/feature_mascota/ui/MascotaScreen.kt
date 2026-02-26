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
import com.cibertec.applovepaws.feature_mascota.MascotaViewModel
import com.cibertec.applovepaws.feature_mascota.data.dto.MascotaDto
import coil.compose.AsyncImage

@Composable
fun MascotaScreen(
    viewModel: MascotaViewModel,
    onVerDetalle: (Int) -> Unit,
    onAdoptar: (Int) -> Unit
) {
    LaunchedEffect(Unit) {
        viewModel.cargarMascotas()
    }

    Box(modifier = Modifier.fillMaxSize()) {
        if (viewModel.loading) {
            CircularProgressIndicator(modifier = Modifier.padding(16.dp))
        } else {
            LazyColumn {
                items(viewModel.mascotas) { mascota ->
                    MascotaItem(
                        m = mascota,
                        onVerDetalle = { onVerDetalle(mascota.id) },
                        onAdoptar = { onAdoptar(mascota.id) }
                    )
                }
            }
        }
    }
}

@Composable
fun MascotaItem(
    m: MascotaDto,
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
                model = m.fotoUrl,
                contentDescription = m.nombre,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            )

            Text(m.nombre, style = MaterialTheme.typography.titleMedium)
            Text("Edad: ${m.edad}")
            Text("Raza: ${m.razaNombre ?: "No especificada"}")
            Text("Estado: ${m.estadoDescripcion ?: "No disponible"}")

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = onVerDetalle,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Ver detalle")
                }
                Button(
                    onClick = onAdoptar,
                    modifier = Modifier.weight(1f)
                ) {
                    Text("Adoptar")
                }
            }
        }
    }
}
