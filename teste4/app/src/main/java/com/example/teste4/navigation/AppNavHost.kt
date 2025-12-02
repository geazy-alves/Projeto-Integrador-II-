package com.example.teste4.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.compose.ui.platform.LocalContext
import com.example.teste4.ui.screens.AboutScreen
import com.example.teste4.ui.screens.HelpScreen
import com.example.teste4.ui.screens.HomeScreen
import com.example.teste4.ui.screens.MedicinesScreen
import com.example.teste4.ui.screens.SettingsScreen
import com.example.teste4.ui.screens.HistoryScreen
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.teste4.AppViewModel
import com.example.teste4.notifications.ReminderScheduler
import com.example.teste4.data.MedicineRepository

@Composable
fun AppNavHost() {
    val navController = rememberNavController()
    val appViewModel: AppViewModel = viewModel()
    val context = LocalContext.current
    if (appViewModel.medicines.value.isEmpty()) {
        val loaded = MedicineRepository.loadAll(context)
        if (loaded.isNotEmpty()) appViewModel.setInitialMedicines(loaded)
    }

    NavHost(navController = navController, startDestination = Routes.Home) {
        composable(Routes.Home) {
            HomeScreen(
                onNavigateMedicines = { navController.navigate(Routes.Medicines) },
                onNavigateSettings = { navController.navigate(Routes.Settings) },
                onNavigateHelp = { navController.navigate(Routes.Help) },
                onNavigateAbout = { navController.navigate(Routes.About) },
                onNavigateHistory = { navController.navigate(Routes.History) }
            )
        }
        composable(Routes.Medicines) {
            MedicinesScreen(
                medicines = appViewModel.medicines.value,
                onAddMedicine = { name, dosage, times, frequency ->
                    appViewModel.addMedicine(name, dosage, times, frequency)
                    val added = appViewModel.medicines.value.lastOrNull()
                    if (added != null) {
                        added.times.forEach { t -> ReminderScheduler.schedule(context, added.id, added.name, added.dosage, t, added.frequency) }
                    }
                    MedicineRepository.save(context, appViewModel.medicines.value)
                },
                onRemoveMedicine = { id ->
                    val old = appViewModel.medicines.value.find { it.id == id }
                    if (old != null) {
                        old.times.forEach { t -> ReminderScheduler.cancel(context, id, t) }
                    }
                    appViewModel.removeMedicine(id)
                    MedicineRepository.save(context, appViewModel.medicines.value)
                },
                onUpdateMedicine = { id, name, dosage, times, frequency ->
                    val old = appViewModel.medicines.value.find { it.id == id }
                    if (old != null) {
                        old.times.forEach { t -> ReminderScheduler.cancel(context, id, t) }
                    }
                    appViewModel.updateMedicine(id, name, dosage, times, frequency)
                    val updated = appViewModel.medicines.value.find { it.id == id }
                    if (updated != null) {
                        updated.times.forEach { t -> ReminderScheduler.schedule(context, updated.id, updated.name, updated.dosage, t, updated.frequency) }
                    }
                    MedicineRepository.save(context, appViewModel.medicines.value)
                },
                onBack = { navController.popBackStack() }
            )
        }
        composable(Routes.Settings) {
            SettingsScreen(
                notificationsEnabled = appViewModel.notificationsEnabled.value,
                onToggleNotifications = { appViewModel.setNotificationsEnabled(it) },
                onBack = { navController.popBackStack() }
            )
        }
        composable(Routes.Help) {
            HelpScreen(onBack = { navController.popBackStack() })
        }
        composable(Routes.About) {
            AboutScreen(onBack = { navController.popBackStack() })
        }
        composable(Routes.History) {
            HistoryScreen(medicines = appViewModel.medicines.value, onBack = { navController.popBackStack() })
        }
    }
}

