package cn.tabidachinokaze.traveler.adapter.holder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cn.tabidachinokaze.traveler.R
import cn.tabidachinokaze.traveler.adapter.data.EmptyItem
import cn.tabidachinokaze.traveler.adapter.data.ItemType

class EmptyHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    fun bind(item: ItemType) {
        view.findViewById<TextView>(R.id.tip_text).text = (item as EmptyItem).text
    }
}