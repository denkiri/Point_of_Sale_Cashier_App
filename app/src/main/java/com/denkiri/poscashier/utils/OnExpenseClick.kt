package com.denkiri.poscashier.utils

interface OnExpenseClick {
    fun selected(pos: Int)
    fun delete(pos: Int)
    fun onClickListener(position1: Int)
}