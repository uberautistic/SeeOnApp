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
import com.example.seeon.models.User
import com.example.seeon.replaceActivity
import com.example.seeon.showToast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso

class AccountFragment : Fragment() {
    private lateinit var binding: FragmentAccountBinding
    private lateinit var mAuth: FirebaseAuth
    private lateinit var mUserTable: DatabaseReference
    private lateinit var mUser: User
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAccountBinding.inflate(inflater, container, false)
        init()
        fillFields()
        return binding.root
    }


    private fun fillFields() {
        val userPhone=mAuth.currentUser?.phoneNumber
        var userName=""
        var userPhotoURL=""
        mUserTable.child(userPhone!!).get().addOnSuccessListener {
            if (it.exists()){
                userName=it.child("uname").value.toString()
                userPhotoURL=it.child("uphoto").value.toString()
                binding.userNameTV.setText(userName)
                binding.userPhoneNumberTV.setText(userPhone)
                Picasso.get()
                    .load(userPhotoURL)
                    .placeholder(R.color.grey)
                    .into(binding.userPhotoIV)
            }
        }
    }

    private fun init() {
        mUserTable= FirebaseDatabase.getInstance().getReference("users")
        mAuth= FirebaseAuth.getInstance()
        registerForContextMenu(binding.menuButton)
        binding.menuButton.setOnClickListener {
            requireActivity().openContextMenu(binding.menuButton)
        }
        val userPhone=mAuth.currentUser?.phoneNumber
        var uid: String
        var uname: String
        var ubirtdate: String
        var uphoto: String
        mUserTable.child(userPhone!!).get().addOnSuccessListener {
            if (it.exists()){
                uid= it.child("uid").value.toString()
                uname=it.child("uname").value.toString()
                uphoto=it.child("uphoto").value.toString()
            }
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