package com.example.seeon.regfragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.seeon.MainActivity
import com.example.seeon.R
import com.example.seeon.databinding.FragmentRegPhoneBinding
import com.example.seeon.showToast
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit


class RegPhoneFragment : Fragment() {
    private lateinit var name: String
    private lateinit var birthdate: String
    private lateinit  var binding: FragmentRegPhoneBinding
    private  lateinit var mPhoneNumber: String
    private lateinit var stringPhotoUri:String
    private lateinit var password: String
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
        init()
    }
    override fun onResume() {
        super.onResume()
        initListeners()
    }
    private fun init(){
        stringPhotoUri=arguments?.getString("uphoto").toString()
        name= arguments?.getString("uname").toString()
        birthdate= arguments?.getString("ubirthdate").toString()
        mAuth=FirebaseAuth.getInstance()
        mCallBack=object :PhoneAuthProvider.OnVerificationStateChangedCallbacks(){
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
                findNavController().navigate(R.id.action_AuthFragment_to_codeFragment, getBundleC(id))
            }
        }
    }
    private fun initListeners() {
        binding.returnB.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.userPasswordConfirmationET.addTextChangedListener {
            val userPassword= binding.userPasswordET.text.toString()
            val userPasswordConf=binding.userPasswordConfirmationET.text.toString()
            if(userPasswordConf!=userPassword){
                highLightPasswordETText(true)
                binding.errorTV.visibility=View.VISIBLE
                binding.nextStageB.visibility=View.GONE
            } else if (binding.errorTV.visibility==View.VISIBLE){
                highLightPasswordETText(false)
                    binding.errorTV.visibility=View.GONE
                    binding.nextStageB.visibility=View.VISIBLE
            }
        }
        binding.nextStageB.setOnClickListener{
            mPhoneNumber = binding.userPhoneNumberET.text.toString()
            password= binding.userPasswordET.text.toString()
            if(mPhoneNumber.isEmpty()){
                showToast(requireActivity(),"Введите номер телефона")
            } else if (mPhoneNumber.length!=10){
                showToast(requireActivity(),"Введите номер телефона корректно")
            } else if(password.length<8)
                showToast(requireActivity(),"Пароль должен содержать не менее 8 символов")
            else{
                binding.SHOWPROGRESS.visibility= View.VISIBLE
                authUser()
            }

        }
    }
    private fun highLightPasswordETText(highlighted: Boolean){
        if (highlighted){
            binding.userPasswordET.setTextColor(ContextCompat.getColor(requireActivity(),R.color.lightRed))
            binding.userPasswordConfirmationET.setTextColor(ContextCompat.getColor(requireActivity(),R.color.lightRed))
        } else {
            binding.userPasswordET.setTextColor(ContextCompat.getColor(requireActivity(),R.color.textColor))
            binding.userPasswordConfirmationET.setTextColor(ContextCompat.getColor(requireActivity(),R.color.textColor))
        }
    }
    private fun getBundleC(id: String):Bundle{
        val bundle= Bundle()
        bundle.putString("uname",name)
        bundle.putString("ubirthdate",birthdate)
        bundle.putString("upassword",password)
        bundle.putString("phoneNumber", mPhoneNumber)
        bundle.putString("uphoto",stringPhotoUri)
        bundle.putString("id", id)
        return bundle
    }
    private fun authUser() {
        mPhoneNumber= "+7$mPhoneNumber"
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