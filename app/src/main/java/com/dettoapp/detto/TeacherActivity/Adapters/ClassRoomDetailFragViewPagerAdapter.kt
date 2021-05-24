package com.dettoapp.detto.TeacherActivity.Adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dettoapp.detto.TeacherActivity.Fragments.*

class ClassRoomDetailFragViewPagerAdapter(
    fa: FragmentActivity,
    private val classroomDetailOperations: ClassroomDetailOperations

) : FragmentStateAdapter(fa) {

    @Suppress("PrivatePropertyName")
    private val NUM_OF_PAGES = 4
    override fun getItemCount() = NUM_OF_PAGES

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ClassroomProjectsFragment(classroomDetailOperations)
            1 -> StudentsInClassFragment(classroomDetailOperations)
            2 -> DeadlineFragment(classroomDetailOperations)
            3->RubricsFragment(classroomDetailOperations)

            else -> {
                StudentsInClassFragment(classroomDetailOperations)
            }
        }
    }

}