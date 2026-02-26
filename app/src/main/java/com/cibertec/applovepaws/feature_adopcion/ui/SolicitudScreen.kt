package com.cibertec.applovepaws.feature_adopcion.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cibertec.applovepaws.feature_adopcion.SolicitudFormData
import com.cibertec.applovepaws.feature_adopcion.SolicitudViewModel

@Composable
fun SolicitudScreen(
    viewModel: SolicitudViewModel,
    usuarioId: Int,
    mascotaId: Int,
    userName: String,
    userEmail: String
) {
    val isLoading by viewModel.isLoading.collectAsState()
    val mensaje by viewModel.mensaje.collectAsState()

    var contactoNombre by remember(userName) { mutableStateOf(userName) }
    var contactoEmail by remember(userEmail) { mutableStateOf(userEmail) }
    var contactoTelefono by remember { mutableStateOf("") }

    var tipoVivienda by remember { mutableStateOf("") }
    var espacioDisponible by remember { mutableStateOf("") }
    var otrasMascotas by remember { mutableStateOf("") }

    var motivoAdopcion by remember { mutableStateOf("") }
    var experienciaMascotas by remember { mutableStateOf("") }
    var compromisoConfirmado by remember { mutableStateOf(false) }

    LaunchedEffect(mensaje) {
        if (mensaje != null) {
            contactoTelefono = ""
            tipoVivienda = ""
            espacioDisponible = ""
            otrasMascotas = ""
            motivoAdopcion = ""
            experienciaMascotas = ""
            compromisoConfirmado = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text("Formulario de solicitud de adopción", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(16.dp))

        Text("1) Datos del contacto", style = MaterialTheme.typography.titleMedium)
        OutlinedTextField(value = contactoNombre, onValueChange = { contactoNombre = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = contactoEmail, onValueChange = { contactoEmail = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = contactoTelefono, onValueChange = { contactoTelefono = it }, label = { Text("Teléfono") }, modifier = Modifier.fillMaxWidth())

        Spacer(Modifier.height(16.dp))
        Text("2) Datos del hogar", style = MaterialTheme.typography.titleMedium)
        OutlinedTextField(value = tipoVivienda, onValueChange = { tipoVivienda = it }, label = { Text("Tipo de vivienda") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = espacioDisponible, onValueChange = { espacioDisponible = it }, label = { Text("Espacio disponible") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = otrasMascotas, onValueChange = { otrasMascotas = it }, label = { Text("¿Tienes otras mascotas?") }, modifier = Modifier.fillMaxWidth())

        Spacer(Modifier.height(16.dp))
        Text("3) Interés del adoptante", style = MaterialTheme.typography.titleMedium)
        OutlinedTextField(value = motivoAdopcion, onValueChange = { motivoAdopcion = it }, label = { Text("Motivo de adopción") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = experienciaMascotas, onValueChange = { experienciaMascotas = it }, label = { Text("Experiencia previa con mascotas") }, modifier = Modifier.fillMaxWidth())

        Spacer(Modifier.height(16.dp))
        Text("4) Confirmación", style = MaterialTheme.typography.titleMedium)
        androidx.compose.foundation.layout.Row(modifier = Modifier.fillMaxWidth()) {
            Text("Confirmo que me comprometo al cuidado responsable", modifier = Modifier.weight(1f))
            Switch(checked = compromisoConfirmado, onCheckedChange = { compromisoConfirmado = it })
        }

        Spacer(Modifier.height(20.dp))
        Button(
            onClick = {
                viewModel.enviarSolicitud(
                    usuarioId = usuarioId,
                    mascotaId = mascotaId,
                    formData = SolicitudFormData(
                        contactoNombre = contactoNombre,
                        contactoEmail = contactoEmail,
                        contactoTelefono = contactoTelefono,
                        tipoVivienda = tipoVivienda,
                        espacioDisponible = espacioDisponible,
                        otrasMascotas = otrasMascotas,
                        motivoAdopcion = motivoAdopcion,
                        experienciaMascotas = experienciaMascotas,
                        compromisoConfirmado = compromisoConfirmado
                    )
                )
            },
            enabled = !isLoading,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (isLoading) CircularProgressIndicator() else Text("Enviar solicitud")
        }

        mensaje?.let {
            Spacer(Modifier.height(12.dp))
            Text(it)
        }
    }
}
