package com.example.teste4.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "medicines")
data class MedicineEntity(
    @PrimaryKey val id: Long,
    val name: String,
    val dosage: String,
    val frequency: String
)

