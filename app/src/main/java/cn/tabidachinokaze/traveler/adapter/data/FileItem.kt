package cn.tabidachinokaze.traveler.adapter.data

import androidx.documentfile.provider.DocumentFile

data class FileItem(val documentFile: DocumentFile) : ItemType() {
    override fun getType(): Int {
        return FILE
    }
}