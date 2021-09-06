package com.metehanbolat.teknofestegitim.view.mainviews.machinelearning

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.metehanbolat.teknofestegitim.R
import com.metehanbolat.teknofestegitim.databinding.FragmentMachineTicTacToeBinding
import com.metehanbolat.teknofestegitim.view.mainviews.machinelearning.model.QuestionAPI
import com.metehanbolat.teknofestegitim.view.mainviews.machinelearning.model.QuestionModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MachineTicTacToeFragment : Fragment(),View.OnClickListener {

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
    private var turSayaci = 0

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

    private var _binding : FragmentMachineTicTacToeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMachineTicTacToeBinding.inflate(inflater, container, false)
        val view = binding.root

        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(),R.color.background_design_start_color)

        baseUrl = resources.getString(R.string.baseUrl)
        answerOne = resources.getString(R.string.answer_one)
        answerTwo = resources.getString(R.string.answer_two)
        answerThree = resources.getString(R.string.answer_three)
        answerFour = resources.getString(R.string.answer_four)

        return view
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                navController = findNavController()
                navController.navigate(R.id.action_machineTicTacToeFragment_to_machineGameListFragment)
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(callback)
        loadData()

        for (i in buttons.indices) {
            val buttonID = "btn_$i"
            val resourceID = resources.getIdentifier(buttonID, "id", context?.packageName)
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
        binding.raundButton.visibility = View.INVISIBLE

        binding.turnButton.setOnClickListener {
            showImagePickDialog(it)

            if (secondTour == 1) {
                binding.raundButton.text = "$turSayaci. Tura Başla"
            }
            if (thirdTour == 1) {
                binding.raundButton.text = resources.getString(R.string.third_round_start)
            }
            binding.turnButton.visibility = View.VISIBLE
            binding.raundButton.visibility = View.INVISIBLE

        }

        binding.raundButton.setOnClickListener {
            playerAgain()

            for (i in buttons.indices) {
                buttons[i]!!.text = resources.getString(R.string.empty)
            }
            if (secondTour == 1) {
                binding.raundButton.text = "$turSayaci. Tura Başla"
            }
            if (thirdTour == 1) {
                binding.raundButton.text = resources.getString(R.string.third_round_start)
            }
            binding.turnButton.visibility = View.VISIBLE
            binding.raundButton.visibility = View.INVISIBLE
        }
        secondTour = 0
        thirdTour = 0
        turSayaci = 1

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
                    turSayaci += 1
                    binding.turnButton.visibility = View.INVISIBLE
                    binding.raundButton.visibility = View.VISIBLE
                    if (playerOneScoreCount == 3 && secondTour == 0) {
                        secondTour = 1
                    } else if (playerOneScoreCount == 3 && secondTour == 1) {
                        thirdTour = 1
                    } else {
                        showImagePickDialog(view)
                    }
                    updatePlayerScore()
                    if (playerOneScoreCount == 3) {
                        Snackbar.make(view, resources.getString(R.string.game_win) , Snackbar.LENGTH_SHORT).show()
                        binding.playerStatus.text = resources.getString(R.string.empty)
                    }

                } else {
                    playerTwoScoreCount++
                    turSayaci += 1
                    binding.turnButton.visibility = View.INVISIBLE
                    binding.raundButton.visibility = View.VISIBLE
                    updatePlayerScore()

                }
            }

            binding.resetGame.setOnClickListener {
                navController = findNavController()
                navController.navigate(R.id.action_machineTicTacToeFragment_self)
            }

        } else if (questionSeeControl && !control) {
            activePlayer =! activePlayer
        } else {
            Snackbar.make(view, resources.getString(R.string.click_question_see), Snackbar.LENGTH_SHORT).show()
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

    @SuppressLint("SetTextI18n")
    private fun updatePlayerScore() {
        binding.playerTwoScore.text = playerOneScoreCount.toString()
        binding.playerOneScore.text = playerTwoScoreCount.toString()
        binding.raundButton.text = "$turSayaci. Tura Başla"
    }

    private fun showImagePickDialog(view : View) {

        val options = arrayOf(questionModels!![questionNumber].answer1,questionModels!![questionNumber].answer2,questionModels!![questionNumber].answer3 ,questionModels!![questionNumber].answer4)
        val builder = AlertDialog.Builder(requireContext())
        questionsRightAnswer = questionModels!![questionNumber].rightAnswer
        builder.setTitle(questionModels!![questionNumber].question)
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
            if (i==2){
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
        val service = retrofit.create(QuestionAPI::class.java)
        val call = service.getData()

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
