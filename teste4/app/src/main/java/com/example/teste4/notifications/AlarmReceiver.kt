package com.example.teste4.notifications

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.teste4.R
import com.example.teste4.data.IntakeRepository

class AlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        val id = intent.getLongExtra("id", 0L).toInt()
        val name = intent.getStringExtra("name") ?: ""
        val dosage = intent.getStringExtra("dosage") ?: ""
        val time = intent.getStringExtra("time") ?: ""
        val freqName = intent.getStringExtra("frequency")

        val builder = NotificationCompat.Builder(context, NotificationHelper.CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(name)
            .setContentText(dosage)
            .setPriority(NotificationManager.IMPORTANCE_HIGH)
            .setAutoCancel(true)

        val takenIntent = Intent(context, ActionReceiver::class.java)
            .putExtra("id", id.toLong())
            .putExtra("time", time)
            .putExtra("action", "TAKEN")
        val missedIntent = Intent(context, ActionReceiver::class.java)
            .putExtra("id", id.toLong())
            .putExtra("time", time)
            .putExtra("action", "MISSED")
        val flags = android.app.PendingIntent.FLAG_UPDATE_CURRENT or if (android.os.Build.VERSION.SDK_INT >= 23) android.app.PendingIntent.FLAG_IMMUTABLE else 0
        val takenPi = android.app.PendingIntent.getBroadcast(context, id, takenIntent, flags)
        val missedPi = android.app.PendingIntent.getBroadcast(context, id + 1, missedIntent, flags)
        builder.addAction(R.drawable.ic_launcher_foreground, "Tomei", takenPi)
        builder.addAction(R.drawable.ic_launcher_foreground, "Não tomei", missedPi)

        NotificationManagerCompat.from(context).notify(id, builder.build())

        val isNudge = intent.getBooleanExtra("nudge", false)
        val dk = intent.getStringExtra("dateKey") ?: java.text.SimpleDateFormat("yyyy-MM-dd").format(java.util.Date())
        val idLong = id.toLong()
        if (!IntakeRepository.hasLog(context, idLong, time)) {
            ReminderScheduler.scheduleNudge(context, idLong, name, dosage, time, dk)
        }

        if (!freqName.isNullOrEmpty()) {
            val f = try { ReminderScheduler.Frequency.valueOf(freqName) } catch (_: Exception) { ReminderScheduler.Frequency.ONCE }
            when (f) {
                ReminderScheduler.Frequency.DAILY -> ReminderScheduler.scheduleNext(context, id.toLong(), name, dosage, time, f)
                ReminderScheduler.Frequency.WEEKLY -> ReminderScheduler.scheduleNext(context, id.toLong(), name, dosage, time, f)
                else -> {}
            }
        }
    }
}

