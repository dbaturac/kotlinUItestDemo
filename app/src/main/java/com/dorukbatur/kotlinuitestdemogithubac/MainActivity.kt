package com.dorukbatur.kotlinuitestdemogithubac


import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: ItemAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchEditText: EditText
    private lateinit var refreshButton: Button

    // Statik örnek veri listesi
    private val originalList = listOf(
        Item(1, "Apple", "A red apple."),
        Item(2, "Banana", "A ripe banana."),
        Item(3, "Cherry", "A small cherry."),
        Item(4, "Date", "Sweet dates."),
        Item(5, "Elderberry", "A dark elderberry.")
    )

    private var displayedList = originalList

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)




        recyclerView = findViewById(R.id.recyclerView)
        searchEditText = findViewById(R.id.searchEditText)
        refreshButton = findViewById(R.id.refreshButton)

        adapter = ItemAdapter(displayedList) { item ->
            // Öğeye tıklanınca DetailActivity'yi başlat
            val intent = Intent(this, DetailActivity::class.java).apply {
                putExtra("item_title", item.title)
                putExtra("item_description", item.description)
            }
            startActivity(intent)
        }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        // Arama (filtre) işlemi
        searchEditText.addTextChangedListener(object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                filterList(s.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        // Refresh butonu: listeyi orijinal haline döndürür ve aramayı temizler
        refreshButton.setOnClickListener {
            displayedList = originalList
            adapter.updateList(displayedList)
            searchEditText.text.clear()
        }
    }

    private fun filterList(query: String) {
        displayedList = originalList.filter { it.title.contains(query, ignoreCase = true) }
        adapter.updateList(displayedList)
    }
}