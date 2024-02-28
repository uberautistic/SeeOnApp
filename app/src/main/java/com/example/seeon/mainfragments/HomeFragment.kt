package com.example.seeon.mainfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.seeon.LoginActivity
import com.example.seeon.databinding.FragmentHomeBinding
import com.example.seeon.replaceActivity
import com.google.firebase.auth.FirebaseAuth


class HomeFragment : Fragment() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var binding: FragmentHomeBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }

    private fun init() {
        mAuth= FirebaseAuth.getInstance()
    }


    override fun onStart() {
        super.onStart()
        init()
        binding.logoutB.setOnClickListener {
            mAuth.signOut()
            replaceActivity(LoginActivity())
        }
    }

}
