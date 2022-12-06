package com.example.myapplication.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.inputmethod.InputBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.databinding.ViewHolderMainBinding
import javax.inject.Inject

class MainAdapter @Inject constructor(): RecyclerView.Adapter<MainAdapter.ViewHolder>(){
    inner class ViewHolder(val viewHolderMainBinding: ViewHolderMainBinding): RecyclerView.ViewHolder(viewHolderMainBinding.root)

      private var list = mutableListOf<String>()
     fun addList(list: List<String>){
         this.list.addAll(list)
         notifyDataSetChanged()
     }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ViewHolderMainBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val binding = holder.viewHolderMainBinding
        binding.tvTitle.text = "rahul"
    }

    override fun getItemCount(): Int {
        return 1 // list size
    }
}