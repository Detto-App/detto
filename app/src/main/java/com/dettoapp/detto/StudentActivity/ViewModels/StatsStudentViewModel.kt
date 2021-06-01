package com.dettoapp.detto.StudentActivity.ViewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.anychart.chart.common.dataentry.DataEntry
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.dettoapp.detto.Db.ProjectDAO
import com.dettoapp.detto.Models.ProjectModel
import com.dettoapp.detto.Models.githubModels.GithubAllModel
import com.dettoapp.detto.Models.githubModels.GithubCommitDistributionModel
import com.dettoapp.detto.UtilityClasses.Resource
import com.dettoapp.detto.UtilityClasses.RetrofitInstance
import com.dettoapp.detto.UtilityClasses.Utility
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class StatsStudentViewModel : ViewModel() {

    private val _githubLink = MutableLiveData<Resource<String>>()
    val githubLink: LiveData<Resource<String>>
        get() = _githubLink

    private val _allGithub = MutableLiveData<Resource<List<GithubAllModel>>>()
    val allGithub: LiveData<Resource<List<GithubAllModel>>>
        get() = _allGithub

    private lateinit var owner: String
    private lateinit var repo: String

    fun addGithubLink(link: String, projectModel: ProjectModel,projectDoa:ProjectDAO) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                validateLink(link)
                projectDoa.updateProject(projectModel)
                RetrofitInstance.projectAPI.updateProject(projectModel, projectModel.pid, Utility.TOKEN)
                _githubLink.postValue(Resource.Success(data = link))
            } catch (e: Exception) {
                _githubLink.postValue(Resource.Error(message = "" + e.localizedMessage))
            }
        }
    }

     fun validateLink(linkGithub: String) {
        var link = linkGithub
        link = link.replace("https://github.com/", "")
        link = link.replace(".git", "")
        val array = link.split("/")
        if (array.size != 2)
            throw Exception("Invalid Link")

        owner = array[0]
        repo = array[1]
        Log.d("DDFF", array.toString())
        collectData()
    }

    fun collectData() {
        viewModelScope.launch(Dispatchers.IO)
        {
            try {
                _allGithub.postValue(Resource.Loading())
                val contributors = RetrofitInstance.githubAPI.getContributors(owner, repo).body()
                //Log.d("DDFF",""+contributors.body())
                val commitHistory = RetrofitInstance.githubAPI.getContributorsCommitHistory(owner, repo).body()
                //Log.d("DDFF",""+commitHistory.body())

                val totalHistory = RetrofitInstance.githubAPI.getTotalCommitHistory(owner, repo)
                //Log.d("DDFF", "" + totalHistory.body())


                val dataCommitHistory = ArrayList<Pair<String, ArrayList<DataEntry>>>()

                commitHistory?.forEach {
                    val listData = ArrayList<DataEntry>()

                    it.weeks.forEach { week ->
                        if (week.c != 0) {
                            listData.add(ValueDataEntry((week.w * 1000L).formatLongDateToFormat("MMM dd"), week.c))
                        }
                    }

                    dataCommitHistory.add(Pair(it.author.login, listData))
                }


                val dataEntries = ArrayList<DataEntry>()
                var totalCommits = 0
                contributors?.forEach { model ->
                    totalCommits += model.contributions
                    dataEntries.add(ValueDataEntry(model.login, model.contributions))
                }
                val githubCommitDistributionModel = GithubCommitDistributionModel(dataEntries, totalCommits)


                val totalCommitHistory = ArrayList<DataEntry>()
                totalHistory.body()?.forEach {
                    if (it.total != 0) {
                        totalCommitHistory.add(ValueDataEntry((it.week * 1000).formatLongDateToFormat("MMM dd"), it.total))
                    }
                }


                //Log.d("DDVV",Gson().toJson(dataCommitHistory))

                val githubAllModel = GithubAllModel(contributors!!, dataCommitHistory, totalCommitHistory, githubCommitDistributionModel)

                _allGithub.postValue(Resource.Success(data = arrayListOf(githubAllModel)))
            } catch (e: Exception) {
                Log.d("DDFF", "" + e.localizedMessage)

            }
        }
    }
}


fun Long.formatLongDateToFormat(dateFormat: String = ""): String {
    val date = Date(this)
    return SimpleDateFormat(dateFormat, Locale.US).format(date)
}