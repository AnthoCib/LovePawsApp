package com.cibertec.applovepaws

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.cibertec.applovepaws.data.api.RetrofitClient
import com.cibertec.applovepaws.data.repository.AdopcionRepository
import com.cibertec.applovepaws.ui.adopcion.SolicitudScreen
import com.cibertec.applovepaws.ui.adopcion.SolicitudViewModel
import com.cibertec.applovepaws.ui.theme.AppLovePawsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Instanciar el repositorio con Retrofit
        val repo = AdopcionRepository(RetrofitClient.api)
        val viewModel = SolicitudViewModel(repo)

        // Usuario y Mascota de ejemplo (en producción vendrían de login y lista de mascotas)
        val usuarioId = 1010
        val mascotaId = 6001

        enableEdgeToEdge()
        setContent {
            MaterialTheme {
                Surface {
                    // Pantalla de Solicitud de Adopción
                    SolicitudScreen(
                        viewModel = viewModel,
                        usuarioId = usuarioId,
                        mascotaId = mascotaId
                    )
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    AppLovePawsTheme {
        Greeting("Android")
    }
}