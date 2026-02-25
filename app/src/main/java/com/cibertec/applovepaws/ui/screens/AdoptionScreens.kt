package com.cibertec.applovepaws.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cibertec.applovepaws.model.AdoptionQuestion
import com.cibertec.applovepaws.model.AdoptionRequest
import com.cibertec.applovepaws.model.Pet

@Composable
fun AdoptionFormScreen(
    selectedPet: Pet?,
    questions: List<AdoptionQuestion>,
    onSubmit: (String, List<AdoptionQuestion>) -> Unit,
    onBack: () -> Unit
) {
    var applicantName by remember { mutableStateOf("") }
    val localQuestions = remember(questions) { mutableStateListOf(*questions.toTypedArray()) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text("Formulario de adopción")
        Text("Mascota: ${selectedPet?.name ?: "No seleccionada"}")

        OutlinedTextField(
            value = applicantName,
            onValueChange = { applicantName = it },
            label = { Text("Tu nombre") },
            modifier = Modifier.fillMaxWidth()
        )

        LazyColumn(modifier = Modifier.weight(1f)) {
            items(localQuestions, key = { it.id }) { question ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = question.checked,
                        onCheckedChange = { checked ->
                            val index = localQuestions.indexOfFirst { it.id == question.id }
                            if (index >= 0) {
                                localQuestions[index] = question.copy(checked = checked)
                            }
                        }
                    )
                    Text(question.text)
                }
            }
        }

        Button(
            onClick = { onSubmit(applicantName, localQuestions.toList()) },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Enviar solicitud")
        }
        OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
            Text("Volver")
        }
    }
}

@Composable
fun AdoptionConfirmationScreen(
    onGoCatalog: () -> Unit,
    onViewRequests: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("✅ Solicitud registrada")
        Spacer(Modifier.height(8.dp))
        Text("Tu solicitud de adopción fue enviada correctamente.")
        Spacer(Modifier.height(12.dp))
        Button(onClick = onGoCatalog, modifier = Modifier.fillMaxWidth()) {
            Text("Volver al catálogo")
        }
        OutlinedButton(onClick = onViewRequests, modifier = Modifier.fillMaxWidth()) {
            Text("Ver mis solicitudes")
        }
    }
}

@Composable
fun MyRequestsScreen(
    requests: List<AdoptionRequest>,
    onBack: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Mis solicitudes de adopción")
        Spacer(Modifier.height(8.dp))
        if (requests.isEmpty()) {
            Text("Aún no tienes solicitudes registradas")
        } else {
            LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(requests) { request ->
                    androidx.compose.material3.Card(modifier = Modifier.fillMaxWidth()) {
                        Column(Modifier.padding(12.dp)) {
                            Text("Mascota: ${request.petName}")
                            Text("Solicitante: ${request.applicantName}")
                            Text("Estado: ${request.status}")
                        }
                    }
                }
            }
        }
        OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
            Text("Volver")
        }
    }
}
