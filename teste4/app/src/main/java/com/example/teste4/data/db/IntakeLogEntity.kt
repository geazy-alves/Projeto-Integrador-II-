package com.example.teste4.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "intake_logs")
data class IntakeLogEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val medicineId: Long,
    val dateKey: String,
    val time: String,
    val status: String,
    val timestamp: Long
)

