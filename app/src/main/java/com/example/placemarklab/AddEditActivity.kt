package com.example.placemarklab

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.placemarklab.models.PlacedMark

class AddEditActivity : AppCompatActivity() {

    private lateinit var titleInput: EditText
    private lateinit var descriptionInput: EditText
    private lateinit var xInput: EditText
    private lateinit var yInput: EditText

    private var editingId: Long? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit)

        val formTitle = findViewById<TextView>(R.id.formTitle)
        titleInput = findViewById(R.id.titleInput)
        descriptionInput = findViewById(R.id.descriptionInput)
        xInput = findViewById(R.id.xInput)
        yInput = findViewById(R.id.yInput)

        val id = intent.getLongExtra("id", -1L)
        if (id != -1L) {
            editingId = id
            formTitle.text = "Edit Mark"
            loadMark(id)
        }

        findViewById<Button>(R.id.saveButton).setOnClickListener {
            saveMark()
        }

        findViewById<Button>(R.id.cancelButton).setOnClickListener {
            finish()
        }
    }

    private fun loadMark(id: Long) {
        val mark = AppData.placedMarks.findOne(id)

        if (mark == null) {
            Toast.makeText(this, "Mark not found", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        titleInput.setText(mark.title)
        descriptionInput.setText(mark.desc)
        xInput.setText(mark.x.toString())
        yInput.setText(mark.y.toString())
    }

    private fun saveMark() {
        val title = titleInput.text.toString().trim()
        val description = descriptionInput.text.toString().trim()
        val x = xInput.text.toString().toDoubleOrNull()
        val y = yInput.text.toString().toDoubleOrNull()

        if (title.isEmpty()) {
            titleInput.error = "Enter a title"
            return
        }

        if (x == null) {
            xInput.error = "Enter a valid number"
            return
        }

        if (y == null) {
            yInput.error = "Enter a valid number"
            return
        }

        val id = editingId

        if (id == null) {
            val mark = PlacedMark(
                title = title,
                desc = description,
                x = x,
                y = y
            )

            AppData.placedMarks.create(mark)
            Toast.makeText(this, "Mark added", Toast.LENGTH_SHORT).show()
        } else {
            val mark = PlacedMark(
                id = id,
                title = title,
                desc = description,
                x = x,
                y = y
            )

            AppData.placedMarks.update(mark)
            Toast.makeText(this, "Mark updated", Toast.LENGTH_SHORT).show()
        }

        finish()
    }
}