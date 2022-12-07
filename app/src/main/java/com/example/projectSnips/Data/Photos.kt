package com.example.projectSnips.Data

import java.util.*

data class Photos (
    var id: String = UUID.randomUUID().toString(),
    var url:String = "",
    var likes:Int = 1,
    var owner: String = ""
)