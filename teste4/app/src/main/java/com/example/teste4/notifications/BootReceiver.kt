package com.example.teste4.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.teste4.data.MedicineRepository

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (Intent.ACTION_BOOT_COMPLETED == intent.action) {
            val meds = MedicineRepository.loadAll(context)
            meds.forEach { m ->
                m.times.forEach { t -> ReminderScheduler.schedule(context, m.id, m.name, m.dosage, t, m.frequency) }
            }
        }
    }
}
