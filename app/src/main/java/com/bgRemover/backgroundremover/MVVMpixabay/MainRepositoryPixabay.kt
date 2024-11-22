package com.bgRemover.backgroundremover.MVVMpixabay

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.bgRemover.backgroundremover.MVVM.pixabayModel
import com.bgRemover.backgroundremover.Retrofit.Model.Datamodel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Query
import java.io.File

class MainRepositoryPixabay  {

    val mMuteableData = MutableLiveData<pixabayModel>()

    val key = "29643680-cc329d3bf258e0ee9013ba1bc"
    val image_type:String="photo"


    fun getImgdata(q: String){
        RetrofitInstancepixabay.api.getAllPic(key,q,image_type).enqueue(object :
            retrofit2.Callback<pixabayModel> {
            override fun onResponse(call: Call<pixabayModel>, response: Response<pixabayModel>) {
//                Log.d("TAG", "onResponse: ${response.message()}")
//                Log.d("TAG", "onResponse: ${response}")
                Log.d("TAG", "onCreate: onResponsedata pixabay")

                if (response.body() != null) {
                    mMuteableData.postValue(response.body())

                } else {
                    return
                }
            }

            override fun onFailure(call: Call<pixabayModel>, t: Throwable) {
                Log.d("TAG", "onFailure" + t.message.toString())
                Log.d("observeLiveData", "=================109  ")
            }
        })

    }
}