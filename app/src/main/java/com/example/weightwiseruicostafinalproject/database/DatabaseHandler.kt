package com.example.weightwiseruicostafinalproject.database

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.weightwiseruicostafinalproject.model.User

/**
 * Helper class for interacting with the user database.
 *
 * @param context The application context.
 */
class DatabaseHelper(context: Context?) :
    SQLiteOpenHelper(
        context,
        DATABASE_NAME,
        null,
        DATABASE_VERSION
    ) {

    private val USER_TABLE =
        ("CREATE TABLE " + TABLE_USER + "("
                + COLUMN_USER_NAME + " TEXT PRIMARY KEY,"
                + COLUMN_USER_PASSWORD + " TEXT" + ")")

    private val DROP_USER_TABLE =
        "DROP TABLE IF EXISTS $TABLE_USER"

    /**
     * Creates the user table in the database.
     */
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(USER_TABLE)
    }

    /**
     * Upgrades the database when a new version is available.
     */
    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        db.execSQL(DROP_USER_TABLE)
        onCreate(db)
    }

    /**
     * Adds a user to the database.
     *
     * @param user The user to add.
     */
    fun addUser(user: User) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(COLUMN_USER_NAME, user.username)
            put(COLUMN_USER_PASSWORD, user.password)
        }
        db.insert(TABLE_USER, null, values)
        db.close()
    }

    /**
     * Checks if a user with the given username exists in the database.
     *
     * @param username The username to check.
     * @return `true` if the user exists, `false` otherwise.
     */
    fun checkUser(username: String): Boolean {
        val columns = arrayOf(COLUMN_USER_NAME)
        val db = this.readableDatabase
        val selection = "$COLUMN_USER_NAME = ?"
        val selectionArgs = arrayOf(username)
        val cursor: Cursor = db.query(
            TABLE_USER,
            columns,
            selection,
            selectionArgs,
            null,
            null,
            null
        )
        val cursorCount: Int = cursor.count
        cursor.close()
        db.close()
        return cursorCount > 0
    }

    /**
     * Checks if a user with the given username and password exists in the database.
     *
     * @param username The username to check.
     * @param password The password to check.
     * @return `true` if the user exists, `false` otherwise.
     */
    fun checkUser(username: String, password: String): Boolean {
        val columns = arrayOf(COLUMN_USER_NAME)
        val db = this.readableDatabase
        val selection = "$COLUMN_USER_NAME = ? AND $COLUMN_USER_PASSWORD = ?"
        val selectionArgs = arrayOf(username, password)
        val cursor: Cursor = db.query(
            TABLE_USER,
            columns,
            selection,
            selectionArgs,
            null,
            null,
            null
        )
        val cursorCount: Int = cursor.count
        cursor.close()
        db.close()
        return cursorCount > 0
    }

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "UserManagerV6.db"
        private const val TABLE_USER = "user"
        private const val COLUMN_USER_NAME = "user_name"
        private const val COLUMN_USER_PASSWORD = "user_password"
    }
}
