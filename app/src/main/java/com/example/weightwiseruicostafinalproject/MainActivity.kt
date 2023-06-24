package com.example.weightwiseruicostafinalproject

import WeightDatabaseHelper
import WeightListAdapter
import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * The main activity of the Weight Wise app.
 */
class MainActivity : AppCompatActivity(), WeightListAdapter.OnItemClickListener {

    private lateinit var dbHelper: WeightDatabaseHelper
    private lateinit var recyclerView: RecyclerView
    private lateinit var emptyView: View

    override fun onResume() {
        super.onResume()

        // Retrieve weights from the database
        val weights = dbHelper.getWeights()

        if (weights.isNotEmpty()) {
            showRecyclerView()
            setupRecyclerView(weights)
            addDividerToRecyclerView() // Add this line to add the divider
        } else {
            showEmptyView()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Request SMS permission
        requestSmsPermission()

        // Set up the toolbar
        val toolbar: Toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.title = "Weight Wise by Rui Costa"
        val titleTextColor = ContextCompat.getColor(this, R.color.white)
        toolbar.overflowIcon?.setTint(ContextCompat.getColor(this, android.R.color.white))

        toolbar.setTitleTextAppearance(this, R.style.ToolbarTitleText)
        for (i in 0 until toolbar.childCount) {
            val view = toolbar.getChildAt(i)
            if (view is TextView && view.text == toolbar.title) {
                view.setTextColor(titleTextColor)
                break
            }
        }

        // Initialize the database helper and views
        dbHelper = WeightDatabaseHelper(this)
        recyclerView = findViewById(R.id.recyclerView)
        emptyView = findViewById(R.id.emptyView)

        // Retrieve weights from the database
        val weights = dbHelper.getWeights()

        if (weights.isNotEmpty()) {
            showRecyclerView()
            setupRecyclerView(weights)
            addDividerToRecyclerView() // Add this line to add the divider
        } else {
            showEmptyView()
        }

        // Set up the "Add Weight" button click listener
        val addWeightButton: Button = findViewById(R.id.addWeightButton)
        addWeightButton.setOnClickListener {
            val intent = Intent(this, GridAddActivity::class.java)
            startActivity(intent)
        }
    }

    private val SMS_PERMISSION_REQUEST_CODE = 123

    /**
     * Requests the SMS permission if it is not already granted.
     */
    private fun requestSmsPermission() {
        val permission = Manifest.permission.SEND_SMS
        val granted = PackageManager.PERMISSION_GRANTED

        if (ContextCompat.checkSelfPermission(this, permission) != granted) {
            ActivityCompat.requestPermissions(this, arrayOf(permission), SMS_PERMISSION_REQUEST_CODE)
        } else {
            // Perform SMS-related operations
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == SMS_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Perform SMS-related operations
            } else {
                showToast("SMS permission denied.")
            }
        }
    }

    /**
     * Adds a divider to the RecyclerView.
     */
    private fun addDividerToRecyclerView() {
        val dividerDrawable = ContextCompat.getDrawable(this, R.drawable.divider)
        val dividerItemDecoration = DividerItemDecoration(this, DividerItemDecoration.VERTICAL)
        if (dividerDrawable != null) {
            dividerItemDecoration.setDrawable(dividerDrawable)
        }
        recyclerView.addItemDecoration(dividerItemDecoration)
    }

    /**
     * Shows the RecyclerView and hides the empty view.
     */
    private fun showRecyclerView() {
        recyclerView.visibility = View.VISIBLE
        emptyView.visibility = View.GONE
    }

    /**
     * Sets up the RecyclerView with the weights.
     */
    private fun setupRecyclerView(weights: Array<Triple<String, String, String>>) {
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = WeightListAdapter(weights, this) // Pass the OnItemClickListener
        recyclerView.adapter = adapter
    }

    /**
     * Shows the empty view and hides the RecyclerView.
     */
    private fun showEmptyView() {
        recyclerView.visibility = View.GONE
        emptyView.visibility = View.VISIBLE
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.sign_out -> {
                val sharedPref = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
                val editor = sharedPref.edit()
                editor.remove("username")
                editor.apply()
                val intent = Intent(this, SignInActivity::class.java)
                startActivity(intent)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    /**
     * Handles the click event on a weight item in the RecyclerView.
     */
    override fun onItemClick(weight: Triple<String, String, String>) {
        val intent = Intent(this, GridActivity::class.java)
        intent.putExtra("id", weight.first)
        intent.putExtra("weight", weight.second)
        intent.putExtra("date", weight.third)
        startActivity(intent)
    }

    /**
     * Displays a toast message.
     */
    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}
