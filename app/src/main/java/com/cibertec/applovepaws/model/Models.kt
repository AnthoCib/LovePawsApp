package com.cibertec.applovepaws.model

data class Pet(
    val id: String,
    val name: String,
    val age: String,
    val breed: String,
    val imageUrl: String,
    val description: String
)

data class AdoptionQuestion(
    val id: String,
    val text: String,
    val checked: Boolean = false
)

data class AdoptionRequest(
    val id: String,
    val petId: String,
    val petName: String,
    val applicantName: String,
    val answers: List<AdoptionQuestion>,
    val status: String = "Registrada"
)
