package com.dettoapp.detto.StudentActivity.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dettoapp.detto.Db.DatabaseDetto
import com.dettoapp.detto.StudentActivity.StudentRepository
import com.dettoapp.detto.StudentActivity.ViewModels.StudentSubmissionViewModel
import com.dettoapp.detto.UtilityClasses.BaseFragment
import com.dettoapp.detto.databinding.FragmentStudentSubmissionBinding

class StudentSubmissionFrag :
    BaseFragment<StudentSubmissionViewModel, FragmentStudentSubmissionBinding, StudentRepository>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    override fun getViewModelClass(): Class<StudentSubmissionViewModel> {
        return StudentSubmissionViewModel::class.java
    }

    override fun getFragmentBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentStudentSubmissionBinding {
        return FragmentStudentSubmissionBinding.inflate(inflater, container, false)
    }

    override fun getRepository(): StudentRepository {
        return StudentRepository(
            DatabaseDetto.getInstance(requireContext().applicationContext).classroomDAO,
            DatabaseDetto.getInstance(requireContext().applicationContext).projectDAO
        )
    }
}