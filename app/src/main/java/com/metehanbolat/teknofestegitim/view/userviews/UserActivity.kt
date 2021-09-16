package com.metehanbolat.teknofestegitim.view.userviews

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.metehanbolat.teknofestegitim.R
import com.metehanbolat.teknofestegitim.databinding.ActivityUserBinding
import com.metehanbolat.teknofestegitim.view.mainviews.main.MainActivity
import com.metehanbolat.teknofestegitim.view.teacherviews.main.TeacherActivity

class UserActivity : AppCompatActivity() {

    private lateinit var binding : ActivityUserBinding
    private lateinit var auth : FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.Theme_TeknofestEgitim)
        binding = ActivityUserBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        auth = Firebase.auth
    }

    override fun onStart() {
        super.onStart()
        val currentUser = auth.currentUser
        if (currentUser != null){
            val controlMail = getFirst(currentUser.email!!)
            if (controlMail == resources.getString(R.string._tmail)){
                val intent = Intent(baseContext, TeacherActivity::class.java)
                startActivity(intent)
                finish()
            }else if (controlMail == resources.getString(R.string._smail)){
                val intent = Intent(baseContext, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    private fun getFirst(word:String) : String{
        var length = 0
        var subsEmail : String? = null
        while(length <= word.length - 1){
            if (word[length].toString() == resources.getString(R.string._et)){
                subsEmail = (word.substring(length + 1, word.length))
            }
            length += 1
        }
        return subsEmail.toString()
    }

}