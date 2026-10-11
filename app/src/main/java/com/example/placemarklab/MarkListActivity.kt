package com.example.placemarklab

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.placemarklab.models.PlacedMark

class MarkListActivity : AppCompatActivity() {

    private lateinit var adapter: PlacedMarkAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_mark_list)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.setPadding(
                systemBars.left,
                systemBars.top,
                systemBars.right,
                systemBars.bottom
            )
            insets
        }

        val recyclerView = findViewById<RecyclerView>(R.id.marksRecyclerView)

        adapter = PlacedMarkAdapter(
            AppData.placedMarks.findAll(),
            onEdit = { mark -> editMark(mark) },
            onDelete = { mark -> deleteMark(mark) }
        )

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        findViewById<Button>(R.id.returnButton).setOnClickListener {
            finish()
        }
    }

    override fun onResume() {
        super.onResume()
        adapter.updateMarks(AppData.placedMarks.findAll())
    }

    private fun editMark(mark: PlacedMark) {
        val intent = Intent(this, AddEditActivity::class.java)
        intent.putExtra("id", mark.id)
        startActivity(intent)
    }

    private fun deleteMark(mark: PlacedMark) {
        AppData.placedMarks.delete(mark.id)
        adapter.updateMarks(AppData.placedMarks.findAll())
    }
}