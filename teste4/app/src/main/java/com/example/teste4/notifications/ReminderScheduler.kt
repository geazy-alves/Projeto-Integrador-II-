package com.example.teste4.notifications

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import java.util.Calendar

object ReminderScheduler {
    enum class Frequency { ONCE, DAILY, WEEKLY }

    fun schedule(context: Context, id: Long, name: String, dosage: String, time: String, frequency: Frequency = Frequency.ONCE) {
        val parts = time.split(":")
        if (parts.size != 2) return
        val h = parts[0].toIntOrNull() ?: return
        val m = parts[1].toIntOrNull() ?: return
        val cal = Calendar.getInstance()
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        cal.set(Calendar.HOUR_OF_DAY, h)
        cal.set(Calendar.MINUTE, m)
        if (cal.timeInMillis <= System.currentTimeMillis()) {
            cal.add(Calendar.DAY_OF_YEAR, 1)
        }

        val intent = Intent(context, AlarmReceiver::class.java)
            .putExtra("id", id)
            .putExtra("name", name)
            .putExtra("dosage", dosage)
            .putExtra("time", time)
            .putExtra("frequency", frequency.name)

        val flags = PendingIntent.FLAG_UPDATE_CURRENT or if (Build.VERSION.SDK_INT >= 23) PendingIntent.FLAG_IMMUTABLE else 0
        val pi = PendingIntent.getBroadcast(context, getRequestCode(id, time), intent, flags)

        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        if (Build.VERSION.SDK_INT >= 23) {
            am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.timeInMillis, pi)
        } else {
            am.setExact(AlarmManager.RTC_WAKEUP, cal.timeInMillis, pi)
        }
    }

    private fun getRequestCode(id: Long, time: String): Int = (id.toInt() * 31) + time.hashCode()

    fun cancel(context: Context, id: Long, time: String) {
        val intent = Intent(context, AlarmReceiver::class.java)
        val flags = PendingIntent.FLAG_UPDATE_CURRENT or if (Build.VERSION.SDK_INT >= 23) PendingIntent.FLAG_IMMUTABLE else 0
        val pi = PendingIntent.getBroadcast(context, getRequestCode(id, time), intent, flags)
        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        am.cancel(pi)
    }

    fun scheduleNext(context: Context, id: Long, name: String, dosage: String, time: String, frequency: Frequency) {
        val parts = time.split(":")
        if (parts.size != 2) return
        val h = parts[0].toIntOrNull() ?: return
        val m = parts[1].toIntOrNull() ?: return
        val cal = Calendar.getInstance()
        cal.set(Calendar.SECOND, 0)
        cal.set(Calendar.MILLISECOND, 0)
        cal.set(Calendar.HOUR_OF_DAY, h)
        cal.set(Calendar.MINUTE, m)
        when (frequency) {
            Frequency.DAILY -> cal.add(Calendar.DAY_OF_YEAR, 1)
            Frequency.WEEKLY -> cal.add(Calendar.WEEK_OF_YEAR, 1)
            else -> return
        }

        val intent = Intent(context, AlarmReceiver::class.java)
            .putExtra("id", id)
            .putExtra("name", name)
            .putExtra("dosage", dosage)
            .putExtra("time", time)
            .putExtra("frequency", frequency.name)

        val flags = PendingIntent.FLAG_UPDATE_CURRENT or if (Build.VERSION.SDK_INT >= 23) PendingIntent.FLAG_IMMUTABLE else 0
        val pi = PendingIntent.getBroadcast(context, getRequestCode(id, time), intent, flags)
        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        if (Build.VERSION.SDK_INT >= 23) {
            am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.timeInMillis, pi)
        } else {
            am.setExact(AlarmManager.RTC_WAKEUP, cal.timeInMillis, pi)
        }
    }

    private fun getNudgeRequestCode(id: Long, time: String, dateKey: String): Int = ((id.toInt() * 37) + time.hashCode() + dateKey.hashCode())

    fun scheduleNudge(context: Context, id: Long, name: String, dosage: String, time: String, dateKey: String, minutesDelay: Int = 15) {
        val cal = Calendar.getInstance()
        cal.add(Calendar.MINUTE, minutesDelay)
        val intent = Intent(context, AlarmReceiver::class.java)
            .putExtra("id", id)
            .putExtra("name", name)
            .putExtra("dosage", dosage)
            .putExtra("time", time)
            .putExtra("frequency", Frequency.ONCE.name)
            .putExtra("dateKey", dateKey)
            .putExtra("nudge", true)
        val flags = PendingIntent.FLAG_UPDATE_CURRENT or if (Build.VERSION.SDK_INT >= 23) PendingIntent.FLAG_IMMUTABLE else 0
        val pi = PendingIntent.getBroadcast(context, getNudgeRequestCode(id, time, dateKey), intent, flags)
        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        if (Build.VERSION.SDK_INT >= 23) am.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, cal.timeInMillis, pi) else am.setExact(AlarmManager.RTC_WAKEUP, cal.timeInMillis, pi)
    }

    fun cancelNudge(context: Context, id: Long, time: String, dateKey: String) {
        val intent = Intent(context, AlarmReceiver::class.java)
        val flags = PendingIntent.FLAG_UPDATE_CURRENT or if (Build.VERSION.SDK_INT >= 23) PendingIntent.FLAG_IMMUTABLE else 0
        val pi = PendingIntent.getBroadcast(context, getNudgeRequestCode(id, time, dateKey), intent, flags)
        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        am.cancel(pi)
    }
}
