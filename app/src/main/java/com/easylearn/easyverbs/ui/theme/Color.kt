package com.easylearn.easyverbs.ui.theme

import androidx.compose.ui.graphics.Color

val Indigo50 = Color(0xFFEEF2FF)
val Indigo100 = Color(0xFFE0E7FF)
val Indigo200 = Color(0xFFC7D2FE)
val Indigo300 = Color(0xFFA5B4FC)
val Indigo400 = Color(0xFF818CF8)
val Indigo500 = Color(0xFF6366F1)
val Indigo600 = Color(0xFF4F46E5)
val Indigo700 = Color(0xFF4338CA)
val Indigo800 = Color(0xFF3730A3)
val Indigo900 = Color(0xFF312E81)

val Purple500 = Color(0xFF7C3AED)
val Purple600 = Color(0xFF6D28D9)

val Emerald500 = Color(0xFF10B981)
val Emerald600 = Color(0xFF059669)

val Rose500 = Color(0xFFF43F5E)
val Rose600 = Color(0xFFE11D48)

val Amber500 = Color(0xFFF59E0B)
val Amber600 = Color(0xFFD97706)

val Sky500 = Color(0xFF06B6D4)
val Sky600 = Color(0xFF0284C7)

val Slate50 = Color(0xFFF8FAFC)
val Slate100 = Color(0xFFF1F5F9)
val Slate200 = Color(0xFFE2E8F0)
val Slate300 = Color(0xFFCBD5E1)
val Slate400 = Color(0xFF94A3B8)
val Slate500 = Color(0xFF64748B)
val Slate600 = Color(0xFF475569)
val Slate700 = Color(0xFF334155)
val Slate800 = Color(0xFF1E293B)
val Slate900 = Color(0xFF0F172A)

val DarkBackground = Color(0xFF0B1120)
val DarkSurface = Color(0xFF131C31)
val DarkSurfaceVariant = Color(0xFF1A2540)
val DarkOnBackground = Color(0xFFF0F4FF)
val DarkOnSurface = Color(0xFF94A3B8)

data class AccentPreset(
    val from: Color,
    val to: Color,
    val label: String
)

object AccentPresets {
    val indigo = AccentPreset(Indigo600, Purple500, "Indigo")
    val emerald = AccentPreset(Emerald600, Emerald500, "Emerald")
    val rose = AccentPreset(Rose600, Rose500, "Rose")
    val amber = AccentPreset(Amber600, Amber500, "Amber")
    val sky = AccentPreset(Sky600, Sky500, "Sky")
    val purple = AccentPreset(Purple600, Color(0xFFA855F7), "Violet")
    val slate = AccentPreset(Slate600, Slate500, "Slate")

    val all = listOf(indigo, emerald, rose, amber, sky, purple, slate)
}
