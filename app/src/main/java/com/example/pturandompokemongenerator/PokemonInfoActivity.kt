package com.example.pturandompokemongenerator

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.pokemon_info.*

private const val TAG = "PokemonInfoActivity"
private const val PTU_STAT_DIVIDER = 10 // (Game stat + 5) / this = PTU stat (+5 to round up)

class PokemonInfoActivity : AppCompatActivity() {

    private lateinit var onePokemon: PokemonData

    // UI data
    private lateinit var nameText: TextView
    private lateinit var typesText: TextView
    private lateinit var hpText: TextView
    private lateinit var atkText: TextView
    private lateinit var satkText: TextView
    private lateinit var defText: TextView
    private lateinit var sdefText: TextView
    private lateinit var spdText: TextView
    private lateinit var abilitiesText: TextView

    // stats data
    private var base_hp = 0
    private var base_atk = 0
    private var base_def = 0
    private var base_satk = 0
    private var base_sdef = 0
    private var base_spd = 0


    // variables
    var pokemonLiveData: LiveData<PokeDataResponse> = MutableLiveData()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.pokemon_info)

        nameText = findViewById(R.id.Name)
        typesText = findViewById(R.id.Types)
        hpText = findViewById(R.id.HPText)
        atkText = findViewById(R.id.Atk)
        satkText = findViewById(R.id.Satk)
        defText = findViewById(R.id.Def)
        sdefText = findViewById(R.id.Sdef)
        spdText = findViewById(R.id.Speed)
        abilitiesText = findViewById(R.id.Abilities)


        val pokemonName = intent.getStringExtra("pokemon_name")
        nameText.text = pokemonName
        pullOnePokemon(pokemonName)
    }

    // pulls a single pokemon from the API based on a given pokemon name
    fun pullOnePokemon(pokeName: String) {
        this.onePokemon = PokemonData()
        Log.d(TAG, "OnePokemon Species: " + pokeName)
        pokemonLiveData = PokeFetchr().fetchPokemonData(pokeName)

        pokemonLiveData.observe(
            this,
            Observer {pokeData ->
                Log.d(TAG, "Setting PokeData...")
                onePokemon.types = pokeData.types
                Log.d(TAG, "pokeData: " + pokeData.types[0].type.name)
                onePokemon.stats = pokeData.stats
                Log.d(TAG, "pokeData: " + pokeData.stats[0].stat.name)
                onePokemon.abilities = pokeData.abilities
                Log.d(TAG, "pokeData: " + pokeData.abilities[0].ability.name)
                onePokemon.sprites = pokeData.sprites
                Log.d(TAG, "pokeData: " + pokeData.sprites.front_default)
                setPokemonInfoText()
            })
    }

    fun setPokemonInfoText() {
        var pokemonTypes: List<PokemonTypeSlot> = onePokemon.types
        var pokemonStats: List<PokemonStatSlot> = onePokemon.stats
        var pokemonAbilities: List<PokemonAbilitySlot> = onePokemon.abilities
        var pokemonSprites: PokemonSprites = onePokemon.sprites

        // clear, then fill mainActivity with pokemon's information
        var text = ""
        for (item in pokemonTypes) {
            text = text + item.type.name + ", "
        }
        typesText.text = text.dropLast(2) // set types text, remove comma on last ability

        // base stats: hp, atk, def, satk, sdef, spd
        base_hp = (pokemonStats[0].base_stat + 5) / PTU_STAT_DIVIDER
        base_atk = (pokemonStats[1].base_stat + 5) / PTU_STAT_DIVIDER
        base_def = (pokemonStats[2].base_stat + 5) / PTU_STAT_DIVIDER
        base_satk = (pokemonStats[3].base_stat + 5) / PTU_STAT_DIVIDER
        base_sdef = (pokemonStats[4].base_stat + 5) / PTU_STAT_DIVIDER
        base_spd = (pokemonStats[5].base_stat + 5) / PTU_STAT_DIVIDER

        // set text
        hpText.text = "/ " + base_hp
        atkText.text = "Atk: " + base_atk
        defText.text = "Def: " + base_def
        satkText.text = "Satk: " + base_satk
        sdefText.text = "Sdef: " + base_sdef
        spdText.text = "Spd: " + base_spd

        // abilities
        text = "Abilities: "
        for (item in pokemonAbilities) {
            text = text + item.ability.name + ", "
        }
        abilitiesText.text = text.dropLast(2) // set ability text, remove comma on last ability

        // load sprite
        Picasso.get().load(pokemonSprites.front_default).into(sprite)
    }
}


