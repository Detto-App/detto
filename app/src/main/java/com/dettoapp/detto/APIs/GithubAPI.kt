package com.dettoapp.detto.APIs

import androidx.lifecycle.LifecycleOwner
import com.dettoapp.detto.Models.githubModels.CommitActivity
import com.dettoapp.detto.Models.githubModels.Contributors
import com.dettoapp.detto.Models.githubModels.ContributorsCommitHistory
import com.dettoapp.detto.Models.githubModels.TotalCommitHistory
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubAPI {
    @GET("/repos/{owner}/{repo}/contributors")
    suspend fun getContributors(@Path(value = "owner") owner: String, @Path(value = "repo") repo: String): Response<List<Contributors>>

    @GET("/repos/{owner}/{repo}/stats/contributors")
    suspend fun getContributorsCommitHistory(@Path(value = "owner") owner: String, @Path(value = "repo") repo: String): Response<List<ContributorsCommitHistory>>


//    /repos/Detto-App/Detto-Backend/commits?since=2021-04-01T00:00:00Z

    @GET("repos/{owner}/{repo}/stats/commit_activity")
    suspend fun getTotalCommitHistory(@Path(value = "owner") owner: String, @Path(value = "repo") repo: String): Response<List<CommitActivity>>

}