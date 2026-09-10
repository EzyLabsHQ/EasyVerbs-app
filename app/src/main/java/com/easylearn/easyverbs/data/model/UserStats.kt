package com.easylearn.easyverbs.data.model

data class UserStats(
    val sessionsCompleted: Int = 0,
    val totalCorrect: Int = 0,
    val totalQuestions: Int = 0,
    val bestStreak: Int = 0,
    val verbsLearned: List<String> = emptyList(),
    val speedHighScore: Int = 0,
    val activityLog: Map<String, Int> = emptyMap()
) {
    val accuracy: Int
        get() = if (totalQuestions > 0) (totalCorrect * 100) / totalQuestions else 0

    val learnedCount: Int
        get() = verbsLearned.size
}
