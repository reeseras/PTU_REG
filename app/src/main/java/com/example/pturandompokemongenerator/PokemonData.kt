package com.example.pturandompokemongenerator

data class PokemonData (
    var types: List<PokemonTypeSlot> = listOf(),
    var stats: List<PokemonStatSlot> = listOf(),
    var abilities: List<PokemonAbilitySlot> = listOf(),
    var sprites: PokemonSprites = PokemonSprites("", "")
)

// Ability
data class PokemonAbilitySlot (
    var ability: PokemonAbility
)
data class PokemonAbility (
    var name: String
)

// Stats
data class PokemonStatSlot (
    var base_stat: Int,
    var stat: PokemonStat
)
data class PokemonStat (
    var name: String
    // var url: String
)

// Type(s)
data class PokemonTypeSlot (
    var type: PokemonType
)
data class PokemonType (
    var name: String,
    var url: String
)

// Sprites
data class PokemonSprites (
    var front_default: String,
    var front_shy: String
)