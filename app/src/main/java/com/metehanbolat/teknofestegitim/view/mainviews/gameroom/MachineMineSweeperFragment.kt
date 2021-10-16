package com.metehanbolat.teknofestegitim.view.mainviews.gameroom

import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import java.util.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.app.AlertDialog
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
import com.metehanbolat.teknofestegitim.databinding.FragmentMachineMineSweeperBinding
import com.metehanbolat.teknofestegitim.utils.UserFirebaseProcess
import com.metehanbolat.teknofestegitim.view.mainviews.machinelearning.model.QuestionAPI
import com.metehanbolat.teknofestegitim.view.mainviews.machinelearning.model.QuestionAPIEnglish
import com.metehanbolat.teknofestegitim.view.mainviews.machinelearning.model.QuestionModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MachineMineSweeperFragment : Fragment() {

    private var _binding : FragmentMachineMineSweeperBinding? = null
    private val binding get() = _binding!!

    private lateinit var firebaseFirestore: FirebaseFirestore
    private lateinit var firebaseAuth: FirebaseAuth

    private var rowNumber = 4
    private var colNumber = 4
    private var size = 16
    private lateinit var map : Array<IntArray>
    private lateinit var board : Array<IntArray>
    private var rand = Random()
    private var chooseOne = 0
    private var chooseTwo = 0
    private var success = 0
    private val mineNumber = 6
    private lateinit var questionSeen : String
    private lateinit var questionAnswered : String
    private lateinit var baseUrl : String
    private lateinit var empty : String

    private lateinit var navController : NavController

    private var questionModels : ArrayList<QuestionModel> ?= null
    private lateinit var questionsRightAnswer : String
    private var questionNumber = Random().nextInt(24)

    private lateinit var call : Call<List<QuestionModel>>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMachineMineSweeperBinding.inflate(inflater, container, false)
        val view = binding.root

        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(),R.color.mine_sweeper_background_color)

        firebaseAuth = Firebase.auth
        firebaseFirestore = Firebase.firestore

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

        mineSweeper(rowNumber, colNumber)
        prepareGame()

        binding.restartMineGame.setOnClickListener {
            navController=findNavController()
            navController.navigate(R.id.action_machineMineSweeperFragment_self)
        }

        binding.btn1.setOnClickListener {
            chooseOne = 0
            chooseTwo = 0
            pressButton(it)
            if (map[chooseOne][chooseTwo] != -1){
                binding.btn1.text = board[chooseOne][chooseTwo].toString()
            }else{
                binding.btn1.setBackgroundResource(R.drawable.bomb)
            }
            binding.btn1.isClickable = false
        }

        binding.btn2.setOnClickListener {
            chooseOne = 0
            chooseTwo = 1
            pressButton(it)
            if (map[chooseOne][chooseTwo] != -1){
                binding.btn2.text = board[chooseOne][chooseTwo].toString()
            }else{
                binding.btn2.setBackgroundResource(R.drawable.bomb)
            }
            binding.btn2.isClickable = false
        }

        binding.btn3.setOnClickListener {
            chooseOne = 0
            chooseTwo = 2
            pressButton(it)
            if (map[chooseOne][chooseTwo] != -1){
                binding.btn3.text = board[chooseOne][chooseTwo].toString()
            }else{
                binding.btn3.setBackgroundResource(R.drawable.bomb)
            }
            binding.btn3.isClickable = false

        }

        binding.btn4.setOnClickListener {
            chooseOne = 0
            chooseTwo = 3
            pressButton(it)
            if (map[chooseOne][chooseTwo] != -1){
                binding.btn4.text = board[chooseOne][chooseTwo].toString()
            }else{
                binding.btn4.setBackgroundResource(R.drawable.bomb)
            }
            binding.btn4.isClickable = false
        }

        binding.btn5.setOnClickListener {
            chooseOne = 1
            chooseTwo = 0
            pressButton(it)
            if (map[chooseOne][chooseTwo] != -1){
                binding.btn5.text = board[chooseOne][chooseTwo].toString()
            }else{
                binding.btn5.setBackgroundResource(R.drawable.bomb)
            }
            binding.btn5.isClickable = false
        }

        binding.btn6.setOnClickListener {
            chooseOne = 1
            chooseTwo = 1
            pressButton(it)
            if (map[chooseOne][chooseTwo] != -1){
                binding.btn6.text = board[chooseOne][chooseTwo].toString()
            }else{
                binding.btn6.setBackgroundResource(R.drawable.bomb)
            }
            binding.btn6.isClickable = false
        }

        binding.btn7.setOnClickListener {
            chooseOne = 1
            chooseTwo = 2
            pressButton(it)
            if (map[chooseOne][chooseTwo] != -1){
                binding.btn7.text = board[chooseOne][chooseTwo].toString()
            }else{
                binding.btn7.setBackgroundResource(R.drawable.bomb)
            }
            binding.btn7.isClickable=false
        }

        binding.btn8.setOnClickListener {
            chooseOne = 1
            chooseTwo = 3
            pressButton(it)
            if (map[chooseOne][chooseTwo] != -1){
                binding.btn8.text = board[chooseOne][chooseTwo].toString()
            }else{
                binding.btn8.setBackgroundResource(R.drawable.bomb)
            }
            binding.btn8.isClickable = false
        }

        binding.btn9.setOnClickListener {
            chooseOne = 2
            chooseTwo = 0
            pressButton(it)
            if (map[chooseOne][chooseTwo] != -1){
                binding.btn9.text = board[chooseOne][chooseTwo].toString()
            }else{
                binding.btn9.setBackgroundResource(R.drawable.bomb)
            }
            binding.btn9.isClickable = false
        }

        binding.btn10.setOnClickListener {
            chooseOne = 2
            chooseTwo = 1
            pressButton(it)
            if (map[chooseOne][chooseTwo] != -1){
                binding.btn10.text = board[chooseOne][chooseTwo].toString()
            }else{
                binding.btn10.setBackgroundResource(R.drawable.bomb)
            }
            binding.btn10.isClickable = false
        }

        binding.btn11.setOnClickListener {
            chooseOne = 2
            chooseTwo = 2
            pressButton(it)
            if (map[chooseOne][chooseTwo] != -1){
                binding.btn11.text = board[chooseOne][chooseTwo].toString()

            }else{
                binding.btn11.setBackgroundResource(R.drawable.bomb)
            }
            binding.btn11.isClickable = false
        }

        binding.btn12.setOnClickListener {
            chooseOne = 2
            chooseTwo = 3
            pressButton(it)
            if (map[chooseOne][chooseTwo] != -1){
                binding.btn12.text = board[chooseOne][chooseTwo].toString()

            }else{
                binding.btn12.setBackgroundResource(R.drawable.bomb)
            }
            binding.btn12.isClickable = false

        }
        binding.btn13.setOnClickListener {
            chooseOne = 3
            chooseTwo = 0
            pressButton(it)
            if (map[chooseOne][chooseTwo] != -1){
                binding.btn13.text = board[chooseOne][chooseTwo].toString()
            }else{
                binding.btn13.setBackgroundResource(R.drawable.bomb)
            }
            binding.btn13.isClickable = false
        }

        binding.btn14.setOnClickListener {
            chooseOne = 3
            chooseTwo = 1
            pressButton(it)
            if (map[chooseOne][chooseTwo] != -1){
                binding.btn14.text = board[chooseOne][chooseTwo].toString()
            }else{
                binding.btn14.setBackgroundResource(R.drawable.bomb)
            }
            binding.btn14.isClickable = false
        }

        binding.btn15.setOnClickListener {
            chooseOne = 3
            chooseTwo = 2
            pressButton(it)
            if (map[chooseOne][chooseTwo] != -1){
                binding.btn15.text = board[chooseOne][chooseTwo].toString()
            }else{
                binding.btn15.setBackgroundResource(R.drawable.bomb)
            }
            binding.btn15.isClickable = false
        }

        binding.btn16.setOnClickListener {
            chooseOne = 3
            chooseTwo = 3
            pressButton(it)
            if (map[chooseOne][chooseTwo] != -1){
                binding.btn16.text = board[chooseOne][chooseTwo].toString()
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
        if (map[chooseOne][chooseTwo] != -1) {
            checkMine(chooseOne, chooseTwo)
            success++
            if (success == (size - (mineNumber))) {
                Snackbar.make(view, resources.getString(R.string.congratulations),Snackbar.LENGTH_SHORT).show()
                val earnGold = UserFirebaseProcess(firebaseFirestore, resources.getString(R.string.firebase_userData), firebaseAuth.currentUser!!.email.toString())
                earnGold.getCoin(resources.getString(R.string.firebase_userCoin)){
                    earnGold.userCoinIncrease(resources.getString(R.string.firebase_userCoin), it!!, 10)
                }
            }
        } else {
            Snackbar.make(view, resources.getString(R.string.press_mine),Snackbar.LENGTH_SHORT).show()
            showImagePickDialog(view)
        }
    }

    private fun mineSweeper(rowNumber: Int, colNumber: Int) {
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
        call = if (Locale.getDefault().language.toString() == resources.getString(R.string.tr)){
            val service = retrofit.create(QuestionAPI::class.java)
            service.getData()
        }else{
            val service = retrofit.create(QuestionAPIEnglish::class.java)
            service.getData()
        }

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
        questionsRightAnswer = questionModels!![questionNumber].rightAnswer
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
                    Snackbar.make(view, trueAnswer, Snackbar.LENGTH_SHORT).show()
                    questionAnswered = alreadyAnswered
                    questionSeen = alreadySeen

                }else{
                    Snackbar.make(view, wrongAnswer, Snackbar.LENGTH_SHORT).show()
                    questionAnswered = haventAnswered
                    questionSeen = haventSeen
                    bombProcess()
                    val coinDecrease = UserFirebaseProcess(firebaseFirestore, resources.getString(R.string.firebase_userData), firebaseAuth.currentUser!!.email.toString())
                    coinDecrease.getCoin(resources.getString(R.string.firebase_userCoin)){
                        coinDecrease.userCoinDecrease(resources.getString(R.string.firebase_userCoin), it!!, 5)
                    }
                }
                questionNumber = Random().nextInt(24)
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
                    val coinDecrease = UserFirebaseProcess(firebaseFirestore, resources.getString(R.string.firebase_userData), firebaseAuth.currentUser!!.email.toString())
                    coinDecrease.getCoin(resources.getString(R.string.firebase_userCoin)){
                        coinDecrease.userCoinDecrease(resources.getString(R.string.firebase_userCoin), it!!, 5)
                    }
                }
                questionNumber = Random().nextInt(24)
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
                    val coinDecrease = UserFirebaseProcess(firebaseFirestore, resources.getString(R.string.firebase_userData), firebaseAuth.currentUser!!.email.toString())
                    coinDecrease.getCoin(resources.getString(R.string.firebase_userCoin)){
                        coinDecrease.userCoinDecrease(resources.getString(R.string.firebase_userCoin), it!!, 5)
                    }
                }
                questionNumber = Random().nextInt(24)

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
                    val coinDecrease = UserFirebaseProcess(firebaseFirestore, resources.getString(R.string.firebase_userData), firebaseAuth.currentUser!!.email.toString())
                    coinDecrease.getCoin(resources.getString(R.string.firebase_userCoin)){
                        coinDecrease.userCoinDecrease(resources.getString(R.string.firebase_userCoin), it!!, 5)
                    }
                }
                questionNumber = Random().nextInt(24)
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