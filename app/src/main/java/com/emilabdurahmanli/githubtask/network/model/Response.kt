package com.emilabdurahmanli.githubtask.network.model

import com.emilabdurahmanli.githubtask.network.model.Item

data class Response(var total_count : Int? = 0, var incomplete_results : Boolean? = false, var items : List<Item> )
