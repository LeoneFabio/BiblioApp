package com.pwm.biblioteca.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.pwm.biblioteca.activity.MainActivity
import com.pwm.biblioteca.R
import com.pwm.biblioteca.utils.User
import com.pwm.biblioteca.databinding.FragmentMenuBinding

class MenuFragment : Fragment() {

    private lateinit var binding: FragmentMenuBinding
    private lateinit var mainActivity: MainActivity


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity = activity as MainActivity



        binding.donaLibroTv.setOnClickListener {
            if(User.id!= 0) {
                mainActivity.showBottomNavigation().show(-1, false)
                mainActivity.showBottomNavigation().clearAllCounts()

                //Apri DonaLibro
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, DonateBookFragment())
                    .commit()
                //Chiudi il menu
                closeMenu()
            }else{
                showAlert("Accesso Negato", "Per accedere alla funzionalità richiesta effettua il Login.")
            }
        }

        binding.prenotaPostoTv.setOnClickListener {
            mainActivity.showBottomNavigation().show(-1, false)
            mainActivity.showBottomNavigation().clearAllCounts()

            //Apri PrenotaPosto
            if(User.id != 0){
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, BookSeatFragment())
                .commit()
            //Chiudi il menu
            closeMenu()
            }else{
                showAlert("Accesso Negato", "Per accedere alla funzionalità richiesta effettua il Login.")
            }
        }

        binding.feedbackTv.setOnClickListener {
            if(User.id != 0) {
                mainActivity.showBottomNavigation().show(-1, false)
                mainActivity.showBottomNavigation().clearAllCounts()

                //Apri LasciaUnFeedback
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, FeedbackFragment())
                    .commit()
                //Chiudi il menu
                closeMenu()
            }else{
                showAlert("Accesso Negato", "Per accedere alla funzionalità richiesta effettua il Login.")
            }
        }

        binding.prestitiTv.setOnClickListener{
            if(User.id != 0) {
                mainActivity.showBottomNavigation().show(-1, false)
                mainActivity.showBottomNavigation().clearAllCounts()

                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, LoansFragment())
                    .commit()

                closeMenu()
            }else{
                showAlert("Accesso Negato", "Per accedere alla funzionalità richiesta effettua il Login.")
            }
        }
        binding.impostazioniTv.setOnClickListener {
            //Apri Impostazioni
            mainActivity.showBottomNavigation().show(-1, false)
            mainActivity.showBottomNavigation().clearAllCounts()

            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, SettingsFragment())
                .commit()
            //Chiudi il menu
            closeMenu()
        }

    }

    private fun closeMenu(){
        val existingFragment = parentFragmentManager.findFragmentByTag("fragmentMenu")
        if (existingFragment != null) {
            parentFragmentManager.beginTransaction()
                .remove(existingFragment)
                .commit()
        }
    }
    private fun showAlert(title: String, message: String){
        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK"){dialog,_ ->
                dialog.dismiss()
                closeMenu()
            }.show()
    }

}