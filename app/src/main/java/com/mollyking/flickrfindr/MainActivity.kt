package com.mollyking.flickrfindr

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.mollyking.flickrfindr.databinding.ActivityMainBinding
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    internal lateinit var viewModelFactory: ViewModelFactory

    lateinit var viewModel: MainViewModel

    lateinit var adapter: ResultsAdapter

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
        adapter = ResultsAdapter(viewModel)
        results_recycler.adapter = adapter

        setSupportActionBar(main_toolbar)

        setObservers()
    }

    fun setObservers() {
        val photosObserver = Observer<List<FlickrPhoto>> {photos ->
            adapter.setPhotos(photos)
        }
        viewModel.photos.observe(this, photosObserver)

        val selectionObserver = Observer<FlickrPhoto> { selected ->
            val url = selected.getLargeUrl()
            val intent = Intent(applicationContext, PhotoViewerActivity::class.java)
            intent.putExtra(PhotoViewerActivity.PHOTO_EXTRA, url)
            startActivity(intent)
        }
        viewModel.selection.observe(this, selectionObserver)

        val errorMessageObserver = Observer<String> { message ->
            Snackbar.make(results_recycler, message, Snackbar.LENGTH_SHORT).show()
        }
        viewModel.errorMessage.observe(this, errorMessageObserver)

    }

    fun dismissKeyboard(view: View) {
        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}
