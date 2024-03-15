package com.example.seeon.mainfragments

import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.example.seeon.FilmActivity
import com.example.seeon.LoginActivity
import com.example.seeon.R
import com.example.seeon.adapter.GenresAdapter
import com.example.seeon.databinding.FilmRecyclerItemBinding
import com.example.seeon.databinding.FragmentHomeBinding
import com.example.seeon.replaceActivity
import com.example.seeon.showToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.imaginativeworld.whynotimagecarousel.listener.CarouselListener
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem
import org.imaginativeworld.whynotimagecarousel.utils.setImage


class HomeFragment : Fragment() {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var binding: FragmentHomeBinding
    private lateinit var genresList : ArrayList<Int>
    private lateinit var filmsList : ArrayList<Int>
    private lateinit var mUserTable: DatabaseReference
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
        val userPhone= mAuth.currentUser?.phoneNumber
        mUserTable= FirebaseDatabase.getInstance().getReference("users")
        mUserTable.child(userPhone!!).get().addOnSuccessListener {
            if (it.exists()){
                binding.helloTV.text=ContextCompat.getString(requireActivity(),R.string.helloTV)+" "+it.child("uname").value.toString()
            }
        }
        binding.genresRecycler.layoutManager= LinearLayoutManager(requireActivity(), RecyclerView.HORIZONTAL, false)
        initGenresList()
        binding.genresRecycler.adapter= GenresAdapter(requireActivity(), genresList)
        val list= mutableListOf<CarouselItem>()
        list.add(CarouselItem(imageDrawable = R.drawable.barbie, caption = "https://nvaunity.github.io/SeeOn/barbie"))
        list.add(CarouselItem(imageDrawable = R.drawable.gotg3, caption = "https://nvaunity.github.io/SeeOn/gotg3"))
        list.add(CarouselItem(imageDrawable = R.drawable.smatsv, caption ="https://nvaunity.github.io/SeeOn/tmatsv"))
        list.add(CarouselItem(imageDrawable = R.drawable.oppenheimer, caption = "https://nvaunity.github.io/SeeOn/oppenheimer"))
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
                    setImage(item)
                }
                currentBinding.imageView.setOnClickListener {
                    val url=item.caption
                    val filmPage= Intent(requireActivity(),FilmActivity::class.java)
                    filmPage.putExtra("URL", url)
                    startActivity(filmPage)
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
                showToast(requireActivity(),"notifications")
                return true
            }
            R.id.cinemaMenuItem->{
                showToast(requireActivity(),"cinema")
                return true
            }
            R.id.savedMenuItem->{
                showToast(requireActivity(),"saved")
                return true
            }
            R.id.supportMenuItem->{
                showToast(requireActivity(),"support")
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


    override fun onStart() {
        super.onStart()
        CoroutineScope(Dispatchers.Main).launch{
            init()
        }
    }

}
