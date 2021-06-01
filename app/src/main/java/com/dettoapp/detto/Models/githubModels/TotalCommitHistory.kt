package com.dettoapp.detto.Models.githubModels


import com.google.gson.annotations.SerializedName

data class TotalCommitHistory(
    @SerializedName("commit")
    val commit: Commit
)