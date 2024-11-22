package com.bgRemover.backgroundremover.Retrofit

import com.bgRemover.backgroundremover.Retrofit.Model.Datamodel
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {
    @Multipart
    @POST("api/remove")
    fun uploadFile(@Part body: MultipartBody.Part, @Header("Authorization") auth:String): Call<Datamodel>
}