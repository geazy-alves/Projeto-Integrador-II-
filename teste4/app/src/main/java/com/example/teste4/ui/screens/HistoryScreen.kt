package com.example.teste4.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import androidx.compose.ui.platform.LocalContext
import com.example.teste4.data.IntakeRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    medicines: List<Medicine>,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val logs = IntakeRepository.getAll(context)
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Histórico") },
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
            if (logs.isEmpty()) {
                Column(
                    modifier = Modifier.fillMaxSize().padding(innerPadding).padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Sem registros de doses.")
                    Button(modifier = Modifier.fillMaxSize(), onClick = onBack) { Text("Voltar") }
                }
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize().padding(innerPadding).padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    items(logs) { log ->
                        val name = medicines.find { it.id == log.medicineId }?.name ?: "#${log.medicineId}"
                        val statusPt = if (log.status == "TAKEN") "Tomado" else "Não tomado"
                        val whenStr = java.text.SimpleDateFormat("dd/MM/yyyy HH:mm").format(java.util.Date(log.timestamp))
                        Text("$name • ${log.time} • $statusPt • $whenStr")
                    }
                }
            }
        }
    }
}

