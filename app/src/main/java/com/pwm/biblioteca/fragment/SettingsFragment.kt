package com.pwm.biblioteca.fragment

import android.os.Bundle
import android.text.InputType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.pwm.biblioteca.activity.MainActivity
import com.pwm.biblioteca.R
import com.pwm.biblioteca.utils.User
import com.pwm.biblioteca.utils.UserSessionManager
import com.pwm.biblioteca.databinding.FragmentImpostazioniBinding
import com.pwm.biblioteca.server.ClientNetwork

class SettingsFragment: Fragment() {

    private lateinit var binding: FragmentImpostazioniBinding
    private lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentImpostazioniBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if(User.id == 0){
            binding.tvEliminaAccount.visibility = View.GONE
        }
        mainActivity = activity as MainActivity
        mainActivity.showBottomNavigation().show(-1, false)
        mainActivity.showBottomNavigation().clearAllCounts()

        binding.btnGestioneAccesso.setOnClickListener{
            if(User.id == 0){
                showAlert("Accesso negato", "Per accedere alla funzionalità richiesta effettua il Login.")
            }else {
                parentFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container, AccessManagementFragment())
                    .addToBackStack("AccessManagementFragment()")
                    .commit()
            }
        }

        val userSessionManager = UserSessionManager(requireContext())
        // Imposta lo stato iniziale dello switch
        binding.swtTemaScuro.isChecked = userSessionManager.isDarkModeEnabled()

        binding.swtTemaScuro.setOnCheckedChangeListener { _, isChecked ->
            // Salva il tema selezionato
            userSessionManager.saveTheme(isChecked)
            if (isChecked){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            }else{
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }

        }

        binding.tvEliminaAccount.setOnClickListener{
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Inserisci Password:")

            val input = EditText(requireContext())
            input.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
            builder.setView(input)

            builder.setPositiveButton("CONFERMA"){  dialog, _ ->
                val password = input.text.toString()

                if(password == User.password){

                    ClientNetwork.removeUser(User.id){check ->

                        if (check){
                            showAlert("Rimozione utente", "Il tuo account è stato eliminato correttamente.")

                            parentFragmentManager.beginTransaction()
                                .replace(R.id.fragment_container, HomeFragment())
                                .commit()

                            User.resetUser()
                            userSessionManager.clearUserCredentials()

                        }else{
                            showAlert("ERRORE", "Si è verificato un problema durante la connessione con il server.")
                        }
                    }

                }else{
                    showAlert("ERRORE", "Password Errata.")
                }
                dialog.dismiss()
            }

            builder.setNegativeButton("ANNULLA"){dialog, _->
                dialog.dismiss()

            }
            builder.create().show()




        }

        binding.btnInfoApp.setOnClickListener{
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ApplicationInfoFragment())
                .addToBackStack("ApplicationInfoFragment()")
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