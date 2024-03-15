package com.example.seeon.mainfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.seeon.R
import com.google.firebase.database.DatabaseReference


class TicketsFragment : Fragment() {

    private lateinit var mDataBase: DatabaseReference
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tickets, container, false)
    }

}