package com.dettoapp.detto.LinkParseActivity

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class LinkParserFactory(private val repository: LinkParserRepository, private val context: Context) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LinkParseViewModel(repository, context) as T
    }
}