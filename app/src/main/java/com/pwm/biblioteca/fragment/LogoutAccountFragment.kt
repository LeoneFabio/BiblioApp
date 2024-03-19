package com.pwm.biblioteca.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.pwm.biblioteca.activity.MainActivity
import com.pwm.biblioteca.R
import com.pwm.biblioteca.utils.User
import com.pwm.biblioteca.utils.UserSessionManager
import com.pwm.biblioteca.databinding.FragmentLogoutAccountBinding

class LogoutAccountFragment: Fragment() {

    private lateinit var binding: FragmentLogoutAccountBinding
    private lateinit var userSessionManager: UserSessionManager
    private lateinit var mainActivity: MainActivity


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLogoutAccountBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userSessionManager = UserSessionManager(requireContext())
        mainActivity = activity as MainActivity

        setImagineProfile(userSessionManager)

        //Logout
        binding.btnLogout.setOnClickListener {
            //Esegui Logout
            User.resetUser()
            showAlert("LOGOUT", "Logout effettuato con successo.")
            mainActivity.showBottomNavigation().show(2, true)
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment())
                .commit()
            userSessionManager.clearUserCredentials()
            closeProfile()
        }

        //Account
        binding.btnAccount.setOnClickListener {
            mainActivity.showBottomNavigation().show(-1, false)
            mainActivity.showBottomNavigation().clearAllCounts()
            //Apri ModificaAccount
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, AccountFragment())
                .commit()
            //Chiudi il fragment profilo
            closeProfile()
        }
    }

    private fun closeProfile(){
        val tag = "fragmentProfile"
        val existingFragment = parentFragmentManager.findFragmentByTag(tag)
        if (existingFragment != null){
            parentFragmentManager.beginTransaction()
                .remove(existingFragment)
                .commit()
        }
    }

    private fun setImagineProfile(userSessionManager: UserSessionManager){
        when (userSessionManager.isDarkModeEnabled()) {
            false -> {
                if(User.gender == "M"){
                    binding.accountIv.setImageResource(R.drawable.ic_account_uomo_chiaro)
                }else{
                    binding.accountIv.setImageResource(R.drawable.ic_account_donna_chiaro)
                }
            }
            true -> {
                if(User.gender == "M"){
                    binding.accountIv.setImageResource(R.drawable.ic_account_uomo_scuro)
                }else{
                    binding.accountIv.setImageResource(R.drawable.ic_account_donna_scuro)
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