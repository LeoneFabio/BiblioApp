package com.pwm.biblioteca.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.pwm.biblioteca.R
import com.pwm.biblioteca.utils.User
import com.pwm.biblioteca.databinding.FragmentDonaLibroBinding
import com.pwm.biblioteca.server.ClientNetwork

class DonateBookFragment: Fragment() {

    private lateinit var binding: FragmentDonaLibroBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDonaLibroBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.donaLibroBtn.setOnClickListener {
            val titolo = binding.donaLibroTitoloEt.text.toString().trim()
            val autore = binding.donaLibroAutoreEt.text.toString().trim()
            val descrizione = binding.etDescrizione.text.toString().trim()
            val stato: String
            when (binding.donaLibroRg.checkedRadioButtonId) {
                R.id.donaLibroOttimoRBtn -> {
                    // Il RadioButton "OTTIMO" è stato selezionato
                    stato = "Ottimo"
                }
                R.id.donaLibroBuonoRBtn -> {
                    // Il RadioButton "BUONO" è stato selezionato
                    stato = "Buono"
                }
                R.id.donaLibroDiscretoRBtn -> {
                    // Il RadioButton "DISCRETO" è stato selezionato
                    stato = "Discreto"
                }
                else -> {
                    stato = ""
                }
            }
            if (titolo.isNotEmpty() && autore.isNotEmpty() && descrizione.isNotEmpty() && stato.isNotEmpty()) {
                // Effettua la donazione
                ClientNetwork.insertDonateBook(User.id, titolo, autore, stato, descrizione){ check ->
                    if(check == true){
                        AlertDialog.Builder(requireContext())
                            .setTitle("Richiesta donazione")
                            .setMessage("La richiesta di donazione libro è avvenuta con successo.")
                            .setPositiveButton("OK") { dialog, _ ->
                                dialog.dismiss()
                                binding.donaLibroRg.clearCheck()
                                binding.donaLibroTitoloEt.text.clear()
                                binding.donaLibroAutoreEt.text.clear()
                                binding.etDescrizione.text.clear()
                            }
                            .show()
                    }else{
                        showAlert("ERRORE", "Si è verificato un problema durante la connessione con il server.")
                    }
                }
            } else {
                showAlert("ATTENZIONE!", "Compila tutti i campi per procedere con la donazione del libro.")
            }
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