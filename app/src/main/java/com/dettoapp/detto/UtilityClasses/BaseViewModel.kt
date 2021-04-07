package com.dettoapp.detto.UtilityClasses


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

abstract class BaseViewModel : ViewModel() {
    inline fun <T> operateWithLiveData(liveData: MutableLiveData<Resource<T>>,
                                       crossinline mainFunction:
                                       suspend (passedLiveData: MutableLiveData<Resource<T>>) -> Unit,
                                       noinline onError: ((errorMessage: String) -> Unit)? = null) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                liveData.postValue(Resource.Loading())
                mainFunction(liveData)
            } catch (e: Exception) {
                onError?.let {
                    onError("" + e.localizedMessage)
                } ?: liveData.postValue(Resource.Error(message = "" + e.localizedMessage))
            }
        }
    }
}