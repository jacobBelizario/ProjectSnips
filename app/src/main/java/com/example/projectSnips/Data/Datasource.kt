package com.example.projectSnips.Data

class Datasource {
    private constructor() {}
    companion object {
        @Volatile
        private lateinit var instance: Datasource

        fun getInstance(): Datasource {
            synchronized(this) {
                if (!::instance.isInitialized) {
                    instance = Datasource()
                }
                return instance
            }
        }
    }
    var heading = ""
    var datalist:ArrayList<Photos> = arrayListOf()

    var imageURI = ""
    var loggedInUser:String = ""
    var likedPhotos: ArrayList<LikedPhoto> = arrayListOf()
    var likes = 0
}