package com.easylearn.easyverbs.ui.screens.stats

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.easylearn.easyverbs.data.local.SettingsManager
import com.easylearn.easyverbs.data.repository.VerbRepository

@Composable
fun StatsScreen(
    repository: VerbRepository,
    settingsManager: SettingsManager
) {
    val allVerbs by repository.allVerbs.collectAsState(initial = emptyList())
    val totalCorrect by settingsManager.totalCorrect.collectAsState(initial = 0)
    val totalQuestions by settingsManager.totalQuestions.collectAsState(initial = 0)
    val bestStreak by settingsManager.bestStreak.collectAsState(initial = 0)
    val sessionsCompleted by settingsManager.sessionsCompleted.collectAsState(initial = 0)

    val accuracy = if (totalQuestions > 0) (totalCorrect * 100) / totalQuestions else 0
    val learnedCount = allVerbs.count { it.lastSeenTimestamp != null }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Surface(
            modifier = Modifier.fillMaxWidth(),
            tonalElevation = 2.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.BarChart,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Статистика", style = MaterialTheme.typography.titleLarge)
                }
            }
        }

        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Stats cards
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatItem("Сессии", "$sessionsCompleted", Modifier.weight(1f))
                StatItem("Вопросов", "$totalQuestions", Modifier.weight(1f))
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatItem("Точность", "$accuracy%", Modifier.weight(1f))
                StatItem("Лучшая серия", "$bestStreak", Modifier.weight(1f))
            }
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatItem("Изучено", "$learnedCount/${allVerbs.size}", Modifier.weight(1f))
                StatItem("Всего глаголов", "${allVerbs.size}", Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Mistakes section
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Частые ошибки",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    val mistakeVerbs = allVerbs.filter { it.errorCount > 0 }
                        .sortedByDescending { it.errorCount }
                        .take(5)
                    if (mistakeVerbs.isEmpty()) {
                        Text(
                            "Ошибок нет",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    } else {
                        mistakeVerbs.forEach { verb ->
                            Row(
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(verb.v1, fontWeight = FontWeight.Medium)
                                Text("${verb.errorCount} ошибок", color = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatItem(label: String, value: String, modifier: Modifier = Modifier) {
    Card(modifier = modifier) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                value,
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
