package com.easylearn.easyverbs.ui.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home")
    data object Dictionary : Screen("dictionary")
    data object Flashcards : Screen("flashcards")
    data object Letters : Screen("letters")
    data object Trainer : Screen("trainer")
    data object Mistakes : Screen("mistakes")
    data object Speed : Screen("speed")
    data object Exam : Screen("exam")
    data object Stats : Screen("stats")
    data object Prepositions : Screen("prepositions")
    data object Settings : Screen("settings")
}
