package com.metehanbolat.teknofestegitim.view.mainviews.gameroom

import android.annotation.SuppressLint
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
import com.metehanbolat.teknofestegitim.R
import com.metehanbolat.teknofestegitim.databinding.FragmentAIGameTwoBinding
import com.metehanbolat.teknofestegitim.view.mainviews.artificialintelligence.getdata.PostModel
import com.metehanbolat.teknofestegitim.view.mainviews.artificialintelligence.getdata.RetrofitInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class AIGameTwoFragment : Fragment() {

    private var _binding : FragmentAIGameTwoBinding? = null
    private val binding get() = _binding!!
    private lateinit var navController: NavController

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View  {
        _binding = FragmentAIGameTwoBinding.inflate(inflater,container,false)
        val view = binding.root

        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(),R.color.background_blue)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val rf = Retrofit.Builder()
            .baseUrl(RetrofitInterface.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
        val api = rf.create(RetrofitInterface::class.java)

        val call = api.posts

        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {

                Snackbar.make(view, resources.getString(R.string.are_u_sure_quit_game), Snackbar.LENGTH_INDEFINITE).setAction(resources.getString(R.string.back)){
                    navController = findNavController()
                    navController.navigate(R.id.action_AIGameTwoFragment_to_machineGameListFragment)
                }.show()

            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)

        call?.enqueue(object: Callback<List<PostModel?>?> {
            override fun onResponse(
                call: Call<List<PostModel?>?>,
                response: Response<List<PostModel?>?>,) {

                val postList : List<PostModel> = response.body() as List<PostModel>
                val question = arrayOfNulls<String>(postList.size)
                val answerOne = arrayOfNulls<String>(postList.size)
                val answerTwo = arrayOfNulls<String>(postList.size)
                val answerThree = arrayOfNulls<String>(postList.size)
                val answerFour = arrayOfNulls<String>(postList.size)
                val correctAnswer = arrayOfNulls<String>(postList.size)

                for ( i in postList.indices){
                    question[i] = postList[i].question
                    answerOne[i] = postList[i].answerA
                    answerTwo[i] = postList[i].answerB
                    answerThree[i] = postList[i].answerC
                    answerFour[i] = postList[i].answerD
                    correctAnswer[i] = postList[i].correctAnswer
                }
                addQuestion(question,answerOne,answerTwo,answerThree,answerFour,correctAnswer)
            }
            override fun onFailure(call: Call<List<PostModel?>?>, t: Throwable) {
                Snackbar.make(view, t.toString(), Snackbar.LENGTH_LONG).show()
            }
        })
    }

    @SuppressLint("SetTextI18n")
    fun addQuestion(listQuestion: Array<String?>,
                    listAnswerA: Array<String?>,
                    listAnswerB: Array<String?>,
                    listAnswerC: Array<String?>,
                    listAnswerD: Array<String?>,
                    correctAnswer: Array<String?>){

        val questionSize = listQuestion.size
        val randNumber = (0 until questionSize).random()

        binding.question.text = listQuestion[randNumber]
        binding.questionNumber.text = resources.getString(R.string.question) + (randNumber + 1)
        binding.textViewAAnswer.text = listAnswerA[randNumber]
        binding.textViewBAnswer.text = listAnswerB[randNumber]
        binding.textViewCAnswer.text = listAnswerC[randNumber]
        binding.textViewDAnswer.text = listAnswerD[randNumber]

        binding.cardViewA.setOnClickListener {
            if (listAnswerA[randNumber] == correctAnswer[randNumber]) {
                Snackbar.make(it, resources.getString(R.string.ai_game_true), Snackbar.LENGTH_LONG).show()
                addQuestion(listQuestion, listAnswerA, listAnswerB, listAnswerC, listAnswerD, correctAnswer)
            } else {
                Snackbar.make(it, resources.getString(R.string.ai_game_false), Snackbar.LENGTH_LONG).show()
            }
        }

        binding.cardViewB.setOnClickListener {
            if (listAnswerB[randNumber] == correctAnswer[randNumber]) {
                Snackbar.make(it, resources.getString(R.string.ai_game_true), Snackbar.LENGTH_LONG).show()
                addQuestion(listQuestion, listAnswerA, listAnswerB, listAnswerC, listAnswerD, correctAnswer)
            } else {
                Snackbar.make(it, resources.getString(R.string.ai_game_false), Snackbar.LENGTH_LONG).show()
            }

        }

        binding.cardViewC.setOnClickListener {
            if (listAnswerC[randNumber] == correctAnswer[randNumber]) {
                Snackbar.make(it, resources.getString(R.string.ai_game_true), Snackbar.LENGTH_LONG).show()
                addQuestion(listQuestion, listAnswerA, listAnswerB, listAnswerC, listAnswerD, correctAnswer)
            } else {
                Snackbar.make(it, resources.getString(R.string.ai_game_false), Snackbar.LENGTH_LONG).show()
            }
        }

        binding.cardViewD.setOnClickListener {
            if (listAnswerD[randNumber] == correctAnswer[randNumber]) {
                Snackbar.make(it, resources.getString(R.string.ai_game_true), Snackbar.LENGTH_LONG).show()
                addQuestion(listQuestion, listAnswerA, listAnswerB, listAnswerC, listAnswerD, correctAnswer)
            } else {
                Snackbar.make(it, resources.getString(R.string.ai_game_false), Snackbar.LENGTH_LONG).show()
            }
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}