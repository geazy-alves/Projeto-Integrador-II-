package com.example.teste4.data

import android.content.Context
import org.json.JSONArray
import org.json.JSONObject
import com.example.teste4.AppViewModel.Medicine
import com.example.teste4.notifications.ReminderScheduler

object MedicineStore {
    private const val PREF = "meds_store"
    private const val KEY = "meds"

    fun save(context: Context, medicines: List<Medicine>) {
        val arr = JSONArray()
        medicines.forEach { m ->
            val o = JSONObject()
                .put("id", m.id)
                .put("name", m.name)
                .put("dosage", m.dosage)
                .put("frequency", m.frequency.name)
            val timesArr = JSONArray()
            m.times.forEach { t -> timesArr.put(t) }
            o.put("times", timesArr)
            arr.put(o)
        }
        context.getSharedPreferences(PREF, Context.MODE_PRIVATE)
            .edit().putString(KEY, arr.toString()).apply()
    }

    fun load(context: Context): List<Medicine> {
        val s = context.getSharedPreferences(PREF, Context.MODE_PRIVATE).getString(KEY, null) ?: return emptyList()
        return try {
            val arr = JSONArray(s)
            (0 until arr.length()).map { i ->
                val o = arr.getJSONObject(i)
                val id = o.getLong("id")
                val name = o.getString("name")
                val dosage = o.getString("dosage")
                val freqStr = o.optString("frequency", ReminderScheduler.Frequency.DAILY.name)
                val freq = try { ReminderScheduler.Frequency.valueOf(freqStr) } catch (_: Exception) { ReminderScheduler.Frequency.DAILY }
                val timesJson = o.getJSONArray("times")
                val times = (0 until timesJson.length()).map { j -> timesJson.getString(j) }
                Medicine(id = id, name = name, dosage = dosage, times = times, frequency = freq)
            }
        } catch (_: Exception) {
            emptyList()
        }
    }
}

