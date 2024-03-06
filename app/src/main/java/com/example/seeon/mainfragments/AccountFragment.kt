package com.example.seeon.mainfragments

import android.os.Bundle
import android.view.ContextMenu
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.seeon.LoginActivity
import com.example.seeon.R
import com.example.seeon.databinding.FragmentAccountBinding
import com.example.seeon.replaceActivity
import com.example.seeon.showToast
import com.google.firebase.auth.FirebaseAuth

class AccountFragment : Fragment() {
    private lateinit var binding: FragmentAccountBinding
    private lateinit var mAuth: FirebaseAuth
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onStart() {
        super.onStart()
        init()
    }

    private fun init() {
        mAuth= FirebaseAuth.getInstance()
        registerForContextMenu(binding.menuButton)
        binding.menuButton.setOnClickListener {
            requireActivity().openContextMenu(binding.menuButton)
        }
    }

    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        requireActivity().menuInflater.inflate(R.menu.menu_2, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.accountMenuItem->{
                showToast(requireContext(),"account management")
                return true
            }
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

}