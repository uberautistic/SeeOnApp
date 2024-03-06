package com.example.seeon.loginfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.seeon.MainActivity
import com.example.seeon.databinding.FragmentLoginCodeBinding
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
    private fun getFormattedUserPhoneNumber(phoneNumber: String): String {
        val group1 = listOf(2, 3, 4)
        val group2 = listOf(5, 6, 7)
        val group3 = listOf(8, 9)
        val group4 = listOf(10, 11)
        return "+7 (" + phoneNumber.slice(group1) + ") " +
                phoneNumber.slice(group2) + "-" + phoneNumber.slice(group3) +
                "-" + phoneNumber.slice(group4)
    }
    private fun initValues() {
        mAuth=FirebaseAuth.getInstance()
        id= arguments?.getString("id").toString()
        phoneNumber = arguments?.getString("phoneNumber").toString()
        binding.userPhoneTV.text= getFormattedUserPhoneNumber(phoneNumber)
    }
}