package com.metehanbolat.teknofestegitim.view.teacherviews

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.metehanbolat.teknofestegitim.databinding.ActivityTeacherBinding
import com.metehanbolat.teknofestegitim.view.userviews.UserActivity

class TeacherActivity : AppCompatActivity() {

    private lateinit var binding : ActivityTeacherBinding

    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeacherBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        firebaseAuth = Firebase.auth

        binding.back.setOnClickListener {
            firebaseAuth.signOut()
            val intent = Intent(this, UserActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}