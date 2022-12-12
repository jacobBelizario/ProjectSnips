package com.example.projectSnips.Data

import java.util.*


data class Photos (
    var id: String = UUID.randomUUID().toString(),
    var caption:String = "",
    var url:String = "",
    var owner:String = "",
    var likes:Int = 0,
    var email: String = "",
    var visibility: String = ""
)