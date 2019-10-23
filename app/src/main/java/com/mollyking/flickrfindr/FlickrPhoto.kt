package com.mollyking.flickrfindr

data class FlickrPhoto(val id: String,
                       val title: String,
                       val farm: Int,
                       val server: String,
                       val secret: String) {

    fun getThumbnailUrl(): String {
        return "https://farm$farm.staticflickr.com/$server/${id}_${secret}_s.jpg"
    }

    fun getMediumUrl(): String {
        return "https://farm$farm.staticflickr.com/$server/${id}_${secret}_c.jpg"
    }
}