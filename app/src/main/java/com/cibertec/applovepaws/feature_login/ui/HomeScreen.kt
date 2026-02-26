package com.cibertec.applovepaws.feature_login.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.Composable
import com.cibertec.applovepaws.feature_login.UserSession

@Composable
fun HomeScreen(
    session: UserSession,
    onIrCatalogo: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("LovePaws", style = MaterialTheme.typography.headlineMedium)
        Text("Hola, ${session.name}", style = MaterialTheme.typography.titleMedium)
        Text("Encuentra a tu próxima mascota compañera")

        Button(
            onClick = onIrCatalogo,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
        ) {
            Text("Ver catálogo de mascotas")
        }
    }
}
