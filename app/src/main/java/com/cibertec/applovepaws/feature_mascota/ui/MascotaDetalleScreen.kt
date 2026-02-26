package com.cibertec.applovepaws.feature_mascota.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.cibertec.applovepaws.feature_mascota.data.dto.MascotaDto

@Composable
fun MascotaDetalleScreen(
    mascota: MascotaDto?,
    onAdoptar: () -> Unit
) {
    if (mascota == null) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text("No se encontró la mascota")
        }
        return
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        AsyncImage(
            model = mascota.fotoUrl,
            contentDescription = mascota.nombre,
            modifier = Modifier
                .fillMaxWidth()
                .height(220.dp)
        )

        Text(mascota.nombre, style = MaterialTheme.typography.headlineSmall)
        Text("Edad: ${mascota.edad}")
        Text("Sexo: ${mascota.sexo}")
        Text("Raza: ${mascota.razaNombre ?: "No especificada"}")
        Text("Categoría: ${mascota.categoriaNombre ?: "No especificada"}")
        Text("Descripción: ${mascota.descripcion ?: "Sin descripción"}")

        Button(
            onClick = onAdoptar,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Adoptar")
        }
    }
}
