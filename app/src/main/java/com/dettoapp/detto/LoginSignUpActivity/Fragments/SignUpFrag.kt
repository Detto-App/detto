package com.dettoapp.detto.LoginSignUpActivity.Fragments

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.dettoapp.detto.R
import com.dettoapp.detto.UtilityClasses.BaseActivity
import com.dettoapp.detto.UtilityClasses.Constants
import com.dettoapp.detto.UtilityClasses.Resource
import com.dettoapp.detto.databinding.FragmentSignUpBinding
import com.dettoapp.detto.loginActivity.ViewModels.LoginSignUpActivityViewModel


@Suppress("SameParameterValue")
class SignUpFrag : Fragment() {

    private lateinit var viewmodel: LoginSignUpActivityViewModel
    private var _binding: FragmentSignUpBinding? = null
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
    ): View {
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
        val roleSelected=binding.role.text.toString()
        val roleIndex=roles.indexOf(roleSelected)
        val  adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, roles)
        binding.role.setAdapter(adapter)

        binding.role.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (p2 == 1) {
                    binding.etUsn.visibility = View.VISIBLE
                } else {
                    binding.etUsn.visibility = View.GONE
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {}
        }

        binding.btnSignUpFrag.setOnClickListener {
            viewmodel.signUpProcess(
                    roleIndex,
                    binding.etname2.editText?.text.toString(),
                    binding.etUsn.editText?.text.toString().toLowerCase(),
                    binding.etEmail.editText?.text.toString().toLowerCase(),
                    binding.password.editText?.text.toString(),
                    binding.etPassword2.editText?.text.toString()
            )
        }
    }

    private fun liveDataObservers() {
        viewmodel.signUp.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    (requireActivity() as BaseActivity).hideProgressBar()
                    showAlertDialog("Verify Email", "A Verification Email has been sent to your email,Please Verify the email and Login Again")
                }
                is Resource.Error -> {

                    (requireActivity() as BaseActivity).hideProgressBar()
                    Toast.makeText(requireContext(), it.message, Toast.LENGTH_LONG).show()
                }
                is Resource.Loading -> {
                    (requireActivity() as BaseActivity).showProgressDialog(Constants.MESSAGE_LOADING)
                }
                else -> {
                }
            }
        })

    }

    private fun showAlertDialog(dialogTitle: String, dialogMessage: String) {

        val builder = AlertDialog.Builder(requireContext())

        with(builder)
        {
            setTitle(dialogTitle)
            setMessage(dialogMessage)
            setPositiveButton("Ok") { _, _ ->
                requireActivity().finish();
            }
        }

        val alertDialog: AlertDialog = builder.create().apply {
            setCancelable(false)
            show()
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
