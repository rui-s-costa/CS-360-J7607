package com.example.weightwiseruicostafinalproject

import WeightDatabaseHelper
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import android.content.Intent
import androidx.appcompat.widget.Toolbar
import com.google.android.material.button.MaterialButton

/**
 * Activity for adding a weight entry to the database.
 */
class GridAddActivity : AppCompatActivity() {
    private lateinit var dbHelper: WeightDatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_grid_add)

        dbHelper = WeightDatabaseHelper(this)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = ""
            setDisplayHomeAsUpEnabled(true)
        }
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        val editTextWeight: EditText = findViewById(R.id.editTextWeight)
        val editTextDate: EditText = findViewById(R.id.editTextDate)
        val editTextId: EditText = findViewById(R.id.editTextId)
        val saveButton: MaterialButton = findViewById(R.id.saveButton)

        saveButton.setOnClickListener {
            val weight = editTextWeight.text.toString().trim()
            val date = editTextDate.text.toString().trim()
            val id = editTextId.text.toString().trim()

            if (weight.isNotEmpty() && date.isNotEmpty() && id.isNotEmpty()) {
                val success = dbHelper.addWeight(weight, date)

                if (success) {
                    showToast("Weight added successfully")
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish() // Close the activity after saving the weight
                } else {
                    showToast("Failed to add weight")
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
            } else {
                showToast("Please enter all fields")
            }
        }

        supportActionBar?.setDisplayHomeAsUpEnabled(true) // Enable the back button
    }

    /**
     * Handles the click event of the back button in the action bar.
     */
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed() // Navigate back to the previous activity
        return true
    }

    /**
     * Displays a toast message.
     */
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
