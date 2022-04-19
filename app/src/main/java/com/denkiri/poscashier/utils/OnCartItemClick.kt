package com.denkiri.poscashier.utils

interface OnCartItemClick {
    fun delete(pos: Int)
    fun increase(pos: Int)
    fun decrease(pos: Int)
    fun onClickListener(position1: Int)
    fun onLongClickListener(position1: Int)
}