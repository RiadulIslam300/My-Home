package com.saklayen.techhome.fragments.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.saklayen.techhome.utils.Event

class HomeViewModel: ViewModel() {

    var navigateToSetUpFragment = MutableLiveData<Event<Unit>>()

    fun navigateToSetUpFragment(){
        navigateToSetUpFragment.value = Event(Unit)
    }
}