package cn.tabidachinokaze.traveler.adapter.holder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cn.tabidachinokaze.traveler.R
import cn.tabidachinokaze.traveler.adapter.data.ItemType
import cn.tabidachinokaze.traveler.adapter.data.NotPermissionItem

class NotPermissionHolder(private val listener: Listener, private val view: View) :
    RecyclerView.ViewHolder(view) {
    init {
        view.setOnClickListener {
            listener.requestPermission()
        }
    }

    fun bind(item: ItemType) {
        view.findViewById<TextView>(R.id.tip_text).text = (item as NotPermissionItem).text
    }

    interface Listener {
        fun requestPermission()
    }
}