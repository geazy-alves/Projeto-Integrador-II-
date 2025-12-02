package com.example.teste4.data

import android.content.Context
import com.example.teste4.data.db.AppDatabase
import com.example.teste4.data.db.IntakeLogEntity

object IntakeRepository {
    private fun dateKey(): String = java.text.SimpleDateFormat("yyyy-MM-dd").format(java.util.Date())

    fun logTaken(context: Context, medicineId: Long, time: String) {
        AppDatabase.get(context).intakeDao().insert(
            IntakeLogEntity(medicineId = medicineId, dateKey = dateKey(), time = time, status = "TAKEN", timestamp = System.currentTimeMillis())
        )
    }

    fun logMissed(context: Context, medicineId: Long, time: String) {
        AppDatabase.get(context).intakeDao().insert(
            IntakeLogEntity(medicineId = medicineId, dateKey = dateKey(), time = time, status = "MISSED", timestamp = System.currentTimeMillis())
        )
    }

    fun getAll(context: Context) = AppDatabase.get(context).intakeDao().getAll()

    fun hasLog(context: Context, medicineId: Long, time: String): Boolean {
        return AppDatabase.get(context).intakeDao().countFor(medicineId, dateKey(), time) > 0
    }
}

