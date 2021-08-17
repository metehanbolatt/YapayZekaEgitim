package com.metehanbolat.teknofestegitim.view.mainviews

import  androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.core.view.GravityCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.metehanbolat.teknofestegitim.R
import com.metehanbolat.teknofestegitim.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        //window.statusBarColor = ContextCompat.getColor(this@MainActivity, R.color.header_status_bar)

        binding.navigationView.setNavigationItemSelectedListener {
            navController = findNavController(R.id.fragmentContainerView2)
            when(it.itemId){

                R.id.menuInbox -> navController.navigate(R.id.action_mainFragment_to_giftFragment).also {
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                }
                R.id.menuInbox2 -> navController.navigate(R.id.action_giftFragment_to_mainFragment).also{
                    binding.drawerLayout.closeDrawer(GravityCompat.START)
                }
            }

            true
        }
    }
}