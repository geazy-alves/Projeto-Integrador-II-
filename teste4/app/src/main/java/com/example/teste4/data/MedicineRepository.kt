package com.example.teste4.data

import android.content.Context
import com.example.teste4.AppViewModel.Medicine
import com.example.teste4.data.db.AppDatabase
import com.example.teste4.data.db.MedicineEntity
import com.example.teste4.data.db.MedicineTimeEntity

object MedicineRepository {
    fun loadAll(context: Context): List<Medicine> {
        val dao = AppDatabase.get(context).medicineDao()
        return dao.getAllWithTimes().map { mt ->
            Medicine(
                id = mt.medicine.id,
                name = mt.medicine.name,
                dosage = mt.medicine.dosage,
                times = mt.times.map { it.time },
                frequency = try { com.example.teste4.notifications.ReminderScheduler.Frequency.valueOf(mt.medicine.frequency) } catch (_: Exception) { com.example.teste4.notifications.ReminderScheduler.Frequency.DAILY }
            )
        }
    }

    fun save(context: Context, medicines: List<Medicine>) {
        val db = AppDatabase.get(context)
        val dao = db.medicineDao()
        medicines.forEach { m ->
            dao.insertMedicine(MedicineEntity(id = m.id, name = m.name, dosage = m.dosage, frequency = m.frequency.name))
            dao.deleteTimesForMedicine(m.id)
            dao.insertTimes(m.times.map { t -> MedicineTimeEntity(medicineId = m.id, time = t) })
        }
    }
}

