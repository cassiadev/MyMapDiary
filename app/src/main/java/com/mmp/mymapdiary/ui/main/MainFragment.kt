package com.mmp.mymapdiary.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.Observer
import com.mmp.mymapdiary.R
import com.mmp.mymapdiary.databinding.MainFragmentBinding
import com.mmp.mymapdiary.data.main.MainViewModel
import kotlinx.android.synthetic.main.main_fragment.*

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private val mainViewModel by lazy {
        ViewModelProvider(activity as MainActivity).get(MainViewModel::class.java)
    }
    private lateinit var binding: MainFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = MainFragmentBinding.inflate(inflater, container, false)
        binding.viewModel = mainViewModel

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        mainViewModel.viewModelText.observe(activity as LifecycleOwner,
            Observer {
                binding.mainExpressMainViewModel.text = it
            }
        )
        mainViewModel.viewModelCounter.observe(activity as LifecycleOwner,
            Observer {
                binding.mainExpressMainViewModelTest1.text = it.toString()
            }
        )

        // Go to FreeTryFragment
        button_goToFreeTryFragment.setOnClickListener{
            activity!!.supportFragmentManager.beginTransaction()
                .replace(R.id.container, FreeTryFragment.newInstance())
                .addToBackStack(null)
                .commit()
        }
    }
}
