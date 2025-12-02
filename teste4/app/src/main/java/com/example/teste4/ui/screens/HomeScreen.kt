package com.example.teste4.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateMedicines: () -> Unit,
    onNavigateSettings: () -> Unit,
    onNavigateHelp: () -> Unit,
    onNavigateAbout: () -> Unit,
    onNavigateHistory: () -> Unit,
) {
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior()
    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(title = { Text("Lembrete de Remédios") }, scrollBehavior = scrollBehavior)
        }
    ) { innerPadding ->
        AnimatedVisibility(visible = true, enter = fadeIn()) {
            Column(
                modifier = Modifier.fillMaxSize().padding(innerPadding).padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Button(modifier = Modifier.fillMaxWidth(), onClick = onNavigateMedicines) { Text("Meus Remédios") }
                Button(modifier = Modifier.fillMaxWidth(), onClick = onNavigateSettings) { Text("Configurações") }
                Button(modifier = Modifier.fillMaxWidth(), onClick = onNavigateHistory) { Text("Histórico") }
                Button(modifier = Modifier.fillMaxWidth(), onClick = onNavigateHelp) { Text("Ajuda") }
                Button(modifier = Modifier.fillMaxWidth(), onClick = onNavigateAbout) { Text("Sobre") }
            }
        }
    }
}
