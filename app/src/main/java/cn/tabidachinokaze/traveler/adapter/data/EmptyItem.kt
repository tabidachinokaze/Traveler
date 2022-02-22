package cn.tabidachinokaze.traveler.adapter.data

data class EmptyItem(val text: String) : ItemType() {
    override fun getType(): Int {
        return EMPTY
    }
}