package com.adam607062300025.assesment2.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contact")
data class Contact(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val nama: String,
    val info: Map<String, String>,
)