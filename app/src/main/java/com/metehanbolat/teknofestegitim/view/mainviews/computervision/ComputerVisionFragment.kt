package com.metehanbolat.teknofestegitim.view.mainviews.computervision

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.metehanbolat.teknofestegitim.R
import com.metehanbolat.teknofestegitim.databinding.FragmentComputerVisionBinding
import kotlin.math.abs

class ComputerVisionFragment : Fragment() {

    private var _binding : FragmentComputerVisionBinding? = null
    private val binding get() = _binding!!

    private var userEmail : String? = null
    private var userCoin : Int? = null
    private var firstCoinControl : Any? = null
    private var secondCoinControl : Any? = null

    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private lateinit var navController: NavController
    private lateinit var viewPager2: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentComputerVisionBinding.inflate(inflater, container, false)
        val view = binding.root

        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                navController = findNavController()
                navController.navigate(R.id.action_computerVisionFragment_to_mainFragment)
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(callback)
        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(),R.color.header_status_bar)

        auth = Firebase.auth
        firestore = Firebase.firestore

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            userEmail = ComputerVisionFragmentArgs.fromBundle(it).userEmail
            userCoin = ComputerVisionFragmentArgs.fromBundle(it).coin
        }

        viewPager2 = binding.viewPagerImageSlider

        val sliderItems : MutableList<SliderItem> = ArrayList()
        sliderItems.add(SliderItem(R.drawable.vision_bee))
        sliderItems.add(SliderItem(R.drawable.vision_rabbit))
        sliderItems.add(SliderItem(R.drawable.vision_snake))
        sliderItems.add(SliderItem(R.drawable.vision_bat))
        sliderItems.add(SliderItem(R.drawable.computer_vision_two))

        viewPager2.adapter = SliderAdapter(sliderItems, viewPager2)

        viewPager2.clipToPadding = false
        viewPager2.clipChildren = false
        viewPager2.offscreenPageLimit = 3
        viewPager2.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

        val compositePageTransformer = CompositePageTransformer()
        compositePageTransformer.addTransformer(MarginPageTransformer(30))
        compositePageTransformer.addTransformer{page, position ->
            val r = 1 - abs(position)
            page.scaleY = 0.85f + r * 0.25f
        }
        viewPager2.setPageTransformer(compositePageTransformer)
        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when(position){
                    0 -> binding.canliText.text = resources.getString(R.string.bee)
                    1 -> binding.canliText.text = resources.getString(R.string.rabbit)
                    2 -> binding.canliText.text = resources.getString(R.string.snake)
                    3 -> binding.canliText.text = resources.getString(R.string.bat).also {
                        binding.button.visibility = View.INVISIBLE
                    }
                    4 -> binding.canliText.text = resources.getString(R.string.computer).also {
                        binding.button.visibility = View.VISIBLE

                    }
                }
            }
        })

        binding.button.setOnClickListener {
            getNextFragment()
            auth.currentUser?.email?.let { email -> getUserData(email) }
        }

    }

    private fun updateCoin(){
        val coinUpdate = firestore.collection("UserData").document(userEmail!!)
        coinUpdate.update("userCoin", userCoin?.plus(10))
        coinUpdate.update("firstCoin",1)
    }

    private fun getNextFragment(){
        val getCoinInfo = firestore.collection("UserData").document(auth.currentUser?.email.toString())
        getCoinInfo.get().addOnSuccessListener { document ->
            if (document != null){
                if (document.data != null){
                    secondCoinControl = document.data!!["secondCoin"]
                    val action = ComputerVisionFragmentDirections.actionComputerVisionFragmentToComputerVisionInfoFragment(secondCoinControl.toString().toInt())
                    Navigation.findNavController(requireView()).navigate(R.id.action_computerVisionFragment_to_computerVisionInfoFragment,action.arguments)
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun getUserData(name : String){
        val getUserData = firestore.collection("UserData").document(name)
        getUserData.get().addOnSuccessListener { document ->
            if (document != null){
                if (document.data != null){
                    firstCoinControl = document.data!!["userCoin"]
                    if (firstCoinControl.toString().toInt() == 0){
                        updateCoin()
                        Snackbar.make(requireView(),resources.getString(R.string.earn_ten_gold),Snackbar.LENGTH_SHORT).show()
                    }else{
                        Snackbar.make(requireView(),resources.getString(R.string.earned_gold),Snackbar.LENGTH_SHORT).show()
                    }
                }else{
                    Snackbar.make(requireView(),resources.getString(R.string.no_user_data), Snackbar.LENGTH_SHORT).show()

                }
            }
        }.addOnFailureListener {
            Toast.makeText(requireContext(), it.localizedMessage, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}