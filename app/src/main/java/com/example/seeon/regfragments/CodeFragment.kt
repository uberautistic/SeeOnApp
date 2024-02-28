package com.example.seeon.regfragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.seeon.MainActivity
import com.example.seeon.databinding.FragmentCodeBinding
import com.example.seeon.showToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider


class CodeFragment : Fragment() {

    private lateinit var binding: FragmentCodeBinding
    private lateinit var phoneNumber: String
    private lateinit var id: String
    private lateinit var mAuth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentCodeBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onStart() {
        super.onStart()
        mAuth= FirebaseAuth.getInstance()
        binding.authButton.setOnClickListener {
            val code= getCode()
            if (code.isEmpty() || code.length!=6){
                showToast(requireActivity(),"Введите код")
            }else{
                enterCode(code)
            }
        }
    }

    private fun enterCode(code: String) {
        id= arguments?.getString("id").toString()
        val credential=PhoneAuthProvider.getCredential(id, code)
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

    fun getCode(): String{
        val code=binding.code1.text.toString()+binding.code2.text.toString()+
                binding.code3.text.toString()+binding.code4.text.toString()+
                binding.code5.text.toString()+binding.code6.text.toString()
        return code
    }
}