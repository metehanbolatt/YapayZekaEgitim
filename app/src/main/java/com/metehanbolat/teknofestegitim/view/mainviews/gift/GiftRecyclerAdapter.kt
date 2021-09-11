package com.metehanbolat.teknofestegitim.view.mainviews.gift

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.metehanbolat.teknofestegitim.R
import com.metehanbolat.teknofestegitim.databinding.GiftCardLayoutBinding
import com.metehanbolat.teknofestegitim.utils.UserCoin

class GiftRecyclerAdapter(private val collectionPath : String, private val documentPath : String, private val coinPath : String, val firestore: FirebaseFirestore, val firebaseAuth: FirebaseAuth, private val giftList : ArrayList<Gift>) : RecyclerView.Adapter<GiftRecyclerAdapter.GiftHolder>() {

    class GiftHolder(val binding : GiftCardLayoutBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GiftHolder {
        val binding = GiftCardLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return GiftHolder(binding)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: GiftHolder, position: Int) {
        holder.binding.cardCoin.text = giftList[position].coin
        holder.binding.recyclerAwardText.text = giftList[position].award

        holder.itemView.setOnLongClickListener {
            val dialogTheme = LayoutInflater.from(holder.itemView.context).inflate(R.layout.gift_alert_dialog,null)
            val builder = AlertDialog.Builder(holder.itemView.context)
            builder.setView(dialogTheme)
            dialogTheme.findViewById<TextView>(R.id.alertTextView).text = giftList[position].award
            dialogTheme.findViewById<TextView>(R.id.alertCoinText).text = giftList[position].coin

            dialogTheme.findViewById<Button>(R.id.alertDeleteButton).setOnClickListener { view ->
                firestore.collection(view.context.resources.getString(R.string.firebase_userData)).document(firebaseAuth.currentUser!!.email.toString()).collection(firebaseAuth.currentUser!!.email.toString()).document(giftList[position].award).delete().addOnSuccessListener {
                    Snackbar.make(view, giftList[position].award + view.context.resources.getString(R.string.this_awards_delete), Snackbar.LENGTH_LONG).show()
                    notifyDataSetChanged()
                }
            }

            dialogTheme.findViewById<Button>(R.id.alertUpdateButton).setOnClickListener {
                val action = GiftFragmentDirections.actionGiftFragmentToGiftAwardsAddFragment(giftList[position].award)
                holder.itemView.findNavController().navigate(action)
            }

            dialogTheme.findViewById<Button>(R.id.alertBuyButton).setOnClickListener {
                val userCoin = UserCoin(firestore, collectionPath, documentPath)
                userCoin.getCoin(coinPath){
                    if (it!! < holder.binding.cardCoin.text.toString().toInt()){
                        Snackbar.make(holder.itemView, holder.itemView.resources.getString(R.string.not_enough_money), Snackbar.LENGTH_LONG).show()
                    }else{
                        userCoin.userCoinDecrease(coinPath, it, holder.binding.cardCoin.text.toString().toInt())
                    }

                }
            }

            builder.show()

            return@setOnLongClickListener true
        }

    }

    override fun getItemCount(): Int {
        return giftList.size
    }

}