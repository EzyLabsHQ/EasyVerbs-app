package com.easylearn.easyverbs.ui.screens.trainer

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
import com.easylearn.easyverbs.data.model.TrainerMode
import com.easylearn.easyverbs.data.model.Verb
import com.easylearn.easyverbs.data.repository.VerbRepository
import com.easylearn.easyverbs.util.HapticManager
import com.easylearn.easyverbs.util.TtsManager

@Composable
fun TrainerScreen(
    repository: VerbRepository,
    ttsManager: TtsManager,
    settingsManager: SettingsManager
) {
    val allVerbs by repository.allVerbs.collectAsState(initial = emptyList())
    var isStarted by remember { mutableStateOf(false) }
    var trainerMode by remember { mutableStateOf(TrainerMode.NORMAL) }
    var currentIndex by remember { mutableIntStateOf(0) }
    var score by remember { mutableIntStateOf(0) }
    var mistakeCount by remember { mutableIntStateOf(0) }
    var v2Input by remember { mutableStateOf("") }
    var v3Input by remember { mutableStateOf("") }
    var isCorrect by remember { mutableStateOf<Boolean?>(null) }
    var showResult by remember { mutableStateOf(false) }
    var streak by remember { mutableIntStateOf(0) }

    val totalQuestions = 10
    val context = LocalContext.current

    val questions = remember(allVerbs) {
        allVerbs.shuffled().take(totalQuestions)
    }

    val currentVerb = questions.getOrNull(currentIndex)

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
                        Icons.Filled.FitnessCenter,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Проверка знаний", style = MaterialTheme.typography.titleLarge)
                }
            }
        }

        if (!isStarted) {
            // Start screen with mode selection
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
                        Text(
                            "Выберите режим",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )

                        TrainerMode.entries.forEach { mode ->
                            FilterChip(
                                selected = trainerMode == mode,
                                onClick = { trainerMode = mode },
                                label = {
                                    Text(when (mode) {
                                        TrainerMode.NORMAL -> "V1 → V2/V3"
                                        TrainerMode.REVERSE_V1 -> "Перевод → V1"
                                        TrainerMode.REVERSE_FORMS -> "V2/V3 → V1"
                                        TrainerMode.SENTENCES -> "Предложения"
                                        TrainerMode.AUDIO -> "Аудио"
                                        TrainerMode.MATCH -> "Сопоставить"
                                    })
                                },
                                modifier = Modifier.fillMaxWidth()
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = {
                                isStarted = true
                                currentIndex = 0
                                score = 0
                                mistakeCount = 0
                                showResult = false
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Начать тренировку")
                        }
                    }
                }
            }
        } else if (showResult) {
            // Results
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
                            "Тренировка завершена!",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(24.dp)) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("$score", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.primary)
                                Text("Верно", style = MaterialTheme.typography.bodySmall)
                            }
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text("$mistakeCount", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.error)
                                Text("Ошибок", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                        Button(onClick = { isStarted = false }) {
                            Text("Тренироваться ещё раз")
                        }
                    }
                }
            }
        } else if (currentVerb != null) {
            // Active training
            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Progress
                LinearProgressIndicator(
                    progress = { (currentIndex + 1).toFloat() / totalQuestions },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Вопрос ${currentIndex + 1} из $totalQuestions")
                    Row {
                        Text("✓ $score  ", color = MaterialTheme.colorScheme.primary)
                        Text("✗ $mistakeCount", color = MaterialTheme.colorScheme.error)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Verb to practice
                Text(
                    currentVerb.translation,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        currentVerb.v1,
                        style = MaterialTheme.typography.headlineLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    IconButton(onClick = { ttsManager.speak(currentVerb.v1) }) {
                        Icon(Icons.Filled.VolumeUp, contentDescription = "Прослушать")
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Input fields based on mode
                when (trainerMode) {
                    TrainerMode.NORMAL -> {
                        OutlinedTextField(
                            value = v2Input,
                            onValueChange = { v2Input = it },
                            placeholder = { Text("Введите V2...") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        OutlinedTextField(
                            value = v3Input,
                            onValueChange = { v3Input = it },
                            placeholder = { Text("Введите V3...") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    else -> {
                        OutlinedTextField(
                            value = v2Input,
                            onValueChange = { v2Input = it },
                            placeholder = { Text("Введите ответ...") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

                // Feedback
                isCorrect?.let { correct ->
                    Spacer(modifier = Modifier.height(8.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = if (correct) MaterialTheme.colorScheme.primaryContainer
                            else MaterialTheme.colorScheme.errorContainer
                        )
                    ) {
                        Text(
                            if (correct) "Отлично! Правильно." else "Ошибка. Правильно: V2: ${currentVerb.v2}, V3: ${currentVerb.v3}",
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                // Buttons
                if (isCorrect == null) {
                    Button(
                        onClick = {
                            val correct = when (trainerMode) {
                                TrainerMode.NORMAL -> {
                                    val v2Match = v2Input.trim().lowercase() in currentVerb.v2.lowercase().split("/").map { it.trim() }
                                    val v3Match = v3Input.trim().lowercase() in currentVerb.v3.lowercase().split("/").map { it.trim() }
                                    v2Match && v3Match
                                }
                                else -> v2Input.trim().lowercase() == currentVerb.v1.lowercase()
                            }
                            isCorrect = correct
                            if (correct) {
                                score++
                                streak++
                                HapticManager.vibrateSuccess(context)
                            } else {
                                mistakeCount++
                                streak = 0
                                HapticManager.vibrateError(context)
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Проверить")
                    }
                } else {
                    Button(
                        onClick = {
                            if (currentIndex < totalQuestions - 1) {
                                currentIndex++
                                v2Input = ""
                                v3Input = ""
                                isCorrect = null
                            } else {
                                showResult = true
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Далее")
                    }
                }
            }
        }
    }
}
