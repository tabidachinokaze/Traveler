package cn.tabidachinokaze.traveler.adapter.data

data class NotPermissionItem(val text: String) : ItemType() {
    override fun getType(): Int {
        return NOT_PERMISSION
    }
}