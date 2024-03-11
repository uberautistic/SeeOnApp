package com.example.seeon.loginfragments

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.seeon.MainActivity
import com.example.seeon.R
import com.example.seeon.databinding.FragmentLoginCodeBinding
import com.example.seeon.getFormattedUserPhoneNumber
import com.example.seeon.replaceActivity
import com.example.seeon.showToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider

class LoginCodeFragment : Fragment() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var binding: FragmentLoginCodeBinding
    private lateinit var id: String
    private lateinit var phoneNumber: String
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLoginCodeBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onStart() {
        super.onStart()
        initValues()
        initListeners()
    }

    override fun onResume() {
        super.onResume()
        val timer = object:  CountDownTimer(60000, 1000){
            override fun onTick(millisUntilFinished: Long) {
                binding.resendCodeTV.text="Отправить код еще раз (${millisUntilFinished/1000})"
            }
            override fun onFinish() {
                binding.resendCodeTV.text="Отправить код еще раз"
                binding.resendCodeTV.setTextColor(ContextCompat.getColor(requireActivity(), R.color.textColor))
            }
        }
        timer.start()
    }
    private fun initListeners() {
        binding.inputCode.addTextChangedListener {
            val code= binding.inputCode.text.toString()
            if (code.length==6)
                enterCode(code)
        }
        binding.returnB.setOnClickListener {
            findNavController().popBackStack()
        }
    }
    private fun enterCode(code:String) {
        val credential= PhoneAuthProvider.getCredential(id, code)
        mAuth.signInWithCredential(credential).addOnCompleteListener { task1->
            if(task1.isSuccessful){
                showToast(requireContext(),"success")
                replaceActivity(MainActivity())
            } else {
                showToast(requireContext(),task1.exception.toString())
            }
        }
    }

    private fun initValues() {
        mAuth=FirebaseAuth.getInstance()
        id= arguments?.getString("id").toString()
        phoneNumber = arguments?.getString("phoneNumber").toString()
        binding.userPhoneTV.text= getFormattedUserPhoneNumber(phoneNumber)
    }
}