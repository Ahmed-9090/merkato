package com.example.mekato_tessst

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import android.content.Context

@Database(entities = [UserEntity::class], version = 4, exportSchema = false) // Update version number if necessary
abstract class UserDB : RoomDatabase() {
    abstract fun userDao(): UserDao // Define your DAO

    companion object {
        @Volatile
        private var INSTANCE: UserDB? = null

        // Migration from version 1 to 2: Add profileImageUrl
        val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE users ADD COLUMN profileImageUrl TEXT")
            }
        }

        // Migration from version 2 to 3: Remove profileImageUrl column
        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                // Create a new table without the profileImageUrl column
                db.execSQL(
                    """
                CREATE TABLE users_new (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    firstname TEXT NOT NULL,
                    lastname TEXT NOT NULL,
                    password TEXT NOT NULL,
                    email TEXT NOT NULL
                )
            """
                )

                // Copy the data from the old table to the new table
                db.execSQL(
                    """
                INSERT INTO users_new (id, firstname, lastname, password, email)
                SELECT id, firstname, lastname, password, email FROM users
            """
                )

                // Drop the old table
                db.execSQL("DROP TABLE users")

                // Rename the new table to the original name
                db.execSQL("ALTER TABLE users_new RENAME TO users")
            }
        }

        val MIGRATION_3_4 = object : Migration(3, 4) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE users ADD COLUMN profileImageUrl TEXT")
            }
        }

        fun getDatabase(context: Context): UserDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    UserDB::class.java,
                    "user_database"
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4) // Add both migrations
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

