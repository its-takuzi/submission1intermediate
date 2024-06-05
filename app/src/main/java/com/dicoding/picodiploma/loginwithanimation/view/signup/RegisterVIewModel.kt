package com.dicoding.picodiploma.loginwithanimation.view.signup

import com.dicoding.picodiploma.loginwithanimation.data.response.Register
import com.dicoding.picodiploma.loginwithanimation.data.response.RegisterResponse
import com.dicoding.picodiploma.loginwithanimation.data.retrofit.ApiConfig
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterVIewModel : ViewModel() {
    private val _registerResponse = MutableLiveData<RegisterResponse>()
    val registerresponse : LiveData<RegisterResponse> = _registerResponse

    fun postRegister(authBody : Register) {

        ApiConfig.getApiService().register(authBody.name, authBody.email, authBody.password).enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(call: Call<RegisterResponse>, response: Response<RegisterResponse>){

            if(response.isSuccessful){
                    _registerResponse.postValue(response.body())
                    Log.d("TAG", response.body().toString())
                }else{
                    _registerResponse.postValue(RegisterResponse(true, "null"))
                    Log.d("TAG", "Register Response Failed")
                }
            }
            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                _registerResponse.postValue(RegisterResponse(true, "null"))
                Log.d("TAG", t.message.toString())
            }

        })


    }
}