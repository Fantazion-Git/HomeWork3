package com.example.homework3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.homework3.databinding.ActivityMainBinding
import com.example.homework3.model.Country
import hollowsoft.country.extension.all
import hollowsoft.country.extension.image
import hollowsoft.country.Country as LibraryCountry

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private val adapter: CountryAdapter = CountryAdapter()
    private val countryList = LibraryCountry.all()
        .take(30)
        .map { Country(it.id, it.image, it.name) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.recycleView.adapter = adapter
        adapter.setCountriesList(countryList)
    }
}