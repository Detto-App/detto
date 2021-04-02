package com.dettoapp.detto.LoginSignUpActivity.Fragments

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import com.dettoapp.detto.Db.DatabaseDetto
import com.dettoapp.detto.LoginSignUpActivity.LoginSignUpRepository
import com.dettoapp.detto.R
import com.dettoapp.detto.UtilityClasses.BaseActivity
import com.dettoapp.detto.UtilityClasses.BaseFragment
import com.dettoapp.detto.UtilityClasses.Constants
import com.dettoapp.detto.UtilityClasses.Resource
import com.dettoapp.detto.databinding.FragmentSignUpBinding
import com.dettoapp.detto.loginActivity.ViewModels.LoginSignUpActivityViewModel
import java.util.*


@Suppress("SameParameterValue")
class SignUpFrag : BaseFragment<LoginSignUpActivityViewModel, FragmentSignUpBinding, LoginSignUpRepository>() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initialise()
        liveDataObservers()

    }

    private fun initialise() {
        val roles = resources.getStringArray(R.array.Roles)

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_dropdown_item, roles)

        binding.role.setOnItemClickListener { _, _, position, _ ->
            if (position == Constants.STUDENT) {
                binding.etUsn.visibility = View.VISIBLE
            } else {
                binding.etUsn.visibility = View.GONE
            }
        }


        binding.role.setAdapter(adapter)

        binding.btnSignUpFrag.setOnClickListener {
            val roleSelected = binding.role.text.toString()
            val roleIndex = roles.indexOf(roleSelected)
            viewModel.signUpProcess(
                    roleIndex,
                    binding.etname2.editText?.text.toString(),
                    binding.etUsn.editText?.text.toString().toLowerCase(Locale.ROOT),
                    binding.etEmail.editText?.text.toString().toLowerCase(Locale.ROOT),
                    binding.password.editText?.text.toString(),
                    binding.etPassword2.editText?.text.toString()
            )
        }
    }

    private fun liveDataObservers() {
        viewModel.signUp.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    baseActivity.hideProgressBar()
                    showAlertDialog("Verify Email", "A Verification Email has been sent to your email,Please Verify the email and Login Again")
                }
                is Resource.Error -> {
                    baseActivity.hideProgressBar()
                    baseActivity.showToast(it.message!!)
                }
                is Resource.Loading -> {
                    baseActivity.showProgressDialog(Constants.MESSAGE_LOADING)
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

    override fun getViewModelClass(): Class<LoginSignUpActivityViewModel> = LoginSignUpActivityViewModel::class.java

    override fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentSignUpBinding = FragmentSignUpBinding.inflate(inflater, container, false)

    override fun getRepository(): LoginSignUpRepository = LoginSignUpRepository(DatabaseDetto.getInstance(requireContext()).classroomDAO)

    override fun getBaseViewModelOwner(): ViewModelStoreOwner = requireActivity()
}
