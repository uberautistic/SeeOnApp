package com.example.seeon

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.seeon.databinding.FragmentAuthBinding


class AuthFragment : Fragment() {

    private lateinit  var binding: FragmentAuthBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAuthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.regActivityLink.setOnClickListener{
            findNavController().navigate(R.id.action_AuthFragment_to_RegFragment)
        }
        binding.enterButton.setOnClickListener{
            val mainActivity = Intent(requireActivity() , MainActivity::class.java)
            startActivity(mainActivity)
            requireActivity().finish()
        }

    }

}