package com.voxcom.topplethetiki.ui.game

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.voxcom.topplethetiki.R
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

            // 🔥 MAP tiki string → image
            val tikiRes = when (tiki) {
                "t1" -> R.drawable.t1
                "t2" -> R.drawable.t2
                "t3" -> R.drawable.t3
                "t4" -> R.drawable.t4
                "t5" -> R.drawable.t5
                "t6" -> R.drawable.t6
                "t7" -> R.drawable.t7
                "t8" -> R.drawable.t8
                "t9" -> R.drawable.t9
                else -> R.drawable.t1
            }

            binding.imgTiki.setImageResource(tikiRes)

            binding.root.setOnClickListener {
                onClick(tiki)
            }
        }
    }
}