package com.metehanbolat.teknofestegitim.view.userviews

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.metehanbolat.teknofestegitim.R
import com.metehanbolat.teknofestegitim.databinding.FragmentUserSignInBinding
import com.metehanbolat.teknofestegitim.view.mainviews.main.MainActivity
import com.metehanbolat.teknofestegitim.view.teacherviews.TeacherActivity

class UserSignInFragment : Fragment() {

    private var _binding : FragmentUserSignInBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController : NavController
    private lateinit var auth : FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserSignInBinding.inflate(inflater, container, false)
        val view = binding.root

        auth = Firebase.auth

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.userButtonSignIn.setOnClickListener{
            val email = binding.userNameSignIn.text.toString()
            val password = binding.userPasswordSignIn.text.toString()

            if (email.isEmpty() || password.isEmpty()){
                Snackbar.make(it, resources.getString(R.string.email_or_password_empty), Snackbar.LENGTH_SHORT).show()
            }else{
                val newEmail = getFirst(email)
                auth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
                    if (newEmail == resources.getString(R.string._tmail)){
                        val intent = Intent(requireContext(), TeacherActivity::class.java)
                        startActivity(intent)
                        activity?.finish()
                    }else if (newEmail == resources.getString(R.string._smail)){
                        val intent = Intent(requireContext(), MainActivity::class.java)
                        startActivity(intent)
                        activity?.finish()
                    }
                }.addOnFailureListener { e ->
                    when(e.localizedMessage){
                        resources.getString(R.string.firebase_mail_error) -> Snackbar.make(view, resources.getString(R.string.please_check_your_email), Snackbar.LENGTH_LONG).show()
                        resources.getString(R.string.firebase_pass_error) -> Snackbar.make(view, resources.getString(R.string.please_check_your_pass), Snackbar.LENGTH_LONG).show()
                        resources.getString(R.string.firebase_exist_user_error) -> Snackbar.make(view, resources.getString(R.string.exist_user_entry), Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        }

        binding.logoLinear.setOnClickListener {
            navController = findNavController()
            navController.navigate(R.id.action_userSignInFragment_to_userSignUpFragment)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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