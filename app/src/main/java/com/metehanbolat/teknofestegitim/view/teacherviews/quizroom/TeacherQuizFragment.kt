package com.metehanbolat.teknofestegitim.view.teacherviews.quizroom

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.metehanbolat.teknofestegitim.R
import com.metehanbolat.teknofestegitim.databinding.FragmentTeacherQuizBinding

class TeacherQuizFragment : Fragment() {

    private var _binding : FragmentTeacherQuizBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController

    private lateinit var auth : FirebaseAuth
    private lateinit var firestore : FirebaseFirestore

    private var counter = 0
    private var radioControl = 0
    private var questionNumberControl = 1

    private lateinit var quizRoom : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentTeacherQuizBinding.inflate(inflater, container, false)
        val view = binding.root

        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(),R.color.teacher_quiz_fragment)

        arguments?.let {
            quizRoom = TeacherQuizFragmentArgs.fromBundle(it).quizRoomId
            binding.teacherQuizTitle.text = resources.getString(R.string.quiz_room_title, quizRoom)
        }

        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                when(counter){
                    0 -> {
                        Snackbar.make(view,resources.getString(R.string.againClickExit), Snackbar.LENGTH_SHORT).show()
                        counter++
                    }
                    1 -> {
                        navController = findNavController()
                        navController.navigate(R.id.action_teacherQuizFragment_to_teacherMainFragment)
                    }
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)

        auth = Firebase.auth
        firestore = Firebase.firestore

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.radioButtonOne.setOnClickListener{
            binding.radioButtonOne.text = resources.getString(R.string.quiz_true)
            binding.radioButtonTwo.text = resources.getString(R.string.empty)
            binding.radioButtonThree.text = resources.getString(R.string.empty)
            binding.radioButtonFour.text = resources.getString(R.string.empty)
            radioControl = 1
        }

        binding.radioButtonTwo.setOnClickListener{
            binding.radioButtonOne.text = resources.getString(R.string.empty)
            binding.radioButtonTwo.text = resources.getString(R.string.quiz_true)
            binding.radioButtonThree.text = resources.getString(R.string.empty)
            binding.radioButtonFour.text = resources.getString(R.string.empty)
            radioControl = 2
        }

        binding.radioButtonThree.setOnClickListener{
            binding.radioButtonOne.text = resources.getString(R.string.empty)
            binding.radioButtonTwo.text = resources.getString(R.string.empty)
            binding.radioButtonThree.text = resources.getString(R.string.quiz_true)
            binding.radioButtonFour.text = resources.getString(R.string.empty)
            radioControl = 3
        }

        binding.radioButtonFour.setOnClickListener{
            binding.radioButtonOne.text = resources.getString(R.string.empty)
            binding.radioButtonTwo.text = resources.getString(R.string.empty)
            binding.radioButtonThree.text = resources.getString(R.string.empty)
            binding.radioButtonFour.text = resources.getString(R.string.quiz_true)
            radioControl = 4
        }

        binding.quizButton.setOnClickListener {

            if (binding.quizQuestionEditText.text.isNullOrBlank()){
                Snackbar.make(view, resources.getString(R.string.u_must_write_a_question), Snackbar.LENGTH_LONG).show()
            }else{
                if (binding.quizAnswerOne.text.isNullOrBlank() || binding.quizAnswerTwo.text.isNullOrBlank() || binding.quizAnswerThree.text.isNullOrBlank() || binding.quizAnswerFour.text.isNullOrBlank()){
                    Snackbar.make(view, resources.getString(R.string.u_must_write_whole_answer_field), Snackbar.LENGTH_LONG).show()
                }else{
                    if (binding.radioButtonOne.text == resources.getString(R.string.quiz_true) || binding.radioButtonTwo.text == resources.getString(R.string.quiz_true) || binding.radioButtonThree.text == resources.getString(R.string.quiz_true) || binding.radioButtonFour.text == resources.getString(R.string.quiz_true)){
                        val questionMap = hashMapOf<String, Any>()
                        questionMap[resources.getString(R.string.quiz_question)] = binding.quizQuestionEditText.text.toString()
                        questionMap[resources.getString(R.string.quiz_answer_one)] = binding.quizAnswerOne.text.toString()
                        questionMap[resources.getString(R.string.quiz_answer_two)] = binding.quizAnswerTwo.text.toString()
                        questionMap[resources.getString(R.string.quiz_answer_three)] = binding.quizAnswerThree.text.toString()
                        questionMap[resources.getString(R.string.quiz_answer_four)] = binding.quizAnswerFour.text.toString()
                        questionMap[resources.getString(R.string.quiz_correct_answer)] = radioControl.toString()
                        questionMap[resources.getString(R.string.quiz_question_number)] = questionNumberControl

                        firestore.collection(resources.getString(R.string.quiz_room)).document(quizRoom).collection(resources.getString(R.string.quiz_questions)).document(resources.getString(R.string.quiz_question)).update(questionMap).addOnSuccessListener {
                            Snackbar.make(view, resources.getString(R.string.success_question_send), Snackbar.LENGTH_LONG).show()
                            questionNumberControl++
                        }.addOnFailureListener {
                            Snackbar.make(view, resources.getString(R.string.failed_question_send), Snackbar.LENGTH_LONG).show()
                        }
                    }else{
                        Snackbar.make(view, resources.getString(R.string.one_answer_must_correct), Snackbar.LENGTH_LONG).show()
                    }
                }
            }
        }

        binding.quizClose.setOnClickListener {
            Snackbar.make(it, resources.getString(R.string.are_u_sure_close_quiz_room, quizRoom), Snackbar.LENGTH_INDEFINITE).setAction(resources.getString(R.string.close)){
                firestore.collection(resources.getString(R.string.quiz_room)).document(quizRoom).collection(resources.getString(R.string.quiz_questions)).document(resources.getString(R.string.quiz_question)).delete().addOnSuccessListener {
                    firestore.collection(resources.getString(R.string.quiz_room)).document(quizRoom).delete().addOnSuccessListener {
                        Snackbar.make(view, resources.getString(R.string.close_quiz_room_snack_text, quizRoom), Snackbar.LENGTH_LONG).show()
                        navController = findNavController()
                        navController.navigate(R.id.action_teacherQuizFragment_to_teacherMainFragment)
                    }
                }
            }.show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}