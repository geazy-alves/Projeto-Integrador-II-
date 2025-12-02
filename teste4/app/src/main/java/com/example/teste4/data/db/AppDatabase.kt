package com.example.teste4.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [MedicineEntity::class, MedicineTimeEntity::class, IntakeLogEntity::class],
    version = 2,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun medicineDao(): MedicineDao
    abstract fun intakeDao(): IntakeDao

    companion object {
        @Volatile private var INSTANCE: AppDatabase? = null

        fun get(context: Context): AppDatabase = INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(context, AppDatabase::class.java, "meds.db")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build().also { INSTANCE = it }
        }
    }
}

