package com.bgRemover.backgroundremover.Retrofit

import androidx.lifecycle.ViewModel
import java.io.File

class DataViewModel( private val repository: MainRepository): ViewModel() {
     val mLiveData = repository.mMuteableData

    fun getData(file: File){
        repository.getData(file)
    }
//    fun observeLiveData(): LiveData<Datamodel> {
//        Log.d("observeLiveData", "observeLiveData: ")
//        return mLiveData
//    }
}