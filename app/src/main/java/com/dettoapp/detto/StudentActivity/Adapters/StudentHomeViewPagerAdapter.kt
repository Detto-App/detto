package com.dettoapp.detto.StudentActivity.Adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dettoapp.detto.Models.Classroom
import com.dettoapp.detto.StudentActivity.Fragments.StudentClassDetailsFrag
import com.dettoapp.detto.StudentActivity.Fragments.StudentDeadlineFrag
import com.dettoapp.detto.StudentActivity.StudentOperations
import com.dettoapp.detto.TeacherActivity.Fragments.ClassroomProjectsFragment
import com.dettoapp.detto.TeacherActivity.Fragments.DeadlineFragment
import com.dettoapp.detto.TeacherActivity.Fragments.StudentsInClassFragment

@Suppress("PrivatePropertyName")
class StudentHomeViewPagerAdapter(fa: FragmentActivity, private val classroom: Classroom,private val studentOperations:
StudentOperations) :
    FragmentStateAdapter
    (fa) {

    private val NUM_OF_PAGES = 1
    override fun getItemCount()= NUM_OF_PAGES

    override fun createFragment(position: Int): Fragment {
        return when (position) {
//            0 -> StudentClassDetailsFrag(classroom)
            0 -> StudentDeadlineFrag(classroom,studentOperations)
//            2 -> DeadlineFragment(classroomDetailOperations)
            else -> {
                StudentDeadlineFrag(classroom,studentOperations)
            }
        }
    }
}