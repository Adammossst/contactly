package com.adam607062300025.assesment2.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.adam607062300025.assesment2.model.Contact

@Database(entities = [Contact::class], version = 1, exportSchema = false)
abstract class ContactDb : RoomDatabase() {
    abstract val dao: ContactDao

    companion object {
        @Volatile
        private var INSTANCE: ContactDb? = null

        fun getInstance(context: Context): ContactDb {
            synchronized(this) {
                var instance = INSTANCE
                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        ContactDb::class.java,
                        "contact.db"
                    ).build()
                    INSTANCE = instance
                }

                return instance
            }
        }
    }
}