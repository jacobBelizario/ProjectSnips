package com.example.projectSnips

import com.example.projectSnips.Data.Photos

interface OnSnipClickListener {
    fun onSnipClicked(snip: Photos)

    fun onSnipLongClicked(snip: Photos)
}