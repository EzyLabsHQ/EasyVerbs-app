package com.easylearn.easyverbs.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "verbs")
data class Verb(
    @PrimaryKey val v1: String,
    val v2: String,
    val v3: String,
    val translation: String,
    val translationEs: String? = null,
    val translationDe: String? = null,
    val translationFr: String? = null,
    val translationPt: String? = null,
    val complexity: Int = 1,
    val isCustom: Boolean = false,
    val isFavorite: Boolean = false,
    val errorCount: Int = 0,
    val lastErrorTimestamp: Long? = null,
    val lastSeenTimestamp: Long? = null,
    val confidence: Int = 0
) {
    val group: VerbGroup
        get() = classifyVerb()

    private fun classifyVerb(): VerbGroup {
        val v2Base = v2.split("/").first().lowercase().trim()
        val v3Base = v3.split("/").first().lowercase().trim()
        val v1Base = v1.lowercase().trim()

        return when {
            v1Base == v2Base && v2Base == v3Base -> VerbGroup.AAA
            v2Base == v3Base -> VerbGroup.ABB
            v1Base == v3Base -> VerbGroup.ABA
            else -> VerbGroup.ABC
        }
    }
}

enum class VerbGroup(val label: String) {
    AAA("AAA"),
    ABB("ABB"),
    ABA("ABA"),
    ABC("ABC")
}
