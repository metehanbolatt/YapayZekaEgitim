package com.metehanbolat.teknofestegitim.view.teacherviews.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.metehanbolat.teknofestegitim.databinding.ActivityTeacherBinding

class TeacherActivity : AppCompatActivity() {

    private lateinit var binding : ActivityTeacherBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTeacherBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }
}