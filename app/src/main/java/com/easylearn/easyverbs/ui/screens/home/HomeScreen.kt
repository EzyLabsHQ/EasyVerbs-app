package com.easylearn.easyverbs.ui.screens.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.easylearn.easyverbs.data.local.SettingsManager
import com.easylearn.easyverbs.data.model.Verb
import com.easylearn.easyverbs.data.repository.VerbRepository
import com.easylearn.easyverbs.ui.navigation.Screen
import com.easylearn.easyverbs.ui.theme.Indigo600
import com.easylearn.easyverbs.util.TtsManager

@Composable
fun HomeScreen(
    repository: VerbRepository,
    settingsManager: SettingsManager,
    onNavigateToScreen: (Screen) -> Unit,
    ttsManager: TtsManager
) {
    val allVerbs by repository.allVerbs.collectAsState(initial = emptyList())
    val totalCorrect by settingsManager.totalCorrect.collectAsState(initial = 0)
    val totalQuestions by settingsManager.totalQuestions.collectAsState(initial = 0)
    val bestStreak by settingsManager.bestStreak.collectAsState(initial = 0)

    val accuracy = if (totalQuestions > 0) (totalCorrect * 100) / totalQuestions else 0
    val learnedCount = allVerbs.count { it.lastSeenTimestamp != null }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxSize()
    ) {
        // Welcome card
        item(span = { GridItemSpan(2) }) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Filled.School,
                        contentDescription = null,
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        "Добро пожаловать!",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        "Тренажёр английских глаголов",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Stats
        item {
            StatCard("Изучено", "$learnedCount/${allVerbs.size}", Icons.Filled.CheckCircle)
        }
        item {
            StatCard("Прогресс", "$totalQuestions", Icons.Filled.TrendingUp)
        }
        item {
            StatCard("Точность", "$accuracy%", Icons.Filled.GpsFixed)
        }
        item {
            StatCard("Серия", "$bestStreak", Icons.Filled.EmojiEvents)
        }

        // Quick start
        item(span = { GridItemSpan(2) }) {
            Text(
                "Быстрый старт",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(top = 8.dp)
            )
        }

        item {
            QuickStartCard("Формы", Icons.Filled.FitnessCenter) {
                onNavigateToScreen(Screen.Trainer)
            }
        }
        item {
            QuickStartCard("Буквы", Icons.Filled.TextFields) {
                onNavigateToScreen(Screen.Letters)
            }
        }
        item {
            QuickStartCard("Спринт", Icons.Filled.Bolt) {
                onNavigateToScreen(Screen.Speed)
            }
        }
        item {
            QuickStartCard("Карточки", Icons.Filled.CreditCard) {
                onNavigateToScreen(Screen.Flashcards)
            }
        }
    }
}

@Composable
fun StatCard(label: String, value: String, icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
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

@Composable
fun QuickStartCard(label: String, icon: androidx.compose.ui.graphics.vector.ImageVector, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                label,
                style = MaterialTheme.typography.labelLarge
            )
        }
    }
}
