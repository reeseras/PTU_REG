package com.example.pturandompokemongenerator

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pturandompokemongenerator.api.PokeApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "PokeFetchr"

class PokeFetchr {

    /*@WorkerThread
    fun fetchPokemon(url: String): Bitmap? {
        val response: Response<ResponseBody> = pokeApi.fetchUrlBytes(url).execute()
        val bitmap = response.body()?.byteStream()?.use(BitmapFactory::decodeStream)
        Log.i(TAG, "Decoded bitmap=$bitmap from Response=$response")
        return bitmap
    }*/

    private val pokeApi: PokeApi

    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        pokeApi = retrofit.create(PokeApi::class.java)
    }

    fun fetchHabitatPokemon(habitat: String): LiveData<List<HabitatPokemon>> {
        val responseLiveData: MutableLiveData<List<HabitatPokemon>> = MutableLiveData()
        val pokeHabitatRequest: Call<PokeHabitatResponse> = pokeApi.getHabitatPokemon(habitat)

        pokeHabitatRequest.enqueue(object : Callback<PokeHabitatResponse> {

            override fun onFailure(call: Call<PokeHabitatResponse>, t: Throwable) {
                Log.e(TAG, "Failed to fetch pokemon from habitat", t)
            }

            override fun onResponse(
                call: Call<PokeHabitatResponse>,
                habitatResponse: Response<PokeHabitatResponse>
            ) {
                Log.d(TAG, "Response received")
                val pokeHabitatResponse: PokeHabitatResponse? = habitatResponse.body()
                responseLiveData.value = pokeHabitatResponse?.habitatPokemonSpecies
            }

        })
        return responseLiveData
    }

    fun fetchPokemonData(pokeName: String): LiveData<PokeDataResponse> {
        val responseLiveData: MutableLiveData<PokeDataResponse> = MutableLiveData()
        val pokeDataRequest: Call<PokeDataResponse> = pokeApi.getPokemonData(pokeName)

        Log.d(TAG, "pokeName: " + pokeName)

        pokeDataRequest.enqueue(object : Callback<PokeDataResponse> {

            override fun onFailure(call: Call<PokeDataResponse>, t: Throwable) {
                Log.e(TAG, "Failed to fetch pokemon data", t)
            }

            override fun onResponse(
                call: Call<PokeDataResponse>,
                dataResponse: Response<PokeDataResponse>
            ) {
                Log.d(TAG, "Response received")
                val pokeDataResponse: PokeDataResponse? = dataResponse.body()
                responseLiveData.value = pokeDataResponse
            }
        })
        return responseLiveData
    }
}