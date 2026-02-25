package com.cibertec.applovepaws.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cibertec.applovepaws.data.AdoptionRequestRepository
import com.cibertec.applovepaws.data.HybridAdoptionRequestRepository
import com.cibertec.applovepaws.data.HybridPetRepository
import com.cibertec.applovepaws.data.PetRepository
import com.cibertec.applovepaws.model.AdoptionQuestion
import com.cibertec.applovepaws.model.AdoptionRequest
import com.cibertec.applovepaws.model.Pet
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class UiState(
    val pets: List<Pet> = emptyList(),
    val selectedPet: Pet? = null,
    val isLoading: Boolean = false,
    val requests: List<AdoptionRequest> = emptyList(),
    val error: String? = null
)

class LovePawsViewModel(
    private val repository: PetRepository = HybridPetRepository(),
    private val adoptionRepository: AdoptionRequestRepository = HybridAdoptionRequestRepository()
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    fun loadPets() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            runCatching { repository.getPets() }
                .onSuccess { pets -> _uiState.update { it.copy(pets = pets, isLoading = false) } }
                .onFailure { throwable -> _uiState.update { state -> state.copy(isLoading = false, error = throwable.message) } }
        }
    }

    fun selectPet(petId: String) {
        viewModelScope.launch {
            val pet = repository.getPetById(petId)
            _uiState.update { it.copy(selectedPet = pet) }
        }
    }

    fun registerAdoptionRequest(applicantName: String, answers: List<AdoptionQuestion>, onDone: () -> Unit = {}) {
        val pet = _uiState.value.selectedPet ?: return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            runCatching {
                adoptionRepository.registerRequest(
                    petId = pet.id,
                    petName = pet.name,
                    applicantName = applicantName,
                    answers = answers
                )
            }.onSuccess {
                _uiState.update { state -> state.copy(requests = state.requests + it, isLoading = false) }
                onDone()
            }.onFailure { throwable ->
                _uiState.update { state -> state.copy(isLoading = false, error = throwable.message ?: "Error registrando solicitud") }
            }
        }
    }

    fun loadMyRequests(userId: Int = 1) {
        viewModelScope.launch {
            runCatching { adoptionRepository.getRequests(userId) }
                .onSuccess { requests -> _uiState.update { state -> state.copy(requests = requests) } }
                .onFailure { throwable -> _uiState.update { state -> state.copy(error = throwable.message) } }
        }
    }

    fun defaultQuestions(): List<AdoptionQuestion> = listOf(
        AdoptionQuestion("q1", "Tengo espacio adecuado para la mascota"),
        AdoptionQuestion("q2", "Acepto controles veterinarios y vacunas"),
        AdoptionQuestion("q3", "Me comprometo a no abandonar a la mascota")
    )
}
