package com.mollyking.flickrfindr

import androidx.lifecycle.Observer
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.Call
import retrofit2.Callback
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import org.junit.rules.TestRule
import org.junit.Rule
import org.mockito.ArgumentCaptor
import org.mockito.Captor
import org.mockito.Mockito
import retrofit2.Response


@RunWith(MockitoJUnitRunner::class)
class MainViewModelTests {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    lateinit var service: FlickrService

    @Mock
    private lateinit var mockSearchCall: Call<SearchResponse>

    @Captor
    private lateinit var searchCaptor: ArgumentCaptor<Callback<SearchResponse>>

    @Mock
    private lateinit var errorMessageObserver: Observer<String>

    @Mock
    private lateinit var photosObserver: Observer<List<FlickrPhoto>>

    private lateinit var viewModel: MainViewModel

    @Before
    fun setup() {
        viewModel = MainViewModel(service)
    }

    @Test
    fun testServiceFailurePostsErrorMessage() {
        viewModel.errorMessage.observeForever(errorMessageObserver)
        Mockito.`when`(service.searchPhotos(Mockito.anyString())).thenReturn(mockSearchCall)

        viewModel.setQuery("Dogs")
        Mockito.verify(mockSearchCall).enqueue(searchCaptor.capture())
        searchCaptor.value.onFailure(mockSearchCall, Throwable("Error Message"))
        Mockito.verify(errorMessageObserver).onChanged("Error Message")
    }

    @Test
    fun testUnsuccessfulResponsePostsErrorMessage() {
        viewModel.errorMessage.observeForever(errorMessageObserver)
        Mockito.`when`(service.searchPhotos(Mockito.anyString())).thenReturn(mockSearchCall)

        viewModel.setQuery("Dogs")
        Mockito.verify(mockSearchCall).enqueue(searchCaptor.capture())
        searchCaptor.value.onResponse(mockSearchCall, getUnsuccessfulResponse())
        Mockito.verify(errorMessageObserver).onChanged("Request Unsuccessful. Response code 404")
    }

    @Test
    fun testSuccessfulResponseCallsObserver() {
        viewModel.photos.observeForever(photosObserver)
        Mockito.`when`(service.searchPhotos(Mockito.anyString())).thenReturn(mockSearchCall)

        viewModel.setQuery("Dogs")
        Mockito.verify(mockSearchCall).enqueue(searchCaptor.capture())
        searchCaptor.value.onResponse(mockSearchCall, getSuccessfulResponse())
        Mockito.verify(photosObserver).onChanged(getMockPhotoData())
    }

    @Test
    fun testStatFailPostsErrorMessage() {
        viewModel.errorMessage.observeForever(errorMessageObserver)
        Mockito.`when`(service.searchPhotos(Mockito.anyString())).thenReturn(mockSearchCall)

        viewModel.setQuery("Dogs")
        Mockito.verify(mockSearchCall).enqueue(searchCaptor.capture())
        searchCaptor.value.onResponse(mockSearchCall, getFailedResponse())
        Mockito.verify(errorMessageObserver).onChanged("Request Failure")
    }

    fun getMockSuccessfulSearchResponse(): SearchResponse {
        return SearchResponse(getMockPhotoPage(), "ok", null)
    }

    fun getMockFailSearchResponse(): SearchResponse {
        return SearchResponse(getMockPhotoPage(), "fail", "Request Failure")
    }

    fun getMockPhotoPage(): PhotosPage {
        return PhotosPage(1, 2, 25, "50", getMockPhotoData())
    }

    fun getMockPhotoData() : List<FlickrPhoto> {
        val one = FlickrPhoto("id1", "Title1", 1, "server1", "secret1")
        val two = FlickrPhoto("id2", "Title2", 2, "server2", "secret2")
        return listOf(one, two)
    }

    fun getUnsuccessfulResponse(): Response<SearchResponse> {
        val json = "application/json; charset=utf-8".toMediaType()
        return Response.error(404, "{}".toResponseBody(json))

    }

    fun getSuccessfulResponse(): Response<SearchResponse> {
        return Response.success(getMockSuccessfulSearchResponse())
    }

    fun getFailedResponse(): Response<SearchResponse> {
        return Response.success(getMockFailSearchResponse())
    }
}