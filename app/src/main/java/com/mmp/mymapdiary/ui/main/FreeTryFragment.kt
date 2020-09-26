package com.mmp.mymapdiary.ui.main

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import com.mmp.mymapdiary.data.main.MainViewModel
import com.mmp.mymapdiary.databinding.FreeTryFragmentBinding
import kotlinx.android.synthetic.main.free_try_fragment.*

class FreeTryFragment: Fragment() {
    companion object {
        fun newInstance() = FreeTryFragment()
    }

    private val mainViewModel by lazy {
        ViewModelProvider(activity as MainActivity).get(MainViewModel::class.java)
    }
    private lateinit var binding: FreeTryFragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        binding = FreeTryFragmentBinding.inflate(inflater, container, false)
        binding.viewModel = mainViewModel

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        this.mainViewModel.run {
            viewModelText.observe(activity as LifecycleOwner,
                Observer {
                    binding.freeTryObserverText.text = it
                }
            )
            viewModelCounter.observe(activity as LifecycleOwner,
                Observer {
                    binding.freeTryObserverNumber.text = it.toString()
                }
            )
            regexInputText.observe(activity as LifecycleOwner,
                Observer {
                    binding.freeTryRegexInput.selectAll()
                }
            )
            regexResultText.observe(activity as LifecycleOwner,
                Observer {
                    binding.freeTryRegexResult.text = it
                }
            )
            apolloText.observe(activity as LifecycleOwner,
                Observer {
                    binding.freeTryApolloGraphqlResult.text = it
                }
            )
        }

        // Regex Test
        val testerRegex = """[^0-9가-힣\u30A1-\u30F6\u30FB\u3000]""".toRegex()
        freeTry_regex_buttonApply.setOnClickListener {
//            freeTry_regex_result.text = testerRegex.replace(freeTry_regex_input.text, "?")
            val resultText = testerRegex.replace(freeTry_regex_input.text, "?")
            mainViewModel.applyRegex(resultText)
        }
    }
}