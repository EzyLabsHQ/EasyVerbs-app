package com.easylearn.easyverbs.ui

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.*
import com.easylearn.easyverbs.EasyVerbsApp
import com.easylearn.easyverbs.data.local.SettingsManager
import com.easylearn.easyverbs.data.repository.VerbRepository
import com.easylearn.easyverbs.ui.navigation.NavItem
import com.easylearn.easyverbs.ui.navigation.Screen
import com.easylearn.easyverbs.ui.navigation.navItems
import com.easylearn.easyverbs.ui.screens.dictionary.DictionaryScreen
import com.easylearn.easyverbs.ui.screens.flashcards.FlashcardsScreen
import com.easylearn.easyverbs.ui.screens.home.HomeScreen
import com.easylearn.easyverbs.ui.screens.letters.LettersScreen
import com.easylearn.easyverbs.ui.screens.mistakes.MistakesScreen
import com.easylearn.easyverbs.ui.screens.speed.SpeedScreen
import com.easylearn.easyverbs.ui.screens.trainer.TrainerScreen
import com.easylearn.easyverbs.ui.screens.settings.SettingsScreen
import com.easylearn.easyverbs.ui.screens.stats.StatsScreen
import com.easylearn.easyverbs.ui.screens.exam.ExamScreen
import com.easylearn.easyverbs.ui.screens.prepositions.PrepositionsScreen
import com.easylearn.easyverbs.util.TtsManager

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EasyVerbsMainScreen() {
    val context = LocalContext.current
    val app = context.applicationContext as EasyVerbsApp
    val repository = remember { VerbRepository(app.database.verbDao()) }
    val settingsManager = remember { SettingsManager(context) }
    val ttsManager = remember {
        TtsManager(context).also {
            androidx.lifecycle.Lifecycle.Event.ON_DESTROY
        }
    }

    DisposableEffect(Unit) {
        onDispose { ttsManager.shutdown() }
    }

    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val darkMode by settingsManager.darkMode.collectAsState(initial = false)

    Scaffold(
        bottomBar = {
            NavigationBar {
                val currentRoute = currentDestination?.route
                navItems.take(6).forEach { item ->
                    val selected = currentRoute == item.screen.route
                    NavigationBarItem(
                        icon = {
                            Icon(
                                if (selected) item.selectedIcon else item.unselectedIcon,
                                contentDescription = item.label
                            )
                        },
                        label = { Text(item.label) },
                        selected = selected,
                        onClick = {
                            navController.navigate(item.screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    repository = repository,
                    settingsManager = settingsManager,
                    onNavigateToScreen = { screen ->
                        navController.navigate(screen.route)
                    },
                    ttsManager = ttsManager
                )
            }

            composable(Screen.Dictionary.route) {
                DictionaryScreen(
                    repository = repository,
                    ttsManager = ttsManager
                )
            }

            composable(Screen.Flashcards.route) {
                FlashcardsScreen(
                    repository = repository,
                    ttsManager = ttsManager
                )
            }

            composable(Screen.Letters.route) {
                LettersScreen(
                    repository = repository,
                    ttsManager = ttsManager,
                    settingsManager = settingsManager
                )
            }

            composable(Screen.Trainer.route) {
                TrainerScreen(
                    repository = repository,
                    ttsManager = ttsManager,
                    settingsManager = settingsManager
                )
            }

            composable(Screen.Mistakes.route) {
                MistakesScreen(
                    repository = repository,
                    ttsManager = ttsManager
                )
            }

            composable(Screen.Speed.route) {
                SpeedScreen(
                    repository = repository,
                    ttsManager = ttsManager,
                    settingsManager = settingsManager
                )
            }

            composable(Screen.Exam.route) {
                ExamScreen(
                    repository = repository,
                    ttsManager = ttsManager,
                    settingsManager = settingsManager
                )
            }

            composable(Screen.Stats.route) {
                StatsScreen(
                    repository = repository,
                    settingsManager = settingsManager
                )
            }

            composable(Screen.Prepositions.route) {
                PrepositionsScreen(
                    repository = repository
                )
            }

            composable(Screen.Settings.route) {
                SettingsScreen(
                    settingsManager = settingsManager
                )
            }
        }
    }
}
