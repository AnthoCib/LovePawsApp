package com.cibertec.applovepaws.feature_login.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cibertec.applovepaws.feature_login.RegisterViewModel

val AzulPrimario = Color(0xFF1565C0)
val AzulFondo   = Color(0xFFE3F2FD)
val GrisBoton   = Color(0xFF9E9E9E)

@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel = viewModel(),
    onRegisterSuccess: () -> Unit,
    onCancelar: () -> Unit,
    onIrAHome: () -> Unit = {}
) {
    var nombre    by remember { mutableStateOf("") }
    var correo    by remember { mutableStateOf("") }
    var username  by remember { mutableStateOf("") }
    var password  by remember { mutableStateOf("") }
    var telefono  by remember { mutableStateOf("") }
    var direccion by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    LaunchedEffect(viewModel.registerSuccess) {
        if (viewModel.registerSuccess) onRegisterSuccess()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(AzulFondo),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
                .shadow(8.dp, RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White)
        ) {
            Column(
                modifier = Modifier
                    .padding(28.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                // Título
                Text(
                    text = "Registrar Nuevo Usuario",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = AzulPrimario,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Crea tu cuenta para adoptar mascotas",
                    fontSize = 13.sp,
                    color = Color.Gray,
                    textAlign = TextAlign.Center
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(onClick = onIrAHome) { Text("Inicio") }
                    TextButton(onClick = onCancelar) { Text("Ir a Login") }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Fila 1: Username + Nombre
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    CampoTexto(
                        modifier = Modifier.weight(1f),
                        valor = username,
                        onValorChange = { username = it },
                        label = "Username",
                    )
                    CampoTexto(
                        modifier = Modifier.weight(1f),
                        valor = nombre,
                        onValorChange = { nombre = it },
                        label = "Nombre",
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Fila 2: Correo + Contraseña
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    CampoTexto(
                        modifier = Modifier.weight(1f),
                        valor = correo,
                        onValorChange = { correo = it },
                        label = "Correo Electrónico",
                    )
                    OutlinedTextField(
                        modifier = Modifier.weight(1f),
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Contraseña", fontSize = 12.sp) },
                        trailingIcon = {
                            IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                Text(if (passwordVisible) "Ocultar" else "Ver", fontSize = 11.sp, color = AzulPrimario)
                            }
                        },
                        visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        singleLine = true,
                        shape = RoundedCornerShape(8.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Teléfono (solo ocupa mitad)
                CampoTexto(
                    modifier = Modifier.fillMaxWidth(0.5f).align(Alignment.Start),
                    valor = telefono,
                    onValorChange = { telefono = it },
                    label = "Teléfono",
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Dirección (ancho completo)
                CampoTexto(
                    modifier = Modifier.fillMaxWidth(),
                    valor = direccion,
                    onValorChange = { direccion = it },
                    label = "Dirección",
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Error
                viewModel.errorMessage?.let {
                    Text(text = it, color = Color.Red, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Loading
                if (viewModel.loading) {
                    CircularProgressIndicator(color = AzulPrimario)
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Botones: Registrar + Cancelar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Button(
                        onClick = {
                            viewModel.register(nombre, correo, username, password, telefono, direccion)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = AzulPrimario),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Registrar", color = Color.White)
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Button(
                        onClick = onCancelar,
                        colors = ButtonDefaults.buttonColors(containerColor = GrisBoton),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Cancelar", color = Color.White)
                    }
                }
            }
        }
    }
}

@Composable
fun CampoTexto(
    modifier: Modifier = Modifier,
    valor: String,
    onValorChange: (String) -> Unit,
    label: String
) {
    OutlinedTextField(
        modifier = modifier,
        value = valor,
        onValueChange = onValorChange,
        label = { Text(label, fontSize = 12.sp) },
        singleLine = true,
        shape = RoundedCornerShape(8.dp)
    )
}