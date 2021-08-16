package com.metehanbolat.teknofestegitim.view.mainviews.computervision

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.makeramen.roundedimageview.RoundedImageView
import com.metehanbolat.teknofestegitim.databinding.SlideItemContainerBinding

class SliderAdapter(sliderItems : MutableList<SliderItem>, viewPager : ViewPager2) : RecyclerView.Adapter<SliderAdapter.SliderViewHolder>() {

    private val sliderItems : List<SliderItem>

    init {
        this.sliderItems = sliderItems
    }

    class SliderViewHolder(val binding: SlideItemContainerBinding) : RecyclerView.ViewHolder(binding.root){
        private val imageView : RoundedImageView = binding.imageSlide

        fun image(sliderItem: SliderItem){
            imageView.setImageResource(sliderItem.image)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SliderViewHolder {
        val binding = SlideItemContainerBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return SliderViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SliderViewHolder, position: Int) {
        holder.image(sliderItems[position])
    }

    override fun getItemCount(): Int {
        return sliderItems.size
    }
}
