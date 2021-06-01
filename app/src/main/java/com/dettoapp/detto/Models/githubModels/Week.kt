package com.dettoapp.detto.Models.githubModels


import com.google.gson.annotations.SerializedName

data class Week(
    @SerializedName("c")
    val c: Int,
    @SerializedName("w")
    val w: Int
)