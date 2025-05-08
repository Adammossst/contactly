package com.adam607062300025.assesment2.database

import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Dao
import com.adam607062300025.assesment2.model.Contact
import kotlinx.coroutines.flow.Flow

@Dao
interface InfoDao {

    @Insert
    suspend fun insert(contact: Contact)

    @Update
    suspend fun update(contact: Contact)

    @Query("SELECT *  FROM Contact WHERE id = :id")
    suspend fun getInfoById(id: Long): Contact?

    @Query("DELETE FROM Contact WHERE id = :id")
    suspend fun deleteById(id: Long)
}