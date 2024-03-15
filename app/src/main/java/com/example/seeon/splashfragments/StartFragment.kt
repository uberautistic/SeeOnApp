package com.example.seeon.splashfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.example.seeon.LoginActivity
import com.example.seeon.R
import com.example.seeon.databinding.FragmentStartBinding
import com.example.seeon.replaceActivity
import org.imaginativeworld.whynotimagecarousel.listener.CarouselOnScrollListener
import org.imaginativeworld.whynotimagecarousel.model.CarouselItem

class StartFragment : Fragment() {

    private lateinit var startList : ArrayList<Int>
    private lateinit var binding: FragmentStartBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding= FragmentStartBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        init()
    }

    private fun init() {
        binding.skipTV.setOnClickListener {
            replaceActivity(LoginActivity())
        }
        binding.startTV.text = ContextCompat.getString(requireActivity(),R.string.startText1)
        startList= ArrayList()
        startList.add(R.drawable.bg1)
        startList.add(R.drawable.bg2)
        startList.add(R.drawable.bg3)
        val list= mutableListOf<CarouselItem>()
        for (i in 0..<startList.size){
            list.add(
                CarouselItem(
                    imageDrawable = startList[i]
                )
            )
        }
        binding.startRecycler.setData(list)
        binding.startRecycler.autoPlay=true
        binding.startRecycler.autoPlayDelay=5000
        binding.startRecycler.onScrollListener=object :CarouselOnScrollListener{
            override fun onScrolled(
                recyclerView: RecyclerView,
                dx: Int,
                dy: Int,
                position: Int,
                carouselItem: CarouselItem?
            ) {
                when(position){
                    0->binding.startTV.text = ContextCompat.getString(requireActivity(),R.string.startText1)
                    1->binding.startTV.text = ContextCompat.getString(requireActivity(),R.string.startText2)
                    2->binding.startTV.text = ContextCompat.getString(requireActivity(),R.string.startText3)
                }
            }
        }
        binding.nextB.setOnClickListener {
            when(binding.startRecycler.currentPosition){
                0->{
                    binding.startTV.text = ContextCompat.getString(requireActivity(),R.string.startText2)
                    binding.startRecycler.next()
                }
                1->{
                    binding.startTV.text = ContextCompat.getString(requireActivity(),R.string.startText3)
                    binding.startRecycler.next()
                }
                2-> replaceActivity(LoginActivity())
            }
        }
    }

}