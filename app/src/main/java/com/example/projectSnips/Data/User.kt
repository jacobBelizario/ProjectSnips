package com.example.projectSnips.Data

import java.util.UUID

data class User (
    var id: String = UUID.randomUUID().toString(),
    var email:String = "",
    var password:String = "",
    var photoList: MutableList<Photos>
    )