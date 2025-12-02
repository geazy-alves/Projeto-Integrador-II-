package com.example.teste4.data.db

import androidx.room.*

data class MedicineWithTimes(
    @Embedded val medicine: MedicineEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "medicineId"
    )
    val times: List<MedicineTimeEntity>
)

@Dao
interface MedicineDao {
    @Transaction
    @Query("SELECT * FROM medicines")
    fun getAllWithTimes(): List<MedicineWithTimes>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertMedicine(medicine: MedicineEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertTimes(times: List<MedicineTimeEntity>)

    @Query("DELETE FROM medicine_times WHERE medicineId = :medicineId")
    fun deleteTimesForMedicine(medicineId: Long)

    @Delete
    fun deleteMedicine(medicine: MedicineEntity)
}

