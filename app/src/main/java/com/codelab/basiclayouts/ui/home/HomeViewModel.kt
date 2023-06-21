package com.codelab.basiclayouts.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel


class HomeViewModel : ViewModel() {
    // ViewModel class for managing data related to the Home Fragment

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    // MutableLiveData is a type of LiveData that can be modified
    // _text is a private mutable live data that holds a String value
    // It is initialized with the value "This is home Fragment" using the apply function

    val text: LiveData<String> = _text
    // Exposing the _text as an immutable LiveData to the external components

    // This allows observing the text LiveData for any changes
}