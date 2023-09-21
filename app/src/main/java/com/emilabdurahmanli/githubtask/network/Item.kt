package com.emilabdurahmanli.githubtask.network

data class Item(var id : Int, var node_id : Int, var name : String, var full_name : String, var private : Boolean, var owner : Owner, var description : String, var stargazers_count : Int, var language : String, var forks : Int, var created_at : String, var html_url : String)
