package com.denkiri.poscashier.utils

interface OnCustomerItemClick {
    fun delete(pos: Int)
    fun edit(pos: Int)
    fun dial(pos: Int)
    fun enable(pos:Int)
    fun disable(pos: Int)
    fun onClickListener(position1: Int)
    fun onLongClickListener(position1: Int)
}