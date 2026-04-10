package com.voxcom.topplethetiki.ui.game

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.voxcom.topplethetiki.databinding.ItemTikiBinding

class GameAdapter(
    private val onClick: (String) -> Unit
) : RecyclerView.Adapter<GameAdapter.ViewHolder>() {

    private val list = mutableListOf<String>()

    fun submitList(newList: List<String>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemTikiBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

    inner class ViewHolder(private val binding: ItemTikiBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(tiki: String) {
            binding.tvTiki.text = tiki
            binding.root.setOnClickListener { onClick(tiki) }
        }
    }
}