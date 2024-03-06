package com.example.seeon.regfragments

import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.seeon.MainActivity
import com.example.seeon.databinding.FragmentCodeBinding
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
    private fun getFormattedUserPhoneNumber(phoneNumber: String): String {
        val group1 = listOf(2, 3, 4)
        val group2 = listOf(5, 6, 7)
        val group3 = listOf(8, 9)
        val group4 = listOf(10, 11)
        return "+7 (" + phoneNumber.slice(group1) + ") " +
                phoneNumber.slice(group2) + "-" + phoneNumber.slice(group3) +
                "-" + phoneNumber.slice(group4)
    }
    private fun enterCode(code: String) {
        val credential=PhoneAuthProvider.getCredential(id, code)
        mAuth.signInWithCredential(credential).addOnCompleteListener{task1->
            if(task1.isSuccessful){
                val userDataMap= mutableMapOf<String,Any>()
                val uid=mAuth.currentUser?.uid.toString()
                val path=mStorage.child("profile_pictures")
                    .child(uid)
                if (stringPhotoUri=="0"){
                    userPhotoURL="https://firebasestorage.googleapis.com/v0/b/seeon-8eade.appspot.com/o/profile_pictures%2FdefaultPFP.png?alt=media&token=3a9c56db-1fe8-4e8c-adc3-27f627f61823"
                }else {
                    val photoUri= Uri.parse(stringPhotoUri)
                    path.putFile(photoUri).addOnCompleteListener{task2->
                        if (task2.isSuccessful){
                            showToast(requireActivity(),"Фото успешно загружено")
                            path.downloadUrl.addOnCompleteListener {
                                if (it.isSuccessful)
                                    userPhotoURL= it.result.toString()
                            }
                        }else{
                            showToast(requireActivity(),task2.exception.toString())
                        }
                    }
                }
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