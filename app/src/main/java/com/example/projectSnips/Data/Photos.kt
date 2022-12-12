package com.example.projectSnips.Data


data class Photos (
    var id: String = "",
    var caption:String = "",
    var url:String = "",
    var likes:Int = 0,
    var owner: String = "",
    var email: String = "",
    var visibility: String = ""
)