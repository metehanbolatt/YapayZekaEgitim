package com.metehanbolat.teknofestegitim.view.mainviews.gameroom

import android.app.AlertDialog
import android.os.Bundle
import android.os.CountDownTimer
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
import com.metehanbolat.teknofestegitim.databinding.AiGameTwoDialogDesignBinding
import com.metehanbolat.teknofestegitim.databinding.FragmentAIGameTwoBinding
import com.metehanbolat.teknofestegitim.utils.UserFirebaseProcess
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

    private var _dialogBinding : AiGameTwoDialogDesignBinding? = null
    private val dialogBinding get() = _dialogBinding!!

    private lateinit var auth : FirebaseAuth
    private lateinit var firestore : FirebaseFirestore
    private lateinit var navController: NavController
    private lateinit var postList: List<PostModel>
    private lateinit var question : Array<String?>
    private lateinit var answerOne : Array<String?>
    private lateinit var answerTwo : Array<String?>
    private lateinit var answerThree : Array<String?>
    private lateinit var answerFour : Array<String?>
    private lateinit var correctAnswer : Array<String?>
    private lateinit var gameTimer: CountDownTimer
    private lateinit var preparationTimer : CountDownTimer
    private var earnedCoin : Int = 0
    private var questionNumber : Int = 1

    private var control : Int = 0
    private var control2 : Int = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View  {
        _binding = FragmentAIGameTwoBinding.inflate(inflater, container,false)
        _dialogBinding = AiGameTwoDialogDesignBinding.inflate(inflater, container, false)
        val view = binding.root

        requireActivity().window.statusBarColor = ContextCompat.getColor(requireContext(),R.color.background_blue)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = Firebase.auth
        firestore = Firebase.firestore

        val callback = object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                Snackbar.make(view, resources.getString(R.string.are_u_sure_quit_game), Snackbar.LENGTH_INDEFINITE).setAction(resources.getString(R.string.back)){
                    if (control == 0 && control2 == 0){
                        navController = findNavController()
                        navController.navigate(R.id.action_AIGameTwoFragment_to_machineGameListFragment)
                    }else if (control == 1 && control2 == 0){
                        preparationTimer.cancel()
                        navController = findNavController()
                        navController.navigate(R.id.action_AIGameTwoFragment_to_machineGameListFragment)
                    } else if (control2 == 1){
                        gameTimer.cancel()
                        navController = findNavController()
                        navController.navigate(R.id.action_AIGameTwoFragment_to_machineGameListFragment)
                    }else{
                        preparationTimer.cancel()
                        gameTimer.cancel()
                        navController = findNavController()
                        navController.navigate(R.id.action_AIGameTwoFragment_to_machineGameListFragment)
                    }
                }.show()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(callback)

        val rf = Retrofit.Builder()
            .baseUrl(RetrofitInterface.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create()).build()
        val api = rf.create(RetrofitInterface::class.java)
        val call = api.posts

        call.enqueue(object: Callback<List<PostModel>> {
            override fun onResponse(
                call: Call<List<PostModel>>,
                response: Response<List<PostModel>>
            ) {
                postList = response.body()  as List<PostModel>
                question = arrayOfNulls<String?>(postList.size)
                answerOne = arrayOfNulls<String?>(postList.size)
                answerTwo = arrayOfNulls<String?>(postList.size)
                answerThree = arrayOfNulls<String?>(postList.size)
                answerFour = arrayOfNulls<String?>(postList.size)
                correctAnswer = arrayOfNulls<String?>(postList.size)

                for ( i in postList.indices){
                    question[i] = postList[i].question
                    answerOne[i] = postList[i].answerA
                    answerTwo[i] = postList[i].answerB
                    answerThree[i] = postList[i].answerC
                    answerFour[i] = postList[i].answerD
                    correctAnswer[i] = postList[i].correctAnswer
                }
            }
            override fun onFailure(call: Call<List<PostModel>>, t: Throwable) {
                Snackbar.make(view, t.toString(), Snackbar.LENGTH_LONG).show()
            }
        })

        binding.noGameButton.setOnClickListener {
            navController = findNavController()
            navController.navigate(R.id.action_AIGameTwoFragment_to_machineGameListFragment)
        }
        binding.yesGameButton.setOnClickListener {
            binding.readyAllConcept.visibility = View.INVISIBLE
            binding.yesGameButton.isClickable = false
            binding.countdownReady.visibility = View.VISIBLE
            binding.countdownReady.text = resources.getString(R.string._three)
            control = 1
            preparationTimer = object: CountDownTimer(3100, 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    binding.countdownReady.text = (millisUntilFinished/1000).toString()
                }
                override fun onFinish() {
                    binding.countdownReady.visibility = View.INVISIBLE
                    binding.allConcept.visibility = View.VISIBLE
                    binding.progressBar.apply {
                        progressMax = 100f
                        setProgressWithAnimation(100f,20010)
                        progressBarWidth = 5f
                        backgroundProgressBarWidth = 6f
                    }
                    addQuestion(question,answerOne,answerTwo,answerThree,answerFour,correctAnswer,view)
                    control2 = 1
                    gameTimer = object  : CountDownTimer(20010,1000){
                        override fun onTick(p0: Long) {
                            binding.timeForProgressbar.text = (p0/1000).toString()
                        }
                        override fun onFinish() {
                            getFalseAlertDialog(resources.getString(R.string._null), 1)
                            val getUserCoin = UserFirebaseProcess(firestore,resources.getString(R.string.firebase_userData),auth.currentUser!!.email.toString())
                            getUserCoin.getCoin(resources.getString(R.string.firebase_userCoin)){
                                getUserCoin.userCoinIncrease(resources.getString(R.string.firebase_userCoin),it!!,earnedCoin)
                            }
                        }
                    }.start()
                }
            }.start()
        }
    }

    fun addQuestion(listQuestion: Array<String?>,
                    listAnswerA: Array<String?>,
                    listAnswerB: Array<String?>,
                    listAnswerC: Array<String?>,
                    listAnswerD: Array<String?>,
                    correctAnswer: Array<String?>,view: View){
        val questionSize = listQuestion.size
        val randNumber = (0 until questionSize).random()

        binding.question.text = listQuestion[randNumber]
        binding.questionNumber.text = resources.getString(R.string.question, questionNumber)
        binding.textViewAAnswer.text = listAnswerA[randNumber]
        binding.textViewBAnswer.text = listAnswerB[randNumber]
        binding.textViewCAnswer.text = listAnswerC[randNumber]
        binding.textViewDAnswer.text = listAnswerD[randNumber]

        binding.cardViewA.setOnClickListener {
            questionNumber += 1
            if (listAnswerA[randNumber] == correctAnswer[randNumber]) {
                earnedCoin += 1
                Snackbar.make(it, resources.getString(R.string.ai_game_true), Snackbar.LENGTH_LONG).show()
                addQuestion(listQuestion, listAnswerA, listAnswerB, listAnswerC, listAnswerD, correctAnswer,view)
            } else {
                Snackbar.make(it, resources.getString(R.string.ai_game_false), Snackbar.LENGTH_LONG).show()
                getFalseAlertDialog(correctAnswer[randNumber].toString(), 0)
            }
        }

        binding.cardViewB.setOnClickListener {
            questionNumber += 1
            if (listAnswerB[randNumber] == correctAnswer[randNumber]) {
                earnedCoin += 1
                Snackbar.make(it, resources.getString(R.string.ai_game_true), Snackbar.LENGTH_LONG).show()
                addQuestion(listQuestion, listAnswerA, listAnswerB, listAnswerC, listAnswerD, correctAnswer,view)
            } else {
                Snackbar.make(it, resources.getString(R.string.ai_game_false), Snackbar.LENGTH_LONG).show()
                getFalseAlertDialog(correctAnswer[randNumber].toString(), 0)
            }
        }

        binding.cardViewC.setOnClickListener {
            questionNumber += 1
            if (listAnswerC[randNumber] == correctAnswer[randNumber]) {
                earnedCoin += 1
                Snackbar.make(it, resources.getString(R.string.ai_game_true), Snackbar.LENGTH_LONG).show()
                addQuestion(listQuestion, listAnswerA, listAnswerB, listAnswerC, listAnswerD, correctAnswer,view)
            } else {
                Snackbar.make(it, resources.getString(R.string.ai_game_false), Snackbar.LENGTH_LONG)
                    .show()
                getFalseAlertDialog(correctAnswer[randNumber].toString(), 0)
            }
        }

        binding.cardViewD.setOnClickListener {
            questionNumber += 1
            if (listAnswerD[randNumber] == correctAnswer[randNumber]) {
                earnedCoin += 1
                Snackbar.make(it, resources.getString(R.string.ai_game_true), Snackbar.LENGTH_LONG).show()
                addQuestion(listQuestion, listAnswerA, listAnswerB, listAnswerC, listAnswerD, correctAnswer,view)
            } else {
                Snackbar.make(it, resources.getString(R.string.ai_game_false), Snackbar.LENGTH_LONG).show()
                getFalseAlertDialog(correctAnswer[randNumber].toString(), 0)
            }
        }
    }

    private fun getFalseAlertDialog(correctAns : String, controlNumber : Int){
        binding.allConcept.visibility = View.INVISIBLE
        gameTimer.cancel()

        val getUserCoin = UserFirebaseProcess(firestore,resources.getString(R.string.firebase_userData),auth.currentUser!!.email.toString())
        getUserCoin.getCoin(resources.getString(R.string.firebase_userCoin)){
            getUserCoin.userCoinIncrease(resources.getString(R.string.firebase_userCoin), it!!, earnedCoin)
        }

        val builder = AlertDialog.Builder(context)
        builder.setView(dialogBinding.root)
        val dialog = builder.create()
        dialog.show()
        dialog.setCancelable(false)

        if (controlNumber == 0){
            dialogBinding.dialogTitle.text = resources.getString(R.string.nice_try)
            dialogBinding.correctAnswer.text = resources.getString(R.string.correct_answer, correctAns)
            dialogBinding.earnedCoin.text = resources.getString(R.string.earned_coin, earnedCoin)
        }else{
            if (earnedCoin > 0){
                dialogBinding.dialogTitle.text = resources.getString(R.string.bravo)
                dialogBinding.correctAnswer.text = resources.getString(R.string.better_than_this)
                dialogBinding.earnedCoin.text = resources.getString(R.string.earned_coin, earnedCoin)
            }else{
                dialogBinding.dialogTitle.text = resources.getString(R.string.are_u_awake)
                dialogBinding.correctAnswer.text = resources.getString(R.string.u_should_move)
                dialogBinding.earnedCoin.text = resources.getString(R.string.earned_coin, earnedCoin)
            }
        }

        dialogBinding.homepage.setOnClickListener {
            navController = findNavController()
            navController.navigate(R.id.action_AIGameTwoFragment_to_machineGameListFragment)
            dialog.dismiss()
        }

        dialogBinding.playAgain.setOnClickListener {
            navController = findNavController()
            navController.navigate(R.id.action_AIGameTwoFragment_self)
            dialog.dismiss()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        _dialogBinding = null
    }
}