package com.example.projectSnips.Data

data class LikedPhoto (
    var id: String = "",
    var likeType: LikeType = LikeType.LIKED
) : java.io.Serializable