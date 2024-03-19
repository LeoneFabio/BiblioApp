package com.pwm.biblioteca.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.pwm.biblioteca.utils.User
import com.pwm.biblioteca.adapter.NotificationAdapter
import com.pwm.biblioteca.databinding.FragmentNotificheBinding
import com.pwm.biblioteca.server.ClientNetwork

class NotificationFragment: Fragment() {
    private lateinit var binding: FragmentNotificheBinding
    private lateinit var notificationAdapter: NotificationAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentNotificheBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tag1 = "fragmentMenu"
        val tag2 = "fragmentProfile"
        closeFragment(tag1)
        closeFragment(tag2)

        ClientNetwork.getNotifications(User.id){ notificationList ->
            if(notificationList.isEmpty()){
                Toast.makeText(requireContext(), "Non sono presenti notifiche", Toast.LENGTH_SHORT).show()
            }else{
                binding.rvNotifiche.apply{
                    layoutManager = LinearLayoutManager(requireContext())
                    notificationAdapter = NotificationAdapter(notificationList)
                    adapter = notificationAdapter
                }
            }
        }




    }

    private fun closeFragment(tag: String){
        val existingFragment = parentFragmentManager.findFragmentByTag(tag)
        if (existingFragment != null) {
            parentFragmentManager.beginTransaction()
                .remove(existingFragment)
                .commit()
        }
    }
}