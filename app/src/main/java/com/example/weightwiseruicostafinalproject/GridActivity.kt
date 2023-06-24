package com.example.weightwiseruicostafinalproject

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import WeightDatabaseHelper
import android.content.Intent
import android.graphics.PorterDuff
import android.view.Menu
import android.view.MenuItem
import androidx.core.content.ContextCompat

/**
 * Activity for displaying and editing a weight item.
 */
class GridActivity : AppCompatActivity() {

    private lateinit var editTextId: EditText
    private lateinit var editTextWeight: EditText
    private lateinit var editTextDate: EditText
    private lateinit var saveButton: Button

    private lateinit var weightDatabaseHelper: WeightDatabaseHelper

    /**
     * Creates the options menu.
     */
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_grid_menu, menu)
        val deleteItem = menu.findItem(R.id.action_delete)
        deleteItem.icon?.setColorFilter(ContextCompat.getColor(this, android.R.color.white), PorterDuff.Mode.SRC_IN)
        return true
    }

    /**
     * Handles the selection of an item in the options menu.
     */
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_delete -> {
                // Handle delete button click here
                deleteItem()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Deletes the weight item.
     */
    private fun deleteItem() {
        val itemId = editTextId.text.toString()

        val weightDatabaseHelper = WeightDatabaseHelper(this)
        val isDeleted = weightDatabaseHelper.deleteWeightById(itemId)

        if (isDeleted) {
            Toast.makeText(this, "Item Deleted", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        } else {
            Toast.makeText(this, "Item FAILED to delete!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * Creates the activity and sets up the views.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.grid_item_info)

        weightDatabaseHelper = WeightDatabaseHelper(this)

        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.apply {
            title = ""
            setDisplayHomeAsUpEnabled(true)
        }
        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        editTextId = findViewById(R.id.editTextId)
        editTextWeight = findViewById(R.id.editTextWeight)
        editTextDate = findViewById(R.id.editTextDate)
        saveButton = findViewById(R.id.saveButton)

        val id = intent.getStringExtra("id")
        val weight = intent.getStringExtra("weight")
        val date = intent.getStringExtra("date")

        editTextId.setText(id)
        editTextWeight.setText(weight)
        editTextDate.setText(date)

        saveButton.setOnClickListener {
            saveChanges()
        }
    }

    /**
     * Saves the changes made to the weight item.
     */
    private fun saveChanges() {
        val id = editTextId.text.toString()
        val newWeight = editTextWeight.text.toString()
        val newDate = editTextDate.text.toString()

        val weightPairs = weightDatabaseHelper.getWeights()
        if (weightPairs.isNotEmpty()) {

            val isUpdated = weightDatabaseHelper.updateWeight(id, newWeight, newDate)
            if (isUpdated) {
                Toast.makeText(this, "Changes saved", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            } else {
                Toast.makeText(this, "Failed to save changes", Toast.LENGTH_SHORT).show()
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }
    }

}
