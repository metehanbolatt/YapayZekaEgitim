package com.metehanbolat.teknofestegitim.view.mainviews.gameroom

import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.metehanbolat.teknofestegitim.R
import com.metehanbolat.teknofestegitim.databinding.FragmentMachineTicTacToeBinding
import com.metehanbolat.teknofestegitim.utils.UserFirebaseProcess
import com.metehanbolat.teknofestegitim.view.mainviews.machinelearning.model.QuestionAPI
import com.metehanbolat.teknofestegitim.view.mainviews.machinelearning.model.QuestionAPIEnglish
import com.metehanbolat.teknofestegitim.view.mainviews.machinelearning.model.QuestionModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import kotlin.collections.ArrayList

class MachineTicTacToeFragment : Fragment(),View.OnClickListener {

    private var _binding : FragmentMachineTicTacToeBinding? = null
    private val binding get() = _binding!!

    private lateinit var firestore : FirebaseFirestore
    private lateinit var firebaseAuth : FirebaseAuth

    private lateinit var navController: NavController

    private lateinit var baseUrl : String

    private var questionModels : ArrayList<QuestionModel> ?= null
    private lateinit var questionsRightAnswer : String
    private var questionNumber = 0

    private val buttons = arrayOfNulls<Button>(9)
    private var playerOneScoreCount = 0
    private var playerTwoScoreCount = 0
    private var roundCount = 0
    private var secondTour = 0
    private var thirdTour = 0
    private var turnCounter = 0

    private lateinit var answerOne : String
    private lateinit var answerTwo : String
    private lateinit var answerThree : String
    private lateinit var answerFour : String

    private var activePlayer = false
    var imageView : ImageView? = null
    private var gameState = intArrayOf(2, 2, 2, 2, 2, 2, 2, 2, 2)
    private var winningPositions = arrayOf(
        intArrayOf(0, 1, 2),
        intArrayOf(3, 4, 5),
        intArrayOf(6, 7, 8),
        intArrayOf(0, 3, 6),
        intArrayOf(1, 4, 7),
        intArrayOf(2, 5, 8),
        intArrayOf(0, 4, 8),
        intArrayOf(2, 4, 6)
    )

    private var control = false
    private var questionSeeControl = false
    private var buttonControl = false

