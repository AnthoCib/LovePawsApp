package com.cibertec.applovepaws.feature_adopcion.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cibertec.applovepaws.feature_adopcion.SolicitudViewModel

@Composable
fun SolicitudScreen(
    viewModel: SolicitudViewModel,
    usuarioId: Int,
    mascotaId: Int,
    onVolver: () -> Unit = {},
    onSolicitudExitosa: () -> Unit = {}
) {
    val isLoading by viewModel.isLoading.collectAsState()
    val mensaje by viewModel.mensaje.collectAsState()
    val pqAdoptar = remember { mutableStateOf("") }

    LaunchedEffect(mensaje) {
        if (mensaje?.contains("correctamente", ignoreCase = true) == true) {
            onSolicitudExitosa()
        }
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Solicitud de Adopción", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))

        TextField(
            value = pqAdoptar.value,
            onValueChange = { pqAdoptar.value = it },
            label = { Text("Por qué quieres adoptar") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))
        Button(
            onClick = { viewModel.enviarSolicitud(usuarioId, mascotaId, pqAdoptar.value) },
            enabled = !isLoading && pqAdoptar.value.isNotBlank(),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Enviar Solicitud")
        }

        Spacer(Modifier.height(8.dp))
        OutlinedButton(
            onClick = onVolver,
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Cancelar")
        }

        Spacer(Modifier.height(16.dp))
        if (isLoading) {
            CircularProgressIndicator()
        }

        mensaje?.let {
            Spacer(Modifier.height(8.dp))
            Text(it)
        }
    }
}
