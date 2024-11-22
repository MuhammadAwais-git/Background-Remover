package com.bgRemover.backgroundremover.MVVMpixabay

import androidx.lifecycle.ViewModel
import retrofit2.http.Query
import java.io.File

class DataViewModelPixabay(private val repository: MainRepositoryPixabay): ViewModel() {
     val mLiveData = repository.mMuteableData

    fun getData(query: String){
        repository.getImgdata(query)
    }
//    fun observeLiveData(): LiveData<Datamodel> {
//        Log.d("observeLiveData", "observeLiveData: ")
//        return mLiveData
//    }
}