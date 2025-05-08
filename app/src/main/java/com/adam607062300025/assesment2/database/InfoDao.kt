package com.adam607062300025.assesment2.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.adam607062300025.assesment2.model.Info

@Dao
interface InfoDao {

    @Insert
    suspend fun insert(info: Info)

    @Update
    suspend fun update(info: Info)

    @Query("SELECT * FROM info WHERE contactId = :id")
    suspend fun getInfoByContactId(id: Long): List<Info>

    @Query("DELETE FROM info WHERE contactId = :id")
    suspend fun deleteById(id: Long)
}