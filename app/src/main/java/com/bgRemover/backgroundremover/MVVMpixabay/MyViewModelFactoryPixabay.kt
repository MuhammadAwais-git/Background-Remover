package com.bgRemover.backgroundremover.MVVMpixabay

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.bgRemover.backgroundremover.MVVM.pixabayModel

class MyViewModelFactoryPixabay constructor(private val repository: MainRepositoryPixabay): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(DataViewModelPixabay::class.java)) {
            DataViewModelPixabay(this.repository) as T
        } else {
            throw IllegalArgumentException("ViewModel Not Found")
        }
    }
}