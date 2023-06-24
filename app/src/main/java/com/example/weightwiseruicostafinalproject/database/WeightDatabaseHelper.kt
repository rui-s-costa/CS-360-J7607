import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import java.util.*

/**
 * Helper class for interacting with the weight database.
 *
 * @param context The application context.
 */
class WeightDatabaseHelper(context: Context) :
    SQLiteOpenHelper(
        context,
        DATABASE_NAME,
        null,
        DATABASE_VERSION
    ) {

    private val appContext: Context = context.applicationContext

    /**
     * Retrieves the username from SharedPreferences.
     *
     * @return The username.
     */
    fun getUsername(): String {
        val sharedPref = appContext.getSharedPreferences("my_prefs", Context.MODE_PRIVATE)
        return sharedPref.getString("username", "") ?: ""
    }

    private val WEIGHT_TABLE =
        ("CREATE TABLE " + TABLE_WEIGHT + "("
                + ID + " TEXT PRIMARY KEY,"
                + COLUMN_USER_NAME + " TEXT,"
                + COLUMN_WEIGHT + " TEXT,"
                + COLUMN_DATE + " TEXT" + ")")

    private val DROP_WEIGHT_TABLE =
        "DROP TABLE IF EXISTS $TABLE_WEIGHT"

    /**
     * Creates the weight table in the database.
     */
    override fun onCreate(db: SQLiteDatabase) {
        db.execSQL(WEIGHT_TABLE)
    }

    /**
     * Upgrades the database when a new version is available.
     */
    override fun onUpgrade(
        db: SQLiteDatabase,
        oldVersion: Int,
        newVersion: Int
    ) {
        db.execSQL(DROP_WEIGHT_TABLE)
        onCreate(db)
    }

    /**
     * Retrieves the weights from the database for the current user.
     *
     * @return An array of weight items.
     */
    fun getWeights(): Array<Triple<String, String, String>> {
        val db = this.readableDatabase
        val username = getUsername() // Retrieve the username from SharedPreferences
        Log.d("WeightDatabaseHelper", "Username: $username")
        val selection = "$COLUMN_USER_NAME = ?"
        val selectionArgs = arrayOf(username)

        val cursor: Cursor = db.query(
            TABLE_WEIGHT,
            null,
            selection,
            selectionArgs,
            null,
            null,
            null
        )

        val weightSet = HashSet<String>() // Use a Set to keep track of unique retrievedWeight values
        val resultList = mutableListOf<Triple<String, String, String>>()

        while (cursor.moveToNext()) {
            val id = cursor.getString(cursor.getColumnIndexOrThrow(ID))
            val retrievedWeight = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_WEIGHT))
            val date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE))

            if (!weightSet.contains(retrievedWeight)) {
                weightSet.add(retrievedWeight)
                val weightPair = Triple(id, retrievedWeight, date)
                resultList.add(weightPair)
            }
        }

        cursor.close()
        db.close()

        return resultList.toTypedArray()
    }

    /**
     * Adds a weight entry to the database.
     *
     * @param weight The weight value.
     * @param date The date of the weight entry.
     * @return `true` if the weight was added successfully, `false` otherwise.
     */
    fun addWeight(weight: String, date: String): Boolean {
        val db = this.writableDatabase
        val username = getUsername() // Retrieve the username from SharedPreferences

        val values = ContentValues().apply {
            put(ID, UUID.randomUUID().toString())
            put(COLUMN_USER_NAME, username)
            put(COLUMN_WEIGHT, weight)
            put(COLUMN_DATE, date)
        }

        val result = db.insert(TABLE_WEIGHT, null, values) != -1L
        db.close()

        return result
    }

    /**
     * Updates a weight entry in the database.
     *
     * @param id The ID of the weight entry.
     * @param weight The updated weight value.
     * @param date The updated date of the weight entry.
     * @return `true` if the weight was updated successfully, `false` otherwise.
     */
    fun updateWeight(id: String, weight: String, date: String): Boolean {
        val db = this.writableDatabase
        val username = getUsername() // Retrieve the username from SharedPreferences

        val values = ContentValues().apply {
            put(COLUMN_USER_NAME, username)
            put(COLUMN_WEIGHT, weight)
            put(COLUMN_DATE, date)
        }

        val whereClause = "$ID = ?"
        val whereArgs = arrayOf(id)

        val rowsAffected = db.update(TABLE_WEIGHT, values, whereClause, whereArgs)
        db.close()

        return rowsAffected > 0
    }

    /**
     * Deletes a weight entry from the database by ID.
     *
     * @param id The ID of the weight entry to delete.
     * @return `true` if the weight was deleted successfully, `false` otherwise.
     */
    fun deleteWeightById(id: String): Boolean {
        val db = this.writableDatabase

        val whereClause = "$ID = ?"
        val whereArgs = arrayOf(id)

        val rowsAffected = db.delete(TABLE_WEIGHT, whereClause, whereArgs)
        db.close()

        return rowsAffected > 0
    }

    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "WeightWiseDatabaseV6.db"
        private const val TABLE_WEIGHT = "weights"
        private const val ID = "id"
        private const val COLUMN_USER_NAME = "user_name"
        private const val COLUMN_WEIGHT = "weight"
        private const val COLUMN_DATE = "date"
    }
}
