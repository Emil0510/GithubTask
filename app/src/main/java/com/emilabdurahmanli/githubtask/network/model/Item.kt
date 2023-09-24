package com.emilabdurahmanli.githubtask.network.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

data class Item(
    var id : Int? = 0,
    var name: String? = "",
    var full_name: String? = "",
    var private: Boolean? = false,
    var owner: Owner,
    var description: String? = "",
    var stargazers_count: Int? = 0,
    var language: String? = "",
    var forks: Int? = 0,
    var created_at: String? = "",
    var html_url: String? = "",
    var isSeen : Boolean = false
) : Serializable

@Entity("Item_Favorites")
data class ItemRoom(
    @PrimaryKey()
    var id : Int? = 0,
    @ColumnInfo("name")
    var name: String? = "",
    @ColumnInfo("full_name")
    var full_name: String? = "",
    @ColumnInfo("private")
    var private: Boolean? = false,
    @ColumnInfo("login")
    var login: String? = "",
    @ColumnInfo("avatar_url")
    var avatar_url: String? = "",
    @ColumnInfo("description")
    var description: String? = "",
    @ColumnInfo("stargazers_count")
    var stargazers_count: Int? = 0,
    @ColumnInfo("language")
    var language: String? = "",
    @ColumnInfo("forks")
    var forks: Int? = 0,
    @ColumnInfo("created_at")
    var created_at: String? = "",
    @ColumnInfo("html_url")
    var html_url: String? = "",
    @ColumnInfo("is_seen")
    var isSeen : Boolean = false
) : Serializable


