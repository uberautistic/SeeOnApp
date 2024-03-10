package com.example.seeon.splashfragments

import android.animation.ObjectAnimator
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.seeon.MainActivity
import com.example.seeon.R
import com.example.seeon.databinding.FragmentSplashBinding
import com.example.seeon.replaceActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashFragment : Fragment() {
    private lateinit var binding: FragmentSplashBinding
    private lateinit var mAuth:FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding=FragmentSplashBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        mAuth=FirebaseAuth.getInstance()
        CoroutineScope(Dispatchers.Main).launch {
            binding.splashProgressBar.progress=10
            val value=100
            ObjectAnimator.ofInt(binding.splashProgressBar, "progress", value).setDuration(2000).start()
            delay(2000)
            if (mAuth.currentUser!=null)
                replaceActivity(MainActivity())
            else{
                findNavController().navigate(R.id.action_splashFragment_to_startFragment)
            }

        }
    }

}