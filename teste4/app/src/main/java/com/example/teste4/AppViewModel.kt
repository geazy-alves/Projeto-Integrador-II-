package com.example.teste4

import androidx.lifecycle.ViewModel
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.State
import com.example.teste4.notifications.ReminderScheduler

class AppViewModel : ViewModel() {
    private val _notificationsEnabled = mutableStateOf(false)
    val notificationsEnabled: State<Boolean> = _notificationsEnabled

    data class Medicine(
        val id: Long,
        val name: String,
        val dosage: String,
        val times: List<String>,
        val frequency: ReminderScheduler.Frequency
    )

    private val _medicines = mutableStateOf(listOf<Medicine>())
    val medicines: State<List<Medicine>> = _medicines
    private var nextId = 1L

    fun setNotificationsEnabled(enabled: Boolean) {
        _notificationsEnabled.value = enabled
    }

    fun addMedicine(name: String, dosage: String, times: List<String>, frequency: ReminderScheduler.Frequency) {
        val n = name.trim()
        val d = dosage.trim()
        val ts = times.map { it.trim() }.filter { it.isNotEmpty() }
        if (n.isEmpty() || ts.isEmpty()) return
        val item = Medicine(nextId++, n, d, ts, frequency)
        _medicines.value = _medicines.value + item
    }

    fun removeMedicine(id: Long) {
        _medicines.value = _medicines.value.filter { it.id != id }
    }

    fun updateMedicine(id: Long, name: String, dosage: String, times: List<String>, frequency: ReminderScheduler.Frequency) {
        val n = name.trim()
        val d = dosage.trim()
        val ts = times.map { it.trim() }.filter { it.isNotEmpty() }
        _medicines.value = _medicines.value.map { m ->
            if (m.id == id) m.copy(name = n.ifEmpty { m.name }, dosage = d, times = ts.ifEmpty { m.times }, frequency = frequency) else m
        }
    }

    fun setInitialMedicines(items: List<Medicine>) {
        _medicines.value = items
        nextId = (items.maxOfOrNull { it.id } ?: 0L) + 1L
    }
}

