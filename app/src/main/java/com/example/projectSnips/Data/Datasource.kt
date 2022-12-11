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
    var heading = "ALL SNIPS"
    var datalist:ArrayList<Photos> = arrayListOf()

    var loggedInUser:User = User("","","", arrayListOf())
}