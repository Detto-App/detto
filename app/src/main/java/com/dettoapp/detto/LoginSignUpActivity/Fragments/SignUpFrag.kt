package com.dettoapp.detto.LoginSignUpActivity.Fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dettoapp.detto.LoginSignUpActivity.ViewModels.LoginSignUpActivityViewModel
import com.dettoapp.detto.R
import com.dettoapp.detto.UtilityClasses.BaseActivity
import com.dettoapp.detto.UtilityClasses.Resource
import com.dettoapp.detto.UtilityClasses.Utility
import com.dettoapp.detto.databinding.FragmentSignUpBinding


class SignUpFrag : Fragment() {
    private lateinit var viewmodel: LoginSignUpActivityViewModel
    private  var _binding: FragmentSignUpBinding? = null
    private val binding
        get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewmodel =
            ViewModelProvider(requireActivity()).get(LoginSignUpActivityViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialise()
        liveDataObservers()

    }

    private fun initialise() {
        val roles = resources.getStringArray(R.array.Roles)
        binding.spinnerId.apply {
            adapter = ArrayAdapter(
                requireContext(),
                android.R.layout.simple_spinner_item, roles
            ).apply {
                setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
            }
        }
        binding.spinnerId.onItemSelectedListener = object:AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if(p2==1){
                    binding.etUsn.visibility=View.VISIBLE
                }
                else{
                    binding.etUsn.visibility=View.GONE
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        binding.btnSignUpFrag.setOnClickListener {
            viewmodel.signUpProcess(
                binding.etname2.text.toString(),
                binding.etUsn.text.toString(),
                binding.etEmail.text.toString(),
                binding.password.text.toString(),
                binding.etPassword2.text.toString()
            )
        }
    }

    private fun liveDataObservers() {
        viewmodel.loginSignUp.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    (requireActivity() as BaseActivity).hideProgressBar()
                    (requireActivity() as BaseActivity).showToast(it.data!!)
                    Utility.navigateFragment(
                        (requireActivity() as BaseActivity).supportFragmentManager,
                        R.id.loginFragContainer,
                        LoginFrag(),
                        "register",
                        true,
                        true
                    )
                }
                is Resource.Error -> {
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
                is Resource.Loading -> {
                    (requireActivity() as BaseActivity).showProgressDialog("wait...")
                }
            }
        })

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
