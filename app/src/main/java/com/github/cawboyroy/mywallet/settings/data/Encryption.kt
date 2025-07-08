package com.github.cawboyroy.mywallet.settings.data

import java.security.SecureRandom
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec
import javax.inject.Inject

interface Encryption {
    suspend fun encrypted(notEncryptedSource: String): ByteArray

    suspend fun decrypted(encryptedEarlier: ByteArray): String

    class Base @Inject constructor(
    ) : Encryption {

        private val password = "securePass123"//todo hardcoded

        override suspend fun encrypted(notEncryptedSource: String): ByteArray {
            return encryptJson(notEncryptedSource)
        }

        override suspend fun decrypted(encryptedEarlier: ByteArray): String {
            return decryptJson(encryptedEarlier)
        }

        private fun deriveKeyFromPassword(salt: ByteArray): SecretKey {
            val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
            val spec = PBEKeySpec(password.toCharArray(), salt, 100_000, 256)
            val tmp = factory.generateSecret(spec)
            return SecretKeySpec(tmp.encoded, "AES")
        }

        private fun encryptJson(json: String): ByteArray {
            val salt = ByteArray(16).also { SecureRandom().nextBytes(it) }
            val iv = ByteArray(12).also { SecureRandom().nextBytes(it) }
            val key = deriveKeyFromPassword(salt)

            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            cipher.init(Cipher.ENCRYPT_MODE, key, GCMParameterSpec(128, iv))
            val encrypted = cipher.doFinal(json.toByteArray(Charsets.UTF_8))

            // saving: [salt (16)] + [iv (12)] + [encrypted]
            return salt + iv + encrypted
        }

        private fun decryptJson(data: ByteArray): String {
            val salt = data.copyOfRange(0, 16)
            val iv = data.copyOfRange(16, 28)
            val encrypted = data.copyOfRange(28, data.size)

            val key = deriveKeyFromPassword(salt)
            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            cipher.init(Cipher.DECRYPT_MODE, key, GCMParameterSpec(128, iv))
            val decrypted = cipher.doFinal(encrypted)

            return String(decrypted, Charsets.UTF_8)
        }
    }
}