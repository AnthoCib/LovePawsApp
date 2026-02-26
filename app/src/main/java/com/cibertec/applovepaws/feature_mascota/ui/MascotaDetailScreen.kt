package com.cibertec.applovepaws.feature_mascota.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.cibertec.applovepaws.feature_mascota.MascotaDetailViewModel

@Composable
fun MascotaDetailScreen(
    mascotaId: Long,
    viewModel: MascotaDetailViewModel = viewModel()
) {
    LaunchedEffect(mascotaId) {
        viewModel.loadMascota(mascotaId)
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        when {
            viewModel.isLoading -> CircularProgressIndicator()
            viewModel.error != null -> Text(viewModel.error ?: "Error")
            viewModel.mascota != null -> {
                val m = viewModel.mascota!!
                AsyncImage(model = m.fotoUrl, contentDescription = m.nombre)
                Text(m.nombre, style = MaterialTheme.typography.headlineSmall)
                Text("ID: ${m.id}")
                Text("Raza ID: ${m.razaId}")
                Text("Categoría ID: ${m.categoriaId}")
                Text("Edad: ${m.edad}")
                Text("Sexo: ${m.sexo}")
                Text("Descripción: ${m.descripcion.orEmpty()}")
                Text("Estado ID: ${m.estadoId}")
            }
        }
    }
}
