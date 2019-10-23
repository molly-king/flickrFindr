package com.mollyking.flickrfindr

data class PhotosPage(val page: Int, val pages: Int, val perpage: Int, val total: String, val photo: List<FlickrPhoto>)