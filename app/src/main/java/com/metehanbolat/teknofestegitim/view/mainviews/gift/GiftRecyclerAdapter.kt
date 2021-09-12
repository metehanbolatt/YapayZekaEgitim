package com.metehanbolat.teknofestegitim.view.mainviews.gift

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.metehanbolat.teknofestegitim.R
import com.metehanbolat.teknofestegitim.databinding.FragmentGiftBinding
import com.metehanbolat.teknofestegitim.databinding.GiftAlertDialogBinding
import com.metehanbolat.teknofestegitim.databinding.GiftCardLayoutBinding
import com.metehanbolat.teknofestegitim.utils.UserCoin

class GiftRecyclerAdapter(private val collectionPath : String, private val documentPath : String, private val coinPath : String, val firestore: FirebaseFirestore, val firebaseAuth: FirebaseAuth, private val giftList : ArrayList<Gift>) : RecyclerView.Adapter<GiftRecyclerAdapter.GiftHolder>() {

    class GiftHolder(val binding : GiftCardLayoutBinding, val dialogTheme : GiftAlertDialogBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GiftHolder {
        val binding = GiftCardLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val dialogTheme = GiftAlertDialogBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return GiftHolder(binding, dialogTheme)
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: GiftHolder, position: Int) {
        holder.binding.cardCoin.text = giftList[position].coin
        holder.binding.recyclerAwardText.text = giftList[position].award

        holder.itemView.setOnClickListener { holderView ->

            val builder = AlertDialog.Builder(holderView.context).create()
            builder.setView(holder.dialogTheme.root)
            holder.dialogTheme.alertTextView.text = giftList[position].award
            holder.dialogTheme.alertCoinText.text = giftList[position].coin

            holder.dialogTheme.alertDeleteButton.setOnClickListener { deleteButtonView ->
                firestore.collection(deleteButtonView.context.resources.getString(R.string.firebase_userData)).document(firebaseAuth.currentUser!!.email.toString()).collection(firebaseAuth.currentUser!!.email.toString()).document(giftList[position].award).delete().addOnSuccessListener {
                    notifyDataSetChanged()
                }
                Snackbar.make(holderView, giftList[position].award + deleteButtonView.context.resources.getString(R.string.this_awards_delete), Snackbar.LENGTH_LONG).show()
                (holder.dialogTheme.root.parent as ViewGroup).removeView(holder.dialogTheme.root)
                builder.dismiss()
            }

            holder.dialogTheme.alertUpdateButton.setOnClickListener { updateButtonView ->
                val action = GiftFragmentDirections.actionGiftFragmentToGiftAwardsAddFragment(giftList[position].award)
                holder.itemView.findNavController().navigate(action)
                (holder.dialogTheme.root.parent as ViewGroup).removeView(holder.dialogTheme.root)
                builder.dismiss()
            }

            holder.dialogTheme.alertBuyButton.setOnClickListener { buyButtonView ->
                val userCoin = UserCoin(firestore, collectionPath, documentPath)
                userCoin.getCoin(coinPath){
                    if (it!! < holder.binding.cardCoin.text.toString().toInt()){
                        Snackbar.make(holderView, buyButtonView.context.resources.getString(R.string.not_enough_money), Snackbar.LENGTH_LONG).show()
                        (holder.dialogTheme.root.parent as ViewGroup).removeView(holder.dialogTheme.root)
                        builder.dismiss()
                    }else{
                        firestore.collection(buyButtonView.context.resources.getString(R.string.firebase_userData)).document(firebaseAuth.currentUser!!.email.toString()).collection(firebaseAuth.currentUser!!.email.toString()).document(giftList[position].award).delete().addOnSuccessListener {
                            notifyDataSetChanged()
                        }
                        userCoin.userCoinDecrease(coinPath, it, holder.binding.cardCoin.text.toString().toInt())
                        Snackbar.make(holderView, buyButtonView.context.resources.getString(R.string.congratulations_gift) + holder.binding.recyclerAwardText.text.toString() + buyButtonView.context.resources.getString(R.string.prize), Snackbar.LENGTH_LONG).show()
                        (holder.dialogTheme.root.parent as ViewGroup).removeView(holder.dialogTheme.root)
                        builder.dismiss()
                    }
                }
            }
            builder.setOnCancelListener {
                (holder.dialogTheme.root.parent as ViewGroup).removeView(holder.dialogTheme.root)
            }

            builder.show()
        }
    }

    override fun getItemCount(): Int {
        return giftList.size
    }

}