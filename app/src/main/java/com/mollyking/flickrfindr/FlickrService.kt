package com.mollyking.flickrfindr

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface FlickrService {

    @GET("/services/rest/?method=flickr.photos.search&api_key=1508443e49213ff84d566777dc211f2a&format=json&nojsoncallback=1&per_page=25")
    fun searchPhotos(@Query("text") searchTerm: String): Call<SearchResponse>
}