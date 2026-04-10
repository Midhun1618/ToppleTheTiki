package com.voxcom.topplethetiki.ui.game

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.voxcom.topplethetiki.R
import com.voxcom.topplethetiki.databinding.ItemCardBinding

class CardAdapter(
    private val onClick: (String) -> Unit
) : RecyclerView.Adapter<CardAdapter.ViewHolder>() {

    private val list = mutableListOf<String>()

    fun submitList(newList: List<String>) {
        list.clear()
        list.addAll(newList)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCardBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

    inner class ViewHolder(private val binding: ItemCardBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(card: String) {

            val res = when (card) {
                "up1" -> R.drawable.card_1up
                "up2" -> R.drawable.card_2up
                "up3" -> R.drawable.card_3up
                "top" -> R.drawable.card_top
                "bottom" -> R.drawable.card_bottom
                "toss" -> R.drawable.card_toss
                else -> R.drawable.card_1up
            }

            binding.imgCard.setImageResource(res)

            binding.root.setOnClickListener {
                onClick(card)
            }
        }
    }
}