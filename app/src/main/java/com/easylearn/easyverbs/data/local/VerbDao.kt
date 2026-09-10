package com.easylearn.easyverbs.data.local

import androidx.room.*
import com.easylearn.easyverbs.data.model.Verb
import kotlinx.coroutines.flow.Flow

@Dao
interface VerbDao {

    @Query("SELECT * FROM verbs ORDER BY v1 ASC")
    fun getAllVerbs(): Flow<List<Verb>>

    @Query("SELECT * FROM verbs WHERE v1 = :v1")
    suspend fun getVerbByV1(v1: String): Verb?

    @Query("SELECT * FROM verbs WHERE isFavorite = 1 ORDER BY v1 ASC")
    fun getFavoriteVerbs(): Flow<List<Verb>>

    @Query("SELECT * FROM verbs WHERE isCustom = 1 ORDER BY v1 ASC")
    fun getCustomVerbs(): Flow<List<Verb>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertVerb(verb: Verb)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(verbs: List<Verb>)

    @Update
    suspend fun updateVerb(verb: Verb)

    @Delete
    suspend fun deleteVerb(verb: Verb)

    @Query("UPDATE verbs SET isFavorite = :isFavorite WHERE v1 = :v1")
    suspend fun setFavorite(v1: String, isFavorite: Boolean)

    @Query("UPDATE verbs SET errorCount = errorCount + 1, lastErrorTimestamp = :timestamp WHERE v1 = :v1")
    suspend fun incrementError(v1: String, timestamp: Long = System.currentTimeMillis())

    @Query("UPDATE verbs SET lastSeenTimestamp = :timestamp WHERE v1 = :v1")
    suspend fun markSeen(v1: String, timestamp: Long = System.currentTimeMillis())

    @Query("UPDATE verbs SET confidence = :confidence WHERE v1 = :v1")
    suspend fun setConfidence(v1: String, confidence: Int)

    @Query("SELECT * FROM verbs WHERE errorCount > 0 ORDER BY errorCount DESC")
    fun getMistakesVerbs(): Flow<List<Verb>>

    @Query("SELECT COUNT(*) FROM verbs")
    suspend fun getVerbCount(): Int

    @Query("DELETE FROM verbs WHERE isCustom = 1")
    suspend fun deleteAllCustomVerbs()
}
