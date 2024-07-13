package com.example.smartpanel

import com.example.smartpanel.model.LoginModel
import com.example.smartpanel.LoginResponseData
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("/auth")
    fun loginUser(@Body loginModel: LoginModel): Call<LoginResponseData>
}