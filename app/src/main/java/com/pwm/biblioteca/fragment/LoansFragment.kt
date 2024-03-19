package com.pwm.biblioteca.fragment

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.pwm.biblioteca.activity.MainActivity
import com.pwm.biblioteca.R
import com.pwm.biblioteca.adapter.LoansAdapter
import com.pwm.biblioteca.utils.User
import com.pwm.biblioteca.databinding.FragmentPrestitiBinding
import com.pwm.biblioteca.server.ClientNetwork

class LoansFragment: Fragment() {
    private lateinit var binding: FragmentPrestitiBinding
    private lateinit var bookAdapter: LoansAdapter
    private lateinit var mainActivity: MainActivity

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPrestitiBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?){
        super.onViewCreated(view, savedInstanceState)

        //inizializzo la recycler view
        setupRecyclerView()

    }

    private fun setupRecyclerView() {
        mainActivity = activity as MainActivity
        var found = false

        ClientNetwork.getLoanBooks(User.id, inCorso = true) { bookList, check ->
            if(check){
                if (bookList.isEmpty()) {
                    binding.tvPrestitiInCorso.visibility = View.GONE
                    binding.rvPrestitiInCorso.visibility = View.GONE
                } else {
                    found = true
                    binding.tvPrestitiInCorso.visibility = View.VISIBLE
                    binding.rvPrestitiInCorso.visibility = View.VISIBLE
                    binding.rvPrestitiInCorso.apply {
                        layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                        bookAdapter = LoansAdapter(bookList, inCorso = true, parentFragmentManager, mainActivity.showBottomNavigation(), requireContext())
                        adapter = bookAdapter
                    }
                }
            }else{
                found = true
                showAlert("ERRORE", "Si è verificato un problema durante la connessione con il server.")
            }

            ClientNetwork.getLoanBooks(User.id, inCorso = false) { bookList, check ->
                if(check){
                    if (bookList.isEmpty()) {
                        binding.tvPrestitiPassati.visibility = View.GONE
                        binding.rvPrestitiPassati.visibility = View.GONE
                    } else {
                        found = true
                        binding.tvPrestitiPassati.visibility = View.VISIBLE
                        binding.rvPrestitiPassati.visibility = View.VISIBLE
                        binding.rvPrestitiPassati.apply {
                            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                            bookAdapter = LoansAdapter(bookList, inCorso = false, parentFragmentManager, mainActivity.showBottomNavigation(), requireContext())
                            adapter = bookAdapter
                        }
                    }
                }else{
                    found = true
                    showAlert("ERRORE", "Si è verificato un problema durante la connessione con il server.")
                }
                if(!found){
                    AlertDialog.Builder(requireContext())
                        .setTitle("ATTENZIONE!")
                        .setMessage("Non sono presenti libri in prestito. Clicca su \"ESPLORA\" per consultare il catalogo dei libri. ")
                        .setPositiveButton("ESPLORA") { dialog, _ ->
                            dialog.dismiss()
                            parentFragmentManager.beginTransaction()
                                .replace(R.id.fragment_container, BookshopFragment())
                                .commit()
                        }
                        .setNeutralButton("OK"){ dialog, _ ->
                            dialog.dismiss()
                        }
                        .show()
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