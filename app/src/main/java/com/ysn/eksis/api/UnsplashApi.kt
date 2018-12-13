package com.ysn.eksis.api

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET

interface UnsplashApi {

    @GET("photos/random?client_id=fcc380bc5c72c1db2fea2f3aeb23690aa0676e8c9cb841660d7719c3c960ad67")
    fun getRandomImage(): Observable<ResponseBody>

}