    private lateinit var call : Call<List<QuestionModel>>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMachineTicTacToeBinding.inflate(inflater, container, false)
        val view = binding.root

        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(),R.color.tictactoe_background)

        firebaseAuth = Firebase.auth
        firestore = Firebase.firestore

        baseUrl = resources.getString(R.string.baseUrl)
        answerOne = resources.getString(R.string.answer_one)
        answerTwo = resources.getString(R.string.answer_two)
        answerThree = resources.getString(R.string.answer_three)
        answerFour = resources.getString(R.string.answer_four)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                Snackbar.make(view, resources.getString(R.string.are_u_sure_quit_game), Snackbar.LENGTH_INDEFINITE).setAction(resources.getString(R.string.exit)){
                    navController = findNavController()
                    navController.navigate(R.id.action_machineTicTacToeFragment_to_machineGameListFragment)
                }.show()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(callback)
        loadData()

        for (i in buttons.indices) {
            val buttonID = resources.getString(R.string.btn_, i)
            val resourceID = resources.getIdentifier(buttonID, resources.getString(R.string.id), view.context.packageName)
            buttons[i] = getView()?.findViewById<View>(resourceID) as Button
            buttons[i]!!.setOnClickListener(this)
        }

        roundCount = 0
        playerOneScoreCount = 0
        playerTwoScoreCount = 0
        activePlayer = true

        binding.playerStatus.text = resources.getString(R.string.your_turn)
        binding.turnButton.text = resources.getString(R.string.see_question)

        buttonControl = true
        binding.roundButton.visibility = View.INVISIBLE

        binding.turnButton.setOnClickListener {
            showImagePickDialog(it)

            if (secondTour == 1) {
                binding.roundButton.text = resources.getString(R.string.roundStart, turnCounter)
            }
            if (thirdTour == 1) {
                binding.roundButton.text = resources.getString(R.string.third_round_start)
            }
            binding.turnButton.visibility = View.VISIBLE
            binding.roundButton.visibility = View.INVISIBLE

        }

        binding.roundButton.setOnClickListener {
            playerAgain()

            for (i in buttons.indices) {
                buttons[i]!!.text = resources.getString(R.string.empty)
            }
            if (secondTour == 1) {
                binding.roundButton.text = resources.getString(R.string.roundStart, turnCounter)
            }
            if (thirdTour == 1) {
                binding.roundButton.text = resources.getString(R.string.third_round_start)
            }
            binding.turnButton.visibility = View.VISIBLE
            binding.roundButton.visibility = View.INVISIBLE
        }
        secondTour = 0
        thirdTour = 0
        turnCounter = 1

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun playerAgain() {
        roundCount = 0
        activePlayer = true
        for (i in buttons.indices) {
            gameState[i] = 2
        }
    }

    override fun onClick(view: View) {

        if (control && questionSeeControl) {
            if ((view as Button).text.toString() != resources.getString(R.string.empty)) {
                return
            }

            val buttonID = view.getResources().getResourceEntryName(view.getId())
            val gameStatePointer = buttonID.substring(buttonID.length - 1, buttonID.length).toInt()

            if (!activePlayer) {
                view.text = resources.getString(R.string.zero)
                view.setTextColor(ContextCompat.getColor(view.context, R.color.tic_tac_toe_red))
                gameState[gameStatePointer] = 0
                questionSeeControl = false
                activePlayer = true
            } else {
                view.text = resources.getString(R.string.x)
                view.setTextColor(ContextCompat.getColor(view.context, R.color.white))
                gameState[gameStatePointer] = 1
                questionSeeControl = false
                activePlayer = false
            }
            roundCount++
            if (checkWinner()) {
                if (activePlayer) {
                    playerOneScoreCount++
                    val coinDecrease = UserFirebaseProcess(firestore, resources.getString(R.string.firebase_userData), firebaseAuth.currentUser!!.email.toString())
                    coinDecrease.getCoin(resources.getString(R.string.firebase_userCoin)){
                        coinDecrease.userCoinDecrease(resources.getString(R.string.firebase_userCoin), it!!, 5)
                    }
                    turnCounter += 1
                    binding.turnButton.visibility = View.INVISIBLE
                    binding.roundButton.visibility = View.VISIBLE
                    if (playerOneScoreCount == 3 && secondTour == 0) {
                        secondTour = 1
                    } else if (playerOneScoreCount == 3 && secondTour == 1) {
                        thirdTour = 1
                    }

                    updatePlayerScore()
                    if (playerOneScoreCount == 3) {
                        Snackbar.make(view, resources.getString(R.string.game_win) , Snackbar.LENGTH_SHORT).show()
                        binding.playerStatus.text = resources.getString(R.string.empty)
                    }

                } else {
                    playerTwoScoreCount++
                    turnCounter += 1
                    binding.turnButton.visibility = View.INVISIBLE
                    binding.roundButton.visibility = View.VISIBLE
                    updatePlayerScore()
                    val earnGold = UserFirebaseProcess(firestore, resources.getString(R.string.firebase_userData), firebaseAuth.currentUser!!.email.toString())
                    earnGold.getCoin(resources.getString(R.string.firebase_userCoin)){
                        earnGold.userCoinIncrease(resources.getString(R.string.firebase_userCoin), it!!, 10)
                    }

                }
            }

            binding.resetGame.setOnClickListener {
                navController = findNavController()
                navController.navigate(R.id.action_machineTicTacToeFragment_self)
            }

        } else if (questionSeeControl && !control) {
            activePlayer =! activePlayer
        } else {
            if (binding.turnButton.isVisible){
                Snackbar.make(view, resources.getString(R.string.click_question_see), Snackbar.LENGTH_SHORT).show()
            }else if (binding.roundButton.isVisible){
                Snackbar.make(view, resources.getString(R.string.please_click_round_button), Snackbar.LENGTH_SHORT).show()
            }
        }
        if (activePlayer) {
            binding.playerStatus.text = resources.getString(R.string.your_turn)
        } else {
            binding.playerStatus.text = resources.getString(R.string.rival_turn)
        }
    }

    private fun checkWinner() : Boolean {
        var winnerResult = false
        for (winningPosition in winningPositions) {
            if (gameState[winningPosition[0]] == gameState[winningPosition[1]] && gameState[winningPosition[1]] == gameState[winningPosition[2]] && gameState[winningPosition[0]] != 2) {
                winnerResult = true
            }
        }
        return winnerResult
    }

    private fun updatePlayerScore() {
        binding.playerTwoScore.text = playerOneScoreCount.toString()
        binding.playerOneScore.text = playerTwoScoreCount.toString()
        binding.roundButton.text = resources.getString(R.string.roundStart, turnCounter)
    }

    private fun showImagePickDialog(view : View) {

        val options = arrayOf(questionModels!![questionNumber].answer1,questionModels!![questionNumber].answer2,questionModels!![questionNumber].answer3 ,questionModels!![questionNumber].answer4)
        val builder = AlertDialog.Builder(requireContext())
        questionsRightAnswer = questionModels!![questionNumber].rightAnswer
        builder.setTitle(questionModels!![questionNumber].question)
        val titleOfDialog = TextView(requireContext())
        titleOfDialog.height = 500
        titleOfDialog.setBackgroundColor(Color.RED)
        titleOfDialog.text = questionModels!![questionNumber].question
        titleOfDialog.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20F)
        titleOfDialog.setTextColor(Color.WHITE)
        titleOfDialog.gravity = Gravity.CENTER
        builder.setCustomTitle(titleOfDialog)
        builder.setCancelable(false)
        builder.setItems(options) { _, i ->
            if (i == 0) {

                if (questionsRightAnswer == answerOne){
                    Snackbar.make(view, resources.getString(R.string.true_answer_two), Snackbar.LENGTH_SHORT).show()
                    control = true
                    questionSeeControl = true
                }else{
                    Snackbar.make(view, resources.getString(R.string.wrong_answer_two), Snackbar.LENGTH_SHORT).show()
                    control = false
                    questionSeeControl = false
                    wrongAnswer()
                }
                questionNumber += 1
            }
            if (i == 1) {

                if (questionsRightAnswer == answerTwo){
                    Snackbar.make(view, resources.getString(R.string.true_answer_two), Snackbar.LENGTH_SHORT).show()
                    control = true
                    questionSeeControl = true
                }else{
                    Snackbar.make(view, resources.getString(R.string.wrong_answer_two), Snackbar.LENGTH_SHORT).show()
                    control = false
                    questionSeeControl = false
                    wrongAnswer()

                }
                questionNumber += 1
            }
            if (i == 2){
                if (questionsRightAnswer == answerThree){
                    Snackbar.make(view, resources.getString(R.string.true_answer_two), Snackbar.LENGTH_SHORT).show()
                    control = true
                    questionSeeControl = true
                }else{
                    Snackbar.make(view, resources.getString(R.string.wrong_answer_two), Snackbar.LENGTH_SHORT).show()
                    control = false
                    questionSeeControl = false
                    wrongAnswer()

                }
                questionNumber += 1

            }
            if (i == 3){
                if (questionsRightAnswer == answerFour){
                    Snackbar.make(view, resources.getString(R.string.true_answer_two), Snackbar.LENGTH_SHORT).show()
                    control = true
                    questionSeeControl = true
                }else{
                    Snackbar.make(view, resources.getString(R.string.wrong_answer_two), Snackbar.LENGTH_SHORT).show()
                    control = false
                    questionSeeControl = false
                    wrongAnswer()

                }
                questionNumber += 1
            }
        }
        builder.create().show()
    }

    private fun loadData(){

        val retrofit = Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).build()
        call = if (Locale.getDefault().language.toString() == resources.getString(R.string.tr)){
            val service = retrofit.create(QuestionAPI::class.java)
            service.getData()
        }else{
            val service = retrofit.create(QuestionAPIEnglish::class.java)
            service.getData()
        }

        call.enqueue(object : Callback<List<QuestionModel>> {
            override fun onResponse(
                call : Call<List<QuestionModel>>,
                response : Response<List<QuestionModel>>
            ) {
                if (response.isSuccessful){
                    response.body()?.let {
                        questionModels = ArrayList(it)
                    }
                }
            }

            override fun onFailure(call: Call<List<QuestionModel>>, t: Throwable) {
                t.printStackTrace()
            }
        })
    }

    private fun wrongAnswer(){

        activePlayer =! activePlayer
        if (binding.playerStatus.text == resources.getString(R.string.your_turn)){
            binding.playerStatus.text = resources.getString(R.string.rival_turn)
        }else{
            binding.playerStatus.text = resources.getString(R.string.your_turn)
        }
    }

}
