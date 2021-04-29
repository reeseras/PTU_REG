package com.example.pturandompokemongenerator.api

import com.example.pturandompokemongenerator.PokeDataResponse
import com.example.pturandompokemongenerator.PokeHabitatResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface PokeApi {

    @GET("pokemon-habitat/{habitat}")
    fun  getHabitatPokemon(@Path("habitat") habitat: String): Call<PokeHabitatResponse>

    @GET("pokemon/{name}")
    fun getPokemonData(@Path("name") name: String): Call<PokeDataResponse>
}