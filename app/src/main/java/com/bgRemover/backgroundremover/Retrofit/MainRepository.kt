package com.bgRemover.backgroundremover.Retrofit

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.bgRemover.backgroundremover.Retrofit.Model.Datamodel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import java.io.File

class MainRepository  {

    val mMuteableData = MutableLiveData<Datamodel>()

    val auth = "Bearer 1|TIhbfxrzDQQFh8MJOX0llHc8XkOtr5MetIv04Nol"
    fun getData(file: File){

        val requestBody = RequestBody.create("image/*".toMediaTypeOrNull(), file)
        val filePart = MultipartBody.Part.createFormData("image", "test.jpg", requestBody)

        RetrofitInstance.api.uploadFile(filePart, auth).enqueue(object :
            retrofit2.Callback<Datamodel> {
            override fun onResponse(call: Call<Datamodel>, response: Response<Datamodel>) {
//                Log.d("TAG", "onResponse: ${response.message()}")
//                Log.d("TAG", "onResponse: ${response}")
                Log.d("TAG", "onCreate: onResponsedata")

                if (response.body() != null) {
                    mMuteableData.postValue(response.body())

                } else {
                    return
                }
            }

            override fun onFailure(call: Call<Datamodel>, t: Throwable) {
                Log.d("TAG", "onFailure" + t.message.toString())
                Log.d("observeLiveData", "=================109  ")
            }
        })

    }
}