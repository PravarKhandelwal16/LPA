package com.example.lpa.security.encryption

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manages encrypted persistence via [EncryptedSharedPreferences].
 *
 * Uses the AndroidX Security crypto library to transparently encrypt keys
 * and values at rest. Backed by a MasterKey securely stored in the
 * Android Keystore.
 *
 * Ideal for storing sensitive lightweight data like auth tokens, symmetric
 * keys, or eUICC challenge metadata that should not be visible in plain XML
 * on rooted devices.
 */
@Singleton
class EncryptedPrefsManager @Inject constructor(
    @ApplicationContext private val context: Context
) {
    /**
     * The lazily initialized instance of the encrypted preferences.
     * Creation requires disk I/O, so it is deferred until first access.
     */
    val sharedPreferences: SharedPreferences by lazy {
        val masterKey = MasterKey.Builder(context)
            .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
            .build()

        EncryptedSharedPreferences.create(
            context,
            PREFS_FILENAME,
            masterKey,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    companion object {
        private const val PREFS_FILENAME = "secure_lpa_prefs"
    }
}
