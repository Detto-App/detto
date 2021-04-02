package com.dettoapp.detto.UtilityClasses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.viewbinding.ViewBinding

abstract class BaseFragment<VM : ViewModel, B : ViewBinding, R : BaseRepository> : Fragment() {

    protected val baseActivity: BaseActivity by lazy { (requireActivity() as BaseActivity) }

    private var _binding: B? = null
    protected val binding
        get() = _binding!!

    protected lateinit var viewModel: VM

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val factory = BaseViewModelFactory(getRepository(), requireContext().applicationContext)
        viewModel = ViewModelProvider(getBaseViewModelOwner(), factory).get(getViewModelClass())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = getFragmentBinding(inflater, container)
        return binding.root
    }

    abstract fun getViewModelClass(): Class<VM>
    abstract fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): B
    abstract fun getRepository(): R

    protected open fun getBaseViewModelOwner(): ViewModelStoreOwner = this

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}