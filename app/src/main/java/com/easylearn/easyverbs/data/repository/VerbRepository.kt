package com.easylearn.easyverbs.data.repository

import com.easylearn.easyverbs.data.local.VerbDao
import com.easylearn.easyverbs.data.model.Verb
import kotlinx.coroutines.flow.Flow

class VerbRepository(private val verbDao: VerbDao) {

    val allVerbs: Flow<List<Verb>> = verbDao.getAllVerbs()

    val favoriteVerbs: Flow<List<Verb>> = verbDao.getFavoriteVerbs()

    val customVerbs: Flow<List<Verb>> = verbDao.getCustomVerbs()

    val mistakeVerbs: Flow<List<Verb>> = verbDao.getMistakesVerbs()

    suspend fun getVerb(v1: String): Verb? = verbDao.getVerbByV1(v1)

    suspend fun insertVerb(verb: Verb) = verbDao.insertVerb(verb)

    suspend fun insertAll(verbs: List<Verb>) = verbDao.insertAll(verbs)

    suspend fun updateVerb(verb: Verb) = verbDao.updateVerb(verb)

    suspend fun deleteVerb(verb: Verb) = verbDao.deleteVerb(verb)

    suspend fun toggleFavorite(v1: String) {
        val verb = verbDao.getVerbByV1(v1) ?: return
        verbDao.setFavorite(v1, !verb.isFavorite)
    }

    suspend fun incrementError(v1: String) = verbDao.incrementError(v1)

    suspend fun markSeen(v1: String) = verbDao.markSeen(v1)

    suspend fun setConfidence(v1: String, confidence: Int) = verbDao.setConfidence(v1, confidence)

    suspend fun getVerbCount(): Int = verbDao.getVerbCount()

    suspend fun deleteAllCustomVerbs() = verbDao.deleteAllCustomVerbs()
}
