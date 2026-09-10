package com.easylearn.easyverbs.ui.screens.flashcards

import androidx.compose.animation.core.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.easylearn.easyverbs.data.model.Verb
import com.easylearn.easyverbs.data.model.VerbGroup
import com.easylearn.easyverbs.data.repository.VerbRepository
import com.easylearn.easyverbs.util.TtsManager
import kotlinx.coroutines.delay

@Composable
fun FlashcardsScreen(
    repository: VerbRepository,
    ttsManager: TtsManager
) {
    val allVerbs by repository.allVerbs.collectAsState(initial = emptyList())
    var currentIndex by remember { mutableIntStateOf(0) }
    var isFlipped by remember { mutableStateOf(false) }
    var selectedGroup by remember { mutableStateOf<String?>(null) }

    val filteredVerbs = remember(allVerbs, selectedGroup) {
        val list = if (selectedGroup == null) allVerbs
        else allVerbs.filter { it.group.name == selectedGroup }
        list.shuffled()
    }

    val currentVerb = filteredVerbs.getOrNull(currentIndex)

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
                        Icons.Filled.CreditCard,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Флеш-карточки", style = MaterialTheme.typography.titleLarge)
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    FilterChip(
                        selected = selectedGroup == null,
                        onClick = { selectedGroup = null },
                        label = { Text("Все") }
                    )
                    VerbGroup.entries.forEach { group ->
                        FilterChip(
                            selected = selectedGroup == group.name,
                            onClick = { selectedGroup = if (selectedGroup == group.name) null else group.name },
                            label = { Text(group.label) }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Card
        if (currentVerb != null) {
            val rotation by animateFloatAsState(
                targetValue = if (isFlipped) 180f else 0f,
                animationSpec = tween(durationMillis = 400, easing = FastOutSlowInEasing),
                label = "flip"
            )

            Box(
                modifier = Modifier
                    .size(300.dp, 200.dp)
                    .graphicsLayer {
                        rotationY = rotation
                        cameraDistance = 12f * density
                    }
                    .clickable { isFlipped = !isFlipped },
                contentAlignment = Alignment.Center
            ) {
                if (rotation <= 90f) {
                    // Front
                    Card(
                        modifier = Modifier.fillMaxSize(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize().padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                currentVerb.v1,
                                style = MaterialTheme.typography.headlineLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                currentVerb.translation,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                "Нажмите, чтобы перевернуть",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                } else {
                    // Back
                    Card(
                        modifier = Modifier.fillMaxSize(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.primary
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize().padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            Text(
                                "V2 (Past Simple)",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                            )
                            Text(
                                currentVerb.v2,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                "V3 (Past Participle)",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.7f)
                            )
                            Text(
                                currentVerb.v3,
                                style = MaterialTheme.typography.headlineMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Controls
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        if (currentIndex > 0) {
                            currentIndex--
                            isFlipped = false
                        }
                    },
                    enabled = currentIndex > 0
                ) {
                    Icon(Icons.Filled.ChevronLeft, contentDescription = "Назад")
                }

                IconButton(onClick = { ttsManager.speak(currentVerb.v1) }) {
                    Icon(Icons.Filled.VolumeUp, contentDescription = "Прослушать")
                }

                Text(
                    "${currentIndex + 1} / ${filteredVerbs.size}",
                    style = MaterialTheme.typography.bodyMedium
                )

                IconButton(
                    onClick = {
                        if (currentIndex < filteredVerbs.size - 1) {
                            currentIndex++
                            isFlipped = false
                        }
                    },
                    enabled = currentIndex < filteredVerbs.size - 1
                ) {
                    Icon(Icons.Filled.ChevronRight, contentDescription = "Вперёд")
                }
            }
        } else {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text("Нет глаголов в этой группе")
            }
        }
    }
}
