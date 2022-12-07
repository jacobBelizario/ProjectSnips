package com.example.projectSnips.Data

import javax.sql.DataSource

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
    var user = ""
}