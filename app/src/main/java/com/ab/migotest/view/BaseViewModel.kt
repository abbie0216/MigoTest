package com.ab.migotest.view

import androidx.lifecycle.ViewModel
import com.ab.migotest.model.api.ApiRepository

abstract class BaseViewModel(
    private val apiRepository: ApiRepository
) : ViewModel() {

}
