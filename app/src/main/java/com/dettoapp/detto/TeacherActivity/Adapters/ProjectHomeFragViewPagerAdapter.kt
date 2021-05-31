package com.dettoapp.detto.TeacherActivity.Adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dettoapp.detto.Chat.ChatFragment
import com.dettoapp.detto.Models.Classroom
import com.dettoapp.detto.Models.ProjectModel
import com.dettoapp.detto.StudentActivity.Fragments.StatsStudentFragment
import com.dettoapp.detto.TeacherActivity.Fragments.*
import com.dettoapp.detto.UtilityClasses.Utility

class ProjectHomeFragViewPagerAdapter(
        fa: FragmentActivity,
        private val projectModel: ProjectModel,
        private val classroom: Classroom
) : FragmentStateAdapter(fa) {
    private val NUM_OF_PAGES = 4
    override fun getItemCount() = NUM_OF_PAGES


    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ProjectHomeFragment(projectModel)
            1 -> TeacherClassSubFrag(projectModel.pid)
            2 -> StatsStudentFragment(nullProjectModel = projectModel)
            3 -> ChatFragment(classroom, Utility.TEACHER.name + " Teacher", Utility.TEACHER.uid, projectModel.pid)
            else -> {
                ProjectHomeFragment(projectModel)
            }
        }
    }
}