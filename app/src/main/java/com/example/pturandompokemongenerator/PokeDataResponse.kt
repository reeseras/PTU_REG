package com.example.pturandompokemongenerator

import com.google.gson.annotations.SerializedName

class PokeDataResponse {
    // @SerializedName("location_area_encounters") // for differing variable names
    // lateinit var location: String

    lateinit var types: List<PokemonTypeSlot>
    lateinit var stats: List<PokemonStatSlot>
    lateinit var abilities: List<PokemonAbilitySlot>
    lateinit var sprites: PokemonSprites

}