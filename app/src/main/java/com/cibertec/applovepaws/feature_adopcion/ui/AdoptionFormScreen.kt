package com.cibertec.applovepaws.feature_adopcion.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cibertec.applovepaws.feature_adopcion.AdoptionViewModel
import com.cibertec.applovepaws.feature_adopcion.data.dto.AdoptionRequestDTO

@Composable
fun AdoptionFormScreen(
    mascotaId: Long,
    viewModel: AdoptionViewModel = viewModel()
) {
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var telefono by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var motivo by remember { mutableStateOf("") }
    var formError by remember { mutableStateOf<String?>(null) }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(value = nombre, onValueChange = { nombre = it }, label = { Text("Nombre") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = telefono, onValueChange = { telefono = it }, label = { Text("Teléfono") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = direccion, onValueChange = { direccion = it }, label = { Text("Dirección") }, modifier = Modifier.fillMaxWidth())
        OutlinedTextField(value = motivo, onValueChange = { motivo = it }, label = { Text("Motivo") }, modifier = Modifier.fillMaxWidth())

        Button(
            onClick = {
                if (nombre.isBlank() || email.isBlank() || telefono.isBlank() || direccion.isBlank() || motivo.isBlank()) {
                    formError = "Completa todos los campos requeridos"
                    return@Button
                }
                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    formError = "Email inválido"
                    return@Button
                }
                formError = null
                viewModel.submit(
                    AdoptionRequestDTO(
                        mascotaId = mascotaId,
                        solicitanteNombre = nombre,
                        solicitanteEmail = email,
                        solicitanteTelefono = telefono,
                        direccion = direccion,
                        motivo = motivo
                    )
                )
            },
            modifier = Modifier.padding(top = 12.dp)
        ) {
            Text("Enviar solicitud")
        }

        if (viewModel.isLoading) CircularProgressIndicator(modifier = Modifier.padding(top = 8.dp))
        if (formError != null) Text(formError!!, modifier = Modifier.padding(top = 8.dp))
        if (viewModel.error != null) Text(viewModel.error!!, modifier = Modifier.padding(top = 8.dp))
        if (viewModel.response != null) Text(viewModel.response!!.mensaje, modifier = Modifier.padding(top = 8.dp))
    }
}
