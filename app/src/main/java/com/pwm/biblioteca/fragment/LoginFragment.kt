package com.pwm.biblioteca.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.pwm.biblioteca.activity.MainActivity
import com.pwm.biblioteca.R
import com.pwm.biblioteca.utils.User
import com.pwm.biblioteca.utils.UserSessionManager
import com.pwm.biblioteca.databinding.FragmentLoginBinding
import com.pwm.biblioteca.server.ClientNetwork
import java.time.LocalDate

class LoginFragment: Fragment(){

    private lateinit var binding: FragmentLoginBinding
    private lateinit var userSessionManager: UserSessionManager
    private var operationToDo = 2 //utile in modo tale da chiudere il fragment quando finiscono le operazioni
    private lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userSessionManager = UserSessionManager(requireContext())
        mainActivity = activity as MainActivity


        binding.loginBtn.setOnClickListener {
            val user = binding.userLoginEt.text.toString()
            val pass = binding.passwordLoginEt.text.toString()
            if(TextUtils.isEmpty(user) || TextUtils.isEmpty(pass)){
                showAlert("ATTENZIONE!", "E' necessario riempire tutti i campi.")
            }else{
                ClientNetwork.getUser(user, pass) { found ->
                        if (found == true) {
                            if (User.gender == "M") {
                                showAlert("Login effettuato con successo", "Benvenuto ${User.name}!")
                            } else {
                                showAlert("Login effettuato con successo", "Benvenuta ${User.name}!")
                            }
                            mainActivity.showBottomNavigation().show(2, true)
                            parentFragmentManager.beginTransaction()
                                .replace(R.id.fragment_container, HomeFragment())
                                .commit()
                            userSessionManager.saveUserCredentials(user, pass)
                            bookReturnNotification() // controlla se ci sono notifiche per libri da restituire
                            bookBackAvailable() // controlla se ci sono libri tornati disponibili
                        } else {
                            showAlert("ERRORE", "Le credenziali immesse non sono corrette.")
                        }


                }
            }

        }
    }

    private fun closeProfile(){
        if(isAdded){
            val tag = "fragmentProfile"
            val existingFragment = parentFragmentManager.findFragmentByTag(tag)
            if (existingFragment != null){
                parentFragmentManager.beginTransaction()
                    .remove(existingFragment)
                    .commit()
            }
        }
    }

    private fun bookReturnNotification() {
        ClientNetwork.getLoanBooks(User.id, inCorso = true) { bookList, _ ->
            operationToDo--
            if(bookList.isNotEmpty()) {
                ClientNetwork.getNotifications(User.id) { notificationList ->
                    for (i in bookList.indices) {
                        if (bookList[i].giorniRestituzione in 0..5) {
                            var found = false
                            for (element in notificationList) {
                                if (element.testo == "${LocalDate.now()}: Rimangono soltanto ${bookList[i].giorniRestituzione} giorni alla scadenza della restituzione del libro \"${bookList[i].titolo}\"") {
                                    found = true
                                    break
                                }
                            }
                            if (!found) {
                                ClientNetwork.insertNotification(
                                    User.id,
                                    "${LocalDate.now()}: Rimangono soltanto ${bookList[i].giorniRestituzione} giorni alla scadenza della restituzione del libro \"${bookList[i].titolo}\""
                                ) { check ->
                                    if (!check) {
                                        showAlert("ERRORE", "Si è verificato un problema durante il collegamento con il server.")
                                    }
                                    if (operationToDo == 0) {
                                        closeProfile()
                                    }
                                }
                            } else {
                                if (operationToDo == 0) {
                                    closeProfile()
                                }
                            }
                        } else {
                            if (operationToDo == 0) {
                                closeProfile()
                            }
                        }
                    }
                }
            }else{
                if(operationToDo == 0){
                    closeProfile()
                }
            }
        }
    }

    private fun bookBackAvailable() {
        ClientNetwork.getBookBackAvailable(User.id){ bookList, check ->
            operationToDo--

                if(!check){
                    showAlert("ERRORE", "Si è verificato un problema durante il collegamento con il server.")
                }else if(bookList.isNotEmpty()){
                    ClientNetwork.removeBookBackAvailable(User.id){ check ->
                        if(check){
                            for (i in bookList.indices){
                                ClientNetwork.insertNotification(User.id, "${LocalDate.now()}: Il libro \"${bookList[i].titolo}\" è tornato disponibile!"){ check ->
                                    if(!check){
                                        showAlert("ERRORE", "Si è verificato un problema durante il collegamento con il server.")
                                    }
                                    if(i == (bookList.size -1) && operationToDo == 0){
                                        closeProfile()
                                    }
                                }
                            }
                        }else{
                            showAlert("ERRORE", "Si è verificato un problema durante il collegamento con il server.")
                        }
                    }
                }else{
                    if(operationToDo == 0){
                        closeProfile()
                    }
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