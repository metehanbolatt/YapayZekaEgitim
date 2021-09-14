package com.metehanbolat.teknofestegitim.utils

import com.google.firebase.firestore.FirebaseFirestore

class UserFirebaseProcess(var firebaseFirestore: FirebaseFirestore, collectionPath : String, documentPath: String) {

    val getUserCoin = firebaseFirestore.collection(collectionPath).document(documentPath)

    fun getCoin(coinPath : String, callback: (Int?) -> Unit){
        getUserCoin.get().addOnSuccessListener{ document ->
            if (document != null){
                if (document.data != null){
                    val incomingCoin = document.data!![coinPath]!!
                    callback.invoke(incomingCoin.toString().toInt())
                }
            }
        }
    }

    fun userCoinIncrease(coinPath: String, currentCoin: Int, amountOfIncrease: Int) : Int{

        val newCurrentCoin = currentCoin + amountOfIncrease
        getUserCoin.update(coinPath, newCurrentCoin)
        return newCurrentCoin

    }

    fun userCoinDecrease(coinPath: String, currentCoin : Int, amountOfDecrease: Int) : Int{

        val newCurrentCoin = currentCoin - amountOfDecrease
        getUserCoin.update(coinPath, newCurrentCoin)
        return newCurrentCoin

    }

    fun userAchievement(achievementPath : String, achievementPoint : Int){

        getUserCoin.update(achievementPath, achievementPoint)

    }

}