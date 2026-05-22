package com.example.lpa.security.securestorage

import android.content.SharedPreferences
import com.example.lpa.core.result.Result
import com.example.lpa.security.encryption.EncryptedPrefsManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Repository for storing and retrieving sensitive data at rest.
 *
 * Wraps the [EncryptedPrefsManager] behind a coroutine-safe, Result-based
 * repository boundary. Ensures all cryptographic I/O happens off the main thread.
 *
 * Use this repository to persist sensitive configuration, auth tokens, or
 * derived cryptographic material that shouldn't be accessible to rooted
 * file-system scans.
 */
@Singleton
class SecureStorageRepository @Inject constructor(
    private val encryptedPrefsManager: EncryptedPrefsManager
) {
    private val prefs: SharedPreferences
        get() = encryptedPrefsManager.sharedPreferences

    /**
     * securely saves a string value against the given key.
     */
    suspend fun saveSecureString(key: String, value: String): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {
                prefs.edit().putString(key, value).commit() // commit() is synchronous to ensure write on IO thread
                Result.Success(Unit)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }

    /**
     * Retrieves a securely stored string.
     * Returns null if the key doesn't exist.
     */
    suspend fun getSecureString(key: String): Result<String?> =
        withContext(Dispatchers.IO) {
            try {
                val value = prefs.getString(key, null)
                Result.Success(value)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }

    /**
     * Securely deletes the value associated with the given key.
     */
    suspend fun clearSecureValue(key: String): Result<Unit> =
        withContext(Dispatchers.IO) {
            try {
                prefs.edit().remove(key).commit()
                Result.Success(Unit)
            } catch (e: Exception) {
                Result.Error(e)
            }
        }
}
