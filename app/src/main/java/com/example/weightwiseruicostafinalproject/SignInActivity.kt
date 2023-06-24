package com.example.weightwiseruicostafinalproject

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.weightwiseruicostafinalproject.database.DatabaseHelper
import com.example.weightwiseruicostafinalproject.databinding.ActivitySigninBinding

/**
 * Activity for user sign-in functionality.
 */
class SignInActivity : AppCompatActivity() {
    private val databaseHelper = DatabaseHelper(this)
    private lateinit var binding: ActivitySigninBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySigninBinding.inflate(layoutInflater)
        setContentView(binding.root)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signin)
        btnSignUpListener()
        btnSignInListener()
    }

    /**
     * Sets up the click listener for the Sign In button.
     */
    private fun btnSignInListener(){
        val signInButton = findViewById<Button>(R.id.signInButton)
        val editTextUsername: TextView = findViewById(R.id.editTextUsername)
        val editTextPassword: TextView = findViewById(R.id.editTextPassword)

        signInButton.setOnClickListener(){
            if(databaseHelper.checkUser(editTextUsername.text.toString().trim(),editTextPassword.text.toString().trim())){

                // Clear saved username
                val sharedPref = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
                val editor = sharedPref.edit()
                editor.remove("username")

                // Save the username in shared preferences
                editor.putString("username", editTextUsername.text.toString().trim())
                editor.apply()

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                Toast.makeText(this,"Login successfully",Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this,"Username or password is wrong, please try again",Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Sets up the click listener for the Sign Up button.
     */
    private fun btnSignUpListener() {
        val signUpButton = findViewById<TextView>(R.id.signUpButton)

        signUpButton.setOnClickListener() {
            val intent = Intent(this,SignUpActivity::class.java)
            startActivity(intent)
        }
    }
}
