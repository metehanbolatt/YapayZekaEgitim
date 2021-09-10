package com.metehanbolat.teknofestegitim.view.mainviews.gameroom

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import java.util.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import com.metehanbolat.teknofestegitim.R
import com.metehanbolat.teknofestegitim.databinding.FragmentMachineMineSweeperBinding
import com.metehanbolat.teknofestegitim.view.mainviews.machinelearning.model.QuestionAPI
import com.metehanbolat.teknofestegitim.view.mainviews.machinelearning.model.QuestionModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MachineMineSweeperFragment : Fragment() {

    private var _binding : FragmentMachineMineSweeperBinding? = null
    private val binding get() = _binding!!

    private var rowNumber = 4
    private var colNumber = 4
    private var size = 16
    private lateinit var map : Array<IntArray>
    private lateinit var board : Array<IntArray>
    private var rand = Random()
    private var secim1 = 0
    private var secim2 = 0
    private var success = 0
    private val mineNumber = 6
    private lateinit var questionSeen : String
    private lateinit var questionAnswered : String
    private lateinit var baseUrl : String
    private lateinit var empty : String

    private lateinit var navController : NavController

    private var questionModels : ArrayList<QuestionModel> ?= null
    private lateinit var questionsRightAnswer : String
    private var questionNumber = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMachineMineSweeperBinding.inflate(inflater, container, false)
        val view = binding.root

        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(),R.color.mine_sweeper_background_color)

        questionSeen = resources.getString(R.string.not_seen)
        questionAnswered = resources.getString(R.string.not_answered)
        baseUrl = resources.getString(R.string.baseUrl)
        empty = resources.getString(R.string.empty)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                Snackbar.make(view, resources.getString(R.string.are_u_sure_quit_game), Snackbar.LENGTH_INDEFINITE).setAction(resources.getString(R.string.exit)){
                    navController=findNavController()
                    navController.navigate(R.id.action_machineMineSweeperFragment_to_machineGameListFragment)
                }.show()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(callback)

        loadData()

        mayinTarlasi(rowNumber, colNumber)
        prepareGame()

        binding.restartMineGame.setOnClickListener {
            navController=findNavController()
            navController.navigate(R.id.action_machineMineSweeperFragment_self)
        }

        binding.btn1.setOnClickListener {
            secim1 = 0
            secim2 = 0
            pressButton(it)
            if (map[secim1][secim2] != -1){
                binding.btn1.text = board[secim1][secim2].toString()
            }else{
                binding.btn1.setBackgroundResource(R.drawable.bomb)
            }
            binding.btn1.isClickable = false
        }

        binding.btn2.setOnClickListener {
            secim1 = 0
            secim2 = 1
            pressButton(it)
            if (map[secim1][secim2] != -1){
                binding.btn2.text = board[secim1][secim2].toString()
            }else{
                binding.btn2.setBackgroundResource(R.drawable.bomb)
            }
            binding.btn2.isClickable = false
        }

        binding.btn3.setOnClickListener {
            secim1 = 0
            secim2 = 2
            pressButton(it)
            if (map[secim1][secim2] != -1){
                binding.btn3.text = board[secim1][secim2].toString()
            }else{
                binding.btn3.setBackgroundResource(R.drawable.bomb)
            }
            binding.btn3.isClickable = false

        }

        binding.btn4.setOnClickListener {
            secim1 = 0
            secim2 = 3
            pressButton(it)
            if (map[secim1][secim2] != -1){
                binding.btn4.text = board[secim1][secim2].toString()
            }else{
                binding.btn4.setBackgroundResource(R.drawable.bomb)
            }
            binding.btn4.isClickable = false
        }

        binding.btn5.setOnClickListener {
            secim1 = 1
            secim2 = 0
            pressButton(it)
            if (map[secim1][secim2] != -1){
                binding.btn5.text = board[secim1][secim2].toString()
            }else{
                binding.btn5.setBackgroundResource(R.drawable.bomb)
            }
            binding.btn5.isClickable = false
        }

        binding.btn6.setOnClickListener {
            secim1 = 1
            secim2 = 1
            pressButton(it)
            if (map[secim1][secim2] != -1){
                binding.btn6.text = board[secim1][secim2].toString()
            }else{
                binding.btn6.setBackgroundResource(R.drawable.bomb)
            }
            binding.btn6.isClickable = false
        }

        binding.btn7.setOnClickListener {
            secim1 = 1
            secim2 = 2
            pressButton(it)
            if (map[secim1][secim2] != -1){
                binding.btn7.text = board[secim1][secim2].toString()
            }else{
                binding.btn7.setBackgroundResource(R.drawable.bomb)
            }
            binding.btn7.isClickable=false
        }

        binding.btn8.setOnClickListener {
            secim1 = 1
            secim2 = 3
            pressButton(it)
            if (map[secim1][secim2] != -1){
                binding.btn8.text = board[secim1][secim2].toString()
            }else{
                binding.btn8.setBackgroundResource(R.drawable.bomb)
            }
            binding.btn8.isClickable = false
        }

        binding.btn9.setOnClickListener {
            secim1 = 2
            secim2 = 0
            pressButton(it)
            if (map[secim1][secim2] != -1){
                binding.btn9.text = board[secim1][secim2].toString()
            }else{
                binding.btn9.setBackgroundResource(R.drawable.bomb)
            }
            binding.btn9.isClickable = false
        }

        binding.btn10.setOnClickListener {
            secim1 = 2
            secim2 = 1
            pressButton(it)
            if (map[secim1][secim2] != -1){
                binding.btn10.text = board[secim1][secim2].toString()
            }else{
                binding.btn10.setBackgroundResource(R.drawable.bomb)
            }
            binding.btn10.isClickable = false
        }

        binding.btn11.setOnClickListener {
            secim1 = 2
            secim2 = 2
            pressButton(it)
            if (map[secim1][secim2] != -1){
                binding.btn11.text = board[secim1][secim2].toString()

            }else{
                binding.btn11.setBackgroundResource(R.drawable.bomb)
            }
            binding.btn11.isClickable = false
        }

        binding.btn12.setOnClickListener {
            secim1 = 2
            secim2 = 3
            pressButton(it)
            if (map[secim1][secim2] != -1){
                binding.btn12.text = board[secim1][secim2].toString()

            }else{
                binding.btn12.setBackgroundResource(R.drawable.bomb)
            }
            binding.btn12.isClickable = false

        }
        binding.btn13.setOnClickListener {
            secim1 = 3
            secim2 = 0
            pressButton(it)
            if (map[secim1][secim2] != -1){
                binding.btn13.text = board[secim1][secim2].toString()
            }else{
                binding.btn13.setBackgroundResource(R.drawable.bomb)
            }
            binding.btn13.isClickable = false
        }

        binding.btn14.setOnClickListener {
            secim1 = 3
            secim2 = 1
            pressButton(it)
            if (map[secim1][secim2] != -1){
                binding.btn14.text = board[secim1][secim2].toString()
            }else{
                binding.btn14.setBackgroundResource(R.drawable.bomb)
            }
            binding.btn14.isClickable = false
        }

        binding.btn15.setOnClickListener {
            secim1 = 3
            secim2 = 2
            pressButton(it)
            if (map[secim1][secim2] != -1){
                binding.btn15.text = board[secim1][secim2].toString()
            }else{
                binding.btn15.setBackgroundResource(R.drawable.bomb)
            }
            binding.btn15.isClickable = false
        }

        binding.btn16.setOnClickListener {
            secim1 = 3
            secim2 = 3
            pressButton(it)
            if (map[secim1][secim2] != -1){
                binding.btn16.text = board[secim1][secim2].toString()
            }else{
                binding.btn16.setBackgroundResource(R.drawable.bomb)
            }
            binding.btn16.isClickable = false
        }

    }

    private fun checkMine(r: Int, c: Int) {
        if (map[r][c] == 0) {

            if (c < colNumber-1 && map[r][c + 1] == -1) {
                board[r][c]++
            }
            if (r < rowNumber-1 && map[r + 1][c] == -1) {
                board[r][c]++
            }
            if (r > 0 && map[r - 1][c] == -1) {
                board[r][c]++
            }
            if (c > 0 && map[r][c - 1] == -1) {
                board[r][c]++
            }
            if (c > 0 && r > 0 && map[r-1][c-1] == -1){
                board[r][c]++
            }
            if (c < colNumber-1  && r > 0 && map[r-1][c+1]==-1){
                board[r][c]++
            }
            if (c > 0 && r < rowNumber-1 && map[r+1][c-1]==-1){
                board[r][c]++
            }
            if (c < colNumber-1 && r < rowNumber-1 && map[r+1][c+1] == -1){
                board[r][c]++
            }
        }
    }

    private fun pressButton(view : View){
        if (map[secim1][secim2] != -1) {
            checkMine(secim1, secim2)
            success++
            if (success == (size-(mineNumber))) {
                Snackbar.make(view, resources.getString(R.string.congratulations),Snackbar.LENGTH_SHORT).show()
            }
        } else {
            Snackbar.make(view, resources.getString(R.string.press_mine),Snackbar.LENGTH_SHORT).show()
            showImagePickDialog(view)
        }
    }

    private fun mayinTarlasi(rowNumber: Int, colNumber: Int) {
        this.rowNumber = rowNumber
        this.colNumber = colNumber
        map = Array(rowNumber) { IntArray(colNumber) }
        board = Array(rowNumber) { IntArray(colNumber) }
        size = rowNumber * colNumber
    }

    private fun prepareGame() {
        var randRow : Int
        var randCol : Int
        var count = 0

        while (count != mineNumber) {
            randRow = rand.nextInt(rowNumber)
            randCol = rand.nextInt(colNumber)
            if (map[randRow][randCol] != -1) {
                map[randRow][randCol] = -1
                count += 1
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun loadData(){

        val retrofit = Retrofit.Builder().baseUrl(baseUrl).addConverterFactory(GsonConverterFactory.create()).build()
        val service = retrofit.create(QuestionAPI::class.java)
        val call = service.getData()

        call.enqueue(object : Callback<List<QuestionModel>> {
            override fun onResponse(
                call: Call<List<QuestionModel>>,
                response: Response<List<QuestionModel>>
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

    private fun showImagePickDialog(view : View) {

        val alreadySeen : String = resources.getString(R.string.already_seen)
        val alreadyAnswered : String = resources.getString(R.string.already_answered)
        val haventSeen : String = resources.getString(R.string.havent_seen)
        val haventAnswered : String = resources.getString(R.string.havent_answered)
        val trueAnswer : String = resources.getString(R.string.true_answer)
        val wrongAnswer : String = resources.getString(R.string.wrong_answer)
        val answerOne : String = resources.getString(R.string.answer_one)
        val answerTwo : String = resources.getString(R.string.answer_two)
        val answerThree : String = resources.getString(R.string.answer_three)
        val answerFour : String = resources.getString(R.string.answer_four)

        val options = arrayOf(questionModels!![questionNumber].answer1,questionModels!![questionNumber].answer2,questionModels!![questionNumber].answer3 ,questionModels!![questionNumber].answer4)
        val builder = AlertDialog.Builder(requireContext())
        questionsRightAnswer=questionModels!![questionNumber].rightAnswer
        builder.setTitle(questionModels!![questionNumber].question)
        builder.setCancelable(false)
        builder.setItems(options) { _, i ->
            if (i == 0) {
                if (questionsRightAnswer == answerOne){
                    Snackbar.make(view, trueAnswer, Snackbar.LENGTH_SHORT).show()
                    questionAnswered = alreadyAnswered
                    questionSeen = alreadySeen

                }else{
                    Snackbar.make(view, wrongAnswer, Snackbar.LENGTH_SHORT).show()
                    questionAnswered = haventAnswered
                    questionSeen = haventSeen
                    bombProcess()
                }
                questionNumber += 1
            }
            if (i == 1) {
                if (questionsRightAnswer == answerTwo){
                    Snackbar.make(view, trueAnswer, Snackbar.LENGTH_SHORT).show()
                    questionAnswered = alreadyAnswered
                    questionSeen = alreadySeen
                }else{
                    Snackbar.make(view, wrongAnswer, Snackbar.LENGTH_SHORT).show()
                    questionAnswered = haventAnswered
                    questionSeen = haventSeen
                    bombProcess()
                }
                questionNumber += 1
            }
            if (i == 2){
                if (questionsRightAnswer == answerThree){
                    Snackbar.make(view, trueAnswer, Snackbar.LENGTH_SHORT).show()
                    questionAnswered = alreadyAnswered
                    questionSeen = alreadySeen
                }else{
                    Snackbar.make(view, wrongAnswer, Snackbar.LENGTH_SHORT).show()
                    questionAnswered = haventAnswered
                    questionSeen = haventSeen
                    bombProcess()
                }
                questionNumber += 1

            }
            if (i == 3){
                if (questionsRightAnswer == answerFour){
                    Snackbar.make(view, trueAnswer, Snackbar.LENGTH_SHORT).show()
                    questionAnswered = alreadyAnswered
                    questionSeen = alreadySeen
                }else{
                    Snackbar.make(view, wrongAnswer, Snackbar.LENGTH_SHORT).show()
                    questionAnswered = haventSeen
                    questionSeen = haventAnswered
                    bombProcess()
                }
                questionNumber += 1
            }
        }
        builder.create().show()
    }

    private fun bombProcess(){

        binding.btn1.isClickable = false
        binding.btn2.isClickable = false
        binding.btn3.isClickable = false
        binding.btn4.isClickable = false
        binding.btn5.isClickable = false
        binding.btn6.isClickable = false
        binding.btn7.isClickable = false
        binding.btn8.isClickable = false
        binding.btn9.isClickable = false
        binding.btn10.isClickable = false
        binding.btn11.isClickable = false
        binding.btn12.isClickable = false
        binding.btn13.isClickable = false
        binding.btn14.isClickable = false
        binding.btn15.isClickable = false
        binding.btn16.isClickable = false

        if (map[0][0] != -1){
            if (binding.btn1.text == empty){
                checkMine(0,0)
                binding.btn1.text = board[0][0].toString()
            }
        }else{
            binding.btn1.setBackgroundResource(R.drawable.bomb)
        }

        if (map[0][1] != -1){
            if (binding.btn2.text == empty){
                checkMine(0,1)
                binding.btn2.text = board[0][1].toString()
            }
        }else{
            binding.btn2.setBackgroundResource(R.drawable.bomb)
        }

        if (map[0][2] != -1){
            if (binding.btn3.text == empty){
                checkMine(0,2)

                binding.btn3.text = board[0][2].toString()
            }
        }else{
            binding.btn3.setBackgroundResource(R.drawable.bomb)
        }

        if (map[0][3] != -1){
            if (binding.btn4.text == empty){
                checkMine(0,3)
                binding.btn4.text = board[0][3].toString()
            }
        }else{
            binding.btn4.setBackgroundResource(R.drawable.bomb)
        }

        if (map[1][0] != -1){
            if (binding.btn5.text == empty){
                checkMine(1,0)
                binding.btn5.text = board[1][0].toString()
            }
        }else{
            binding.btn5.setBackgroundResource(R.drawable.bomb)
        }

        if (map[1][1] != -1){
            if (binding.btn6.text == empty){
                checkMine(1,1)
                binding.btn6.text = board[1][1].toString()
            }
        }else{
            binding.btn6.setBackgroundResource(R.drawable.bomb)
        }

        if (map[1][2] != -1){
            if (binding.btn7.text == empty){
                checkMine(1,2)
                binding.btn7.text = board[1][2].toString()
            }
        }else{
            binding.btn7.setBackgroundResource(R.drawable.bomb)
        }

        if (map[1][3] != -1){
            if (binding.btn8.text == empty){
                checkMine(1,3)
                binding.btn8.text = board[1][3].toString()
            }
        }else{
            binding.btn8.setBackgroundResource(R.drawable.bomb)
        }

        if (map[2][0] != -1){
            if(binding.btn9.text == empty){
                checkMine(2,0)
                binding.btn9.text = board[2][0].toString()
            }
        }else{
            binding.btn9.setBackgroundResource(R.drawable.bomb)
        }

        if (map[2][1] != -1){
            if (binding.btn10.text == empty){
                checkMine(2,1)
                binding.btn10.text = board[2][1].toString()
            }
        }else{
            binding.btn10.setBackgroundResource(R.drawable.bomb)
        }

        if (map[2][2] != -1){
            if (binding.btn11.text == empty){
                checkMine(2,2)
                binding.btn11.text = board[2][2].toString()
            }
        }else{
            binding.btn11.setBackgroundResource(R.drawable.bomb)
        }

        if (map[2][3] != -1){
            if (binding.btn12.text == empty){
                checkMine(2,3)
                binding.btn12.text = board[2][3].toString()
            }
        }else{
            binding.btn12.setBackgroundResource(R.drawable.bomb)
        }

        if (map[3][0] != -1){
            if (binding.btn13.text == empty){
                checkMine(3,0)
                binding.btn13.text = board[3][0].toString()
            }
        }else{
            binding.btn13.setBackgroundResource(R.drawable.bomb)
        }

        if (map[3][1] != -1){
            if (binding.btn14.text == empty){
                checkMine(3,1)
                binding.btn14.text = board[3][1].toString()
            }
        }else{
            binding.btn14.setBackgroundResource(R.drawable.bomb)
        }

        if (map[3][2] != -1){
            if (binding.btn15.text == empty){
                checkMine(3,2)
                binding.btn15.text = board[3][2].toString()
            }
        }else{
            binding.btn15.setBackgroundResource(R.drawable.bomb)
        }
        
        if (map[3][3] != -1){
            if (binding.btn16.text == empty){
                checkMine(3,3)
                binding.btn16.text = board[3][3].toString()
            }
        }else{
            binding.btn16.setBackgroundResource(R.drawable.bomb)
        }
    }

}