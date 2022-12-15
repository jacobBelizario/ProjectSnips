package com.example.projectSnips.Data

import java.util.*

data class User (
    var id: String = UUID.randomUUID().toString(),
    var email:String = "",
    var password:String = "",
    var photoList: MutableList<LikedPhoto>
    ):java.io.Serializable