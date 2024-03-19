package com.pwm.biblioteca.adapter

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import com.pwm.biblioteca.utils.Book
import com.pwm.biblioteca.R
import com.pwm.biblioteca.utils.User
import com.pwm.biblioteca.databinding.LayoutLibroPrestitoBinding
import com.pwm.biblioteca.fragment.OpenBookFragment
import com.pwm.biblioteca.server.ClientNetwork

class LoansAdapter (private var loansList: List<Book>,
                    private var inCorso: Boolean,
                    private var fragmentManager: FragmentManager,
                    private var bottomNavigation: MeowBottomNavigation,
                    private var context: Context,
                    ): RecyclerView.Adapter<LoansAdapter.LoansViewHolder>(){

    inner class LoansViewHolder(private var binding: LayoutLibroPrestitoBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(book: Book){
            binding.tvAutore.text = "Autore: ${book.autore}"
            binding.tvTitolo.text = "Titolo: ${book.titolo}"
            if(book.giorniRestituzione != -1){
                binding.tvRestituzione.text = "Giorni rimanenti: ${book.giorniRestituzione}"
            }
            binding.ibCopertina.scaleType = ImageView.ScaleType.CENTER_CROP
            binding.ibCopertina.setImageBitmap(book.copertina)
            if(inCorso && book.giorniRestituzione<=5){
                binding.btnRinnova.visibility = View.VISIBLE
            }else{
                binding.btnRinnova.visibility = View.GONE
            }
            binding.ibCopertina.setOnClickListener {
                val openBookFragment = OpenBookFragment()
                val bundle = Bundle().apply {
                    putParcelable("bookKey", book)
                }
                openBookFragment.arguments = bundle

                fragmentManager.beginTransaction()
                    .replace(
                        R.id.fragment_container,
                        openBookFragment
                    )
                    .addToBackStack("OpenbookFragment()")
                    .commit()
                bottomNavigation.show(-1, false)
                bottomNavigation.clearAllCounts()
            }
            binding.btnRinnova.setOnClickListener {
                AlertDialog.Builder(context)
                    .setTitle("Rinnova prestito")
                    .setMessage("Clicca su \"CONFERMA\" per rinnovare per altri 15 giorni il prestito del libro.")
                    .setPositiveButton("CONFERMA") { dialog, _ ->
                        dialog.dismiss()
                        ClientNetwork.updateDaysLoan(User.id, book.id, book.giorniRestituzione){ check ->
                            if(check){
                                showAlert("Rinnova prestito", "Il rinnovo del prestito è avvenuto con successo.")
                                binding.tvRestituzione.text = "Giorni rimanenti: ${book.giorniRestituzione + 15}"
                                binding.btnRinnova.visibility = View.GONE
                            }else{
                                showAlert("ERRORE", "Si è verificato un problema durante la connessione con il server.")
                            }
                        }
                    }
                    .setNegativeButton("ANNULLA"){ dialog, _ ->
                        dialog.dismiss()
                    }
                    .show()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LoansViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = LayoutLibroPrestitoBinding.inflate(layoutInflater, parent, false)
        return LoansViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return loansList.size
    }

    override fun onBindViewHolder(holder: LoansViewHolder, position: Int) {
        val book = loansList[position]
        holder.bind(book)
    }
    private fun showAlert(title: String, message: String){
        AlertDialog.Builder(context)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK"){dialog,_ ->
                dialog.dismiss()
            }.show()
    }
}