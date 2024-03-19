package com.pwm.biblioteca.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.pwm.biblioteca.R
import com.pwm.biblioteca.utils.User
import com.pwm.biblioteca.utils.UserSessionManager
import com.pwm.biblioteca.databinding.FragmentRegistrazioneBinding
import com.pwm.biblioteca.server.ClientNetwork

class SignUpFragment: Fragment() {

    private lateinit var binding: FragmentRegistrazioneBinding
    private lateinit var userSessionManager: UserSessionManager
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRegistrazioneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userSessionManager = UserSessionManager(requireContext())
        var sesso = ""

        binding.sessoRg.setOnCheckedChangeListener { radioGroup, checkedId ->

            when (checkedId) {
                R.id.maschioRb -> {
                    sesso = "M"
                    setImagineProfile(userSessionManager, sesso)
                }
                R.id.femminaRb -> {
                    sesso = "F"
                    setImagineProfile(userSessionManager, sesso)
                }
                else -> {
                    sesso = ""
                }
            }
        }
        binding.RegistratiBtn.setOnClickListener {

            val nome = binding.nomeEt.text.toString()
            val cognome = binding.cognomeEt.text.toString()
            val indirizzo = binding.indirizzoEt.text.toString()
            val eta = binding.etaEt.text.toString()
            val email = binding.emailEt.text.toString()
            val cellulare = binding.cellulareEt.text.toString()
            val username = binding.usernameEt.text.toString()
            val password = binding.passwordEt.text.toString()
            val ripetiPassword = binding.ripetiPasswordEt.text.toString()



            if ( // controllo che tutti i campi siano riempiti
                nome == "" ||
                cognome == "" ||
                sesso == "" ||
                indirizzo == "" ||
                eta == "" ||
                email == "" ||
                cellulare == "" ||
                username == "" ||
                password == ""
            ) {
                showAlert("ATTENZIONE!", "E' necessario compilare tutti i campi.")
            } else if ( //controllo che le 2 password siano uguali
                password != ripetiPassword
            ) {
                showAlert("ATTENZIONE!", "Inserisci due password uguali.")
            } else if(!checkPassword(password)){
                showAlert("ATTENZIONE!", "La password deve contenere almeno otto caratteri, lettere maiuscole, miuscole, numeri e caratteri speciali.")
            }else if(eta.toInt() >150 || eta.toInt() < 10){
                showAlert("ATTENZIONE!", "Inserisci un'età valida: l'età minima per potersi registrare è di 10 anni.")
            }else if(!checkEmailValidity(email)){
                showAlert("ATTENZIONE!", "Inserire una email valida. (example@example.com)")
            }else{

                ClientNetwork.getUser(username, password) { found ->

                    User.resetUser()

                    if (found == true) { // verifico se user e password appartengono già a qualcuno
                        showAlert("Errore Registrazione", "Username o password già esistenti.")
                    } else if(found == false){
                        // inserisco l'utente
                        ClientNetwork.insertUser(
                            nome,
                            cognome,
                            sesso,
                            indirizzo,
                            eta.toInt(),
                            email,
                            cellulare,
                            username,
                            password
                        ) { inserted ->
                            if (inserted == true) {
                                ClientNetwork.getUser(username, password) {
                                    if (sesso == "M") { //se è uomo restituisce questo numero non so perchè
                                        showAlert("Registrazione effettuata con successo", "Benvenuto ${User.name}")
                                    } else {
                                        showAlert("Registrazione effettuata con successo", "Benvenuta ${User.name}")
                                    }
                                    ClientNetwork.getUser(username, password) {}
                                    closeSignUp()
                                }
                            } else {
                                showAlert("ERRORE", "Si è verificato un problema durante la connessione con il server.")
                            }
                        }
                    }else{
                        showAlert("ERRORE", "Si è verificato un problema durante la connessione con il server.")
                    }
                }
            }
        }
    }


    private fun closeSignUp() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.fragment_container,HomeFragment())
            .commit()
    }

    private fun setImagineProfile(userSessionManager: UserSessionManager, gender: String){
        when (userSessionManager.isDarkModeEnabled()) {
            false -> {
                if(gender == "M"){
                    binding.registrazioneIv.setImageResource(R.drawable.ic_account_uomo_chiaro)
                }else{
                    binding.registrazioneIv.setImageResource(R.drawable.ic_account_donna_chiaro)
                }
            }
            true -> {
                if(gender == "M"){
                    binding.registrazioneIv.setImageResource(R.drawable.ic_account_uomo_scuro)
                }else{
                    binding.registrazioneIv.setImageResource(R.drawable.ic_account_donna_scuro)
                }
            }
        }
    }

    private fun checkPassword(password: String): Boolean {
        val regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[!@#\$%^&*_\\-+=~.,:;?])[A-Za-z\\d!@#\$%^&*_\\-+=~.,:;?]{8,}$".toRegex()
        return regex.matches(password)
    }


    private fun checkEmailValidity(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
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