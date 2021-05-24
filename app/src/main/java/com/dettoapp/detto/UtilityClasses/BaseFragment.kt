package com.dettoapp.detto.UtilityClasses

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
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
        getBaseOnCreate()
    }

    protected open fun getBaseOnCreate()
    {

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = getFragmentBinding(inflater, container)
        return binding.root
    }

    abstract fun getViewModelClass(): Class<VM>
    abstract fun getFragmentBinding(inflater: LayoutInflater, container: ViewGroup?): B
    abstract fun getRepository(): R


    inline fun <T> observeWithLiveData(liveData: LiveData<Resource<T>>, noinline onSuccess: ((data: T) -> Unit)? = null,
                                       noinline onLoading: (() -> Unit)? = null, noinline onError: ((message: String) -> Unit)? = null,
                                       noinline onConfirm: (() -> Unit)? = null) {
        liveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Resource.Success -> {
                    onSuccess?.let { function ->
                        function(it.data!!)
                    }
                }
                is Resource.Loading -> {
                    onLoading?.let { function ->
                        function()
                    }
                }
                is Resource.Error -> {
                    onError?.let { function ->
                        function(it.message!!)
                    }
                }
                is Resource.Confirm -> {
                    onConfirm?.let { function ->
                        function()
                    }
                }
                else -> {

                }
            }
        })
    }

    protected open fun getBaseViewModelOwner(): ViewModelStoreOwner = this

    override fun onDestroy() {
        _binding = null
        onBaseDestroy()
        super.onDestroy()
    }

    protected open fun onBaseDestroy()
    {

    }
}