package com.example.teste4.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.teste4.AppViewModel.Medicine
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.foundation.lazy.LazyRow
import com.example.teste4.notifications.ReminderScheduler

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MedicinesScreen(
    medicines: List<Medicine>,
    onAddMedicine: (String, String, List<String>, ReminderScheduler.Frequency) -> Unit,
    onRemoveMedicine: (Long) -> Unit,
    onUpdateMedicine: (Long, String, String, List<String>, ReminderScheduler.Frequency) -> Unit,
    onBack: () -> Unit
) {
    val nameState = remember { mutableStateOf("") }
    val dosageState = remember { mutableStateOf("") }
    val timeState = remember { mutableStateOf("") }
    val timesState = remember { mutableStateOf(listOf<String>()) }
    val freqState = remember { mutableStateOf(ReminderScheduler.Frequency.DAILY) }
    val freqExpanded = remember { mutableStateOf(false) }
    val editIdState = remember { mutableStateOf<Long?>(null) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Meus Remédios") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Voltar")
                    }
                },
                scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
            )
        }
    ) { innerPadding ->
        AnimatedVisibility(visible = true, enter = fadeIn()) {
            Column(
                modifier = Modifier.fillMaxSize().padding(innerPadding).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = nameState.value,
                    onValueChange = { nameState.value = it },
                    label = { Text("Nome do remédio") }
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = dosageState.value,
                    onValueChange = { dosageState.value = it },
                    label = { Text("Dosagem") }
                )
                OutlinedTextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = timeState.value,
                    onValueChange = { timeState.value = it },
                    label = { Text("Horário (HH:mm)") }
                )
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Button(onClick = {
                        val t = timeState.value.trim()
                        if (t.isNotEmpty()) {
                            timesState.value = (timesState.value + t).distinct()
                            timeState.value = ""
                        }
                    }) { Text("Adicionar horário") }
                    Button(onClick = { timesState.value = listOf() }) { Text("Limpar horários") }
                }
                if (timesState.value.isNotEmpty()) {
                    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(timesState.value) { t ->
                            Row(verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                Text(t)
                                Button(onClick = { timesState.value = timesState.value.filterNot { it == t } }) { Text("Remover") }
                            }
                        }
                    }
                }
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                    Button(onClick = { freqExpanded.value = true }) { Text("Frequência: ${freqState.value}") }
                    DropdownMenu(expanded = freqExpanded.value, onDismissRequest = { freqExpanded.value = false }) {
                        DropdownMenuItem(text = { Text("Único") }, onClick = { freqState.value = ReminderScheduler.Frequency.ONCE; freqExpanded.value = false })
                        DropdownMenuItem(text = { Text("Diário") }, onClick = { freqState.value = ReminderScheduler.Frequency.DAILY; freqExpanded.value = false })
                        DropdownMenuItem(text = { Text("Semanal") }, onClick = { freqState.value = ReminderScheduler.Frequency.WEEKLY; freqExpanded.value = false })
                    }
                }
                if (editIdState.value == null) {
                    Button(modifier = Modifier.fillMaxWidth(), onClick = {
                        val typed = timeState.value.trim()
                        val finalTimes = (
                            if (timesState.value.isEmpty() && typed.isNotEmpty()) timesState.value + typed else timesState.value
                        ).distinct()
                        onAddMedicine(nameState.value, dosageState.value, finalTimes, freqState.value)
                        nameState.value = ""
                        dosageState.value = ""
                        timeState.value = ""
                        timesState.value = listOf()
                    }) { Text("Cadastrar") }
                } else {
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Button(onClick = {
                            val id = editIdState.value!!
                            val typed = timeState.value.trim()
                            val finalTimes = (
                                if (timesState.value.isEmpty() && typed.isNotEmpty()) timesState.value + typed else timesState.value
                            ).distinct()
                            onUpdateMedicine(id, nameState.value, dosageState.value, finalTimes, freqState.value)
                            editIdState.value = null
                            nameState.value = ""
                            dosageState.value = ""
                            timeState.value = ""
                            timesState.value = listOf()
                        }) { Text("Salvar") }
                        Spacer(modifier = Modifier.width(8.dp))
                        Button(onClick = {
                            editIdState.value = null
                            nameState.value = ""
                            dosageState.value = ""
                            timeState.value = ""
                            timesState.value = listOf()
                        }) { Text("Cancelar") }
                    }
                }
                if (medicines.isEmpty()) {
                    Text("Nenhum remédio cadastrado.")
                } else {
                    LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        items(medicines) { med ->
                            Row(horizontalArrangement = Arrangement.spacedBy(8.dp), verticalAlignment = Alignment.CenterVertically) {
                                Text("${med.name} • ${med.dosage} • ${med.times.joinToString(", ")}")
                                Button(onClick = {
                                    editIdState.value = med.id
                                    nameState.value = med.name
                                    dosageState.value = med.dosage
                                    timeState.value = ""
                                    timesState.value = med.times
                                    freqState.value = med.frequency
                                }) { Text("Editar") }
                                Button(onClick = { onRemoveMedicine(med.id) }) { Text("Remover") }
                            }
                        }
                    }
                }
                Button(modifier = Modifier.fillMaxWidth(), onClick = onBack) { Text("Voltar") }
            }
        }
    }
}
