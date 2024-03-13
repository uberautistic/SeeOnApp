package com.example.seeon.filmsfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.seeon.databinding.FragmentSeatsBinding

class SeatsFragment : Fragment() {

    private lateinit var binding: FragmentSeatsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSeatsBinding.inflate(inflater, container, false)
        return binding.root
    }
}