package com.dettoapp.detto.Models.githubModels


import com.google.gson.annotations.SerializedName

data class Author(
    @SerializedName("login")
    val login: String
)