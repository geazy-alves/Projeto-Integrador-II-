package com.example.teste4.data.db

import androidx.room.Entity

@Entity(tableName = "medicine_times", primaryKeys = ["medicineId", "time"])
data class MedicineTimeEntity(
    val medicineId: Long,
    val time: String
)

