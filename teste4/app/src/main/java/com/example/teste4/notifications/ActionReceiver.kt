package com.example.teste4.notifications

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationManagerCompat
import com.example.teste4.data.IntakeRepository
import com.example.teste4.notifications.ReminderScheduler

class ActionReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val id = intent.getLongExtra("id", 0L)
        val time = intent.getStringExtra("time") ?: ""
        val action = intent.getStringExtra("action") ?: ""
        if (action == "TAKEN") IntakeRepository.logTaken(context, id, time)
        if (action == "MISSED") IntakeRepository.logMissed(context, id, time)
        val dk = java.text.SimpleDateFormat("yyyy-MM-dd").format(java.util.Date())
        ReminderScheduler.cancelNudge(context, id, time, dk)
        NotificationManagerCompat.from(context).cancel(id.toInt())
    }
}

