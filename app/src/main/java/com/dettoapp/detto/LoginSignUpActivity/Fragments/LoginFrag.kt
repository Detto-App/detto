package com.dettoapp.detto.LoginSignUpActivity.Fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.viewbinding.library.fragment.viewBinding
import android.widget.*
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dettoapp.detto.Db.DatabaseDetto
import com.dettoapp.detto.LoginSignUpActivity.LoginSignUpRepository
import com.dettoapp.detto.LoginSignUpActivity.ViewModels.LoginSignUpActivityViewModelFactory
import com.dettoapp.detto.R
import com.dettoapp.detto.StudentActivity.StudentActivity
import com.dettoapp.detto.TeacherActivity.TeacherActivity
import com.dettoapp.detto.UtilityClasses.*
import com.dettoapp.detto.databinding.FragmentLoginBinding
import com.dettoapp.detto.loginActivity.ViewModels.LoginSignUpActivityViewModel
import java.util.*


class LoginFrag : BaseFragment() {

    private val binding: FragmentLoginBinding by viewBinding()

    private val viewModel: LoginSignUpActivityViewModel by activityViewModels(factoryProducer = {
        LoginSignUpActivityViewModelFactory(
            LoginSignUpRepository(
                DatabaseDetto.getInstance(requireContext()).classroomDAO
            ), requireContext().applicationContext
        )
    })

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialise()
        liveDataObservers()
    }

    private fun initialise() {

        val roles = resources.getStringArray(R.array.Roles)

        binding.btnLoginFrag.setOnClickListener {

            val roleSelected = binding.role.text.toString()
            val role = roles.indexOf(roleSelected)
            Log.d("AAA", role.toString())

            viewModel.loginProcess(
                role,
                binding.emailLogin.editText?.text.toString().toLowerCase(Locale.ROOT),
                binding.passwordLogin.editText?.text.toString()
            )
        }

        binding.signUpText.setOnClickListener {
            Utility.navigateFragment(
                requireActivity().supportFragmentManager,
                R.id.loginFragContainer, SignUpFrag(), "splash", addToBackStack = true
            )

            //viewModel.loginProcess(binding!!.email.text.toString(), binding!!.password.text.toString())
        }

        val adapter =
            ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, roles)
        binding.role.setAdapter(adapter)


    }

    private fun liveDataObservers() {
        viewModel.login.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    baseActivity.hideProgressBar()
                    baseActivity.showToast(it.message!!)
                    val intent = if (it.data == Constants.TEACHER)
                        Intent(requireActivity(), TeacherActivity::class.java)
                    else
                        Intent(requireActivity(), StudentActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    Utility.initialiseData(it.data!!, requireContext().applicationContext)
                    startActivity(intent)
                }
                is Resource.Error -> {
                    baseActivity.hideProgressBar()
                    baseActivity.showErrorSnackMessage(it.message!!)
                }
                is Resource.Loading -> {
                    baseActivity.showProgressDialog("loading...")
                    baseActivity.closeKeyBoard(view)
                }
                else -> {
                }
            }
        })
    }
}