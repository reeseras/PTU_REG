package com.example.pturandompokemongenerator

import com.google.gson.annotations.SerializedName

class PokeHabitatResponse {
    @SerializedName("pokemon_species") // for differing variable names
    lateinit var habitatPokemonSpecies: List<HabitatPokemon>
}