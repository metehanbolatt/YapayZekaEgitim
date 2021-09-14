package com.metehanbolat.teknofestegitim.view.userviews

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.metehanbolat.teknofestegitim.R
import com.metehanbolat.teknofestegitim.databinding.FragmentUserSignUpBinding
import com.metehanbolat.teknofestegitim.view.mainviews.main.MainActivity
import java.util.*

class UserSignUpFragment : Fragment() {

    private var _binding : FragmentUserSignUpBinding? = null
    private val binding get() = _binding!!

    private lateinit var auth : FirebaseAuth
    private lateinit var firestore : FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserSignUpBinding.inflate(inflater, container, false)
        val view = binding.root

        auth = Firebase.auth
        firestore = Firebase.firestore

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val education = resources.getStringArray(R.array.education_level)
        val arrayAdapter = ArrayAdapter(requireContext(), R.layout.dropdown_item, education)
        binding.autoCompleteTextView.setAdapter(arrayAdapter)

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)

        binding.userDateSignUp.setOnClickListener {
            val dpd = DatePickerDialog(requireContext(), { _, mYear, mMonth, mDay->
                binding.userDateSignUp.setText(resources.getString(R.string.sign_up_date, mDay, mMonth, mYear))
            }, year, month, day)
            dpd.show()
        }

        binding.signUpButton.setOnClickListener {
            val email = binding.userEmailSignUp.text.toString()
            val password = binding.userPasswordSignUp.text.toString()
            val name = binding.userNameSignUp.text.toString()
            val surname = binding.userSurnameSignUp.text.toString()
            val birthday = binding.userDateSignUp.text.toString()
            val educationLevel = binding.autoCompleteTextView.text.toString()
            if (email.isEmpty() || password.isEmpty() || name.isEmpty() || surname.isEmpty() || birthday.isEmpty() || educationLevel.isEmpty()){
                Snackbar.make(it, resources.getString(R.string.please_check_field), Snackbar.LENGTH_LONG).show()
            }else{
                userData(it)
            }
        }
    }

    private fun goMainActivity(){
        val intent = Intent(requireContext(), MainActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }

    private fun userData(view : View){
        val userDataMap = hashMapOf<String, Any>()
        userDataMap[resources.getString(R.string.firebase_userName)] = binding.userNameSignUp.text.toString()
        userDataMap[resources.getString(R.string.firebase_userSurname)] = binding.userSurnameSignUp.text.toString()
        userDataMap[resources.getString(R.string.firebase_userNick)] = binding.userNickSignUp.text.toString()
        userDataMap[resources.getString(R.string.firebase_userEmail)] = binding.userEmailSignUp.text.toString()
        userDataMap[resources.getString(R.string.firebase_userBirthday)] = binding.userDateSignUp.text.toString()
        userDataMap[resources.getString(R.string.firebase_educationLevel)] = binding.autoCompleteTextView.text.toString()
        userDataMap[resources.getString(R.string.firebase_userCoin)] = 0
        userDataMap[resources.getString(R.string.firebase_firstCoin)] = 0
        userDataMap[resources.getString(R.string.firebase_secondCoin)] = 0
        userDataMap[resources.getString(R.string.firebase_thirdCoin)] = 0
        userDataMap[resources.getString(R.string.firebase_fourthCoin)] = 0
        userDataMap[resources.getString(R.string.firebase_fifthCoin)] = 0
        userDataMap[resources.getString(R.string.firebase_first_achievement)] = 0
        userDataMap[resources.getString(R.string.firebase_second_achievement)] = 0
        userDataMap[resources.getString(R.string.firebase_third_achievement)] = 0
        userDataMap[resources.getString(R.string.firebase_sixthCoin)] = 0
        userDataMap[resources.getString(R.string.ai_learning_one)] = resources.getString(R.string.false_ai)
        userDataMap[resources.getString(R.string.ai_learning_two)] = resources.getString(R.string.false_ai)
        userDataMap[resources.getString(R.string.ai_learning_three)] = resources.getString(R.string.false_ai)

        firestore.collection(resources.getString(R.string.firebase_userData)).document(binding.userEmailSignUp.text.toString()).set(userDataMap).addOnSuccessListener {
            auth.createUserWithEmailAndPassword(binding.userEmailSignUp.text.toString(),binding.userPasswordSignUp.text.toString()).addOnSuccessListener {
                goMainActivity()
            }.addOnFailureListener {
                if (it.localizedMessage == resources.getString(R.string.firebase_mail_error)){
                    Snackbar.make(view, resources.getString(R.string.please_check_your_email), Snackbar.LENGTH_LONG).show()
                }else if (it.localizedMessage == resources.getString(R.string.firebase_sign_up_pass_error)){
                    Snackbar.make(view, resources.getString(R.string.please_check_your_pass_size), Snackbar.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}