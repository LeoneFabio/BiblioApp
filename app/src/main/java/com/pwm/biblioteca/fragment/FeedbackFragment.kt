package com.pwm.biblioteca.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.pwm.biblioteca.utils.User
import com.pwm.biblioteca.databinding.FragmentFeedbackBinding
import com.pwm.biblioteca.server.ClientNetwork

class FeedbackFragment: Fragment() {

    private lateinit var binding: FragmentFeedbackBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFeedbackBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnInvia.setOnClickListener {
            val feedback = binding.etFeedback.text.toString().trim()
            if (feedback.isNotEmpty()) {
                //Invia feedback
                ClientNetwork.insertFeedback(User.id, feedback){ check ->
                    if(check == true){
                        AlertDialog.Builder(requireContext())
                            .setTitle("INVIO FEEDBACK")
                            .setMessage("L'invio del feedback è avvenuto con successo.")
                            .setPositiveButton("OK") { dialog, _ ->
                                dialog.dismiss()
                                binding.etFeedback.text.clear()
                            }
                            .show()
                    }else{
                        showAlert("ERRORE", "Si è verificato un problema durante la connessione con il server.")
                    }
                }
            } else {
                showAlert("ATTENZIONE!", "Riempi il campo testo per poter inviare correttamente il feedback.")
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