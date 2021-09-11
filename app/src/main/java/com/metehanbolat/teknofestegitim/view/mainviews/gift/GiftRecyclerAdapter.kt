package com.metehanbolat.teknofestegitim.view.mainviews.gift

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.metehanbolat.teknofestegitim.R
import com.metehanbolat.teknofestegitim.databinding.GiftCardLayoutBinding
import com.metehanbolat.teknofestegitim.utils.UserCoin

class GiftRecyclerAdapter(private val collectionPath : String, private val documentPath : String, private val coinPath : String, val firestore: FirebaseFirestore, private val giftList : ArrayList<Gift>) : RecyclerView.Adapter<GiftRecyclerAdapter.GiftHolder>() {

    class GiftHolder(val binding : GiftCardLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GiftHolder {
        val binding = GiftCardLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return GiftHolder(binding)
    }

    override fun onBindViewHolder(holder: GiftHolder, position: Int) {
        holder.binding.cardCoin.text = giftList[position].coin
        holder.binding.recyclerAwardText.text = giftList[position].award
        holder.itemView.setOnClickListener {
            val userCoin = UserCoin(firestore, collectionPath, documentPath)
            userCoin.getCoin(coinPath){
                if (it!! < holder.binding.cardCoin.text.toString().toInt()){
                    Snackbar.make(holder.itemView, holder.itemView.resources.getString(R.string.not_enough_money), Snackbar.LENGTH_LONG).show()
                }else{
                    userCoin.userCoinDecrease(coinPath, it, holder.binding.cardCoin.text.toString().toInt())
                }

            }
        }
    }

    override fun getItemCount(): Int {
        return giftList.size
    }

}