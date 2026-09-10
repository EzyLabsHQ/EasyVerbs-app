package com.easylearn.easyverbs.ui.screens.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.easylearn.easyverbs.data.local.SettingsManager
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    settingsManager: SettingsManager
) {
    val darkMode by settingsManager.darkMode.collectAsState(initial = false)
    val showTranslation by settingsManager.showTranslation.collectAsState(initial = true)
    val autoAdvance by settingsManager.autoAdvance.collectAsState(initial = false)
    val shuffle by settingsManager.shuffle.collectAsState(initial = true)
    val soundEnabled by settingsManager.soundEnabled.collectAsState(initial = true)
    val haptics by settingsManager.haptics.collectAsState(initial = true)
    val smartOrder by settingsManager.smartOrder.collectAsState(initial = true)
    val questionCount by settingsManager.questionCount.collectAsState(initial = 10)
    val lang by settingsManager.lang.collectAsState(initial = "ru")

    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            tonalElevation = 2.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.Settings,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Настройки", style = MaterialTheme.typography.titleLarge)
                }
            }
        }

        // Training section
        SettingsSection("Обучение") {
            SettingsRow("Количество вопросов") {
                var expanded by remember { mutableStateOf(false) }
                Box {
                    FilterChip(
                        selected = true,
                        onClick = { expanded = true },
                        label = { Text("$questionCount") }
                    )
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        listOf(5, 10, 15, 20, 30).forEach { count ->
                            DropdownMenuItem(
                                text = { Text("$count") },
                                onClick = {
                                    scope.launch { settingsManager.setQuestionCount(count) }
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
            SettingsToggle("Показывать перевод", showTranslation) {
                scope.launch { settingsManager.setShowTranslation(it) }
            }
            SettingsToggle("Автопереход", autoAdvance) {
                scope.launch { settingsManager.setAutoAdvance(it) }
            }
            SettingsToggle("Перемешивать", shuffle) {
                scope.launch { settingsManager.setShuffle(it) }
            }
            SettingsToggle("Умный порядок", smartOrder) {
                scope.launch { settingsManager.setSmartOrder(it) }
            }
        }

        // Appearance section
        SettingsSection("Внешний вид") {
            SettingsToggle("Тёмная тема", darkMode) {
                scope.launch { settingsManager.setDarkMode(it) }
            }
        }

        // Sound section
        SettingsSection("Звук и вибрация") {
            SettingsToggle("Звуковые эффекты", soundEnabled) {
                scope.launch { settingsManager.setSoundEnabled(it) }
            }
            SettingsToggle("Вибрация", haptics) {
                scope.launch { settingsManager.setHaptics(it) }
            }
        }

        // Language section
        SettingsSection("Язык интерфейса") {
            var expanded by remember { mutableStateOf(false) }
            SettingsRow("Язык") {
                Box {
                    FilterChip(
                        selected = true,
                        onClick = { expanded = true },
                        label = {
                            Text(when (lang) {
                                "ru" -> "Русский"
                                "en" -> "English"
                                "es" -> "Español"
                                "de" -> "Deutsch"
                                "fr" -> "Français"
                                "pt" -> "Português"
                                else -> lang
                            })
                        }
                    )
                    DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                        listOf("ru" to "Русский", "en" to "English", "es" to "Español", "de" to "Deutsch", "fr" to "Français", "pt" to "Português").forEach { (code, name) ->
                            DropdownMenuItem(
                                text = { Text(name) },
                                onClick = {
                                    scope.launch { settingsManager.setLang(code) }
                                    expanded = false
                                }
                            )
                        }
                    }
                }
            }
        }

        // Danger zone
        SettingsSection("Данные", isDanger = true) {
            SettingsRow("Сброс прогресса") {
                OutlinedButton(
                    onClick = { scope.launch { settingsManager.resetAll() } },
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Icon(Icons.Filled.DeleteForever, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Сбросить")
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun SettingsSection(
    title: String,
    isDanger: Boolean = false,
    content: @Composable ColumnScope.() -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
        Text(
            title.uppercase(),
            style = MaterialTheme.typography.labelSmall,
            color = if (isDanger) MaterialTheme.colorScheme.error
            else MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(bottom = 4.dp)
        )
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface
            )
        ) {
            Column(modifier = Modifier.padding(4.dp)) {
                content()
            }
        }
    }
}

@Composable
fun SettingsRow(
    label: String,
    trailing: @Composable () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        trailing()
    }
}

@Composable
fun SettingsToggle(
    label: String,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        Switch(checked = checked, onCheckedChange = onCheckedChange)
    }
}
