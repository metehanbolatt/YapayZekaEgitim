package com.metehanbolat.teknofestegitim.view.teacherviews

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.metehanbolat.teknofestegitim.R
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