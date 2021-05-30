package com.dettoapp.detto.Models.githubModels


import com.google.gson.annotations.SerializedName

data class ContributorsCommitHistory(
    @SerializedName("author")
    val author: Author,
    @SerializedName("total")
    val total: Int,
    @SerializedName("weeks")
    val weeks: List<Week>
)