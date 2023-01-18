package com.yayanurc.photogallery.data.local

enum class SharedPrefTypes {
    BOOLEAN,
    FLOAT,
    INTEGER,
    LONG,
    STRING
}

interface SharedPrefDataSource {
    fun setSharedPref(name: String, value: Any, type: Int)
    fun getSharedPref(name: String, type: Int): Any
}