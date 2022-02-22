package cn.tabidachinokaze.traveler.adapter.holder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import cn.tabidachinokaze.traveler.R
import cn.tabidachinokaze.traveler.adapter.data.HeaderItem
import cn.tabidachinokaze.traveler.adapter.data.ItemType

class HeaderHolder(private val view: View) : RecyclerView.ViewHolder(view) {
    fun bind(item: ItemType) {
        view.findViewById<TextView>(R.id.header_title).text = (item as HeaderItem).title
        view.findViewById<TextView>(R.id.header_count).text = item.count.toString()
    }
}