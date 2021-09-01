package com.metehanbolat.teknofestegitim.view.mainviews.gift

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.metehanbolat.teknofestegitim.databinding.GiftCardLayoutBinding

class GiftRecyclerAdapter(private val giftList : ArrayList<Gift>) : RecyclerView.Adapter<GiftRecyclerAdapter.GiftHolder>() {

    class GiftHolder(val binding : GiftCardLayoutBinding) : RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GiftHolder {
        val binding = GiftCardLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return GiftHolder(binding)
    }

    override fun onBindViewHolder(holder: GiftHolder, position: Int) {
        holder.binding.cardCoin.text = giftList[position].giftCoin
        holder.binding.cardImageView.setImageResource(giftList[position].giftImage)
    }

    override fun getItemCount(): Int {
        return giftList.size
    }
}