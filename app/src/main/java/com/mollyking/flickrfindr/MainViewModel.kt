package com.mollyking.flickrfindr

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject

class MainViewModel @Inject constructor(val service: FlickrService): ViewModel() {

    companion object {
        const val TAG= "Main View Model"
    }

    val isLoading: MutableLiveData<Boolean> by lazy {
        MutableLiveData(false)
    }
    val photos: MutableLiveData<List<FlickrPhoto>> by lazy {
        MutableLiveData<List<FlickrPhoto>>(listOf())
    }
    val selection: MutableLiveData<FlickrPhoto> by lazy {
        MutableLiveData<FlickrPhoto>()
    }
    val errorMessage: MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }

    private var query: String? = null

    fun setQuery(newQuery: String) {
        query = newQuery
        isLoading.value = true
        photos.value = listOf()
        service.searchPhotos(query!!).enqueue(object : Callback<SearchResponse> {
            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                isLoading.value = false
                errorMessage.postValue(t.localizedMessage)
            }

            override fun onResponse(call: Call<SearchResponse>, response: Response<SearchResponse>) {
                isLoading.value = false
                if (response.isSuccessful && response.body() != null) {
                    val searchResponse = response.body()!!
                    if (searchResponse.stat == "ok") {
                        photos.value = searchResponse.photos.photo
                    } else if (searchResponse.stat == "fail") {
                        val errMessage = searchResponse.message
                        errorMessage.postValue(errMessage)
                    }
                } else {
                    errorMessage.postValue("Request Unsuccessful. Response code ${response.code()}")
                }
            }
        })
    }

    fun selectItem(photo: FlickrPhoto) {
        selection.postValue(photo)
    }
}