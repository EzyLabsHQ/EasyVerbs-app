package com.easylearn.easyverbs.ui.screens.dictionary

import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.easylearn.easyverbs.data.model.Verb
import com.easylearn.easyverbs.data.model.VerbGroup
import com.easylearn.easyverbs.data.repository.VerbRepository
import com.easylearn.easyverbs.util.TtsManager
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DictionaryScreen(
    repository: VerbRepository,
    ttsManager: TtsManager
) {
    val allVerbs by repository.allVerbs.collectAsState(initial = emptyList())
    val scope = rememberCoroutineScope()
    var searchQuery by remember { mutableStateOf("") }
    var selectedGroup by remember { mutableStateOf<String?>(null) }
    var showFavoritesOnly by remember { mutableStateOf(false) }
    var selectedVerb by remember { mutableStateOf<Verb?>(null) }

    val filteredVerbs = remember(allVerbs, searchQuery, selectedGroup, showFavoritesOnly) {
        allVerbs.filter { verb ->
            val matchesSearch = searchQuery.isBlank() ||
                    verb.v1.contains(searchQuery, ignoreCase = true) ||
                    verb.v2.contains(searchQuery, ignoreCase = true) ||
                    verb.v3.contains(searchQuery, ignoreCase = true) ||
                    verb.translation.contains(searchQuery, ignoreCase = true)
            val matchesGroup = selectedGroup == null || verb.group.name == selectedGroup
            val matchesFav = !showFavoritesOnly || verb.isFavorite
            matchesSearch && matchesGroup && matchesFav
        }
    }

    Column(modifier = Modifier.fillMaxSize()) {
        // Header
        Surface(
            modifier = Modifier.fillMaxWidth(),
            tonalElevation = 2.dp
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        Icons.Filled.MenuBook,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        "Словарь глаголов",
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(onClick = { showFavoritesOnly = !showFavoritesOnly }) {
                        Icon(
                            if (showFavoritesOnly) Icons.Filled.Star else Icons.Filled.StarBorder,
                            contentDescription = "Избранное",
                            tint = if (showFavoritesOnly) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Search
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Поиск...") },
                    leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
                    trailingIcon = {
                        if (searchQuery.isNotEmpty()) {
                            IconButton(onClick = { searchQuery = "" }) {
                                Icon(Icons.Filled.Clear, contentDescription = "Очистить")
                            }
                        }
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Group filters
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    FilterChip(
                        selected = selectedGroup == null,
                        onClick = { selectedGroup = null },
                        label = { Text("Все") }
                    )
                    VerbGroup.entries.forEach { group ->
                        FilterChip(
                            selected = selectedGroup == group.name,
                            onClick = {
                                selectedGroup = if (selectedGroup == group.name) null else group.name
                            },
                            label = { Text(group.label) }
                        )
                    }
                }
            }
        }

        // Verb list
        if (filteredVerbs.isEmpty()) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    if (searchQuery.isNotBlank()) "Ничего не найдено" else "Нет глаголов",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(bottom = 16.dp)
            ) {
                items(filteredVerbs, key = { it.v1 }) { verb ->
                    VerbRow(
                        verb = verb,
                        onFavoriteClick = {
                            scope.launch { repository.toggleFavorite(verb.v1) }
                        },
                        onSpeak = {
                            ttsManager.speak(verb.v1)
                        },
                        onClick = { selectedVerb = verb }
                    )
                }
            }
        }
    }

    // Verb detail dialog
    selectedVerb?.let { verb ->
        VerbDetailDialog(
            verb = verb,
            onDismiss = { selectedVerb = null },
            onFavoriteClick = {
                scope.launch { repository.toggleFavorite(verb.v1) }
            },
            onSpeak = { ttsManager.speak(verb.v1) }
        )
    }
}

@Composable
fun VerbRow(
    verb: Verb,
    onFavoriteClick: () -> Unit,
    onSpeak: () -> Unit,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onFavoriteClick,
            modifier = Modifier.size(32.dp)
        ) {
            Icon(
                if (verb.isFavorite) Icons.Filled.Star else Icons.Filled.StarBorder,
                contentDescription = if (verb.isFavorite) "Убрать из избранного" else "В избранное",
                tint = if (verb.isFavorite) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(18.dp)
            )
        }

        Column(modifier = Modifier.weight(1f)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    verb.v1,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.width(6.dp))
                IconButton(
                    onClick = onSpeak,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        Icons.Filled.VolumeUp,
                        contentDescription = "Прослушать",
                        modifier = Modifier.size(14.dp),
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }
            Text(
                "${verb.v2} → ${verb.v3}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                verb.translation,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        // Group badge
        Surface(
            shape = MaterialTheme.shapes.small,
            color = MaterialTheme.colorScheme.secondaryContainer,
            modifier = Modifier.padding(start = 8.dp)
        ) {
            Text(
                verb.group.label,
                style = MaterialTheme.typography.labelSmall,
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
            )
        }
    }
}

@Composable
fun VerbDetailDialog(
    verb: Verb,
    onDismiss: () -> Unit,
    onFavoriteClick: () -> Unit,
    onSpeak: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(verb.v1, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f))
                IconButton(onClick = onSpeak) {
                    Icon(Icons.Filled.VolumeUp, contentDescription = "Прослушать")
                }
                IconButton(onClick = onFavoriteClick) {
                    Icon(
                        if (verb.isFavorite) Icons.Filled.Star else Icons.Filled.StarBorder,
                        contentDescription = "Избранное",
                        tint = if (verb.isFavorite) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                DetailRow("V1 (Infinitive)", verb.v1)
                DetailRow("V2 (Past Simple)", verb.v2)
                DetailRow("V3 (Past Participle)", verb.v3)
                DetailRow("Перевод", verb.translation)
                DetailRow("Группа", verb.group.label)
                DetailRow("Сложность", when (verb.complexity) {
                    1 -> "Базовый"
                    2 -> "Средний"
                    3 -> "Сложный"
                    else -> "Неизвестно"
                })
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Закрыть")
            }
        }
    )
}

@Composable
fun DetailRow(label: String, value: String) {
    Column {
        Text(
            label,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            value,
            style = MaterialTheme.typography.bodyLarge,
            fontWeight = FontWeight.Medium
        )
    }
}
