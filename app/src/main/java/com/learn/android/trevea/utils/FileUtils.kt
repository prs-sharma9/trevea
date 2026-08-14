package com.learn.android.trevea.utils

import android.content.Context
import android.net.Uri
import android.util.Log
import java.io.File
import java.io.FileOutputStream

private const val tag = "Trevea: FileUtils"

fun saveImageToInternalStorage(context: Context, uri: Uri): String? {
    try {
        val inputStream = context.contentResolver.openInputStream(uri)
        val fileName = "trevea_profile_picture_${System.currentTimeMillis()}.jpg"
        val file = File(context.filesDir, fileName)

//        if (file.exists()) file.delete()

        inputStream?.use { input ->
            FileOutputStream(file).use {
                output -> input.copyTo(output)
            }
        }
        return file.absolutePath
    } catch ( e: Exception ) {
        Log.e(tag, "Error: Failed to save file: ${e.message}")
        return null
    }
}