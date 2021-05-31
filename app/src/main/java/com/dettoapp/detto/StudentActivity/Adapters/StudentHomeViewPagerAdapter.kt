package com.dettoapp.detto.StudentActivity.Adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dettoapp.detto.Models.Classroom
import com.dettoapp.detto.StudentActivity.Fragments.*
import com.dettoapp.detto.StudentActivity.StudentOperations

@Suppress("PrivatePropertyName")
class StudentHomeViewPagerAdapter(
        fa: FragmentActivity, private val classroom: Classroom, private val
        studentOperations: StudentOperations
) :
        FragmentStateAdapter
        (fa) {

    private val NUM_OF_PAGES = 1
    override fun getItemCount() = NUM_OF_PAGES

    override fun createFragment(position: Int): Fragment {
        return when (position) {
//            0 -> StudentDeadlineFrag(classroom, studentOperations)
//            1 -> StudentSubmissionFrag(classroom.classroomuid)
//            2 -> TodoFrag( classroom.classroomuid,studentOperations)
//            3 -> StatsStudentFragment(studentOperations)
//            4 -> TimelineFrag(classroom.classroomuid, studentOperations)
            0 -> StudentSubmissionFrag(classroom.classroomuid)
            else -> {
                StudentDeadlineFrag(classroom, studentOperations)
            }
        }
    }
}