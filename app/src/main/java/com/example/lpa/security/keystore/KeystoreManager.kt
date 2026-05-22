package com.example.lpa.security.keystore

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import java.security.KeyStore
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Manages cryptographic keys within the Android Keystore system.
 *
 * Provides a secure environment where key material never enters the application
 * process space. Used for generating and retrieving symmetric/asymmetric keys
 * for payload encryption, such as securing SM-DP+ server communications or
 * database passphrase encryption.
 */
@Singleton
class KeystoreManager @Inject constructor() {

    init {
        setupKeystore()
    }

    private lateinit var keyStore: KeyStore

    private fun setupKeystore() {
        keyStore = KeyStore.getInstance(ANDROID_KEYSTORE).apply {
            load(null)
        }
    }

    /**
     * Retrieves an existing symmetric key or generates a new one if it doesn't exist.
     *
     * @param alias The unique alias identifying the key in the Keystore.
     * @return The secure [SecretKey].
     */
    fun getOrGenerateSymmetricKey(alias: String): SecretKey {
        if (!keyStore.containsAlias(alias)) {
            generateSymmetricKey(alias)
        }
        val entry = keyStore.getEntry(alias, null) as KeyStore.SecretKeyEntry
        return entry.secretKey
    }

    private fun generateSymmetricKey(alias: String) {
        val keyGenerator = KeyGenerator.getInstance(
            KeyProperties.KEY_ALGORITHM_AES,
            ANDROID_KEYSTORE
        )

        val parameterSpec = KeyGenParameterSpec.Builder(
            alias,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(256)
            // Can be enabled for production system-apps requiring hardware backing
            // .setIsStrongBoxBacked(true)
            .build()

        keyGenerator.init(parameterSpec)
        keyGenerator.generateKey()
    }

    /**
     * Deletes a key from the Keystore.
     */
    fun deleteKey(alias: String) {
        if (keyStore.containsAlias(alias)) {
            keyStore.deleteEntry(alias)
        }
    }

    companion object {
        private const val ANDROID_KEYSTORE = "AndroidKeyStore"
    }
}
