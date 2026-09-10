package com.easylearn.easyverbs.ui.screens.prepositions

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.easylearn.easyverbs.data.model.Verb
import com.easylearn.easyverbs.data.repository.VerbRepository
import com.easylearn.easyverbs.util.HapticManager

data class PrepositionQuestion(
    val verb: String,
    val preposition: String,
    val sentence: String
)

val prepositionData = listOf(
    PrepositionQuestion("listen", "to", "Listen to me!"),
    PrepositionQuestion("depend", "on", "It depends on the weather."),
    PrepositionQuestion("wait", "for", "Wait for the bus."),
    PrepositionQuestion("look", "at", "Look at the picture."),
    PrepositionQuestion("think", "about", "Think about your future."),
    PrepositionQuestion("apologize", "for", "I apologize for being late."),
    PrepositionQuestion("believe", "in", "I believe in you."),
    PrepositionQuestion("care", "about", "I care about you."),
    PrepositionQuestion("consist", "of", "The team consists of five members."),
    PrepositionQuestion("dream", "about", "I dream about traveling."),
    PrepositionQuestion("insist", "on", "She insists on paying."),
    PrepositionQuestion("succeed", "in", "He succeeded in passing the exam."),
    PrepositionQuestion("agree", "with", "I agree with you."),
    PrepositionQuestion("apologize", "to", "Apologize to your friend."),
    PrepositionQuestion("congratulate", "on", "Congratulate him on his success."),
    PrepositionQuestion("concentrate", "on", "Concentrate on your work."),
    PrepositionQuestion("participate", "in", "Participate in the competition."),
    PrepositionQuestion("reply", "to", "Reply to the email."),
    PrepositionQuestion("suffer", "from", "She suffers from headaches."),
    PrepositionQuestion("belong", "to", "This book belongs to me.")
)

@Composable
fun PrepositionsScreen(
    repository: VerbRepository
) {
    var isStarted by remember { mutableStateOf(false) }
    var currentIndex by remember { mutableIntStateOf(0) }
    var score by remember { mutableIntStateOf(0) }
    var mistakes by remember { mutableIntStateOf(0) }
    var userInput by remember { mutableStateOf("") }
    var isCorrect by remember { mutableStateOf<Boolean?>(null) }
    var showResult by remember { mutableStateOf(false) }

    val totalQuestions = 10
    val context = LocalContext.current

    val questions = remember {
        prepositionData.shuffled().take(totalQuestions)
    }

    val currentQuestion = questions.getOrNull(currentIndex)

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
                        Icons.Filled.Link,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Предлоги с глаголами", style = MaterialTheme.typography.titleLarge)
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
                            Icons.Filled.Link,
                            contentDescription = null,
                            modifier = Modifier.size(48.dp),
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Text(
                            "Предлоги с глаголами",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            "Вставьте правильный предлог к глаголу",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Button(
                            onClick = {
                                isStarted = true
                                currentIndex = 0
                                score = 0
                                mistakes = 0
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
                                Text("$mistakes", style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.error)
                                Text("Ошибок", style = MaterialTheme.typography.bodySmall)
                            }
                        }
                        Button(onClick = { isStarted = false }) {
                            Text("Тренироваться ещё раз")
                        }
                    }
                }
            }
        } else if (currentQuestion != null) {
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
                    Text("Слово ${currentIndex + 1} из $totalQuestions")
                    Row {
                        Text("✓ $score  ", color = MaterialTheme.colorScheme.primary)
                        Text("✗ $mistakes", color = MaterialTheme.colorScheme.error)
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    "Вставьте правильный предлог:",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(8.dp))

                // Sentence with blank
                Text(
                    currentQuestion.sentence.replace(currentQuestion.preposition, "_____"),
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(24.dp))

                OutlinedTextField(
                    value = userInput,
                    onValueChange = { userInput = it },
                    placeholder = { Text("Введите предлог...") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

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
                            if (correct) "Верно!" else "Ошибка. Правильно: ${currentQuestion.preposition}",
                            modifier = Modifier.padding(16.dp),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                if (isCorrect == null) {
                    Button(
                        onClick = {
                            val correct = userInput.trim().lowercase() == currentQuestion.preposition.lowercase()
                            isCorrect = correct
                            if (correct) {
                                score++
                                HapticManager.vibrateSuccess(context)
                            } else {
                                mistakes++
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
