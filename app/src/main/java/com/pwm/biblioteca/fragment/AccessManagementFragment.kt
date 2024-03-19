package com.pwm.biblioteca.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.pwm.biblioteca.utils.User
import com.pwm.biblioteca.databinding.FragmentGestioneAccessoBinding
import com.pwm.biblioteca.server.ClientNetwork

class AccessManagementFragment: Fragment() {
    private lateinit var binding: FragmentGestioneAccessoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGestioneAccessoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        val tag1 = "fragmentMenu"
        val tag2 = "fragmentProfile"
        closeFragment(tag1)
        closeFragment(tag2)

        binding.etVecchiaPassword.visibility = View.GONE
        binding.etNuovaPassword.visibility = View.GONE
        binding.etRipetiNuovaPassword.visibility = View.GONE
        binding.btnConfermaCredenziali.visibility = View.GONE
        binding.etUsername.setText(User.username)
        setEditable(binding.etUsername, false)

        binding.tvCambiaUsername.setOnClickListener {
            binding.etVecchiaPassword.text.clear()
            binding.etNuovaPassword.text.clear()
            binding.etRipetiNuovaPassword.text.clear()
            if(binding.etNuovaPassword.isVisible){
                binding.etNuovaPassword.visibility = View.GONE
                binding.etRipetiNuovaPassword.visibility = View.GONE
            }else if (binding.etVecchiaPassword.isVisible) {
                setEditable(binding.etUsername, false)
                binding.etVecchiaPassword.visibility = View.GONE
                binding.btnConfermaCredenziali.visibility = View.GONE
            } else{
                setEditable(binding.etUsername, true)
                binding.etVecchiaPassword.visibility = View.VISIBLE
                binding.btnConfermaCredenziali.visibility = View.VISIBLE
            }
        }

        binding.tvCambiaPassord.setOnClickListener{
            binding.etVecchiaPassword.text.clear()
            binding.etNuovaPassword.text.clear()
            binding.etRipetiNuovaPassword.text.clear()
            if(binding.etNuovaPassword.isVisible){
                binding.etVecchiaPassword.visibility = View.GONE
                binding.etNuovaPassword.visibility = View.GONE
                binding.etRipetiNuovaPassword.visibility = View.GONE
                binding.btnConfermaCredenziali.visibility = View.GONE
                binding.etUsername.setText(User.username)
                setEditable(binding.etUsername, false)
            }else{
                binding.etVecchiaPassword.visibility = View.VISIBLE
                binding.etNuovaPassword.visibility = View.VISIBLE
                binding.etRipetiNuovaPassword.visibility = View.VISIBLE
                binding.btnConfermaCredenziali.visibility = View.VISIBLE
            }


        }

        binding.btnConfermaCredenziali.setOnClickListener {
            var username = binding.etUsername.text.toString()
            val password = binding.etVecchiaPassword.text.toString()
            val newPassword = binding.etNuovaPassword.text.toString()
            val repeateNewPassword = binding.etRipetiNuovaPassword.text.toString()
            if (binding.etNuovaPassword.isVisible) {
                if (username == "") {
                    username = User.username
                }

                if (password == "" || newPassword == "" || repeateNewPassword == "") {
                    showAlert("ATTENZIONE!", "E' necessario compilare tutti i campi password.")
                } else if (password != User.password) {
                    showAlert("ERRORE", "Hai inserito una password errata.")
                } else if (newPassword != repeateNewPassword) {
                    showAlert("ERRORE", "Hai inserito due password differenti.")
                } else if (newPassword == password) {
                    showAlert("ERRORE", "La password inserita è uguale alla precedente.")
                } else if(!checkPassword(newPassword)){
                    showAlert("ATTENZIONE!", "La password deve contenere almeno otto caratteri, lettere maiuscole, miuscole, numeri e caratteri speciali.")
                }else {
                    ClientNetwork.updateCredentials(User.id, username, newPassword) { updated ->
                        if (updated == true) {
                            showAlert("AGGIORNAMENTO", "Le tue credenziali sono state modificate con successo.")
                        } else {
                            showAlert("ERRORE", "Si è verificato un problema durante la connessione con il server.")
                        }

                    }
                    User.username = username
                    User.password = newPassword
                }
            } else {
                if (username == "") {
                    showAlert("ATTENZIONE!", "E' necessario compilare tutti i campi.")
                } else if (password != User.password) {
                    showAlert("ERRORE", "Hai inserito una password errata.")
                } else if (username == User.username) {
                    showAlert("ERRORE", "Hai inserito lo stesso username che hai attualmente.")
                } else {
                    ClientNetwork.updateCredentials(User.id, username, User.password) { updated ->
                        if (updated == true) {
                            showAlert("AGGIORNAMENTO", "Il tuo username è stato modificato con successo.")

                            User.username = username
                            User.password = password
                        } else {
                            showAlert("ERRORE", "Si è verificato un problema durante la connessione con il server.")
                        }
                    }
                }
            }
        }
    }

    private fun setEditable(id: EditText, editable:Boolean){
        id.isClickable = editable
        id.isFocusable = editable
        id.isFocusableInTouchMode = editable
    }

    private fun checkPassword(password: String): Boolean {
        val regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#\$%^&*_\\-+=~.,:;?])[A-Za-z\\d!@#\$%^&*_\\-+=~.,:;?]{8,}$".toRegex()
        return regex.matches(password)
    }

    private fun closeFragment(tag: String){
        val existingFragment = parentFragmentManager.findFragmentByTag(tag)
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
            }.show()
    }

}