package com.example.seeon.regfragments

import android.net.Uri
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
import com.example.seeon.databinding.FragmentCodeBinding
import com.example.seeon.getFormattedUserPhoneNumber
import com.example.seeon.replaceActivity
import com.example.seeon.showToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference


class CodeFragment : Fragment() {

    private lateinit var binding: FragmentCodeBinding
    private lateinit var phoneNumber: String
    private lateinit var id: String
    private lateinit var name: String
    private lateinit var birthdate: String
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mDataBase: DatabaseReference
    private lateinit var stringPhotoUri: String
    private lateinit var mStorage: StorageReference
    private lateinit var userPhotoURL: String
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
        init()
    }

    override fun onResume() {
        super.onResume()
        val timer = object:  CountDownTimer(60000, 1000){
            override fun onTick(millisUntilFinished: Long) {
                binding.resendCodeTV.text="Отправить код еще раз (${millisUntilFinished/1000})"
            }
            override fun onFinish() {
                binding.resendCodeTV.text="Отправить код еще раз"
                binding.resendCodeTV.setTextColor(ContextCompat.getColor(requireContext(),R.color.textColor))
            }
        }
        timer.start()
    }
    private fun init() {
        binding.returnB.setOnClickListener {
            findNavController().popBackStack()
        }
        mStorage= FirebaseStorage.getInstance().reference
        stringPhotoUri= arguments?.getString("uphoto").toString()
        mAuth= FirebaseAuth.getInstance()
        mDataBase=FirebaseDatabase.getInstance().reference
        id= arguments?.getString("id").toString()
        phoneNumber= arguments?.getString("phoneNumber").toString()
        name= arguments?.getString("uname").toString()
        birthdate=arguments?.getString("ubirthdate").toString()
        binding.userPhoneTV.text=phoneNumber
        binding.inputCode.addTextChangedListener{
            val code = binding.inputCode.text.toString()
            if(code.length==6)
                enterCode(code)
        }
        binding.userPhoneTV.text=getFormattedUserPhoneNumber(phoneNumber)
    }
    private fun registerUser(uid: String){
        val userDataMap= mutableMapOf<String,Any>()
        userDataMap["uid"]=uid
        userDataMap["uphone"]=phoneNumber
        userDataMap["uname"]=name
        userDataMap["uphoto"]=userPhotoURL
        userDataMap["ubirthdate"]=birthdate
        mDataBase.child("users").child(phoneNumber).updateChildren(userDataMap).addOnCompleteListener { task3->
            if(task3.isSuccessful){
                showToast(requireActivity(),"Регистрация прошла успешно!")
                replaceActivity(MainActivity())
            } else showToast(requireActivity(),task3.exception?.message.toString())
        }
    }
    private fun enterCode(code: String) {
        val credential=PhoneAuthProvider.getCredential(id, code)
        mAuth.signInWithCredential(credential).addOnCompleteListener{task1->
            if(task1.isSuccessful){
                val uid=mAuth.currentUser?.uid.toString()
                if (stringPhotoUri=="0"){
                    userPhotoURL="https://firebasestorage.googleapis.com/v0/b/seeon-8eade.appspot.com/o/profile_pictures%2FdefaultPFP.png?alt=media&token=3a9c56db-1fe8-4e8c-adc3-27f627f61823"
                    registerUser(uid)
                }else {
                    val path=mStorage.child("profile_pictures")
                        .child(uid)
                    val photoUri= Uri.parse(stringPhotoUri)
                    path.putFile(photoUri).addOnCompleteListener{task2->
                        if (task2.isSuccessful){
                            showToast(requireActivity(),"Фото успешно загружено")
                            path.downloadUrl.addOnCompleteListener {
                                if (it.isSuccessful){
                                    userPhotoURL=it.result.toString()
                                    registerUser(uid)
                                }
                            }
                        }else{
                            showToast(requireActivity(),task2.exception.toString())
                        }
                    }
                }

                /*val photoUri= Uri.parse(stringPhotoUri)
                path.putFile(photoUri).addOnCompleteListener{task2->
                    if (task2.isSuccessful){
                        showToast(requireActivity(),"Фото успешно загружено")
                        path.downloadUrl.addOnCompleteListener {
                            if (it.isSuccessful)
                            {
                                userPhotoURL= it.result.toString()
                                userDataMap["uid"]=uid
                                userDataMap["uphone"]=phoneNumber
                                userDataMap["uname"]=name
                                userDataMap["uphoto"]=userPhotoURL
                                userDataMap["ubirthdate"]=birthdate
                                mDataBase.child("users").child(phoneNumber).updateChildren(userDataMap).addOnCompleteListener { task3->
                                    if(task3.isSuccessful){
                                        showToast(requireActivity(),"Регистрация прошла успешно!")
                                        replaceActivity(MainActivity())
                                    } else showToast(requireActivity(),task3.exception?.message.toString())
                                }
                            }
                            else
                                showToast(requireActivity(),it.exception.toString())
                        }
                    } else{
                        showToast(requireActivity(),task2.exception.toString())
                    }
                }*/
            } else{
                showToast(requireActivity(),task1.exception?.message.toString())
            }
        }
    }
}