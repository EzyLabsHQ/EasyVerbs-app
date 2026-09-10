package com.easylearn.easyverbs.ui.screens.exam

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

@Composable
fun ExamScreen(
    repository: VerbRepository,
    ttsManager: TtsManager,
    settingsManager: SettingsManager
) {
    val allVerbs by repository.allVerbs.collectAsState(initial = emptyList())
    var isStarted by remember { mutableStateOf(false) }
    var currentIndex by remember { mutableIntStateOf(0) }
    var score by remember { mutableIntStateOf(0) }
    var mistakeCount by remember { mutableIntStateOf(0) }
    var userInput by remember { mutableStateOf("") }
    var isCorrect by remember { mutableStateOf<Boolean?>(null) }
    var showResult by remember { mutableStateOf(false) }
    var questionType by remember { mutableIntStateOf(0) } // 0=forms, 1=translation, 2=v1reverse

    val totalQuestions = 20
    val context = LocalContext.current

    val questions = remember(allVerbs) {
        allVerbs.shuffled().take(totalQuestions)
    }

    val currentVerb = questions.getOrNull(currentIndex)

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
                        Icons.Filled.School,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Финальный экзамен", style = MaterialTheme.typography.titleLarge)
                }
            }
        }

        if (!isStarted) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Card(modifier = Modifier.fillMaxWidth().padding(32.dp)) {
                    Column(
                        modifier = Modifier.padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Icon(
                            Icons.Filled.GraduationCap,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            "Экзамен",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Проверьте все свои знания! Вас ждут вопросы разного типа.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            "$totalQuestions вопросов",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Button(
                            onClick = {
                                isStarted = true
                                currentIndex = 0
                                score = 0
                                mistakeCount = 0
                                showResult = false
                                questionType = (0..2).random()
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Начать экзамен")
                        }
                    }
                }
            }
        } else if (showResult) {
            val percentage = (score * 100) / totalQuestions
            val grade = when {
                percentage >= 95 -> "Идеально!"
                percentage >= 80 -> "Отлично!"
                percentage >= 60 -> "Хорошо"
                percentage >= 40 -> "Удовлетворительно"
                else -> "Нужно больше практики"
            }

            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Card(modifier = Modifier.fillMaxWidth().padding(32.dp)) {
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
                            "Экзамен завершён!",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "$score/$totalQuestions ($percentage%)",
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            grade,
                            style = MaterialTheme.typography.titleLarge
                        )
                        Button(onClick = { isStarted = false }) {
                            Text("Пройти ещё раз")
                        }
                    }
                }
            }
        } else if (currentVerb != null) {
            Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
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
                    Text("✓ $score", color = MaterialTheme.colorScheme.primary)
                }

                Spacer(modifier = Modifier.height(32.dp))

                when (questionType) {
                    0 -> {
                        // Forms question
                        Text(currentVerb.translation, style = MaterialTheme.typography.bodyMedium)
                        Text(
                            currentVerb.v1,
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = userInput,
                            onValueChange = { userInput = it },
                            placeholder = { Text("Введите V2 и V3 через запятую...") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    1 -> {
                        // Translation question
                        Text(
                            currentVerb.v1,
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Как переводится этот глагол?",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = userInput,
                            onValueChange = { userInput = it },
                            placeholder = { Text("Введите перевод...") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    2 -> {
                        // V1 reverse question
                        Text(
                            "${currentVerb.v2} / ${currentVerb.v3}",
                            style = MaterialTheme.typography.headlineLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            "Какая начальная форма (V1)?",
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        OutlinedTextField(
                            value = userInput,
                            onValueChange = { userInput = it },
                            placeholder = { Text("Введите инфинитив...") },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }

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
                            if (correct) "Верно!"
                            else "Ошибка. Правильно: V1: ${currentVerb.v1}, V2: ${currentVerb.v2}, V3: ${currentVerb.v3}",
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                if (isCorrect == null) {
                    Button(
                        onClick = {
                            val input = userInput.trim().lowercase()
                            val correct = when (questionType) {
                                0 -> {
                                    val parts = input.split(",").map { it.trim() }
                                    val v2Options = currentVerb.v2.lowercase().split("/").map { it.trim() }
                                    val v3Options = currentVerb.v3.lowercase().split("/").map { it.trim() }
                                    parts.size >= 2 && parts[0] in v2Options && parts[1] in v3Options
                                }
                                1 -> input in currentVerb.translation.lowercase().split(",").map { it.trim() }
                                2 -> input == currentVerb.v1.lowercase()
                                else -> false
                            }
                            isCorrect = correct
                            if (correct) {
                                score++
                                HapticManager.vibrateSuccess(context)
                            } else {
                                mistakeCount++
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
                                userInput = ""
                                isCorrect = null
                                questionType = (0..2).random()
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
