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
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.cibertec.applovepaws.model.Pet

@Composable
fun CatalogScreen(
    pets: List<Pet>,
    isLoading: Boolean,
    onSeeDetail: (String) -> Unit,
    onAdopt: (String) -> Unit,
    onMyRequests: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Catálogo de mascotas", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(8.dp))
        OutlinedButton(onClick = onMyRequests, modifier = Modifier.fillMaxWidth()) {
            Text("Ver mis solicitudes")
        }
        Spacer(Modifier.height(8.dp))

        if (isLoading) {
            CircularProgressIndicator()
            return
        }

        LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
            items(pets) { pet ->
                PetCard(pet = pet, onSeeDetail = onSeeDetail, onAdopt = onAdopt)
            }
        }
    }
}

@Composable
private fun PetCard(
    pet: Pet,
    onSeeDetail: (String) -> Unit,
    onAdopt: (String) -> Unit
) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(12.dp)) {
            AsyncImage(
                model = pet.imageUrl,
                contentDescription = pet.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth().height(180.dp)
            )
            Spacer(Modifier.height(8.dp))
            Text(pet.name, style = MaterialTheme.typography.titleMedium)
            Text("${pet.breed} · ${pet.age}")
            Spacer(Modifier.height(8.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = { onSeeDetail(pet.id) }, modifier = Modifier.weight(1f)) {
                    Text("Ver detalle")
                }
                Button(onClick = { onAdopt(pet.id) }, modifier = Modifier.weight(1f)) {
                    Text("Adoptar")
                }
            }
        }
    }
}

@Composable
fun PetDetailScreen(
    pet: Pet?,
    onBack: () -> Unit,
    onAdopt: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        if (pet == null) {
            Text("No se encontró la mascota")
            OutlinedButton(onClick = onBack) { Text("Volver") }
            return
        }

        AsyncImage(
            model = pet.imageUrl,
            contentDescription = pet.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth().height(220.dp)
        )
        Spacer(Modifier.height(10.dp))
        Text(pet.name, style = MaterialTheme.typography.headlineSmall)
        Text("Raza: ${pet.breed}")
        Text("Edad: ${pet.age}")
        Spacer(Modifier.height(8.dp))
        Text(pet.description)
        Spacer(Modifier.height(12.dp))
        Button(onClick = onAdopt, modifier = Modifier.fillMaxWidth()) {
            Text("Adoptar")
        }
        OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth()) {
            Text("Volver")
        }
    }
}
