package com.dettoapp.detto.TeacherActivity.Adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.dettoapp.detto.Models.ProjectModel
import com.dettoapp.detto.TeacherActivity.Fragments.*

class ProjectHomeFragViewPagerAdapter(
        fa: FragmentActivity,
        private val projectModel: ProjectModel
) : FragmentStateAdapter(fa) {
    private val NUM_OF_PAGES = 4
    override fun getItemCount() = NUM_OF_PAGES


    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> ProjectHomeFragment(projectModel)
            1 -> TeacherClassSubFrag(projectModel.cid)
            else -> {
                ProjectHomeFragment(projectModel)
            }
        }
    }
}