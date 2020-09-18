package com.mmp.mymapdiary.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.mmp.mymapdiary.R
import com.mmp.mymapdiary.data.main.MainViewModel
import com.mmp.mymapdiary.databinding.MainActivityBinding

class MainActivity : AppCompatActivity() {
    private val mainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding: MainActivityBinding =
            DataBindingUtil.setContentView(this,
                R.layout.main_activity
            )
        binding.lifecycleOwner = this
        binding.viewModel = mainViewModel

        // MainFragment is shown as default in the container
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }
}
