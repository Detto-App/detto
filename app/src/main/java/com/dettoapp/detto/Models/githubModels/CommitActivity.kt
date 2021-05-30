package com.dettoapp.detto.Models.githubModels


import com.google.gson.annotations.SerializedName

data class CommitActivity(
    @SerializedName("days")
    val days: List<Int>,
    @SerializedName("total")
    val total: Int,
    @SerializedName("week")
    val week: Long
)