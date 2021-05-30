package com.dettoapp.detto.Models.githubModels

import com.anychart.chart.common.dataentry.DataEntry

data class GithubAllModel(val contributors: List<Contributors>,
                          val commitHistory: ArrayList<Pair<String, ArrayList<DataEntry>>>,
                          val totalCommitHistory: List<DataEntry>,
                          val githubCommitDistributionModel: GithubCommitDistributionModel)