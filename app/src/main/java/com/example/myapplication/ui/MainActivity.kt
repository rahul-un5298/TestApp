package com.example.myapplication.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.example.myapplication.base.TestApplication
import com.example.myapplication.databinding.ActivityMainBinding
import com.example.myapplication.ui.adapter.MainAdapter
import com.example.myapplication.viewmodel.TestViewModel
import com.example.myapplication.viewmodel.ViewModelFactory
import javax.inject.Inject

class MainActivity : AppCompatActivity() {
    private var _binding: ActivityMainBinding? = null
    private val binding:ActivityMainBinding
    get() = _binding!!

    @Inject
    lateinit var mainAdapter: MainAdapter

    @Inject
    lateinit var factory: ViewModelFactory

    @Inject
    lateinit var viewModel: TestViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TestApplication.getApplication()?.appComponent?.inject(this)
        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        observerData()
        viewModel.fetchListData()
    }

    private fun observerData() {
         // viewModel.productList.observe(lifecycleScope){}
    }

    private fun initView() {
       binding.recyclerView.adapter = mainAdapter
    }
}