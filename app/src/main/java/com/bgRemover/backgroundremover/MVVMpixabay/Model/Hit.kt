package com.bgRemover.backgroundremover.MVVM


import com.google.gson.annotations.SerializedName

data class Hit(
    @SerializedName("collections")
    val collections: Int, // 2102
    @SerializedName("comments")
    val comments: Int, // 328
    @SerializedName("downloads")
    val downloads: Int, // 542740
    @SerializedName("id")
    val id: Int, // 434918
    @SerializedName("imageHeight")
    val imageHeight: Int, // 1453
    @SerializedName("imageSize")
    val imageSize: Int, // 587598
    @SerializedName("imageWidth")
    val imageWidth: Int, // 2165
    @SerializedName("largeImageURL")
    val largeImageURL: String, // https://pixabay.com/get/g3052569047a1ee3daa0a4a807d8655a3b49d80b3ae845aa16556b3e70a54a6efb3e02381904552299c026c81e07f4b761f585c8a6344a6f3905cfddb65c8d05d_1280.jpg
    @SerializedName("likes")
    val likes: Int, // 1656
    @SerializedName("pageURL")
    val pageURL: String, // https://pixabay.com/photos/shoes-legs-car-car-window-woman-434918/
    @SerializedName("previewHeight")
    val previewHeight: Int, // 100
    @SerializedName("previewURL")
    val previewURL: String, // https://cdn.pixabay.com/photo/2014/09/03/20/15/shoes-434918_150.jpg
    @SerializedName("previewWidth")
    val previewWidth: Int, // 150
    @SerializedName("tags")
    val tags: String, // shoes, legs, car
    @SerializedName("type")
    val type: String, // photo
    @SerializedName("user")
    val user: String, // Greyerbaby
    @SerializedName("user_id")
    val userId: Int, // 2323
    @SerializedName("userImageURL")
    val userImageURL: String, // https://cdn.pixabay.com/user/2014/11/14/05-39-07-629_250x250.jpg
    @SerializedName("views")
    val views: Int, // 1050674
    @SerializedName("webformatHeight")
    val webformatHeight: Int, // 429
    @SerializedName("webformatURL")
    val webformatURL: String, // https://pixabay.com/get/g3ce1b947287bdb72efb84af73d180d5472088652e28f8cb8e835a00d0e7850b21bb65f24ef8ad6c578cddf8b419a61f5_640.jpg
    @SerializedName("webformatWidth")
    val webformatWidth: Int // 640
)