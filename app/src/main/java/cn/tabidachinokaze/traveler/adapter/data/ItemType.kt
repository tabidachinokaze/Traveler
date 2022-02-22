package cn.tabidachinokaze.traveler.adapter.data

abstract class ItemType {
    companion object {
        const val EMPTY = 0
        const val FILE = 1
        const val HEADER = 2
        const val NOT_PERMISSION = 3
    }

    abstract fun getType(): Int
}