package com.cibertec.applovepaws.feature_mascota.ui

import android.app.Application
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.room.Room
import com.cibertec.applovepaws.core.database.AppDatabase
import com.cibertec.applovepaws.core.network.RetrofitClient
import com.cibertec.applovepaws.feature_mascota.MascotaViewModel
import com.cibertec.applovepaws.feature_mascota.data.repository.MascotaRepository

@Composable
fun RegisterMascotaScreen(
    currentRole: String
) {
    val context = LocalContext.current
    val appContext = context.applicationContext

    val mascotaViewModel: MascotaViewModel = viewModel(
        factory = MascotaViewModelFactory(appContext as Application, currentRole)
    )

    val mascotas by mascotaViewModel.mascotas.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        mascotaViewModel.sincronizarPendientes()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Registro de Mascotas", style = MaterialTheme.typography.headlineSmall)

        if (!mascotaViewModel.isGestor) {
            Text(
                text = "Solo los gestores pueden registrar mascotas",
                color = MaterialTheme.colorScheme.error
            )
        }

        OutlinedTextField(
            value = mascotaViewModel.formState.nombre,
            onValueChange = mascotaViewModel::onNombreChange,
            label = { Text("Nombre") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = mascotaViewModel.formState.raza,
            onValueChange = mascotaViewModel::onRazaChange,
            label = { Text("Raza") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = mascotaViewModel.formState.edad,
            onValueChange = mascotaViewModel::onEdadChange,
            label = { Text("Edad") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = mascotaViewModel.formState.sexo,
            onValueChange = mascotaViewModel::onSexoChange,
            label = { Text("Sexo") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = mascotaViewModel.formState.descripcion,
            onValueChange = mascotaViewModel::onDescripcionChange,
            label = { Text("Descripción") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = mascotaViewModel.formState.fotoUrl,
            onValueChange = mascotaViewModel::onFotoUrlChange,
            label = { Text("Foto URL (opcional)") },
            modifier = Modifier.fillMaxWidth()
        )
        OutlinedTextField(
            value = mascotaViewModel.formState.estado,
            onValueChange = mascotaViewModel::onEstadoChange,
            label = { Text("Estado") },
            modifier = Modifier.fillMaxWidth()
        )

        Button(
            onClick = mascotaViewModel::registrarMascota,
            enabled = mascotaViewModel.isGestor && !mascotaViewModel.loading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Registrar Mascota")
        }

        if (mascotaViewModel.loading) {
            CircularProgressIndicator()
        }

        mascotaViewModel.message?.let {
            Text(
                text = it,
                color = if (it.contains("registrada", true)) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
            )
        }

        Text("Mascotas locales: ${mascotas.size}")
    }
}

@Composable
fun MascotaScreen(currentRole: String = "ADOPTANTE") {
    RegisterMascotaScreen(currentRole = currentRole)
}

private class MascotaViewModelFactory(
    private val application: Application,
    private val role: String
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        val db = Room.databaseBuilder(application, AppDatabase::class.java, "lovepaws-db")
            .fallbackToDestructiveMigration()
            .build()
        val repository = MascotaRepository(
            context = application,
            api = RetrofitClient.mascotaApi,
            dao = db.mascotaDao()
        )
        return MascotaViewModel(repository, role) as T
    }
}
