package com.rotate.application

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class BitcoinTracker (val x:X)
{
    data class X(val time:Long,val hash:String,val out:List<Out>)
    data class  Out(
        var value:String
    )
}