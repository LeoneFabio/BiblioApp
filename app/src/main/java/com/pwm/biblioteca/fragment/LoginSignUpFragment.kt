package com.pwm.biblioteca.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.pwm.biblioteca.activity.MainActivity
import com.pwm.biblioteca.R
import com.pwm.biblioteca.databinding.FragmentLoginRegistrazioneBinding

class LoginSignUpFragment: Fragment() {

    private lateinit var binding: FragmentLoginRegistrazioneBinding
    private lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLoginRegistrazioneBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mainActivity = activity as MainActivity


        //Login
        binding.loginRegistrazioneBtn.setOnClickListener {
            val tag = "fragmentProfile"
            //Apri Modulo Login
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_profilo, LoginFragment(), tag)
                .commit()
        }

        //Registrati
        binding.RegistratiCliccabileTv.setOnClickListener {
            //Apri Modulo Registrazione
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, SignUpFragment())
                .addToBackStack("SignUpFragment()")
                .commit()
            //Chiudi il fragment profilo
            closeProfile()
            mainActivity.showBottomNavigation().show(-1, false)
            mainActivity.showBottomNavigation().clearAllCounts()
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


}