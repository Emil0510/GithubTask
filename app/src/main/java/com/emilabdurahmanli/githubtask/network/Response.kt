package com.emilabdurahmanli.githubtask.network

data class Response(var total_count : Int, var incomplete_results : Boolean, var items : List<Item> )
