package com.cibertec.applovepaws.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cibertec.applovepaws.data.FakePetRepository
import com.cibertec.applovepaws.data.PetRepository
import com.cibertec.applovepaws.model.AdoptionQuestion
import com.cibertec.applovepaws.model.AdoptionRequest
import com.cibertec.applovepaws.model.Pet
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

data class UiState(
    val pets: List<Pet> = emptyList(),
    val selectedPet: Pet? = null,
    val isLoading: Boolean = false,
    val requests: List<AdoptionRequest> = emptyList()
)

class LovePawsViewModel(
    private val repository: PetRepository = FakePetRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun loadPets() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val pets = repository.getPets()
            _uiState.update { it.copy(pets = pets, isLoading = false) }
        }
    }

    fun selectPet(petId: String) {
        viewModelScope.launch {
            val pet = repository.getPetById(petId)
            _uiState.update { it.copy(selectedPet = pet) }
        }
    }

    fun registerAdoptionRequest(applicantName: String, answers: List<AdoptionQuestion>) {
        val pet = _uiState.value.selectedPet ?: return
        val request = AdoptionRequest(
            id = UUID.randomUUID().toString(),
            petId = pet.id,
            petName = pet.name,
            applicantName = applicantName,
            answers = answers
        )
        _uiState.update { it.copy(requests = it.requests + request) }
    }

    fun defaultQuestions(): List<AdoptionQuestion> = listOf(
        AdoptionQuestion("q1", "Tengo espacio adecuado para la mascota"),
        AdoptionQuestion("q2", "Acepto controles veterinarios y vacunas"),
        AdoptionQuestion("q3", "Me comprometo a no abandonar a la mascota")
    )
}
