package com.adam607062300025.assesment2.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.adam607062300025.assesment2.model.Contact
import com.adam607062300025.assesment2.model.Info

@Database(entities = [Contact::class, Info::class], version = 1, exportSchema = false)
abstract class ContactDb : RoomDatabase() {
    abstract val dao: ContactDao
    abstract val dao2: InfoDao

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