package com.cibertec.applovepaws

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.cibertec.applovepaws.core.network.RetrofitClient
import com.cibertec.applovepaws.core.theme.AppLovePawsTheme
import com.cibertec.applovepaws.feature_adopcion.SolicitudViewModel
import com.cibertec.applovepaws.feature_adopcion.data.repository.AdopcionRepository
import com.cibertec.applovepaws.feature_adopcion.ui.SolicitudScreen
import com.cibertec.applovepaws.feature_mascota.data.dto.MascotaDto
import com.cibertec.applovepaws.feature_mascota.ui.MascotaDetalleScreen
import com.cibertec.applovepaws.feature_mascota.ui.MascotaScreen
import kotlinx.coroutines.launch

// Navegacion simple tipo “navbar” casero esto puede ser temporal como el amor que ella me dio
private sealed class Screen {
    data object Catalogo : Screen()
    data class Detalle(val mascota: MascotaDto) : Screen()
    data class Formulario(val mascota: MascotaDto) : Screen()
    data object Login : Screen()
}

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AppLovePawsTheme {
                Surface(modifier = Modifier.fillMaxSize()) {

                    var screen by remember { mutableStateOf<Screen>(Screen.Catalogo) }

                    // Sesión simple: si el login pasó, ya tienes cookie en el cliente.
                    var sesionOk by remember { mutableStateOf(false) }
                    var usuario by remember { mutableStateOf("") }
                    var celular by remember { mutableStateOf("-") }
                    var correo by remember { mutableStateOf("") }

                    var mascotaPendiente by remember { mutableStateOf<MascotaDto?>(null) }

                    val adopcionRepo = remember { AdopcionRepository(RetrofitClient.adoptionApi) }
                    val solicitudVM = remember { SolicitudViewModel(adopcionRepo) }

                    fun irCatalogo() {
                        solicitudVM.limpiarMensaje()
                        screen = Screen.Catalogo
                    }

                    when (val s = screen) {
                        is Screen.Login -> {
                            LoginScreen(
                                initialUsername = usuario,
                                onCancelar = {
                                    mascotaPendiente = null
                                    screen = Screen.Catalogo
                                },
                                onLoginOk = { u ->
                                    sesionOk = true
                                    usuario = u
                                    correo = u

                                    val pendiente = mascotaPendiente
                                    mascotaPendiente = null
                                    screen = if (pendiente != null) Screen.Formulario(pendiente) else Screen.Catalogo
                                },
                                onLoginError = { sesionOk = false }
                            )
                        }

                        is Screen.Catalogo -> {
                            MascotaScreen(
                                onSeleccionarMascota = { mascota ->
                                    screen = Screen.Detalle(mascota)
                                }
                            )
                        }

                        is Screen.Detalle -> {
                            MascotaDetalleScreen(
                                mascota = s.mascota,
                                onVolver = { irCatalogo() },
                                onDeseoAdoptar = {
                                    if (!sesionOk) {
                                        mascotaPendiente = s.mascota
                                        screen = Screen.Login
                                        return@MascotaDetalleScreen
                                    }

                                    solicitudVM.limpiarMensaje()
                                    screen = Screen.Formulario(s.mascota)
                                }
                            )
                        }

                        is Screen.Formulario -> {
                            SolicitudScreen(
                                viewModel = solicitudVM,
                                mascotaId = s.mascota.id,
                                mascotaNombre = s.mascota.nombre,
                                solicitanteNombre = "", // Simplemente no lo mostramos al patito feo
                                usuario = usuario,
                                celular = celular,
                                correo = correo,
                                onVolver = { screen = Screen.Detalle(s.mascota) },
                                onIrHome = { irCatalogo() } // Fuimonos al catalogo
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun LoginScreen(
    initialUsername: String,
    onCancelar: () -> Unit,
    onLoginOk: (String) -> Unit,
    onLoginError: () -> Unit
) {
    val scope = rememberCoroutineScope()

    var username by remember { mutableStateOf(initialUsername) }
    var password by remember { mutableStateOf("") }
    var loading by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf<String?>(null) }

    fun intentarLogin() {
        scope.launch {
            loading = true
            error = null

            RetrofitClient.clearSession()

            try {
                val resp = RetrofitClient.authApi.login(username.trim(), password)
                val code = resp.code()
                val finalPath = resp.raw().request.url.encodedPath
                val cayoEnLogin = finalPath.contains("/usuarios/login", ignoreCase = true)

                if (code in 200..399 && !cayoEnLogin) {
                    onLoginOk(username.trim())
                } else {
                    onLoginError()
                    error = "Usuario o contraseña incorrectos"
                }
            } catch (e: Exception) {
                onLoginError()
                error = "No pude conectar con el servidor"
            } finally {
                loading = false
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text("Iniciar sesión", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = username,
            onValueChange = { username = it },
            label = { Text("Correo o usuario") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(10.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Contraseña") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        if (!error.isNullOrBlank()) {
            Spacer(Modifier.height(10.dp))
            Text(error ?: "", color = MaterialTheme.colorScheme.error)
        }

        Spacer(Modifier.height(16.dp))

        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            OutlinedButton(onClick = onCancelar, enabled = !loading) { Text("Cancelar") }

            Button(
                onClick = { intentarLogin() },
                enabled = !loading && username.isNotBlank() && password.isNotBlank()
            ) {
                Text(if (loading) "Ingresando..." else "Ingresar")
            }
        }

        Spacer(Modifier.height(8.dp))
        Text(
            "El catálogo funciona sin login, pero para adoptar sí o sí tienes que iniciar sesión.",
            style = MaterialTheme.typography.bodySmall
        )
    }
}