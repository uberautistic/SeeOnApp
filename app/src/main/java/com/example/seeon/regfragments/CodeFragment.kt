package com.example.seeon.regfragments

import android.content.Intent
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
    private lateinit var password: String
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
        password=arguments?.getString("upassword").toString()
        binding.userPhoneTV.text=phoneNumber
        binding.inputCode.addTextChangedListener{
            val code = binding.inputCode.text.toString()
            if(code.length==6)
                enterCode(code)
        }

    }

    private fun enterCode(code: String) {
        userPhotoURL="https://www.flaticon.com/ru/free-icon/user-profile_5675059?term=user&page=1&position=11&origin=search&related_id=5675059"
        val credential=PhoneAuthProvider.getCredential(id, code)
        mAuth.signInWithCredential(credential).addOnCompleteListener{task1->
            if(task1.isSuccessful){
                var userDataMap= mutableMapOf<String,Any>()

                /*const val NODE_USERS: String ="users"
                const val CHILD_UID : String="uid"
                const val CHILD_UPHONE : String="uphone"
                const val CHILD_UNAME : String="uname"
                const val CHILD_UBIRTHDATE : String="ubirthdate"
                const val CHILD_UPHOTO : String="uphoto"
                const val CHILD_UPASSWORD : String="upassword"*/

                val uid=mAuth.currentUser?.uid.toString()
                val path=mStorage.child("profile_pictures")
                    .child(uid)
                val photoUri= Uri.parse(stringPhotoUri)
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
                                userDataMap["upassword"]=password
                                userDataMap["ubirthdate"]=birthdate
                                mDataBase.child("users").child(uid).updateChildren(userDataMap).addOnCompleteListener { task3->
                                    if(task3.isSuccessful){
                                        showToast(requireActivity(),"Регистрация прошла успешно!")
                                        val mainActivity = Intent( requireActivity(), MainActivity::class.java)
                                        startActivity(mainActivity)
                                        requireActivity().finish()
                                    } else showToast(requireActivity(),task3.exception?.message.toString())
                                }
                            }

                            else
                                showToast(requireActivity(),it.exception.toString())
                        }
                    } else{
                        showToast(requireActivity(),task2.exception.toString())
                    }
                }

            } else{
                showToast(requireActivity(),task1.exception?.message.toString())
            }
        }
    }

}