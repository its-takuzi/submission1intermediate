package com.dicoding.picodiploma.loginwithanimation.data.retrofit

import com.dicoding.picodiploma.loginwithanimation.data.response.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @FormUrlEncoded
    @POST("register")
    fun register(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ) : Call<RegisterResponse>

    @FormUrlEncoded
    @POST("login")
    fun login(
        @Field("email") email: String,
        @Field("password") password: String
    ) : Call<LoginResponse>


    @GET("stories")
    suspend fun getStories(
        @Header("Authorization") token: String,
        @Query("page") page: Int = 1,
        @Query("size") size: Int = 20
    ): StoryListResponse

    @Multipart
    @POST("stories")
    suspend fun uploadImage(
        @Header("Authorization")token :String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): UploadResponse

    @GET("stories")
    fun getStoriesWithLocation(
        @Header("Authorization") token: String?,
        @Query("location") location : Int = 1,
    ): Call<StoryListResponse>
}