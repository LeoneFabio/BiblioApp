package com.pwm.biblioteca.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.pwm.biblioteca.R
import com.pwm.biblioteca.databinding.FragmentInfoApplicazioneBinding

class ApplicationInfoFragment: Fragment() {

    private lateinit var binding: FragmentInfoApplicazioneBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentInfoApplicazioneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val tag1 = "fragmentMenu"
        val tag2 = "fragmentProfile"
        closeFragment(tag1)
        closeFragment(tag2)

        binding.tvInfo1.text = getString(R.string.infoApp)
    }

    private fun closeFragment(tag: String){
        val existingFragment = parentFragmentManager.findFragmentByTag(tag)
        if (existingFragment != null) {
            parentFragmentManager.beginTransaction()
                .remove(existingFragment)
                .commit()
        }
    }
}