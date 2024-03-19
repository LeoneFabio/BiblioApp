package com.pwm.biblioteca.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.pwm.biblioteca.R
import com.pwm.biblioteca.utils.User
import com.pwm.biblioteca.utils.UserSessionManager
import com.pwm.biblioteca.databinding.FragmentModificaAccountBinding
import com.pwm.biblioteca.server.ClientNetwork

class AccountFragment: Fragment() {

    private lateinit var binding: FragmentModificaAccountBinding
    private lateinit var userSessionManager: UserSessionManager
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentModificaAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userSessionManager = UserSessionManager(requireContext())
        setDataUser(userSessionManager)
        setAllEditable(false)

        binding.swModifica.setOnCheckedChangeListener{ _, isChecked ->
            if(isChecked){
                setAllEditable(true)
            }else{
                setAllEditable(false)
            }
        }

        binding.btnConferma.setOnClickListener{

            if(
                binding.nomeAccountEt.text.toString() == "" ||
                binding.cognomeAccountEt.text.toString() == "" ||
                binding.sessoAccountEt.text.toString() == "" ||
                binding.indirizzoAccountEt.text.toString() == "" ||
                binding.etaAccountEt.text.toString() == "" ||
                binding.emailAccountEt.text.toString() == "" ||
                binding.cellulareAccountEt.text.toString() == ""
            ){
                showAlert("ATTENZIONE!", "E' necessario compilare tutti i campi.")
            }else if(
                binding.sessoAccountEt.text.toString() != "M" &&
                binding.sessoAccountEt.text.toString() != "F"
            ){
                showAlert("ATTENZIONE!", "Nel campo sesso inserisci 'M' o 'F'.")
            }else if(
                binding.nomeAccountEt.text.toString() == User.name &&
                binding.cognomeAccountEt.text.toString() == User.surname &&
                binding.sessoAccountEt.text.toString() == User.gender &&
                binding.indirizzoAccountEt.text.toString() == User.address &&
                binding.etaAccountEt.text.toString().toInt() == User.age &&
                binding.emailAccountEt.text.toString() == User.email &&
                binding.cellulareAccountEt.text.toString() == User.phone
            ){
                showAlert("ERRORE", "Non hai modificato nessun dato.")
            }else if(binding.etaAccountEt.text.toString().toInt() >150 || binding.etaAccountEt.text.toString().toInt() < 10){
                showAlert("ATTENZIONE!", "Inserisci un'età valida: l'età minima è di 10 anni.")
            }else if(!checkEmailValidity(binding.emailAccountEt.text.toString())){
                showAlert("ATTENZIONE!", "Inserire una email valida. (example@example.com)")
            }else{
                if(binding.swModifica.isChecked) {binding.swModifica.isChecked = !binding.swModifica.isChecked}
                ClientNetwork.updateUser(
                    User.id,
                    binding.nomeAccountEt.text.toString(),
                    binding.cognomeAccountEt.text.toString(),
                    binding.sessoAccountEt.text.toString(),
                    binding.indirizzoAccountEt.text.toString(),
                    binding.etaAccountEt.text.toString().toInt(),
                    binding.emailAccountEt.text.toString(),
                    binding.cellulareAccountEt.text.toString(),
                ) { updated ->
                    if (updated == true) {
                        showAlert("AGGIORNAMENTO", "I tuoi dati sono stati modificati con successo.")
                    } else {
                        showAlert("ERRORE", "Si è verificato un problema durante la connessione con il server.")
                    }

                    ClientNetwork.getUser(User.username, User.password) {
                        setDataUser(userSessionManager)
                        setAllEditable(false)
                    }
                }
            }
        }
    }
    private fun setDataUser(userSessionManager: UserSessionManager){

            when (userSessionManager.isDarkModeEnabled()) {
                false -> {
                    if(User.gender == "M"){
                        binding.modificaAccountIv.setImageResource(R.drawable.ic_account_uomo_chiaro)
                    }else{
                        binding.modificaAccountIv.setImageResource(R.drawable.ic_account_donna_chiaro)
                    }
                }
                true -> {
                    if(User.gender == "M"){
                        binding.modificaAccountIv.setImageResource(R.drawable.ic_account_uomo_scuro)
                    }else{
                        binding.modificaAccountIv.setImageResource(R.drawable.ic_account_donna_scuro)
                    }
                }
            }


        binding.nomeAccountEt.setText(User.name)
        binding.cognomeAccountEt.setText(User.surname)
        binding.sessoAccountEt.setText(User.gender)
        binding.indirizzoAccountEt.setText(User.address)
        binding.etaAccountEt.setText(User.age.toString())
        binding.cellulareAccountEt.setText(User.phone)
        binding.emailAccountEt.setText(User.email)
    }
    private fun setAllEditable(editable: Boolean){
        setEditable(binding.nomeAccountEt, editable)
        setEditable(binding.cognomeAccountEt, editable)
        setEditable(binding.sessoAccountEt, editable)
        setEditable(binding.indirizzoAccountEt, editable)
        setEditable(binding.etaAccountEt, editable)
        setEditable(binding.cellulareAccountEt, editable)
        setEditable(binding.emailAccountEt, editable)
    }
    private fun setEditable(id:EditText, editable:Boolean){
        id.isClickable = editable
        id.isFocusable = editable
        id.isFocusableInTouchMode = editable
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