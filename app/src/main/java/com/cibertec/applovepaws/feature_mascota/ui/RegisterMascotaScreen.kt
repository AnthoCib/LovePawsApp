package com.cibertec.applovepaws.feature_mascota.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cibertec.applovepaws.feature_mascota.MascotaViewModel
import com.cibertec.applovepaws.feature_mascota.MascotaViewModelFactory

val AzulPrimario = Color(0xFF1565C0)

@Composable
fun RegisterMascotaScreen(
    onRegistroExitoso: () -> Unit,
    onCancelar: () -> Unit
) {
    val context = LocalContext.current
    val viewModel: MascotaViewModel = viewModel(factory = MascotaViewModelFactory(context))

    var nombre by remember { mutableStateOf("") }
    var edad by remember { mutableStateOf("") }
    var descripcion by remember { mutableStateOf("") }
    var fotoUrl by remember { mutableStateOf("") }

    // Sexo
    val sexoOpciones = listOf("M" to "Macho", "H" to "Hembra")
    var sexoSeleccionado by remember { mutableStateOf("M") }
    var sexoExpanded by remember { mutableStateOf(false) }

    // Categoría
    val categorias = listOf(
        5000 to "Cachorro", 5001 to "Joven", 5002 to "Adulto",
        5003 to "Senior", 5004 to "Especial"
    )
    var categoriaSeleccionada by remember { mutableStateOf(categorias[0]) }
    var categoriaExpanded by remember { mutableStateOf(false) }

    // Raza
    val razas = listOf(
        4000 to "Mestizo", 4001 to "Labrador", 4002 to "Pastor Alemán",
        4003 to "Criollo", 4004 to "Siamés", 4005 to "Persa"
    )
    var razaSeleccionada by remember { mutableStateOf(razas[0]) }
    var razaExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(viewModel.registroExitoso) {
        if (viewModel.registroExitoso) onRegistroExitoso()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Registrar nueva mascota",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = AzulPrimario
                )
                Text(
                    text = "Completa los datos para publicar una nueva mascota en adopción.",
                    fontSize = 13.sp,
                    color = Color.Gray
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Nombre
                OutlinedTextField(
                    value = nombre,
                    onValueChange = { nombre = it },
                    label = { Text("Nombre") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Edad y Sexo en fila
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedTextField(
                        value = edad,
                        onValueChange = { edad = it },
                        label = { Text("Edad") },
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(8.dp),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
                    )

                    // Dropdown Sexo
                    Box(modifier = Modifier.weight(1f)) {
                        OutlinedTextField(
                            value = sexoOpciones.first { it.first == sexoSeleccionado }.second,
                            onValueChange = {},
                            label = { Text("Sexo") },
                            readOnly = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(8.dp),
                            trailingIcon = {
                                TextButton(onClick = { sexoExpanded = true }) {
                                    Text("▼", color = AzulPrimario)
                                }
                            }
                        )
                        DropdownMenu(expanded = sexoExpanded, onDismissRequest = { sexoExpanded = false }) {
                            sexoOpciones.forEach { (id, nombre) ->
                                DropdownMenuItem(
                                    text = { Text(nombre) },
                                    onClick = { sexoSeleccionado = id; sexoExpanded = false }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Dropdown Categoría
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = categoriaSeleccionada.second,
                        onValueChange = {},
                        label = { Text("Categoría") },
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        trailingIcon = {
                            TextButton(onClick = { categoriaExpanded = true }) {
                                Text("▼", color = AzulPrimario)
                            }
                        }
                    )
                    DropdownMenu(expanded = categoriaExpanded, onDismissRequest = { categoriaExpanded = false }) {
                        categorias.forEach { opcion ->
                            DropdownMenuItem(
                                text = { Text(opcion.second) },
                                onClick = { categoriaSeleccionada = opcion; categoriaExpanded = false }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Dropdown Raza
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedTextField(
                        value = razaSeleccionada.second,
                        onValueChange = {},
                        label = { Text("Raza") },
                        readOnly = true,
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        trailingIcon = {
                            TextButton(onClick = { razaExpanded = true }) {
                                Text("▼", color = AzulPrimario)
                            }
                        }
                    )
                    DropdownMenu(expanded = razaExpanded, onDismissRequest = { razaExpanded = false }) {
                        razas.forEach { opcion ->
                            DropdownMenuItem(
                                text = { Text(opcion.second) },
                                onClick = { razaSeleccionada = opcion; razaExpanded = false }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Descripción
                OutlinedTextField(
                    value = descripcion,
                    onValueChange = { descripcion = it },
                    label = { Text("Descripción") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(100.dp),
                    shape = RoundedCornerShape(8.dp),
                    maxLines = 4
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Foto URL
                OutlinedTextField(
                    value = fotoUrl,
                    onValueChange = { fotoUrl = it },
                    label = { Text("URL de foto (opcional)") },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    singleLine = true
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Error
                viewModel.errorMessage?.let {
                    Text(text = it, color = Color.Red, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                }

                if (viewModel.loading) {
                    CircularProgressIndicator(color = AzulPrimario)
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Botones
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Button(
                        onClick = {
                            viewModel.registrarMascota(
                                nombre = nombre,
                                razaId = razaSeleccionada.first,
                                categoriaId = categoriaSeleccionada.first,
                                edad = edad.toIntOrNull() ?: 0,
                                sexo = sexoSeleccionado,
                                descripcion = descripcion.ifBlank { null },
                                fotoUrl = fotoUrl.ifBlank { null },
                                estadoId = "DISPONIBLE"
                            )
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = AzulPrimario),
                        shape = RoundedCornerShape(8.dp),
                        enabled = !viewModel.loading
                    ) {
                        Text("Guardar Mascota", color = Color.White)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Button(
                        onClick = onCancelar,
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Cancelar", color = Color.White)
                    }
                }
            }
        }
    }
}
