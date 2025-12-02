package com.example.teste4.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface IntakeDao {
    @Insert
    fun insert(log: IntakeLogEntity)

    @Query("SELECT COUNT(*) FROM intake_logs WHERE medicineId = :medicineId AND dateKey = :dateKey AND time = :time")
    fun countFor(medicineId: Long, dateKey: String, time: String): Int

    @Query("SELECT * FROM intake_logs ORDER BY timestamp DESC")
    fun getAll(): List<IntakeLogEntity>
}
