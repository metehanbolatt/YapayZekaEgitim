package com.metehanbolat.teknofestegitim.view.mainviews.gift

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.metehanbolat.teknofestegitim.R
import com.metehanbolat.teknofestegitim.databinding.FragmentGiftBinding

class GiftFragment : Fragment() {

    private var _binding : FragmentGiftBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController

    private lateinit var giftList : ArrayList<Gift>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGiftBinding.inflate(inflater, container, false)
        val view = binding.root

        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                navController = findNavController()
                navController.navigate(R.id.action_giftFragment_to_mainFragment)

            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(callback)
        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(),R.color.background_color)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        giftList = ArrayList()

        val aqua = Gift("50", R.drawable.aqua)
        val zoo = Gift("40", R.drawable.zoo)
        val aqua2 = Gift("50", R.drawable.aqua)
        val zoo2 = Gift("40", R.drawable.zoo)
        val aqua3 = Gift("50", R.drawable.aqua)
        val zoo3 = Gift("40", R.drawable.zoo)
        val aqua4 = Gift("50", R.drawable.aqua)
        val zoo4 = Gift("40", R.drawable.zoo)
        val aqua5 = Gift("50", R.drawable.aqua)
        val zoo5 = Gift("40", R.drawable.zoo)
        val aqua6 = Gift("50", R.drawable.aqua)
        val zoo6 = Gift("40", R.drawable.zoo)

        giftList.add(aqua)
        giftList.add(zoo)
        giftList.add(aqua2)
        giftList.add(zoo2)
        giftList.add(aqua3)
        giftList.add(zoo3)
        giftList.add(aqua4)
        giftList.add(zoo4)
        giftList.add(aqua5)
        giftList.add(zoo5)
        giftList.add(aqua6)
        giftList.add(zoo6)

        binding.giftRecyclerView.layoutManager = StaggeredGridLayoutManager(2,GridLayoutManager.VERTICAL)
        val giftAdapter = GiftRecyclerAdapter(giftList)
        binding.giftRecyclerView.adapter = giftAdapter

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}