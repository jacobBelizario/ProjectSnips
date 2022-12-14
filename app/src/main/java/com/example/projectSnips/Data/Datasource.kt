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
    var datalist:ArrayList<Photos> = arrayListOf()
    var privateList:ArrayList<Photos> = arrayListOf()
    var publicList:ArrayList<Photos> = arrayListOf()

    var imageURI = ""
    var loggedInUser:String = ""
    var email = ""
    var likedPhotos: ArrayList<LikedPhoto> = arrayListOf()

    fun reset() {
        this.datalist = arrayListOf()
        this.privateList = arrayListOf()
        this.publicList = arrayListOf()
        this.imageURI = ""
        this.loggedInUser = ""
        this.email = ""
        this.likedPhotos = arrayListOf()
    }

}