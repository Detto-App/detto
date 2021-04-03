package com.dettoapp.detto.TeacherActivity.Adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dettoapp.detto.TeacherActivity.Fragments.ClassroomDetailOperations
import com.dettoapp.detto.TeacherActivity.Fragments.ClassroomProjectsFragment
import com.dettoapp.detto.TeacherActivity.Fragments.StudentsInClassFragment

class ClassRoomDetailFragViewPagerAdapter(
    fa: FragmentActivity,
    private val classroomDetailOperations: ClassroomDetailOperations

) : FragmentStateAdapter(fa) {

    @Suppress("PrivatePropertyName")
    private val NUM_OF_PAGES = 2
    override fun getItemCount() = NUM_OF_PAGES

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ClassroomProjectsFragment(classroomDetailOperations)
            1 -> StudentsInClassFragment(classroomDetailOperations)
            else -> {
                StudentsInClassFragment(classroomDetailOperations)
            }
        }
    }

}