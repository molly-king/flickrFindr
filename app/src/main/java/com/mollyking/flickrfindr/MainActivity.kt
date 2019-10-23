package com.mollyking.flickrfindr

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.mollyking.flickrfindr.databinding.ActivityMainBinding
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    internal lateinit var viewModelFactory: ViewModelFactory

    lateinit var viewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MainViewModel::class.java)
        val binding : ActivityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this
        search_button.setOnClickListener {
            viewModel.setQuery(search_box.text.toString())
        }
        results_recycler.layoutManager = LinearLayoutManager(this)
        val adapter = ResultsAdapter()
        results_recycler.adapter = adapter

        val photosObserver = Observer<List<FlickrPhoto>> {photos ->
            adapter.setPhotos(photos)
        }
        viewModel.photos.observe(this, photosObserver)
    }
}
