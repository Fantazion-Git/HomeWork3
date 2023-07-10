package com.example.homework3

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.homework3.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {
    private val viewModel: MainActivityViewModel by lazy {
        ViewModelProvider(this)[MainActivityViewModel::class.java]
    }

    private lateinit var binding: ActivityMainBinding

    private val adapter by lazy { ContactAdapter(viewModel) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()
        observeData()
    }

    private fun initViews() {
        binding.recyclerView.adapter = adapter
        binding.addButton.setOnClickListener {
            viewModel.addContact()
            binding.recyclerView.scrollToPosition(adapter.itemCount - 1)
        }
        binding.deleteButton.setOnClickListener { viewModel.deleteSelected() }
    }

    private fun observeData() {
        viewModel.list.observe(this) { adapter.items = it }
    }
}