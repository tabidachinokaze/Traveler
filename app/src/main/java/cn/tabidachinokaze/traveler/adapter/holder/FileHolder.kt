package cn.tabidachinokaze.traveler.adapter.holder

import android.app.AlertDialog
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.*
import androidx.documentfile.provider.DocumentFile
import androidx.recyclerview.widget.RecyclerView
import cn.tabidachinokaze.traveler.R
import cn.tabidachinokaze.traveler.Util
import cn.tabidachinokaze.traveler.adapter.data.FileItem
import cn.tabidachinokaze.traveler.adapter.data.ItemType

class FileHolder(private val listener: Listener, private val view: View) :
    RecyclerView.ViewHolder(view) {
    private lateinit var documentFile: DocumentFile
    fun bind(item: ItemType, onBindingIcon: (name: String?) -> Drawable?) {
        val menuMaps = mapOf(
            Pair(Util.YUANSHEN_OFFICIAL, R.id.item_change_official),
            Pair(Util.YUANSHEN_BILIBILI, R.id.item_change_bilibili),
            Pair(Util.GENSHIN_IMPACT, R.id.item_change_international)
        )
        documentFile = (item as FileItem).documentFile
        view.apply {
            updateView(onBindingIcon(documentFile.name))
            findViewById<ImageButton>(R.id.item_button).setOnClickListener {
                val popupMenu = PopupMenu(context, it).apply {
                    inflate(R.menu.item_file)
                    menuMaps[documentFile.name]?.let { name ->
                        menu.findItem(name).isVisible = false
                    }
                    setOnMenuItemClickListener { menuItem ->
                        when (menuItem.itemId) {
                            R.id.item_open -> {
                                listener.openApp(documentFile.name)
                            }
                            R.id.item_change_official, R.id.item_change_bilibili, R.id.item_change_international -> {
                                menuMaps.forEach { entry ->
                                    if (entry.value == menuItem.itemId) {
                                        documentFile.renameTo(entry.key)
                                        updateView(onBindingIcon(documentFile.name))
                                    }
                                }
                            }
                            R.id.item_delete -> {
                                AlertDialog.Builder(context).apply {
                                    setTitle(context.getString(R.string.delete))
                                    setMessage(context.getString(R.string.delete_message))
                                    setPositiveButton(context.getString(R.string.no)) { dialog, _ ->
                                        dialog.dismiss()
                                    }
                                    setNeutralButton(context.getString(R.string.yes)) { _, _ ->
                                        documentFile.delete()
                                        listener.deleteFile(item)
                                        Toast.makeText(context, context.getString(R.string.deleted_message), Toast.LENGTH_SHORT).show()
                                    }
                                }.show()
                            }
                        }
                        true
                    }
                }
                popupMenu.show()
            }
        }
    }

    private fun updateView(icon: Drawable?) {
        view.apply {
            findViewById<ImageView>(R.id.item_icon).setImageDrawable(icon)
            findViewById<TextView>(R.id.item_name).text = documentFile.name
            findViewById<TextView>(R.id.item_size).text = documentFile.length().toString()
        }
    }

    interface Listener {
        fun openApp(packageName: String?)
        fun deleteFile(fileItem: FileItem)
    }
}