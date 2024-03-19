package com.pwm.biblioteca.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.pwm.biblioteca.activity.MainActivity
import com.pwm.biblioteca.adapter.FavouritesBookAdapter
import com.pwm.biblioteca.utils.User
import com.pwm.biblioteca.databinding.FragmentPreferitiBinding
import com.pwm.biblioteca.server.ClientNetwork

class FavouritesFragment : Fragment(){

    private lateinit var binding: FragmentPreferitiBinding
    private lateinit var favouritesBookAdapter: FavouritesBookAdapter
    private lateinit var mainActivity: MainActivity



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPreferitiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)
        mainActivity = activity as MainActivity
        mainActivity.showBottomNavigation().show(3, true)


        //inizializzo la recycler view
        ClientNetwork.getFavouriteBooks(User.id){ favList ->
            if (favList.isEmpty()){
                Toast.makeText(requireContext(), "Non sono presenti preferiti", Toast.LENGTH_SHORT).show()
            }else{
                binding.rvPreferiti.apply {
                    layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                    favouritesBookAdapter = FavouritesBookAdapter(favList, parentFragmentManager, mainActivity.showBottomNavigation())
                    adapter = favouritesBookAdapter
                }
            }
        }
    }
}