package com.example.pturandompokemongenerator

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlin.random.Random

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var habitatRandbutton: Button
    private lateinit var habitatSpinner: Spinner
    private lateinit var pokemonFromHabitat: List<HabitatPokemon>
    private lateinit var speciesNameText: TextView


    var habitatPokemonLiveData: LiveData<List<HabitatPokemon>> = MutableLiveData()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Creating and populating the habitat spinner:
        // EXTRA: would be nice if the database populated the habitats_array by itself
        habitatSpinner = findViewById(R.id.habitatSpinner)
        ArrayAdapter.createFromResource(
            this,
            R.array.habitats_array,
            android.R.layout.simple_spinner_item
        ).also {adapter ->
            // Layout used when list of choices appears:
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            habitatSpinner.adapter = adapter
        }

        // change array of pokemon when habitat is changed
        habitatSpinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>,
                                        view: View, position: Int, id: Long) {
                pullAllByHabitat()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        //pullAllByHabitat()

        // Buttons
        habitatRandbutton = findViewById(R.id.buttonHabitatRandomize)
        habitatRandbutton.setOnClickListener {
            byHabitatRoll()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Log.i(TAG, "On RandAll_Window result")

        Log.d(TAG, "requestCode: " + requestCode)
        Log.d(TAG, "resultCode: " + resultCode)
        Log.d(TAG, "data: " + data)
    }

    // rolls a single random species of pokemon and sets its data to the textViews
    // called by randomize button
    fun byHabitatRoll() {
        var pokeIndex = Random.nextInt(0, pokemonFromHabitat.size - 1)
        var oneHabitatPokemon = pokemonFromHabitat[pokeIndex]
        // speciesNameText.text = oneHabitatPokemon.name

        val intent = Intent(this, PokemonInfoActivity::class.java).apply {
            putExtra("pokemon_name", oneHabitatPokemon.name)
        }
        startActivity(intent)


        //pullOnePokemon(oneHabitatPokemon.name)
    }

    // pulls all pokemon species from a habitat indicated by the habitat spinner, sets them to "pokemonFromHabitat" list
    fun pullAllByHabitat() {
        habitatRandbutton.isEnabled = false // disable button while loading pokemon (pressing the random button before it's done loading will load the previous habitat)

        // pulling from PokeAPI:
        habitatPokemonLiveData = PokeFetchr().fetchHabitatPokemon(habitatSpinner.selectedItem.toString())

        habitatPokemonLiveData.observe(  // pg. 495
            this,
            Observer { pokeItems -> // pg. 495
                Log.d(TAG, pokeItems.toString())
                pokemonFromHabitat = pokeItems
                habitatRandbutton.isEnabled = true;
            })
    }

    // pulls a single pokemon based on the name in speciesNameText
    /*
    fun pullOnePokemon(pokeName: String) {
        this.onePokemon = PokemonData()
        Log.d(TAG, "OnePokemon Species: " + pokeName)
        pokemonLiveData = PokeFetchr().fetchPokemonData(pokeName)

        pokemonLiveData.observe(
            this,
            Observer {pokeData ->
                Log.d(TAG, "Setting PokeData...")
                onePokemon.types = pokeData.types // crashes here why?
                Log.d(TAG, "pokeData: " + pokeData.types[0].type.name)
                onePokemon.stats = pokeData.stats
                Log.d(TAG, "pokeData: " + pokeData.stats[0].stat.name)
                onePokemon.abilities = pokeData.abilities
                Log.d(TAG, "pokeData: " + pokeData.abilities[0].ability.name)
                onePokemon.sprites = pokeData.sprites
                Log.d(TAG, "pokeData: " + pokeData.sprites.front_default)
                //setPokemonTextData()
            })
    }
     */

    /*
    fun setPokemonTextData() {
        var pokemonTypes: List<PokemonTypeSlot> = onePokemon.types
        var pokemonStats: List<PokemonStatSlot> = onePokemon.stats
        var pokemonAbilities: List<PokemonAbilitySlot> = onePokemon.abilities
        var pokemonSprites: PokemonSprites = onePokemon.sprites

        // clear, then fill mainActivity with pokemon's information
        var text = "Types: "
        for (item in pokemonTypes) {
            text = text + item.type.name + " "
        }
        typesText.text = text

        // base stats (base_stat still needs to be configured to PTU)
        text = "Stats: \n"
        for (item in pokemonStats) {
            text = text + item.stat.name + ": " + item.base_stat + " \n"
        }
        statsText.text = text

        // abilities
        text = "Abilities: "
        for (item in pokemonAbilities) {
            text = text + item.ability.name + " "
        }
        abilitiesText.text = text

        spriteUrlText.text = pokemonSprites.front_default;

        Picasso.get().load(pokemonSprites.front_default).into(image_pokemonSprite)
    }
    */

}

