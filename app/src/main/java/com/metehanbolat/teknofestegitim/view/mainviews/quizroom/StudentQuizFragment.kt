package com.metehanbolat.teknofestegitim.view.mainviews.quizroom

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
import com.metehanbolat.teknofestegitim.databinding.FragmentStudentQuizBinding

class StudentQuizFragment : Fragment() {

    private var _binding : FragmentStudentQuizBinding? = null
    private val binding get() = _binding!!

    private lateinit var navController: NavController

    private lateinit var auth : FirebaseAuth
    private lateinit var firestore : FirebaseFirestore

    private lateinit var title : String
    private var counter = 0

    private lateinit var correct : String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentStudentQuizBinding.inflate(inflater, container, false)
        val view = binding.root

        arguments?.let {
            title = StudentQuizFragmentArgs.fromBundle(it).title
            binding.studentQuestionRoomTitle.text = resources.getString(R.string.quiz_room_title, title)
        }

        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(), R.color.teacher_quiz_fragment)

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

        firestore.collection(resources.getString(R.string.quiz_room)).document(title).collection(resources.getString(R.string.quiz_questions)).document(resources.getString(R.string.quiz_question)).addSnapshotListener { value, _ ->
            if (value != null && value.exists()){
                val question = value.data!![resources.getString(R.string.quiz_question)]
                val answerOne = value.data!![resources.getString(R.string.quiz_answer_one)]
                val answerTwo = value.data!![resources.getString(R.string.quiz_answer_two)]
                val answerThree = value.data!![resources.getString(R.string.quiz_answer_three)]
                val answerFour = value.data!![resources.getString(R.string.quiz_answer_four)]
                correct = value.data!![resources.getString(R.string.quiz_correct_answer)].toString()
                val questionNumber = value.data!![resources.getString(R.string.quiz_question_number)]

                binding.studentQuestion.text = question.toString()
                binding.studentAnswerOne.text = answerOne.toString()
                binding.studentAnswerTwo.text = answerTwo.toString()
                binding.studentAnswerThree.text = answerThree.toString()
                binding.studentAnswerFour.text = answerFour.toString()
                binding.questionNumber.text = resources.getString(R.string.question_number, questionNumber.toString())

                answerBackgroundClear()
                answerClickableTrue()
            }
        }

        binding.studentAnswerOne.setOnClickListener {

            answerClickableFalse()

            when(correct){
                resources.getString(R.string._one) -> binding.studentAnswerOne.background = ContextCompat.getDrawable(requireContext(), R.drawable.question_true_edit_text_background)
                resources.getString(R.string._two) -> {
                    binding.studentAnswerOne.background = ContextCompat.getDrawable(requireContext(), R.drawable.question_false_edit_text_background)
                    binding.studentAnswerTwo.background = ContextCompat.getDrawable(requireContext(), R.drawable.question_true_edit_text_background)
                }
                resources.getString(R.string._three) -> {
                    binding.studentAnswerOne.background = ContextCompat.getDrawable(requireContext(), R.drawable.question_false_edit_text_background)
                    binding.studentAnswerThree.background = ContextCompat.getDrawable(requireContext(), R.drawable.question_true_edit_text_background)
                }
                resources.getString(R.string._four) -> {
                    binding.studentAnswerOne.background = ContextCompat.getDrawable(requireContext(), R.drawable.question_false_edit_text_background)
                    binding.studentAnswerFour.background = ContextCompat.getDrawable(requireContext(), R.drawable.question_true_edit_text_background)
                }
            }
        }

        binding.studentAnswerTwo.setOnClickListener {

            answerClickableFalse()

            when(correct){
                resources.getString(R.string._two) -> binding.studentAnswerTwo.background = ContextCompat.getDrawable(requireContext(), R.drawable.question_true_edit_text_background)
                resources.getString(R.string._one) -> {
                    binding.studentAnswerTwo.background = ContextCompat.getDrawable(requireContext(), R.drawable.question_false_edit_text_background)
                    binding.studentAnswerOne.background = ContextCompat.getDrawable(requireContext(), R.drawable.question_true_edit_text_background)
                }
                resources.getString(R.string._three) -> {
                    binding.studentAnswerTwo.background = ContextCompat.getDrawable(requireContext(), R.drawable.question_false_edit_text_background)
                    binding.studentAnswerThree.background = ContextCompat.getDrawable(requireContext(), R.drawable.question_true_edit_text_background)
                }
                resources.getString(R.string._four) -> {
                    binding.studentAnswerTwo.background = ContextCompat.getDrawable(requireContext(), R.drawable.question_false_edit_text_background)
                    binding.studentAnswerFour.background = ContextCompat.getDrawable(requireContext(), R.drawable.question_true_edit_text_background)
                }
            }
        }

        binding.studentAnswerThree.setOnClickListener {

            answerClickableFalse()

            when(correct){
                resources.getString(R.string._three) -> binding.studentAnswerThree.background = ContextCompat.getDrawable(requireContext(), R.drawable.question_true_edit_text_background)
                resources.getString(R.string._one) -> {
                    binding.studentAnswerThree.background = ContextCompat.getDrawable(requireContext(), R.drawable.question_false_edit_text_background)
                    binding.studentAnswerOne.background = ContextCompat.getDrawable(requireContext(), R.drawable.question_true_edit_text_background)
                }
                resources.getString(R.string._two) -> {
                    binding.studentAnswerThree.background = ContextCompat.getDrawable(requireContext(), R.drawable.question_false_edit_text_background)
                    binding.studentAnswerTwo.background = ContextCompat.getDrawable(requireContext(), R.drawable.question_true_edit_text_background)
                }
                resources.getString(R.string._four) -> {
                    binding.studentAnswerThree.background = ContextCompat.getDrawable(requireContext(), R.drawable.question_false_edit_text_background)
                    binding.studentAnswerFour.background = ContextCompat.getDrawable(requireContext(), R.drawable.question_true_edit_text_background)
                }
            }
        }

        binding.studentAnswerFour.setOnClickListener {

            answerClickableFalse()

            when(correct){
                resources.getString(R.string._four) -> binding.studentAnswerFour.background = ContextCompat.getDrawable(requireContext(), R.drawable.question_true_edit_text_background)
                resources.getString(R.string._one) -> {
                    binding.studentAnswerFour.background = ContextCompat.getDrawable(requireContext(), R.drawable.question_false_edit_text_background)
                    binding.studentAnswerOne.background = ContextCompat.getDrawable(requireContext(), R.drawable.question_true_edit_text_background)
                }
                resources.getString(R.string._two) -> {
                    binding.studentAnswerFour.background = ContextCompat.getDrawable(requireContext(), R.drawable.question_false_edit_text_background)
                    binding.studentAnswerTwo.background = ContextCompat.getDrawable(requireContext(), R.drawable.question_true_edit_text_background)
                }
                resources.getString(R.string._three) -> {
                    binding.studentAnswerFour.background = ContextCompat.getDrawable(requireContext(), R.drawable.question_false_edit_text_background)
                    binding.studentAnswerThree.background = ContextCompat.getDrawable(requireContext(), R.drawable.question_true_edit_text_background)
                }
            }
        }

        binding.studentButton.setOnClickListener {

            Snackbar.make(it, resources.getString(R.string.are_u_sure_leave_quiz), Snackbar.LENGTH_INDEFINITE).setAction(resources.getString(R.string.yes)){
                navController = findNavController()
                navController.navigate(R.id.action_studentQuizFragment_to_mainFragment)
            }.show()

        }
    }

    private fun answerClickableFalse(){
        binding.studentAnswerOne.isClickable = false
        binding.studentAnswerTwo.isClickable = false
        binding.studentAnswerThree.isClickable = false
        binding.studentAnswerFour.isClickable = false
    }

    private fun answerClickableTrue(){
        binding.studentAnswerOne.isClickable = true
        binding.studentAnswerTwo.isClickable = true
        binding.studentAnswerThree.isClickable = true
        binding.studentAnswerFour.isClickable = true
    }

    private fun answerBackgroundClear(){
        binding.studentAnswerOne.background = ContextCompat.getDrawable(requireContext(), R.drawable.edittext_background)
        binding.studentAnswerTwo.background = ContextCompat.getDrawable(requireContext(), R.drawable.edittext_background)
        binding.studentAnswerThree.background = ContextCompat.getDrawable(requireContext(), R.drawable.edittext_background)
        binding.studentAnswerFour.background = ContextCompat.getDrawable(requireContext(), R.drawable.edittext_background)

    }

}