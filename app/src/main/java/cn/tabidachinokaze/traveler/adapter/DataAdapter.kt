package cn.tabidachinokaze.traveler.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import cn.tabidachinokaze.traveler.R
import cn.tabidachinokaze.traveler.adapter.data.FileItem
import cn.tabidachinokaze.traveler.adapter.data.HeaderItem
import cn.tabidachinokaze.traveler.adapter.data.ItemType
import cn.tabidachinokaze.traveler.adapter.holder.EmptyHolder
import cn.tabidachinokaze.traveler.adapter.holder.FileHolder
import cn.tabidachinokaze.traveler.adapter.holder.HeaderHolder
import cn.tabidachinokaze.traveler.adapter.holder.NotPermissionHolder

class DataAdapter(
    private val context: Context,
    private val items: MutableList<ItemType>,
    private val iconMaps: Map<String, Drawable>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), FileHolder.Listener {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ItemType.HEADER -> {
                val view = LayoutInflater.from(context).inflate(R.layout.item_header, parent, false)
                HeaderHolder(view)
            }
            ItemType.EMPTY -> {
                val view = LayoutInflater.from(context).inflate(R.layout.item_tip, parent, false)
                EmptyHolder(view)
            }
            ItemType.NOT_PERMISSION -> {
                val view = LayoutInflater.from(context).inflate(R.layout.item_tip, parent, false)
                NotPermissionHolder(context as NotPermissionHolder.Listener, view)
            }
            else -> {
                val view = LayoutInflater.from(context).inflate(R.layout.item_file, parent, false)
                FileHolder(this, view)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].getType()
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = items[position]
        when (getItemViewType(position)) {
            ItemType.HEADER -> {
                (holder as HeaderHolder).bind(item)
            }
            ItemType.EMPTY -> {
                (holder as EmptyHolder).bind(item)
            }
            ItemType.NOT_PERMISSION -> {
                (holder as NotPermissionHolder).bind(item)
            }
            else -> {
                (holder as FileHolder).bind(item) {
                    iconMaps[it]
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun openApp(packageName: String?) {
        packageName?.let {
            val intent = context.packageManager.getLaunchIntentForPackage(it)
            context.startActivity(intent)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun deleteFile(fileItem: FileItem) {
        items.remove(fileItem)
        var header = 0
        var n = 0
        for ((i, item) in items.withIndex()) {
            if (item.getType() == ItemType.FILE) {
                n++
            }
            if (item.getType() == ItemType.HEADER) {
                header = i
            }
        }
        (items[header] as HeaderItem).count = n
        notifyDataSetChanged()
    }
}