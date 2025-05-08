package com.adam607062300025.assesment2.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(tableName = "info", foreignKeys =
    [
        ForeignKey(entity = Contact::class,
            parentColumns = ["id"],
            childColumns = ["contactId"],
            onDelete = ForeignKey.CASCADE)
    ]
)
data class Info(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val contactId: Long,
    val type: String,
    val value: String,
)