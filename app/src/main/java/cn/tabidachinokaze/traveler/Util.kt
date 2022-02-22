package cn.tabidachinokaze.traveler

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.DocumentsContract
import androidx.documentfile.provider.DocumentFile

object Util {
    const val YUANSHEN_OFFICIAL = "com.miHoYo.Yuanshen"
    const val YUANSHEN_BILIBILI = "com.miHoYo.ys.bilibili"
    const val GENSHIN_IMPACT = "com.miHoYo.GenshinImpact"
    const val EXTERNAL_STORAGE_URI =
        "content://com.android.externalstorage.documents/tree/primary%3A"
    const val ANDROID_DATA_URI =
        "content://com.android.externalstorage.documents/tree/primary%3AAndroid%2Fdata"

    fun getDirectorySize(documentFile: DocumentFile): Long {
        var size = 0L
        try {
            val fileList = documentFile.listFiles()
            for (file in fileList) {
                size += if (file.isDirectory) {
                    getDirectorySize(file)
                } else {
                    file.length()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return size
    }

    private fun changeToUri(path: String): String {
        var p = path
        if (path.endsWith("/")) {
            p = path.substring(0, path.length - 1)
        }
        val path2 = p.replace("/storage/emulated/0/", "").replace("/", "%2F")
        return "${Util.ANDROID_DATA_URI}/document/primary%3A$path2"
    }

    fun requestIntent(path: String): Intent {
        val uri = changeToUri(path)
        val parse: Uri = Uri.parse(uri)
        val intent = Intent("android.intent.action.OPEN_DOCUMENT_TREE")
        intent.addFlags(
            Intent.FLAG_GRANT_READ_URI_PERMISSION
                    or Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                    or Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
                    or Intent.FLAG_GRANT_PREFIX_URI_PERMISSION
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            intent.putExtra(DocumentsContract.EXTRA_INITIAL_URI, parse)
        }
        return intent
    }
}