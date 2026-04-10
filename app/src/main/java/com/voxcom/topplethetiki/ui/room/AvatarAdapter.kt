package com.voxcom.topplethetiki.ui.room

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.voxcom.topplethetiki.databinding.ItemAvatarBinding

class AvatarAdapter(
    private val onClick: (String) -> Unit
) : RecyclerView.Adapter<AvatarAdapter.AvatarViewHolder>() {

    private val avatars = listOf("a1", "a2", "a3", "a4")

    private var selectedAvatar: String? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AvatarViewHolder {
        val binding = ItemAvatarBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AvatarViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AvatarViewHolder, position: Int) {
        holder.bind(avatars[position])
    }

    override fun getItemCount(): Int = avatars.size

    inner class AvatarViewHolder(
        private val binding: ItemAvatarBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(avatar: String) {

            // TEMP placeholder image
            binding.imgAvatar.setImageResource(android.R.drawable.ic_menu_gallery)

            // highlight selected
            binding.imgAvatar.alpha = if (avatar == selectedAvatar) 1f else 0.5f

            binding.root.setOnClickListener {
                selectedAvatar = avatar
                notifyDataSetChanged()
                onClick(avatar)
            }
        }
    }
}