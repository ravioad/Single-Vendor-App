package com.example.singlevendorapp.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.singlevendorapp.R

class PendingOrdersFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        //TODO: get Data from firebase to show pending orders here

        return inflater.inflate(R.layout.fragment_pending_orders, container, false)
    }

}