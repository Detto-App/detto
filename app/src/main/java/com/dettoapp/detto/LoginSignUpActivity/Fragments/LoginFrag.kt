package com.dettoapp.detto.LoginSignUpActivity.Fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dettoapp.detto.LoginSignUpActivity.ViewModels.LoginSignUpActivityViewModel
import com.dettoapp.detto.R
import com.dettoapp.detto.TeacherActivity.TeacherActivity
import com.dettoapp.detto.UtilityClasses.BaseActivity
import com.dettoapp.detto.UtilityClasses.Resource
import com.dettoapp.detto.UtilityClasses.Utility
import com.dettoapp.detto.databinding.FragmentLoginBinding


class LoginFrag : Fragment() {

    private lateinit var viewModel: LoginSignUpActivityViewModel
    private var binding: FragmentLoginBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(requireActivity()).get(LoginSignUpActivityViewModel::class.java)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialise()
        liveDataObservers()
    }

    private fun initialise()
    {
        binding!!.btnLoginFrag.setOnClickListener {
            viewModel.loginProcess(binding!!.emailLogin.text.toString(), binding!!.passwordLogin.text.toString())
        }

        binding!!.signUpText.setOnClickListener {
            Utility.navigateFragment(requireActivity().supportFragmentManager,R.id.loginFragContainer, SignUpFrag(),"splash",addToBackStack = true)

            //viewModel.loginProcess(binding!!.email.text.toString(), binding!!.password.text.toString())
        }
    }

    private fun liveDataObservers() {
        viewModel.loginSignUp.observe(viewLifecycleOwner, Observer{
            when (it) {
                is Resource.Success -> {
                //    (requireActivity() as BaseActivity).hideProgressBar()
                  //  Utility.navigateFragment(requireActivity().supportFragmentManager,R.id.loginFragContainer, SignUpFrag(),"splash",addToBackStack = true)
                    (requireActivity() as BaseActivity).hideProgressBar()
                    (requireActivity() as BaseActivity).showToast(it.data!!)
                    val intent :Intent= Intent(requireActivity(),TeacherActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                    startActivity(intent)
                }
                is Resource.Error -> {
                    (requireActivity() as BaseActivity).hideProgressBar()
                    (requireActivity() as BaseActivity).showErrorSnackMessage(it.message!!)
                }
                is Resource.Loading ->{
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
        binding = null
    }
}