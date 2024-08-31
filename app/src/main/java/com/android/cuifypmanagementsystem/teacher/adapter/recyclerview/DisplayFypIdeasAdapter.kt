package com.android.cuifypmanagementsystem.teacher.adapter.recyclerview

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.android.cuifypmanagementsystem.databinding.RvItemFypIdeaBinding
import com.android.cuifypmanagementsystem.datamodels.FypIdea


class DisplayFypIdeasAdapter(private var ideas: List<FypIdea>) :
    RecyclerView.Adapter<DisplayFypIdeasAdapter.FypIdeaViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FypIdeaViewHolder {
        val binding = RvItemFypIdeaBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return FypIdeaViewHolder(binding)
    }

    override fun onBindViewHolder(holder: FypIdeaViewHolder, position: Int) {
        val fypIdea = ideas[position]
        holder.bind(fypIdea)
    }

    override fun getItemCount(): Int = ideas.size

    fun updateIdeas(ideas: List<FypIdea>) {
        this.ideas = ideas
        notifyDataSetChanged()
        Log.d("TestingDisplayIdeas","data in adapter update: ${ideas}")
    }

    class FypIdeaViewHolder(private val binding: RvItemFypIdeaBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(fypIdea: FypIdea) {
            binding.tvFypIdeaTitle.text = fypIdea.title ?: "No Title"
            binding.tvFypIdeaDescription.text = fypIdea.description ?: "No Description"
            binding.tvFypIdeaLinks.text = fypIdea.links?.joinToString("\n") ?: "No Links"
            binding.tvFypIdeaAuthor.text = fypIdea.author ?: "Anonymous"
            binding.tvFypIdeaDateTime.text = fypIdea.dateTime.toString() // Format date as needed
        }
    }
}
