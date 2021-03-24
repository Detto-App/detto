package com.dettoapp.detto.TeacherActivity

import com.dettoapp.detto.TeacherActivity.db.Classroom

interface DataBaseOperations
{
    fun onClassRoomDelete(classroom: Classroom)
}