package com.cibertec.applovepaws.feature_mascota.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.cibertec.applovepaws.feature_mascota.data.dto.MascotaDto

@Composable
fun MascotaDetalleScreen(
    mascota: MascotaDto,
    onVolver: () -> Unit,
    onAdoptar: (MascotaDto) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Detalle de mascota", style = MaterialTheme.typography.headlineSmall)

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(12.dp)) {
                AsyncImage(
                    model = mascota.fotoUrl,
                    contentDescription = mascota.nombre,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(220.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(mascota.nombre, style = MaterialTheme.typography.titleLarge)
                Text("Edad: ${mascota.edad}")
                Text("Sexo: ${mascota.sexo}")
                Text("Raza: ${mascota.razaNombre ?: "No especificada"}")
                Text("Categoría: ${mascota.categoriaNombre ?: "No especificada"}")
                Text("Estado: ${mascota.estadoDescripcion ?: "Sin estado"}")
                Text(
                    text = mascota.descripcion ?: "Sin descripción",
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 8.dp)
                )
            }
        }

        Button(
            onClick = { onAdoptar(mascota) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Solicitar adopción")
        }

        OutlinedButton(
            onClick = onVolver,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Volver al catálogo")
        }
    }
}
