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
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem


class HomeFragment : Fragment() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var binding: FragmentHomeBinding
    private lateinit var genresList : ArrayList<Int>
    private lateinit var filmsList : ArrayList<Int>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }

    private fun init() {
        mAuth= FirebaseAuth.getInstance()
        binding.genresRecycler.layoutManager= LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
        initGenresList()
        initFilmsList()
        binding.genresRecycler.adapter= GenresAdapter(requireContext(), genresList)
        val list= mutableListOf<CarouselItem>()
        for (i in 0..<filmsList.size){
            list.add(
                CarouselItem(
                    imageDrawable = filmsList[i]
                )
            )
        }
        binding.filmsRecycler.setData(list)
        binding.filmsRecycler.registerLifecycle(lifecycle)

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
    private fun initFilmsList(){
        filmsList= ArrayList()
        filmsList.add(R.drawable.barbie)
        filmsList.add(R.drawable.gotg3)
        filmsList.add(R.drawable.smatsv)
    }

    private fun getFilmList(): List<String>{
        return listOf(
            "https://www.kasandbox.org/programming-images/avatars/spunky-sam.png",
            "https://www.kasandbox.org/programming-images/avatars/spunky-sam-green.png",
            "https://www.kasandbox.org/programming-images/avatars/purple-pi.png",
            "https://www.kasandbox.org/programming-images/avatars/purple-pi-teal.png",
            "https://www.kasandbox.org/programming-images/avatars/purple-pi-pink.png",
            "https://www.kasandbox.org/programming-images/avatars/primosaur-ultimate.png",
            "https://www.kasandbox.org/programming-images/avatars/primosaur-tree.png",
            "https://www.kasandbox.org/programming-images/avatars/primosaur-sapling.png"
        )
    }

    override fun onStart() {
        super.onStart()
        init()

    }

}
