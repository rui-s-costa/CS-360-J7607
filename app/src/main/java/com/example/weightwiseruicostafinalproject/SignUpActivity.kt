package com.example.weightwiseruicostafinalproject

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.weightwiseruicostafinalproject.database.DatabaseHelper;
import com.example.weightwiseruicostafinalproject.databinding.ActivitySignupBinding
import com.example.weightwiseruicostafinalproject.model.User;

/**
 * Activity for user signup functionality.
 */
class SignUpActivity : AppCompatActivity() {
    private val databaseHelper = DatabaseHelper(this)
    private lateinit var binding: ActivitySignupBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivitySignupBinding.inflate(layoutInflater)
        setContentView(binding.root)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        signUpListener()
        signInListener()
    }

    /**
     * Sets up the click listener for the Sign Up button.
     */
    private fun signUpListener() {
        val signUpButton = findViewById<TextView>(R.id.signUpButton)
        val editTextUsername: EditText = findViewById(R.id.editTextUsername)
        val editTextPassword: EditText = findViewById(R.id.editTextPassword)
        val editTextConfirmPassword: EditText = findViewById(R.id.editTextConfirmPassword)

        signUpButton.setOnClickListener() {
            if (!databaseHelper.checkUser(editTextUsername.text.toString().trim())) {
                val user = User()

                user.username = editTextUsername.text.toString().trim()
                user.password = editTextPassword.text.toString().trim()

                if (editTextPassword.text.toString().trim() == editTextConfirmPassword.text.toString().trim() ){
                    databaseHelper.addUser(user)

                    // Clear saved username
                    val sharedPref = getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
                    val editor = sharedPref.edit()
                    editor.remove("username")

                    // Save the username in shared preferences
                    editor.putString("username", editTextUsername.text.toString().trim())
                    editor.apply()

                    Toast.makeText(this,"User created successfully",Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                } else {
                    Toast.makeText(this,"Passwords do not match",Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this,"User already exists",Toast.LENGTH_SHORT).show()
            }
        }
    }

    /**
     * Sets up the click listener for the Sign In button.
     */
    private fun signInListener() {
        val signInButton = findViewById<TextView>(R.id.signInButton)

        signInButton.setOnClickListener() {
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)
        }
    }
}
