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
import com.example.seeon.hideKeyboard
import com.example.seeon.showToast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.concurrent.TimeUnit


class PhoneNumberFragment : Fragment() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var binding: FragmentPhoneNumberBinding
    private lateinit var phoneNumber: String
    private lateinit var mDatabase: DatabaseReference
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
        mDatabase= FirebaseDatabase.getInstance().getReference("users")
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
                hideKeyboard()
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
            val check= "+7$phoneNumber"
            mDatabase.child(check).get().addOnSuccessListener {
                if (it.exists()){
                    binding.SHOWPROGRESS.visibility= View.VISIBLE
                    authUser()
                }else{
                    binding.SHOWPROGRESS.visibility= View.VISIBLE
                    val builder = AlertDialog.Builder(requireContext())
                    builder.setTitle("Ошибка")
                    builder.setMessage("Пользователь с номером:\n "+getFormattedUserPhoneNumber(check)
                                +" не найден в базе данных, желаете зарегистрироваться?")
                    builder.setPositiveButton("Да") { dialogInterface, which->
                        findNavController().navigate(R.id.userInfoFragment)
                        hideKeyboard()
                    }
                    builder.setNegativeButton("Нет"){ dialogInterface, which->
                        binding.SHOWPROGRESS.visibility= View.INVISIBLE
                    }
                    val errorDialog : AlertDialog= builder.create()
                    errorDialog.setCancelable(false)
                    errorDialog.show()
                }
            }
        }
        binding.returnB.setOnClickListener {
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Закрыть SeeOn?")
            builder.setNegativeButton("Да") { dialogInterface, which->
                requireActivity().finish()
            }
            builder.setPositiveButton("Нет"){ dialogInterface, which->
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
            hideKeyboard()
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