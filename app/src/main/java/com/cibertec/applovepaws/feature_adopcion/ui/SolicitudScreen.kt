package com.cibertec.applovepaws.feature_adopcion.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.collectAsState
import com.cibertec.applovepaws.feature_adopcion.SolicitudViewModel
import com.cibertec.applovepaws.feature_adopcion.data.dto.MascotaRefDto
import com.cibertec.applovepaws.feature_adopcion.data.dto.SolicitudAdopcionDto

@Composable
fun SolicitudScreen(
    viewModel: SolicitudViewModel,
    usuarioId: Int,
    mascotaId: Int,
    mascotaNombre: String,
    solicitanteNombre: String = "",
    celular: String = "",
    correo: String = "",
    onVolver: () -> Unit,
    onIrHome: () -> Unit   // ✅ NUEVO
) {
    val loading by viewModel.isLoading.collectAsState()
    val mensaje by viewModel.mensaje.collectAsState()

    // =========================
    // Opciones (IGUAL a la web)
    // =========================
    val optHoras = listOf("1 a 3 horas", "3 a 8 horas", "Todo el día")
    val optMotivacion = listOf(
        "Compañía y bienestar emocional",
        "Amor por los animales rescatados",
        "Mi familia desea integrar una mascota",
        "Quiero brindar un hogar responsable"
    )
    val optExperiencia = listOf(
        "Primera vez",
        "Sí, actualmente tengo mascota(s)",
        "Sí, tuve antes y tengo experiencia"
    )
    val optCostos = listOf("Sí, completamente", "Sí, con apoyo familiar", "Necesito orientación")
    val optVivienda = listOf("Casa con patio", "Casa sin patio", "Departamento")
    val optNinosMascotas = listOf("No", "Niños en casa", "Otras mascotas", "Niños y mascotas")

    // Extras (no se guardan en BD/API actualmente)
    val optResponsable = listOf("Yo", "Yo y mi pareja", "Toda la familia")
    val optPaseos = listOf("2 veces al día", "1 vez al día", "Días alternos")
    val optFueraCasa = listOf("Dentro de casa", "Con familiar/cuidador", "Guardería temporal")
    val optViajeSemanas = listOf("Familiar cercano", "Cuidador de confianza", "No suelo viajar")
    val optViajeMeses = listOf("Llevarla conmigo", "Dejarla con familiar responsable", "Servicio profesional de cuidado")
    val optPlanComportamiento = listOf(
        "Buscar ayuda veterinaria y adiestramiento",
        "Aplicar rutina de corrección con paciencia",
        "Pedir orientación profesional inmediata"
    )

    // =========================
    // Estado del wizard
    // =========================
    var paso by remember { mutableStateOf(1) } // 1..4
    var error by remember { mutableStateOf<String?>(null) }

    // Datos contacto (en móvil los puedes mostrar/editar; el backend usa el usuario del token)
    var sNombre by remember { mutableStateOf(solicitanteNombre) }
    var sCelular by remember { mutableStateOf(celular) }
    var sCorreo by remember { mutableStateOf(correo) }

    // Preguntas guardables (BD/API)
    var p1Horas by remember { mutableStateOf("") }           // tiempoDedicado
    var p2Motivacion by remember { mutableStateOf("") }      // pqAdoptar
    var p3Experiencia by remember { mutableStateOf("") }     // experiencia
    var p4Costos by remember { mutableStateOf("") }          // cubrirCostos
    var p5Vivienda by remember { mutableStateOf("") }        // tipoVivienda
    var p6NinosMascotas by remember { mutableStateOf("") }   // ninosOtraMascotas
    var p13Plan by remember { mutableStateOf("") }           // planMascota
    var p16Info by remember { mutableStateOf("") }           // infoAdicional (texto libre)

    // Extras (se irán dentro de infoAdicional)
    var p7Segura by remember { mutableStateOf("") }          // Sí/No
    var p8Responsable by remember { mutableStateOf("") }
    var p9Paseos by remember { mutableStateOf("") }
    var p10FueraCasa by remember { mutableStateOf("") }
    var p11UnAnio by remember { mutableStateOf("") }         // Sí/No
    var p12DiezAnios by remember { mutableStateOf("") }      // Sí/No
    var p14Semanas by remember { mutableStateOf("") }
    var p15Meses by remember { mutableStateOf("") }

    // Para menús (reutilizable)
    var menuKey by remember { mutableStateOf<String?>(null) }

    fun validarPaso(actual: Int): Boolean {
        error = null
        return when (actual) {
            1 -> {
                if (p1Horas.isBlank()) { error = "Selecciona cuántas horas dedicarás (P1)."; false }
                else if (p2Motivacion.isBlank()) { error = "Selecciona tu motivación (P2)."; false }
                else if (p3Experiencia.isBlank()) { error = "Selecciona experiencia previa (P3)."; false }
                else if (p4Costos.isBlank()) { error = "Selecciona si puedes cubrir costos (P4)."; false }
                else true
            }
            2 -> {
                if (p5Vivienda.isBlank()) { error = "Selecciona tipo de vivienda (P5)."; false }
                else if (p6NinosMascotas.isBlank()) { error = "Selecciona niños/mascotas (P6)."; false }
                else if (p7Segura.isBlank()) { error = "Responde si tu vivienda es segura (P7)."; false }
                else if (p8Responsable.isBlank()) { error = "Selecciona responsable principal (P8)."; false }
                else if (p9Paseos.isBlank()) { error = "Selecciona frecuencia de paseos (P9)."; false }
                else if (p10FueraCasa.isBlank()) { error = "Selecciona dónde permanecerá fuera de casa (P10)."; false }
                else true
            }
            3 -> {
                if (p11UnAnio.isBlank()) { error = "Responde si mantendrás el cuidado 1 año (P11)."; false }
                else if (p12DiezAnios.isBlank()) { error = "Responde el horizonte 5-10 años (P12)."; false }
                else if (p13Plan.isBlank()) { error = "Selecciona qué harías ante problemas (P13)."; false }
                else if (p14Semanas.isBlank()) { error = "Selecciona quién cuidará si viajas semanas (P14)."; false }
                else if (p15Meses.isBlank()) { error = "Selecciona qué harías si viajas meses (P15)."; false }
                else true
            }
            else -> true
        }
    }

    fun construirInfoAdicionalFinal(): String? {
        val extras = buildString {
            if (sNombre.isNotBlank()) appendLine("Solicitante: $sNombre")
            if (sCelular.isNotBlank()) appendLine("Celular: $sCelular")
            if (sCorreo.isNotBlank()) appendLine("Correo: $sCorreo")

            if (p7Segura.isNotBlank()) appendLine("P7 Vivienda segura: $p7Segura")
            if (p8Responsable.isNotBlank()) appendLine("P8 Responsable cuidado: $p8Responsable")
            if (p9Paseos.isNotBlank()) appendLine("P9 Frecuencia paseos: $p9Paseos")
            if (p10FueraCasa.isNotBlank()) appendLine("P10 Fuera de casa: $p10FueraCasa")
            if (p11UnAnio.isNotBlank()) appendLine("P11 Mantener 1 año: $p11UnAnio")
            if (p12DiezAnios.isNotBlank()) appendLine("P12 5-10 años: $p12DiezAnios")
            if (p14Semanas.isNotBlank()) appendLine("P14 Viaje semanas: $p14Semanas")
            if (p15Meses.isNotBlank()) appendLine("P15 Viaje meses: $p15Meses")
        }.trim()

        val libre = p16Info.trim()
        val combinado = listOf(libre, extras)
            .filter { it.isNotBlank() }
            .joinToString("\n\n")
            .trim()

        return if (combinado.isBlank()) null else combinado
    }

    val scroll = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scroll)
            .padding(16.dp)
    ) {
        Text("Formulario de adopción", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(4.dp))
        Text("Mascota: $mascotaNombre", style = MaterialTheme.typography.bodyMedium)

        Spacer(Modifier.height(12.dp))

        // Indicador simple de pasos
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(if (paso == 1) "● 1 Contacto" else "○ 1 Contacto")
            Text(if (paso == 2) "● 2 Hogar" else "○ 2 Hogar")
            Text(if (paso == 3) "● 3 Intereses" else "○ 3 Intereses")
            Text(if (paso == 4) "● 4 Confirmación" else "○ 4 Confirmación")
        }

        Spacer(Modifier.height(12.dp))

        if (!error.isNullOrBlank()) {
            Text(error ?: "", color = MaterialTheme.colorScheme.error)
            Spacer(Modifier.height(10.dp))
        }

        // =========================
        // PASO 1: Contacto + P1..P4
        // =========================
        if (paso == 1) {
            OutlinedTextField(
                value = sNombre,
                onValueChange = { sNombre = it },
                label = { Text("Solicitante") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = sCelular,
                onValueChange = { sCelular = it },
                label = { Text("Celular") },
                modifier = Modifier.fillMaxWidth()
            )
            Spacer(Modifier.height(8.dp))
            OutlinedTextField(
                value = sCorreo,
                onValueChange = { sCorreo = it },
                label = { Text("Correo electrónico") },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(Modifier.height(12.dp))

            Text("1. ¿Cuántas horas al día podrá dedicar a la mascota?")
            optHoras.forEach { op ->
                Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                    RadioButton(selected = p1Horas == op, onClick = { p1Horas = op })
                    Text(op)
                }
            }

            Spacer(Modifier.height(10.dp))

            DropdownSimple(
                label = "2. ¿Cuál es su principal motivación para adoptar?",
                opciones = optMotivacion,
                selected = p2Motivacion,
                menuKey = "p2",
                openKey = menuKey,
                onOpen = { menuKey = "p2" },
                onClose = { menuKey = null },
                onSelect = { p2Motivacion = it; menuKey = null }
            )

            Spacer(Modifier.height(10.dp))

            DropdownSimple(
                label = "3. ¿Ha tenido mascotas anteriormente?",
                opciones = optExperiencia,
                selected = p3Experiencia,
                menuKey = "p3",
                openKey = menuKey,
                onOpen = { menuKey = "p3" },
                onClose = { menuKey = null },
                onSelect = { p3Experiencia = it; menuKey = null }
            )

            Spacer(Modifier.height(10.dp))

            DropdownSimple(
                label = "4. ¿Puede cubrir alimentación, salud y cuidados básicos?",
                opciones = optCostos,
                selected = p4Costos,
                menuKey = "p4",
                openKey = menuKey,
                onOpen = { menuKey = "p4" },
                onClose = { menuKey = null },
                onSelect = { p4Costos = it; menuKey = null }
            )
        }

        // =========================
        // PASO 2: Hogar + P5..P10
        // =========================
        if (paso == 2) {
            DropdownSimple(
                label = "5. Tipo de vivienda",
                opciones = optVivienda,
                selected = p5Vivienda,
                menuKey = "p5",
                openKey = menuKey,
                onOpen = { menuKey = "p5" },
                onClose = { menuKey = null },
                onSelect = { p5Vivienda = it; menuKey = null }
            )

            Spacer(Modifier.height(10.dp))

            DropdownSimple(
                label = "6. ¿Hay niños u otras mascotas en casa?",
                opciones = optNinosMascotas,
                selected = p6NinosMascotas,
                menuKey = "p6",
                openKey = menuKey,
                onOpen = { menuKey = "p6" },
                onClose = { menuKey = null },
                onSelect = { p6NinosMascotas = it; menuKey = null }
            )

            Spacer(Modifier.height(10.dp))

            Text("7. ¿Tu vivienda tiene zonas seguras para evitar escapes?")
            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                RadioButton(selected = p7Segura == "Sí", onClick = { p7Segura = "Sí" })
                Text("Sí")
                Spacer(Modifier.width(14.dp))
                RadioButton(selected = p7Segura == "No", onClick = { p7Segura = "No" })
                Text("No")
            }

            Spacer(Modifier.height(10.dp))

            DropdownSimple(
                label = "8. ¿Quién será el responsable principal del cuidado?",
                opciones = optResponsable,
                selected = p8Responsable,
                menuKey = "p8",
                openKey = menuKey,
                onOpen = { menuKey = "p8" },
                onClose = { menuKey = null },
                onSelect = { p8Responsable = it; menuKey = null }
            )

            Spacer(Modifier.height(10.dp))

            DropdownSimple(
                label = "9. ¿Con qué frecuencia paseará a la mascota?",
                opciones = optPaseos,
                selected = p9Paseos,
                menuKey = "p9",
                openKey = menuKey,
                onOpen = { menuKey = "p9" },
                onClose = { menuKey = null },
                onSelect = { p9Paseos = it; menuKey = null }
            )

            Spacer(Modifier.height(10.dp))

            DropdownSimple(
                label = "10. ¿Dónde permanecerá la mascota cuando salga de casa?",
                opciones = optFueraCasa,
                selected = p10FueraCasa,
                menuKey = "p10",
                openKey = menuKey,
                onOpen = { menuKey = "p10" },
                onClose = { menuKey = null },
                onSelect = { p10FueraCasa = it; menuKey = null }
            )
        }

        // =========================
        // PASO 3: Intereses + P11..P16
        // =========================
        if (paso == 3) {
            Text("11. ¿Podrá mantener el cuidado por al menos 1 año?")
            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                RadioButton(selected = p11UnAnio == "Sí", onClick = { p11UnAnio = "Sí" })
                Text("Sí")
                Spacer(Modifier.width(14.dp))
                RadioButton(selected = p11UnAnio == "No", onClick = { p11UnAnio = "No" })
                Text("No")
            }

            Spacer(Modifier.height(10.dp))

            Text("12. ¿Y de aquí a 5 o 10 años?")
            Row(verticalAlignment = androidx.compose.ui.Alignment.CenterVertically) {
                RadioButton(selected = p12DiezAnios == "Sí", onClick = { p12DiezAnios = "Sí" })
                Text("Sí")
                Spacer(Modifier.width(14.dp))
                RadioButton(selected = p12DiezAnios == "No", onClick = { p12DiezAnios = "No" })
                Text("No")
            }

            Spacer(Modifier.height(10.dp))

            DropdownSimple(
                label = "13. ¿Qué haría si la mascota presenta problemas de comportamiento?",
                opciones = optPlanComportamiento,
                selected = p13Plan,
                menuKey = "p13",
                openKey = menuKey,
                onOpen = { menuKey = "p13" },
                onClose = { menuKey = null },
                onSelect = { p13Plan = it; menuKey = null }
            )

            Spacer(Modifier.height(10.dp))

            DropdownSimple(
                label = "14. Si viaja por semanas, ¿quién cuidará la mascota?",
                opciones = optViajeSemanas,
                selected = p14Semanas,
                menuKey = "p14",
                openKey = menuKey,
                onOpen = { menuKey = "p14" },
                onClose = { menuKey = null },
                onSelect = { p14Semanas = it; menuKey = null }
            )

            Spacer(Modifier.height(10.dp))

            DropdownSimple(
                label = "15. Si viaja por meses, ¿qué hará?",
                opciones = optViajeMeses,
                selected = p15Meses,
                menuKey = "p15",
                openKey = menuKey,
                onOpen = { menuKey = "p15" },
                onClose = { menuKey = null },
                onSelect = { p15Meses = it; menuKey = null }
            )

            Spacer(Modifier.height(10.dp))

            OutlinedTextField(
                value = p16Info,
                onValueChange = { p16Info = it },
                label = { Text("16. Información adicional (opcional y breve)") },
                modifier = Modifier.fillMaxWidth(),
                minLines = 3
            )
        }

        // =========================
        // PASO 4: Confirmación
        // =========================
        if (paso == 4) {
            Text("RESULTADOS DE LA SOLICITUD", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(10.dp))

            Text("Mascota: $mascotaNombre")
            Spacer(Modifier.height(6.dp))

            Text("P1 Horas: $p1Horas")
            Text("P2 Motivación: $p2Motivacion")
            Text("P3 Experiencia: $p3Experiencia")
            Text("P4 Costos: $p4Costos")
            Text("P5 Vivienda: $p5Vivienda")
            Text("P6 Niños/mascotas: $p6NinosMascotas")
            Text("P13 Plan: $p13Plan")

            Spacer(Modifier.height(12.dp))

            val enviadoOk = !mensaje.isNullOrBlank() && mensaje!!.contains("enviada", ignoreCase = true)

            Button(
                onClick = {
                    if (enviadoOk) {
                        onIrHome() // ✅ vuelve al home
                    } else {
                        val dto = SolicitudAdopcionDto(
                            mascota = MascotaRefDto(mascotaId),
                            pqAdoptar = p2Motivacion,
                            tiempoDedicado = p1Horas,
                            experiencia = p3Experiencia,
                            cubrirCostos = p4Costos,
                            tipoVivienda = p5Vivienda,
                            ninosOtraMascotas = p6NinosMascotas,
                            planMascota = p13Plan,
                            infoAdicional = construirInfoAdicionalFinal()
                        )
                        viewModel.enviarSolicitudCompleta(dto) // ✅ envía
                    }
                },
                enabled = !loading,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    when {
                        loading -> "Enviando..."
                        enviadoOk -> "Ir al inicio"
                        else -> "Finalizar"
                    }
                )
            }

            if (!mensaje.isNullOrBlank()) {
                Spacer(Modifier.height(12.dp))
                Text(mensaje ?: "")
            }
        }

        Spacer(Modifier.height(16.dp))

        // Botonera (Cancelar / Atrás / Siguiente) — como la web
        if (paso != 4) {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                OutlinedButton(onClick = onVolver) { Text("Cancelar") }

                Row {
                    OutlinedButton(
                        onClick = { if (paso > 1) { paso -= 1; error = null } },
                        enabled = paso > 1
                    ) { Text("Atrás") }

                    Spacer(Modifier.width(10.dp))

                    Button(
                        onClick = {
                            if (validarPaso(paso)) paso += 1
                        }
                    ) { Text("Siguiente") }
                }
            }
        }
    }
}

@Composable
private fun DropdownSimple(
    label: String,
    opciones: List<String>,
    selected: String,
    menuKey: String,
    openKey: String?,
    onOpen: () -> Unit,
    onClose: () -> Unit,
    onSelect: (String) -> Unit
) {
    Column {
        Text(label)
        Box {
            OutlinedButton(
                onClick = onOpen,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(if (selected.isBlank()) "Selecciona" else selected)
            }

            DropdownMenu(
                expanded = openKey == menuKey,
                onDismissRequest = onClose,
                modifier = Modifier.fillMaxWidth()
            ) {
                opciones.forEach { op ->
                    DropdownMenuItem(
                        text = { Text(op) },
                        onClick = { onSelect(op) }
                    )
                }
            }
        }
    }
}