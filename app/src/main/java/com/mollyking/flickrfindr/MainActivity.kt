package com.mollyking.flickrfindr

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
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
            dismissKeyboard(it)
            viewModel.setQuery(search_box.text.toString())
        }
        results_recycler.layoutManager = LinearLayoutManager(this)
        val adapter = ResultsAdapter(viewModel)
        results_recycler.adapter = adapter

        val photosObserver = Observer<List<FlickrPhoto>> {photos ->
            adapter.setPhotos(photos)
        }
        viewModel.photos.observe(this, photosObserver)

        val selectionObserver = Observer<FlickrPhoto> { selected ->
            val url = selected.getMediumUrl()
            val intent = Intent(applicationContext, PhotoViewerActivity::class.java)
            intent.putExtra(PhotoViewerActivity.PHOTO_EXTRA, url)
            startActivity(intent)
        }
        viewModel.selection.observe(this, selectionObserver)
    }

    fun dismissKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
