package com.easylearn.easyverbs.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.ui.graphics.vector.ImageVector

data class NavItem(
    val screen: Screen,
    val label: String,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector
)

val navItems = listOf(
    NavItem(Screen.Home, "Главная", Icons.Filled.Home, Icons.Outlined.Home),
    NavItem(Screen.Dictionary, "Словарь", Icons.Filled.MenuBook, Icons.Outlined.MenuBook),
    NavItem(Screen.Flashcards, "Карточки", Icons.Filled.CreditCard, Icons.Outlined.CreditCard),
    NavItem(Screen.Letters, "Буквы", Icons.Filled.TextFields, Icons.Outlined.TextFields),
    NavItem(Screen.Trainer, "Формы", Icons.Filled.FitnessCenter, Icons.Outlined.FitnessCenter),
    NavItem(Screen.Mistakes, "Ошибки", Icons.Filled.Assignment, Icons.Outlined.Assignment),
    NavItem(Screen.Speed, "Спринт", Icons.Filled.Bolt, Icons.Outlined.Bolt),
    NavItem(Screen.Exam, "Экзамен", Icons.Filled.School, Icons.Outlined.School),
    NavItem(Screen.Stats, "Статистика", Icons.Filled.BarChart, Icons.Outlined.BarChart),
    NavItem(Screen.Prepositions, "Предлоги", Icons.Filled.Link, Icons.Outlined.Link),
    NavItem(Screen.Settings, "Настройки", Icons.Filled.Settings, Icons.Outlined.Settings)
)
