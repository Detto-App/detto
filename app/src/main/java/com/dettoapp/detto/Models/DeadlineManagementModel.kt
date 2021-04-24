package com.dettoapp.detto.Models


data class DeadlineManagementModel(
        val cid:String,
        val deadlineslist:ArrayList<DeadlineModel> = ArrayList()
)