package com.example.seeon.mainfragments

import android.os.Bundle
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.seeon.LoginActivity
import com.example.seeon.R
import com.example.seeon.adapter.GenresAdapter
import com.example.seeon.databinding.FilmRecyclerItemBinding
import com.example.seeon.databinding.FragmentHomeBinding
import com.example.seeon.replaceActivity
import com.example.seeon.showToast
import com.google.firebase.auth.FirebaseAuth
import org.imaginativeworld.whynotimagecarousel.listener.CarouselListener
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem
import org.imaginativeworld.whynotimagecarousel.utils.setImage


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
        registerForContextMenu(binding.menuButton)
        binding.menuButton.setOnClickListener {
            requireActivity().openContextMenu(binding.menuButton)
        }
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
        binding.filmsRecycler.autoPlay=true
        binding.filmsRecycler.autoPlayDelay=8000
        binding.filmsRecycler.registerLifecycle(lifecycle)
        binding.filmsRecycler.carouselListener= object: CarouselListener{
            override fun onCreateViewHolder(
                layoutInflater: LayoutInflater,
                parent: ViewGroup
            ): ViewBinding {
                return FilmRecyclerItemBinding.inflate(layoutInflater, parent, false)
            }
            override fun onBindViewHolder(binding: ViewBinding, item: CarouselItem, position: Int) {
                val currentBinding = binding as FilmRecyclerItemBinding


                currentBinding.imageView.apply {
                    //scaleType= ImageView.ScaleType.CENTER
                    setImage(item)
                }
                currentBinding.imageView.setOnClickListener {
                    showToast(requireContext(), position.toString())
                }
            }
        }

    }


    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        requireActivity().menuInflater.inflate(R.menu.option_menu, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
         when(item.itemId){
            R.id.notificationsMenuItem->{
                showToast(requireContext(),"notifications")
                return true
            }
            R.id.cinemaMenuItem->{
                showToast(requireContext(),"cinema")
                return true
            }
            R.id.savedMenuItem->{
                showToast(requireContext(),"saved")
                return true
            }
            R.id.supportMenuItem->{
                showToast(requireContext(),"support")
                return true
            }
             R.id.logoutMenuItem->{
                 mAuth.signOut()
                 replaceActivity(LoginActivity())
                 return true
             }
            else -> return false
        }
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


    override fun onStart() {
        super.onStart()
        init()

    }

}
