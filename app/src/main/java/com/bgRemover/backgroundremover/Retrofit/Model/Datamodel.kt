package com.bgRemover.backgroundremover.Retrofit.Model

import com.google.gson.annotations.SerializedName

class Datamodel (
    @SerializedName("status"  ) var status  : String? = null,
    @SerializedName("message" ) var message : String? = null,
    @SerializedName("url"     ) var url     : String? = null
)