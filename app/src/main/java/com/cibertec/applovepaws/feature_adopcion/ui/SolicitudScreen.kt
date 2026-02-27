package com.cibertec.applovepaws.feature_adopcion.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.cibertec.applovepaws.feature_adopcion.SolicitudViewModel
import com.cibertec.applovepaws.feature_adopcion.data.dto.MascotaRefDto
import com.cibertec.applovepaws.feature_adopcion.data.dto.SolicitudAdopcionDto

@Composable
fun SolicitudScreen(
    viewModel: SolicitudViewModel,
    mascotaId: Int,
    mascotaNombre: String,

    // (por ahora llega desde MainActivity; después lo jalas del usuario logueado real)
    solicitanteNombre: String,
    usuario: String = "",
    celular: String,
    correo: String,

    onVolver: () -> Unit,
    onIrHome: () -> Unit
) {
    val loading by viewModel.isLoading.collectAsState()
    val mensajeText = (viewModel.mensaje.collectAsState().value ?: "").trim()

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

    var paso by remember { mutableStateOf(1) } // 1..4
    var error by remember { mutableStateOf<String?>(null) }

    var p1Horas by remember { mutableStateOf("") }
    var p2Motivacion by remember { mutableStateOf("") }
    var p3Experiencia by remember { mutableStateOf("") }
    var p4Costos by remember { mutableStateOf("") }
    var p5Vivienda by remember { mutableStateOf("") }
    var p6NinosMascotas by remember { mutableStateOf("") }
    var p7Segura by remember { mutableStateOf("") }
    var p8Responsable by remember { mutableStateOf("") }
    var p9Paseos by remember { mutableStateOf("") }
    var p10FueraCasa by remember { mutableStateOf("") }
    var p11UnAnio by remember { mutableStateOf("") }
    var p12DiezAnios by remember { mutableStateOf("") }
    var p13Plan by remember { mutableStateOf("") }
    var p14Semanas by remember { mutableStateOf("") }
    var p15Meses by remember { mutableStateOf("") }
    var p16Info by remember { mutableStateOf("") }

    var openMenuKey by remember { mutableStateOf<String?>(null) }

    fun validarPaso(actual: Int): Boolean {
        error = null
        return when (actual) {
            1 -> when {
                p1Horas.isBlank() -> { error = "Selecciona cuántas horas dedicarás (P1)."; false }
                p2Motivacion.isBlank() -> { error = "Selecciona tu motivación (P2)."; false }
                p3Experiencia.isBlank() -> { error = "Selecciona experiencia previa (P3)."; false }
                p4Costos.isBlank() -> { error = "Selecciona si puedes cubrir costos (P4)."; false }
                else -> true
            }
            2 -> when {
                p5Vivienda.isBlank() -> { error = "Selecciona tipo de vivienda (P5)."; false }
                p6NinosMascotas.isBlank() -> { error = "Selecciona niños/mascotas (P6)."; false }
                p7Segura.isBlank() -> { error = "Responde si tu vivienda es segura (P7)."; false }
                p8Responsable.isBlank() -> { error = "Selecciona responsable principal (P8)."; false }
                p9Paseos.isBlank() -> { error = "Selecciona frecuencia de paseos (P9)."; false }
                p10FueraCasa.isBlank() -> { error = "Selecciona dónde permanecerá fuera de casa (P10)."; false }
                else -> true
            }
            3 -> when {
                p11UnAnio.isBlank() -> { error = "Responde si mantendrás el cuidado 1 año (P11)."; false }
                p12DiezAnios.isBlank() -> { error = "Responde el horizonte 5-10 años (P12)."; false }
                p13Plan.isBlank() -> { error = "Selecciona qué harías ante problemas (P13)."; false }
                p14Semanas.isBlank() -> { error = "Selecciona quién cuidará si viajas semanas (P14)."; false }
                p15Meses.isBlank() -> { error = "Selecciona qué harías si viajas meses (P15)."; false }
                else -> true
            }
            else -> true
        }
    }

    fun construirInfoAdicionalFinal(): String? {
        val extras = buildString {
            if (usuario.isNotBlank()) appendLine("Usuario: $usuario")
            if (celular.isNotBlank()) appendLine("Celular: $celular")
            if (correo.isNotBlank()) appendLine("Correo: $correo")

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

        return combinado.takeIf { it.isNotBlank() }
    }

    val enviadoOk =
        mensajeText.isNotBlank() && (
                mensajeText.contains("enviad", ignoreCase = true) ||
                        mensajeText.contains("correct", ignoreCase = true)
                )

    val solicitudActiva =
        mensajeText.isNotBlank() && (
                mensajeText.contains("solicitud activa", ignoreCase = true) ||
                        (mensajeText.contains("ya tiene", ignoreCase = true) && mensajeText.contains("solicitud", ignoreCase = true))
                )

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

        when (paso) {
            1 -> {
                if (usuario.isNotBlank()) {
                    ReadOnlyField(label = "Usuario", value = usuario)
                    Spacer(Modifier.height(8.dp))
                }
                ReadOnlyField(label = "Celular", value = celular)
                Spacer(Modifier.height(8.dp))
                ReadOnlyField(label = "Correo electrónico", value = correo)

                Spacer(Modifier.height(12.dp))

                Text("1. ¿Cuántas horas al día podrá dedicar a la mascota?")
                optHoras.forEach { op ->
                    Row(verticalAlignment = Alignment.CenterVertically) {
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
                    openKey = openMenuKey,
                    onOpen = { openMenuKey = "p2" },
                    onClose = { openMenuKey = null },
                    onSelect = { p2Motivacion = it; openMenuKey = null }
                )

                Spacer(Modifier.height(10.dp))

                DropdownSimple(
                    label = "3. ¿Ha tenido mascotas anteriormente?",
                    opciones = optExperiencia,
                    selected = p3Experiencia,
                    menuKey = "p3",
                    openKey = openMenuKey,
                    onOpen = { openMenuKey = "p3" },
                    onClose = { openMenuKey = null },
                    onSelect = { p3Experiencia = it; openMenuKey = null }
                )

                Spacer(Modifier.height(10.dp))

                DropdownSimple(
                    label = "4. ¿Puede cubrir alimentación, salud y cuidados básicos?",
                    opciones = optCostos,
                    selected = p4Costos,
                    menuKey = "p4",
                    openKey = openMenuKey,
                    onOpen = { openMenuKey = "p4" },
                    onClose = { openMenuKey = null },
                    onSelect = { p4Costos = it; openMenuKey = null }
                )
            }

            2 -> {
                DropdownSimple(
                    label = "5. Tipo de vivienda",
                    opciones = optVivienda,
                    selected = p5Vivienda,
                    menuKey = "p5",
                    openKey = openMenuKey,
                    onOpen = { openMenuKey = "p5" },
                    onClose = { openMenuKey = null },
                    onSelect = { p5Vivienda = it; openMenuKey = null }
                )

                Spacer(Modifier.height(10.dp))

                DropdownSimple(
                    label = "6. ¿Hay niños u otras mascotas en casa?",
                    opciones = optNinosMascotas,
                    selected = p6NinosMascotas,
                    menuKey = "p6",
                    openKey = openMenuKey,
                    onOpen = { openMenuKey = "p6" },
                    onClose = { openMenuKey = null },
                    onSelect = { p6NinosMascotas = it; openMenuKey = null }
                )

                Spacer(Modifier.height(10.dp))

                Text("7. ¿Tu vivienda tiene zonas seguras para evitar escapes?")
                Row(verticalAlignment = Alignment.CenterVertically) {
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
                    openKey = openMenuKey,
                    onOpen = { openMenuKey = "p8" },
                    onClose = { openMenuKey = null },
                    onSelect = { p8Responsable = it; openMenuKey = null }
                )

                Spacer(Modifier.height(10.dp))

                DropdownSimple(
                    label = "9. ¿Con qué frecuencia paseará a la mascota?",
                    opciones = optPaseos,
                    selected = p9Paseos,
                    menuKey = "p9",
                    openKey = openMenuKey,
                    onOpen = { openMenuKey = "p9" },
                    onClose = { openMenuKey = null },
                    onSelect = { p9Paseos = it; openMenuKey = null }
                )

                Spacer(Modifier.height(10.dp))

                DropdownSimple(
                    label = "10. ¿Dónde permanecerá la mascota cuando salga de casa?",
                    opciones = optFueraCasa,
                    selected = p10FueraCasa,
                    menuKey = "p10",
                    openKey = openMenuKey,
                    onOpen = { openMenuKey = "p10" },
                    onClose = { openMenuKey = null },
                    onSelect = { p10FueraCasa = it; openMenuKey = null }
                )
            }

            3 -> {
                Text("11. ¿Podrá mantener el cuidado por al menos 1 año?")
                Row(verticalAlignment = Alignment.CenterVertically) {
                    RadioButton(selected = p11UnAnio == "Sí", onClick = { p11UnAnio = "Sí" })
                    Text("Sí")
                    Spacer(Modifier.width(14.dp))
                    RadioButton(selected = p11UnAnio == "No", onClick = { p11UnAnio = "No" })
                    Text("No")
                }

                Spacer(Modifier.height(10.dp))

                Text("12. ¿Y de aquí a 5 o 10 años?")
                Row(verticalAlignment = Alignment.CenterVertically) {
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
                    openKey = openMenuKey,
                    onOpen = { openMenuKey = "p13" },
                    onClose = { openMenuKey = null },
                    onSelect = { p13Plan = it; openMenuKey = null }
                )

                Spacer(Modifier.height(10.dp))

                DropdownSimple(
                    label = "14. Si viaja por semanas, ¿quién cuidará la mascota?",
                    opciones = optViajeSemanas,
                    selected = p14Semanas,
                    menuKey = "p14",
                    openKey = openMenuKey,
                    onOpen = { openMenuKey = "p14" },
                    onClose = { openMenuKey = null },
                    onSelect = { p14Semanas = it; openMenuKey = null }
                )

                Spacer(Modifier.height(10.dp))

                DropdownSimple(
                    label = "15. Si viaja por meses, ¿qué hará?",
                    opciones = optViajeMeses,
                    selected = p15Meses,
                    menuKey = "p15",
                    openKey = openMenuKey,
                    onOpen = { openMenuKey = "p15" },
                    onClose = { openMenuKey = null },
                    onSelect = { p15Meses = it; openMenuKey = null }
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

            4 -> {
                Text("Confirmación", style = MaterialTheme.typography.titleMedium)
                Spacer(Modifier.height(10.dp))

                Text("Mascota: $mascotaNombre")
                Spacer(Modifier.height(10.dp))

                if (mensajeText.isNotBlank()) {
                    Text(mensajeText, color = if (enviadoOk) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error)
                    Spacer(Modifier.height(12.dp))
                }

                // si ya hay solicitud activa, no dejamos reenviar y fuimonos a al catálogo
                val tituloBtn = when {
                    loading -> "Enviando..."
                    enviadoOk -> "Volver al catálogo"
                    solicitudActiva -> "Volver al catálogo"
                    else -> "Finalizar"
                }

                Button(
                    onClick = {
                        when {
                            enviadoOk || solicitudActiva -> onIrHome()
                            else -> {
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
                                viewModel.enviarSolicitudCompleta(dto)
                            }
                        }
                    },
                    enabled = !loading,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(tituloBtn)
                }

                Spacer(Modifier.height(10.dp))

                OutlinedButton(
                    onClick = onIrHome,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cancelar y volver al catálogo")
                }
            }
        }

        Spacer(Modifier.height(16.dp))

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
                        onClick = { if (validarPaso(paso)) paso += 1 }
                    ) { Text("Siguiente") }
                }
            }
        }
    }
}

@Composable
private fun ReadOnlyField(label: String, value: String) {
    OutlinedTextField(
        value = value,
        onValueChange = {},
        label = { Text(label) },
        modifier = Modifier.fillMaxWidth(),
        enabled = false,
        readOnly = true,
        colors = OutlinedTextFieldDefaults.colors(
            disabledTextColor = MaterialTheme.colorScheme.onSurface,
            disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
            disabledBorderColor = MaterialTheme.colorScheme.outline,
            disabledContainerColor = MaterialTheme.colorScheme.surface
        )
    )
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