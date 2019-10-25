# FlickrFindr

A small, multi-activity demo app using the Flickr search API.

Enter your search term in the EditText on the first screen and tap search. 25 results from the Flickr API will load in the recyclerview below. Simply tap on a list item to launch the second activity and view a larger version of the photo.

## Third-party libraries

- Fresco - Simple image loading
- Retrofit/OkHttp - Networking
- Dagger2/Dagger Android - Dependency injection

## Architecture

MainActivity uses an MVVM architecture with an injected ViewModel. It uses databinding to show/hide the loading ProgressBar. It uses an observer pattern to interact with the ViewModel and update the recycler with new data when it becomes available.
