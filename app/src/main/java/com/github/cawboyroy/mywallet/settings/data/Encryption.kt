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

    suspend fun encrypted(password: String, notEncryptedSource: String): ByteArray

    suspend fun decrypted(password: String, encryptedEarlier: ByteArray): String

    class Base @Inject constructor() : Encryption {

        override suspend fun encrypted(password: String, notEncryptedSource: String): ByteArray {
            val salt = ByteArray(16).also { SecureRandom().nextBytes(it) }
            val iv = ByteArray(12).also { SecureRandom().nextBytes(it) }
            val key = deriveKeyFromPassword(password, salt)

            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            cipher.init(Cipher.ENCRYPT_MODE, key, GCMParameterSpec(128, iv))
            val encrypted = cipher.doFinal(notEncryptedSource.toByteArray(Charsets.UTF_8))

            // saving: [salt (16)] + [iv (12)] + [encrypted]
            return salt + iv + encrypted
        }

        override suspend fun decrypted(password: String, encryptedEarlier: ByteArray): String {
            val salt = encryptedEarlier.copyOfRange(0, 16)
            val iv = encryptedEarlier.copyOfRange(16, 28)
            val encrypted = encryptedEarlier.copyOfRange(28, encryptedEarlier.size)

            val key = deriveKeyFromPassword(password, salt)
            val cipher = Cipher.getInstance("AES/GCM/NoPadding")
            cipher.init(Cipher.DECRYPT_MODE, key, GCMParameterSpec(128, iv))
            val decrypted = cipher.doFinal(encrypted)

            return String(decrypted, Charsets.UTF_8)
        }

        private fun deriveKeyFromPassword(password: String, salt: ByteArray): SecretKey {
            val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
            val spec = PBEKeySpec(password.toCharArray(), salt, 100_000, 256)
            val tmp = factory.generateSecret(spec)
            return SecretKeySpec(tmp.encoded, "AES")
        }
    }
}