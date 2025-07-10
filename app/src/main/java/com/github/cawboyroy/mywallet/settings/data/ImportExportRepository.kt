package com.github.cawboyroy.mywallet.settings.data

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import com.github.cawboyroy.mywallet.R
import com.github.cawboyroy.mywallet.main.data.FinancialRecordEntity
import com.github.cawboyroy.mywallet.main.data.FinancialRecordsDao
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

interface ImportExportRepository {

    suspend fun import(password: String, uri: Uri): Boolean

    suspend fun export(password: String): String

    class Base @Inject constructor(
        private val dao: FinancialRecordsDao,
        @ApplicationContext private val context: Context,
        private val encryption: Encryption,
    ) : ImportExportRepository {

        private val gson: Gson = Gson()
        private val type = object : TypeToken<List<FinancialRecordEntity>>() {}.type
        private val formatter = DateTimeFormatter.ofPattern("dd-MMM-yyyy-HH-mm")

        override suspend fun import(password: String, uri: Uri): Boolean = try {
            val encryptedFromOuterFile = context.contentResolver.openInputStream(uri)!!.readBytes()
            val decrypted = encryption.decrypted(password, encryptedFromOuterFile)
            val transactions: List<FinancialRecordEntity> = gson.fromJson(decrypted, type)
            dao.clearAll()
            dao.addAll(transactions)
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }

        override suspend fun export(password: String): String {
            val currentDateTime = LocalDateTime.now()
            val formattedDateTime = currentDateTime.format(formatter)
            val transactions = dao.all()
            val json = gson.toJson(transactions)
            val appName = context.getString(R.string.app_name)
            val file = File(context.cacheDir, "$appName-backup-$formattedDateTime.json")
            file.writeBytes(encryption.encrypted(password, json))
            val uri: Uri = FileProvider.getUriForFile(
                context, "${context.packageName}.provider", file
            )
            return uri.toString()
        }
    }
}