package cn.tabidachinokaze.traveler.adapter.data

data class HeaderItem(val title: String, var count: Int) : ItemType() {
    override fun getType(): Int {
        return HEADER
    }
}