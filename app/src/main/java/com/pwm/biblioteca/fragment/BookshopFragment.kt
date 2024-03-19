package com.pwm.biblioteca.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.pwm.biblioteca.activity.MainActivity
import com.pwm.biblioteca.R
import com.pwm.biblioteca.databinding.FragmentLibreriaBinding

class BookshopFragment : Fragment() {

    private lateinit var binding: FragmentLibreriaBinding
    private lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentLibreriaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity = activity as MainActivity
        mainActivity.showBottomNavigation().show(1, true)

        PaperBooksFragment()
        parentFragmentManager.beginTransaction()
            .replace(
                R.id.fcLibreria,
                PaperBooksFragment()
            )
            .commit()
        binding.btnFisici.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(
                    R.id.fcLibreria,
                    PaperBooksFragment()
                )
                .commit()
        }

        binding.btnDigitali.setOnClickListener{
            parentFragmentManager.beginTransaction()
                .replace(R.id.fcLibreria, DigitalBooksFragment())
                .commit()
        }

        binding.btnAudiolibri.setOnClickListener{
            parentFragmentManager.beginTransaction()
                .replace(R.id.fcLibreria, AudioBooksFragment())
                .commit()
        }


    }
}


