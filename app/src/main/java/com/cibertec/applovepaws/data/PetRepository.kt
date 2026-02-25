package com.cibertec.applovepaws.data

import com.cibertec.applovepaws.BuildConfig
import com.cibertec.applovepaws.core.network.RetrofitClient
import com.cibertec.applovepaws.feature_mascota.data.dto.MascotaDto
import com.cibertec.applovepaws.model.Pet
import kotlinx.coroutines.delay

interface PetRepository {
    suspend fun getPets(): List<Pet>
    suspend fun getPetById(id: String): Pet?
}

class RemotePetRepository : PetRepository {
    override suspend fun getPets(): List<Pet> {
        return RetrofitClient.mascotaApi.listarMascotas().map { it.toPet() }
    }

    override suspend fun getPetById(id: String): Pet? {
        return getPets().firstOrNull { it.id == id }
    }
}

/**
 * Repositorio de prueba para seguir desarrollando mientras se integra la API real en Render.
 */
class FakePetRepository : PetRepository {

    private val pets = listOf(
        Pet(
            id = "1",
            name = "Luna",
            age = "2 años",
            breed = "Mestiza",
            imageUrl = "https://images.unsplash.com/photo-1517849845537-4d257902454a",
            description = "Luna es juguetona, cariñosa y le encanta pasear por el parque."
        ),
        Pet(
            id = "2",
            name = "Max",
            age = "1 año",
            breed = "Labrador",
            imageUrl = "https://images.unsplash.com/photo-1583512603805-3cc6b41f3edb",
            description = "Max es sociable y perfecto para familias con niños."
        ),
        Pet(
            id = "3",
            name = "Misha",
            age = "3 años",
            breed = "Siamesa",
            imageUrl = "https://images.unsplash.com/photo-1519052537078-e6302a4968d4",
            description = "Misha es tranquila, limpia y muy dulce con las personas."
        )
    )

    override suspend fun getPets(): List<Pet> {
        delay(350)
        return pets
    }

    override suspend fun getPetById(id: String): Pet? {
        delay(200)
        return pets.firstOrNull { it.id == id }
    }
}

class HybridPetRepository(
    private val remote: PetRepository = RemotePetRepository(),
    private val fake: PetRepository = FakePetRepository()
) : PetRepository {

    override suspend fun getPets(): List<Pet> {
        return runCatching { remote.getPets() }
            .getOrElse { if (BuildConfig.USE_FAKE_FALLBACK) fake.getPets() else emptyList() }
    }

    override suspend fun getPetById(id: String): Pet? {
        return runCatching { remote.getPetById(id) }
            .getOrElse { if (BuildConfig.USE_FAKE_FALLBACK) fake.getPetById(id) else null }
    }
}

private fun MascotaDto.toPet(): Pet {
    return Pet(
        id = id.toString(),
        name = nombre,
        age = "$edad años",
        breed = razaNombre ?: "Sin raza",
        imageUrl = fotoUrl.orEmpty(),
        description = descripcion ?: "Sin descripción"
    )
}
