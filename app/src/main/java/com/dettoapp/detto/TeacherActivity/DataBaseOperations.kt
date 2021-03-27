package com.dettoapp.detto.TeacherActivity

import com.dettoapp.detto.Models.Classroom


interface DataBaseOperations
{
    fun onClassRoomDelete(classroom: Classroom)
}