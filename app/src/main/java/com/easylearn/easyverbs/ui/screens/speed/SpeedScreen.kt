package com.easylearn.easyverbs.ui.screens.speed

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.easylearn.easyverbs.data.local.SettingsManager
import com.easylearn.easyverbs.data.model.Verb
import com.easylearn.easyverbs.data.repository.VerbRepository
import com.easylearn.easyverbs.util.HapticManager
import com.easylearn.easyverbs.util.TtsManager
import kotlinx.coroutines.delay

@Composable
fun SpeedScreen(
    repository: VerbRepository,
    ttsManager: TtsManager,
    settingsManager: SettingsManager
) {
    val allVerbs by repository.allVerbs.collectAsState(initial = emptyList())
    var isStarted by remember { mutableStateOf(false) }
    var timeLeft by remember { mutableIntStateOf(60) }
    var currentVerb by remember { mutableStateOf<Verb?>(null) }
    var userInput by remember { mutableStateOf("") }
    var score by remember { mutableIntStateOf(0) }
    var totalAnswered by remember { mutableIntStateOf(0) }
    var isCorrect by remember { mutableStateOf<Boolean?>(null) }
    var showResult by remember { mutableStateOf(false) }
    var highScore by remember { mutableIntStateOf(0) }

    val context = LocalContext.current

    LaunchedEffect(isStarted, timeLeft) {
        if (isStarted && timeLeft > 0) {
            delay(1000)
            timeLeft--
            if (timeLeft <= 0) {
                showResult = true
            }
        }
    }

    fun pickNextVerb() {
        currentVerb = allVerbs.random()
        userInput = ""
        isCorrect = null
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header
        Surface(
            modifier = Modifier.fillMaxWidth(),
            tonalElevation = 2.dp
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Filled.Bolt,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Спринт на время", style = MaterialTheme.typography.titleLarge)
                }
            }
        }

        if (!isStarted) {
            // Start screen
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(32.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            Icons.Filled.Bolt,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            "60 секунд",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Ответьте как можно больше глаголов! Каждый правильный ответ добавляет +1 сек.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        if (highScore > 0) {
                            Text(
                                "Рекорд: $highScore",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Button(
                            onClick = {
                                isStarted = true
                                timeLeft = 60
                                score = 0
                                totalAnswered = 0
                                showResult = false
                                pickNextVerb()
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Начать спринт")
                        }
                    }
                }
            }
        } else if (showResult) {
            // Results
            val accuracy = if (totalAnswered > 0) (score * 100) / totalAnswered else 0
            val newRecord = score > highScore
            if (newRecord) highScore = score

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(32.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            Icons.Filled.EmojiEvents,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            "Время вышло!",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        if (newRecord) {
                            Text(
                                "🎉 Новый рекорд!",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("$score", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.primary)
                                Text("Результат", style = MaterialTheme.typography.bodySmall)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("$accuracy%", style = MaterialTheme.typography.headlineMedium)
                                Text("Точность", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                        Button(onClick = { isStarted = false }) {
                            Text("Спринт ещё раз")
                        }
                    }
                }
            }
        } else if (currentVerb != null) {
            // Active sprint
            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Timer
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = if (timeLeft <= 10) MaterialTheme.colorScheme.errorContainer
                        else MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "⏱ $timeLeft",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "✓ $score",
                            style = MaterialTheme.typography.titleLarge,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Verb
                Text(
                    currentVerb!!.v1,
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    currentVerb!!.translation,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Input
                OutlinedTextField(
                    value = userInput,
                    onValueChange = { userInput = it },
                    placeholder = { Text("Введите V2...") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Check button
                Button(
                    onClick = {
                        val v2Options = currentVerb!!.v2.lowercase().split("/").map { it.trim() }
                        val correct = userInput.trim().lowercase() in v2Options
                        if (correct) {
                            score++
                            timeLeft++
                            HapticManager.vibrateSuccess(context)
                        } else {
                            HapticManager.vibrateError(context)
                        }
                        totalAnswered++
                        pickNextVerb()
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Проверить")
                }
            }
        }
    }
}
