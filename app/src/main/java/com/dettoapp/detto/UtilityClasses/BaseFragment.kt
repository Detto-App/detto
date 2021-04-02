package com.dettoapp.detto.UtilityClasses

import androidx.fragment.app.Fragment

open class BaseFragment : Fragment()
{
    val baseActivity: BaseActivity by lazy { (requireActivity() as BaseActivity) }
}