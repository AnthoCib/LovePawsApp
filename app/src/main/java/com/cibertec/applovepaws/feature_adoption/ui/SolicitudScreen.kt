package com.cibertec.applovepaws.feature_adoption.ui
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cibertec.applovepaws.feature_adoption.SolicitudViewModel

@Composable
fun SolicitudScreen(viewModel: SolicitudViewModel, usuarioId: Int, mascotaId: Int) {

    val isLoading by viewModel.isLoading.collectAsState()
    val mensaje by viewModel.mensaje.collectAsState()
    var pqAdoptar = remember { mutableStateOf("") } // Campo editable en UI

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
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Enviar Solicitud")
        }

        Spacer(Modifier.height(16.dp))
        if (isLoading) {
            CircularProgressIndicator()
        }

        mensaje?.let {
            Spacer(Modifier.height(8.dp))
            Text(it)
        }
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

