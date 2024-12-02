package com.example.projekmap

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.fragment.findNavController


class FragmentPilihan : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_pilihan, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val vendorButton = view.findViewById<Button>(R.id.vendor_button)
        val userButton = view.findViewById<Button>(R.id.user_button)
        (activity as MainActivity).hideBottomNavbar()

        vendorButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("role", "vendor")
            findNavController().navigate(R.id.loginFragment, bundle)
        }

        userButton.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("role", "user")
            findNavController().navigate(R.id.loginFragment, bundle)
        }


    }



    companion object {

    }
}