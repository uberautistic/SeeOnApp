package com.example.seeon.mainfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.seeon.R
import com.example.seeon.adapter.GenresAdapter
import com.example.seeon.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth


class HomeFragment : Fragment() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var binding: FragmentHomeBinding
    private lateinit var genresList : ArrayList<Int>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }

    private fun init() {
        mAuth= FirebaseAuth.getInstance()
        binding.genresRecycler.layoutManager= LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        initGenresList()

        binding.genresRecycler.adapter= GenresAdapter(requireContext(), genresList)
    }

    private fun initGenresList() {
        genresList=ArrayList<Int>()
        genresList.add(R.drawable.fantastic)
        genresList.add(R.drawable.horror)
        genresList.add(R.drawable.comedy)
        genresList.add(R.drawable.fantasy)
        genresList.add(R.drawable.melodrama)
        genresList.add(R.drawable.thriller)
    }


    override fun onStart() {
        super.onStart()
        init()

    }

}
