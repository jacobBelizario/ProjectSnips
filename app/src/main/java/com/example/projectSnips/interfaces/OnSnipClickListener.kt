package com.example.projectSnips.interfaces

import com.example.projectSnips.Data.Photos

interface OnSnipClickListener {
    fun onSnipClicked(snip: Photos)

    fun onSnipLongClicked(snip: Photos)
}