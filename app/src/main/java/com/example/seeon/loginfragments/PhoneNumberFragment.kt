package com.example.seeon.loginfragments

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.seeon.MainActivity
import com.example.seeon.R
import com.example.seeon.databinding.FragmentPhoneNumberBinding
import com.example.seeon.showToast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit


class PhoneNumberFragment : Fragment() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var binding: FragmentPhoneNumberBinding
    private lateinit var phoneNumber: String
    private lateinit var mCallBack: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentPhoneNumberBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onStart() {
        super.onStart()
        initValues()
        initListeners()
    }

    private fun initValues() {
        mAuth=FirebaseAuth.getInstance()
        mCallBack=object : PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                mAuth.signInWithCredential(credential).addOnCompleteListener{
                    if(it.isSuccessful){
                        showToast(requireActivity(),"Регистрация прошла успешно!")
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
                findNavController().navigate(R.id.action_phoneNumberFragment_to_loginCodeFragment, getBundleC(id))
            }
        }
    }
    private fun getBundleC(id: String): Bundle{
        val bundle=Bundle()
        bundle.putString("id", id)
        bundle.putString("phoneNumber", phoneNumber)
        return bundle
    }
    private fun initListeners() {
        binding.nextStageB.setOnClickListener {
            phoneNumber=binding.userPhoneNumberET.text.toString()
            binding.SHOWPROGRESS.visibility= View.VISIBLE
            authUser()
        }
        binding.returnB.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Закрыть SeeOn?")
            builder.setPositiveButton("Да") { dialogInterface, which->
                requireActivity().finish()
            }
            builder.setNegativeButton("Нет"){ dialogInterface, which->
            }
            val closeDialog : AlertDialog= builder.create()
            closeDialog.setCancelable(false)
            closeDialog.show()
        }
        binding.userPhoneNumberET.addTextChangedListener {
            val phoneNumber = binding.userPhoneNumberET.text.toString()
            if (phoneNumber.length==10)
                binding.nextStageB.visibility= View.VISIBLE else
                    binding.nextStageB.visibility= View.INVISIBLE
        }
        binding.toRegTV.setOnClickListener {
            findNavController().navigate(R.id.userInfoFragment)
        }
    }
    private fun authUser() {
        phoneNumber= "+7$phoneNumber"
        PhoneAuthProvider.verifyPhoneNumber(
            PhoneAuthOptions
                .newBuilder()
                .setActivity(requireActivity())
                .setPhoneNumber(phoneNumber)
                .setTimeout(60L, TimeUnit.SECONDS)
                .setCallbacks(mCallBack)
                .build()
        )
    }

}