package com.example.seeon.regfragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.seeon.MainActivity
import com.example.seeon.R
import com.example.seeon.RegFragment
import com.example.seeon.databinding.FragmentRegPhoneBinding
import com.example.seeon.replaceFragment
import com.example.seeon.showToast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit


class RegPhoneFragment : Fragment() {

    private lateinit  var binding: FragmentRegPhoneBinding
    private  lateinit var mPhoneNumber: String
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mCallBack: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentRegPhoneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        showToast(requireActivity(),"tut vse ok")
        mAuth=FirebaseAuth.getInstance()
        mCallBack=object :PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                mAuth.signInWithCredential(credential).addOnCompleteListener{
                    if(it.isSuccessful){
                        showToast(requireActivity(),"dobro pojalovat")
                        val mainActivity = Intent( requireActivity(), MainActivity::class.java)
                        startActivity(mainActivity)
                        requireActivity().finish()
                    } else{
                        showToast(requireActivity(),it.exception?.message.toString())
                    }
                }
            }

            override fun onVerificationFailed(p0: FirebaseException) {

                showToast(requireActivity(),p0.message.toString())
            }

            override fun onCodeSent(id: String, token: PhoneAuthProvider.ForceResendingToken) {
                val bundle = Bundle()
                bundle.putString("phoneNumber", mPhoneNumber)
                bundle.putString("id", id)
                findNavController().navigate(R.id.action_AuthFragment_to_codeFragment, bundle)
            }
        }
    }
    override fun onResume() {
        super.onResume()
        binding.regActivityLink.setOnClickListener{
            replaceFragment(RegFragment())
        }
        binding.enterButton.setOnClickListener{
            mPhoneNumber = binding.inputPhoneNumber.text.toString()
            if(mPhoneNumber.isEmpty()){
                showToast(requireActivity(),"Введите номер телефона")
            } else if (mPhoneNumber.length!=10){
                showToast(requireActivity(),"Введите номер телефона корректно")
            } else{
                showToast(requireActivity(),"tut vse ok")
                authUser()

            }

        }
    }

    private fun authUser() {
        mPhoneNumber="+7"+mPhoneNumber

        /*PhoneAuthProvider.getInstance().verifyPhoneNumber(
            mPhoneNumber,
            60,
            TimeUnit.SECONDS,
            requireActivity(),
            mCallBack

        )*/


        PhoneAuthProvider.verifyPhoneNumber(
            PhoneAuthOptions
                .newBuilder()
                .setActivity(requireActivity())
                .setPhoneNumber(mPhoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setCallbacks(mCallBack)
                .build()
        )
    }

}