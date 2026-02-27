package com.cibertec.applovepaws.feature_home.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.cibertec.applovepaws.core.session.SessionManager

val AzulNavbar  = Color(0xFF1565C0)
val AzulFondoH  = Color(0xFFE3F2FD)
val VerdeAdopta = Color(0xFF2E7D32)

@Composable
fun HomeScreen(
    onIrACatalogo: () -> Unit = {},
    onIrALogin: () -> Unit = {},
    onIrARegistro: () -> Unit = {},
    onCerrarSesion: () -> Unit = {}
) {
    val context = LocalContext.current
    val estaLogueado = SessionManager.estaLogueado(context)
    val username = SessionManager.obtenerUsername(context)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AzulFondoH)
            .verticalScroll(rememberScrollState())
    ) {

        // ── NAVBAR ──────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(AzulNavbar)
                .padding(horizontal = 16.dp, vertical = 12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "🐾 LovePaws",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    TextButton(onClick = onIrACatalogo) {
                        Text("Catálogo", color = Color.White, fontSize = 13.sp)
                    }
                    if (estaLogueado) {
                        TextButton(onClick = {
                            SessionManager.cerrarSesion(context)
                            onCerrarSesion()
                        }) {
                            Text("Cerrar sesión", color = Color.White, fontSize = 13.sp)
                        }
                    } else {
                        TextButton(onClick = onIrALogin) {
                            Text("Login", color = Color.White, fontSize = 13.sp)
                        }
                        TextButton(onClick = onIrARegistro) {
                            Text("Registro", color = Color.White, fontSize = 13.sp)
                        }
                    }
                }
            }
        }

        // ── HERO ────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .background(
                    Brush.verticalGradient(
                        listOf(Color(0xFF1565C0), Color(0xFF42A5F5))
                    )
                ),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                if (estaLogueado) {
                    Text(
                        text = "¡Hola, $username! 👋",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                }
                Text(
                    text = "Adopta con amor,\ntransforma una vida",
                    color = Color.White,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = onIrACatalogo,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    Text("Ver catálogo", color = AzulNavbar, fontWeight = FontWeight.Bold)
                }
            }
        }

        // ── MÉTRICAS ────────────────────────────────────────
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            MetricaCard("+500", "Mascotas adoptadas", Modifier.weight(1f))
            MetricaCard("+120", "Voluntarios activos", Modifier.weight(1f))
            MetricaCard("24/7", "Soporte", Modifier.weight(1f))
        }

        // ── CARDS DE FUNCIONES ───────────────────────────────
        Text(
            text = "¿Qué puedes hacer?",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            FuncionCard(
                emoji = "🔍",
                titulo = "Explorar mascotas",
                descripcion = "Filtra por raza, categoría y edad para encontrar tu compañero ideal.",
                onClick = onIrACatalogo
            )
            FuncionCard(
                emoji = "📋",
                titulo = "Solicitar adopción",
                descripcion = "Envía tu solicitud y sigue su estado desde tu panel.",
                onClick = if (estaLogueado) onIrACatalogo else onIrALogin
            )
            FuncionCard(
                emoji = "🐾",
                titulo = "Únete a la comunidad",
                descripcion = "Forma parte de historias de rescate y adopción responsable.",
                onClick = {}
            )
        }

        // ── CTA ──────────────────────────────────────────────
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .background(AzulNavbar, RoundedCornerShape(12.dp))
                .padding(24.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Tu hogar puede cambiar su historia",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Cada adopción abre espacio para rescatar una nueva mascota.",
                    color = Color.White.copy(alpha = 0.85f),
                    fontSize = 13.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(12.dp))
                Button(
                    onClick = onIrACatalogo,
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White)
                ) {
                    Text("❤️ Quiero adoptar", color = AzulNavbar, fontWeight = FontWeight.Bold)
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

// ── COMPONENTES AUXILIARES ───────────────────────────────────

@Composable
fun MetricaCard(valor: String, label: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(
            modifier = Modifier.padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(valor, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = AzulNavbar)
            Text(label, fontSize = 11.sp, color = Color.Gray, textAlign = TextAlign.Center)
        }
    }
}

@Composable
fun FuncionCard(emoji: String, titulo: String, descripcion: String, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(4.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(emoji, fontSize = 28.sp)
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(titulo, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Text(descripcion, fontSize = 12.sp, color = Color.Gray)
            }
        }
    }
}