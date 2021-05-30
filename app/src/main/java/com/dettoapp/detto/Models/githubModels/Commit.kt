package com.dettoapp.detto.Models.githubModels


import com.google.gson.annotations.SerializedName

data class Commit(
    @SerializedName("author")
    val author: AuthorX,
    @SerializedName("message")
    val message: String
)