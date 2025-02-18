package com.example.myapplication.ui

class AgeUiItem(
    val value: Int,
    val selected: Boolean,
    val onClick: () -> Unit,
) {
    val uid: Int get() = value

    override fun hashCode(): Int {
        return value.hashCode() + selected.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return other is AgeUiItem &&
                other.value == value &&
                other.selected == selected
    }
}
