package com.ab.migotest.model.vo
import com.google.gson.annotations.SerializedName

data class StatusItem(
    @SerializedName("message")
    val message: String,
    @SerializedName("status")
    val status: Int
)