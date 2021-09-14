package com.metehanbolat.teknofestegitim.view.mainviews.achievement

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.metehanbolat.teknofestegitim.databinding.RecyclerAchievementRowBinding

class AchievementRecyclerAdapter(private val achievementList : ArrayList<String>) : RecyclerView.Adapter<AchievementRecyclerAdapter.AchievementViewHolder>() {

    class AchievementViewHolder(val binding : RecyclerAchievementRowBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AchievementViewHolder {
        val binding = RecyclerAchievementRowBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return AchievementViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AchievementViewHolder, position: Int) {
        holder.binding.recyclerAchievementText.text = achievementList[position]
    }

    override fun getItemCount(): Int {
        return achievementList.size
    }
}