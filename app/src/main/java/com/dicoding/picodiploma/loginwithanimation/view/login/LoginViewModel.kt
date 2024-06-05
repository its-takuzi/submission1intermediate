package com.dicoding.picodiploma.loginwithanimation.view.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.picodiploma.loginwithanimation.data.response.Login
import com.dicoding.picodiploma.loginwithanimation.data.response.LoginResponse
import com.dicoding.picodiploma.loginwithanimation.data.response.LoginResult
import com.dicoding.picodiploma.loginwithanimation.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class LoginViewModel : ViewModel(){

    private val _loginresponse = MutableLiveData<LoginResponse>()
    val loginResponse : LiveData<LoginResponse> = _loginresponse

    fun postLogin(authBody: Login){
        ApiConfig.getApiService().login(authBody.Email, authBody.Password).enqueue(object : Callback<LoginResponse>{
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if(response.isSuccessful){
                    Log.d("TAG", response.body().toString())
                    _loginresponse.postValue(response.body())
                }else{
                    Log.d("TAG", "Login Response Failed")
                    _loginresponse.postValue(LoginResponse(true, "null", LoginResult("null", "null", "null") ))

                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.d("TAG", t.message.toString())
                _loginresponse.postValue(LoginResponse(true, "null", LoginResult("null", "null", "null") ))
            }
        })
    }
}