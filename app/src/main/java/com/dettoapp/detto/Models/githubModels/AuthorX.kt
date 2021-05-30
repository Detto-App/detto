package com.dettoapp.detto.Models.githubModels


import com.google.gson.annotations.SerializedName

data class AuthorX(
    @SerializedName("date")
    val date: String,
    @SerializedName("name")
    val name: String
)