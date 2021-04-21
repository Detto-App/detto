package com.dettoapp.detto.Models

import com.dettoapp.data.DeadlineModel

data class DeadlineManagementModel(
        val cid:String,
        val deadlinesList:ArrayList<DeadlineModel> = ArrayList()
)