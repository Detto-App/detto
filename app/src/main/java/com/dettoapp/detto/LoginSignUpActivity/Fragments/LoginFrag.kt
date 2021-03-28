package com.dettoapp.detto.LoginSignUpActivity.Fragments

import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dettoapp.detto.Db.ClassroomDAO
import com.dettoapp.detto.Db.ClassroomDatabase
import com.dettoapp.detto.LoginSignUpActivity.LoginSignUpRepository
import com.dettoapp.detto.LoginSignUpActivity.ViewModels.LoginSignUpActivityViewModelFactory
import com.dettoapp.detto.R
import com.dettoapp.detto.StudentActivity.StudentActivity
import com.dettoapp.detto.TeacherActivity.TeacherActivity
import com.dettoapp.detto.UtilityClasses.BaseActivity
import com.dettoapp.detto.UtilityClasses.Constants
import com.dettoapp.detto.UtilityClasses.Resource
import com.dettoapp.detto.UtilityClasses.Utility
import com.dettoapp.detto.databinding.FragmentLoginBinding
import com.dettoapp.detto.loginActivity.ViewModels.LoginSignUpActivityViewModel


class LoginFrag : Fragment() {

    private lateinit var viewModel: LoginSignUpActivityViewModel
    private var _binding: FragmentLoginBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val factory = LoginSignUpActivityViewModelFactory(LoginSignUpRepository(ClassroomDatabase.getInstance(requireContext()).classroomDAO), requireContext().applicationContext)
        viewModel = ViewModelProvider(requireActivity(), factory).get(LoginSignUpActivityViewModel::class.java)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialise()
        liveDataObservers()
    }

    private fun initialise() {
//        binding!!.passwordLogin.isPasswordVisibilityToggleEnabled
        binding!!.btnLoginFrag.setOnClickListener {
            viewModel.loginProcess(binding.spinnerId2.selectedItemPosition,
                    binding!!.emailLogin.editText?.text.toString().toLowerCase(), binding!!.passwordLogin.editText?.text.toString())
        }

        binding!!.signUpText.setOnClickListener {
            Utility.navigateFragment(requireActivity().supportFragmentManager,
                    R.id.loginFragContainer, SignUpFrag(), "splash", addToBackStack = true)

            //viewModel.loginProcess(binding!!.email.text.toString(), binding!!.password.text.toString())
        }
        val roles = resources.getStringArray(R.array.Roles)
        binding.spinnerId2.apply {
            adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item, roles
            ).apply {
                setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
            }
        }


    }

    private fun liveDataObservers() {
        viewModel.login.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    //    (requireActivity() as BaseActivity).hideProgressBar()
                    //  Utility.navigateFragment(requireActivity().supportFragmentManager,R.id.loginFragContainer, SignUpFrag(),"splash",addToBackStack = true)
                    (requireActivity() as BaseActivity).hideProgressBar()
                    (requireActivity() as BaseActivity).showToast(it.message!!)
                    val intent = if (it.data == Constants.TEACHER)
                        Intent(requireActivity(), TeacherActivity::class.java)
                    else
                        Intent(requireActivity(), StudentActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)

                    startActivity(intent)
                }
                is Resource.Error -> {
                    (requireActivity() as BaseActivity).hideProgressBar()
                    (requireActivity() as BaseActivity).showErrorSnackMessage(it.message!!)
                }
                is Resource.Loading -> {
                    (requireActivity() as BaseActivity).showProgressDialog("loading...")
                    (requireActivity() as BaseActivity).closeKeyBoard(view)
                }
                else -> {

                }
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}