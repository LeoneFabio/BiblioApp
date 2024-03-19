package com.pwm.biblioteca.fragment

import android.app.AlertDialog
import android.os.*
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.pwm.biblioteca.utils.Book
import com.pwm.biblioteca.R
import com.pwm.biblioteca.utils.User
import com.pwm.biblioteca.databinding.FragmentLibroApertoBinding
import com.pwm.biblioteca.server.ClientNetwork
import java.text.DecimalFormat
import java.time.LocalDate

class OpenBookFragment: Fragment() {

    private lateinit var binding: FragmentLibroApertoBinding


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLibroApertoBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val tag1 = "fragmentMenu"
        val tag2 = "fragmentProfile"
        closeFragment(tag1)
        closeFragment(tag2)
        val book = if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            arguments?.getParcelable("bookKey", Book::class.java)
        }else {
            @Suppress("DEPRECATION")
            arguments?.getParcelable("bookKey")
        }



        if (book != null) {
            if(book.numCopie != 0){
                binding.tvInfoDisponibilita.visibility = View.GONE
                binding.btnPrenota.text = "PRENOTA"
            }else{
                binding.tvInfoDisponibilita.visibility = View.VISIBLE
                binding.btnPrenota.text = "NOTIFICA DISPONIBILITA'"

            }
            binding.tvAutore.text = book.autore
            when (book.numCopie){
                0 -> binding.tvCopie.text = "Nessuna copia disponibile"
                1 -> binding.tvCopie.text = "Disponibile solo una copia"
                else -> binding.tvCopie.text = "${book.numCopie} copie disponibili"
            }
            binding.tvTitolo.text = book.titolo
            binding.tvGenere.text = book.genere
            binding.tvDescrizione.text = book.descrizione
            binding.ivCopertina.setImageBitmap(book.copertina)
            ClientNetwork.getValutazioneMedia(book) { media ->
                if (media != null) {
                    val numericMedia = media.toFloatOrNull() ?: 0f
                    val decimalFormat = DecimalFormat("#.#")
                    val truncatedMedia = decimalFormat.format(numericMedia)
                    binding.tvValutazione.text = "Valutazione media per questo libro: $truncatedMedia/5"
                } else {
                    binding.tvValutazione.text = "Nessuna valutazione per questo libro"
                }
            }

            var contFlag = 1 //flag utile per gestire l'inizializzazione del rating
            if(User.id != 0) {
                // Chiamata al server per verificare la presenza della recensione esistente
                ClientNetwork.getRating(User.id, book.id) { existingRating ->
                    if(existingRating==null){
                        showAlert("ERRORE", "Si è verificato un problema durante la connessione con il server.")
                    }else if(existingRating != -1.0f){
                        // Recensione esistente, imposta la ratingBar
                        contFlag = 0
                        binding.rbRecensione.rating = existingRating
                    }
                }
            }

            var check = 1 //flag per gestire il cambiamento del rating
            binding.rbRecensione.setOnRatingBarChangeListener { ratingBar, _, _ ->
                val myRating = ratingBar.rating

                if(User.id != 0){
                    // Chiamata al server per verificare la presenza della recensione esistente
                    check = 1
                    ClientNetwork.getRating(User.id, book.id) { existingRating ->
                        if(existingRating==null){
                            showAlert("ERRORE", "Si è verificato un problema durante la connessione con il server.")
                        }else if (existingRating != -1.0f) {
                            // Recensione esistente, esegui l'aggiornamento
                            ClientNetwork.updateRating(User.id, book.id, myRating) { updateCheck ->
                                if (updateCheck == true) {
                                    // Aggiornamento recensione riuscito
                                    check = 1
                                    if (contFlag != 0){
                                        showAlert("AGGIORNAMENTO", "Recensione aggiornata con successo.")
                                        ClientNetwork.getValutazioneMedia(book) { media ->
                                            if (media != null) {
                                                val numericMedia = media.toFloatOrNull() ?: 0f
                                                val decimalFormat = DecimalFormat("#.#")
                                                val truncatedMedia = decimalFormat.format(numericMedia)
                                                binding.tvValutazione.text = "Valutazione media per questo libro: $truncatedMedia/5"
                                            } else {
                                                binding.tvValutazione.text = "Nessuna valutazione per questo libro"
                                            }
                                        }
                                    }
                                    contFlag++
                                } else {
                                    // Aggiornamento recensione non riuscito
                                    check++
                                    if(check % 2 == 0){
                                        AlertDialog.Builder(requireContext())
                                            .setTitle("ERRORE")
                                            .setMessage("L'aggiornamento della tua recensione non è andato a buon fine.")
                                            .setPositiveButton("OK") { dialog, _ ->
                                                dialog.dismiss()
                                                binding.rbRecensione.rating = existingRating // Ripristina il valore precedente
                                            }.show()
                                    }

                                }
                            }
                        } else{
                            // Nessuna recensione esistente, esegui l'inserimento
                            ClientNetwork.insertRating(User.id, book.id, myRating) { insertCheck ->
                                if (insertCheck == true) {
                                    // Inserimento recensione riuscito
                                    check = 1
                                    showAlert("Inserimento recensione", "Grazie per la tua recensione!")
                                    ClientNetwork.getValutazioneMedia(book) { media ->
                                        if (media != null) {
                                            val numericMedia = media.toFloatOrNull() ?: 0f
                                            val decimalFormat = DecimalFormat("#.#")
                                            val truncatedMedia = decimalFormat.format(numericMedia)
                                            binding.tvValutazione.text = "Valutazione media per questo libro: $truncatedMedia/5"
                                        } else {
                                            binding.tvValutazione.text = "Nessuna valutazione per questo libro"
                                        }
                                    }

                                } else {
                                    // Inserimento recensione non riuscito
                                    check ++
                                    if (check % 2 == 0){
                                        AlertDialog.Builder(requireContext())
                                            .setTitle("ERRORE")
                                            .setMessage("L'inserimento della tua recensione NON è andato a buon fine.")
                                            .setPositiveButton("OK") { dialog, _ ->
                                                dialog.dismiss()
                                                binding.rbRecensione.rating = 0f // Reimposta a 0 stelle
                                            }.show()
                                    }

                                }
                            }
                        }
                    }
                }else{
                    check++
                    if(check % 2 == 0 ){
                        AlertDialog.Builder(requireContext())
                            .setTitle("Accesso Negato")
                            .setMessage("Per accedere alla funzionalità richiesta effettua il Login")
                            .setPositiveButton("OK"){dialog,_ ->
                                dialog.dismiss()
                                binding.rbRecensione.rating = 0f // Reimposta a 0 stelle
                            }.show()
                    }

                }

            }

            //Preferiti
            if(User.id != 0) {
                // Chiamata al server per verificare se il libro è un preferito
                ClientNetwork.getInfoFavourite(User.id, book.id) { value ->
                    when(value){
                        1 -> //il libro è tra i preferiti dell'utente
                            {binding.ibCuore.setImageResource(R.drawable.ic_cuore_rosso)
                            binding.ibCuore.tag = R.drawable.ic_cuore_rosso}
                        2 -> //il libro non è tra i preferiti dell'utente
                            {binding.ibCuore.setImageResource(R.drawable.ic_cuore_bianco)
                            binding.ibCuore.tag = R.drawable.ic_cuore_bianco}
                        else -> //Il collegamento con il server non è andato a buon fine
                            showAlert("ERRORE", "Si è verificato un problema durante la connessione con il server.")
                    }
                }
            }
            binding.ibCuore.setOnClickListener {

                if(User.id != 0){
                    // Controlla se l'ImageButton mostra l'immagine del cuore bianco
                    val whiteDrawableId = R.drawable.ic_cuore_bianco
                    val isWhite = (binding.ibCuore.tag == whiteDrawableId)
                    if (isWhite) {
                        // Il libro non era tra i preferiti al momento del click
                        ClientNetwork.insertFavouriteBook(User.id, book.id){ check ->
                            if(check == true){
                                binding.ibCuore.setImageResource(R.drawable.ic_cuore_rosso)
                                binding.ibCuore.tag = R.drawable.ic_cuore_rosso
                            }else{
                                showAlert("ERRORE", "Si è verificato un problema durante la connessione con il server.")
                            }
                        }
                    } else {
                        // Il libro era tra i preferiti
                        ClientNetwork.deleteFavouriteBook(User.id, book.id){ check ->
                            if(check == true){
                                binding.ibCuore.setImageResource(R.drawable.ic_cuore_bianco)
                                binding.ibCuore.tag = R.drawable.ic_cuore_bianco
                            }else{
                                showAlert("ERRORE", "Si è verificato un problema durante la connessione con il server.")
                            }
                        }
                    }
                }else{
                    showAlert("Accesso Negato", "Per accedere alla funzionalità richiesta effettua il Login.")
                }

            }

            binding.btnPrenota.setOnClickListener{
                if(User.id == 0){
                    showAlert("Accesso Negato", "Per accedere alla funzionalità richiesta effettua il Login.")
                }else{
                    if(binding.btnPrenota.text == "PRENOTA") {
                        var found1 = false
                        var found2 = false
                        ClientNetwork.getLoanBooks(User.id, inCorso = true) { bookList, check ->
                            if(check){
                                if (bookList.isNotEmpty()) {
                                    for (element in bookList) {
                                        if (element.id == book.id) {
                                            found1 = true
                                        }
                                    }
                                }
                                if (!found1) {
                                        ClientNetwork.getLoanBooks(User.id, inCorso = false){ bookList, check ->
                                            if(check){
                                                if (bookList.isNotEmpty()) {
                                                    for (element in bookList) {
                                                        if (element.id == book.id) {
                                                            found2 = true
                                                        }
                                                    }
                                                }
                                                if(!found2){
                                                    //il libro non è nè nei prestiti in corso nè in quelli passati
                                                    ClientNetwork.insertLoan(User.id, book.id) { prenotated ->
                                                        if (prenotated) {
                                                            ClientNetwork.updateBookCopies(
                                                                book.id,
                                                                book.numCopie - 1
                                                            ) { check ->
                                                                if (check) {
                                                                    showAlert("Prenotazione libro", "La prenotazione del libro è avvenuta con successo.")
                                                                    val numCopie = (book.numCopie - 1)
                                                                    when (numCopie) {
                                                                        0 -> binding.tvCopie.text =
                                                                            "Nessuna copia disponibile"
                                                                        1 -> binding.tvCopie.text =
                                                                            "Disponibile solo una copia"
                                                                        else -> binding.tvCopie.text =
                                                                            "${numCopie} copie disponibili"
                                                                    }
                                                                    ClientNetwork.insertNotification(
                                                                        User.id,
                                                                        "${LocalDate.now()}: Hai effettuato la prenotazione di \"${book.titolo}\". Il libro è stato aggiunto ai tuoi prestiti"
                                                                    ) { check ->
                                                                        if (!check) {
                                                                            showAlert("ERRORE", "Si è verificato un problema durante la connessione con il server.")
                                                                        }
                                                                    }
                                                                } else {
                                                                    showAlert("ERRORE", "Si è verificato un problema durante la connessione con il server.")
                                                                }
                                                            }
                                                        } else {
                                                            showAlert("ERRORE", "Si è verificato un problema durante la connessione con il server.")
                                                        }

                                                    }
                                                }else{
                                                    //Il libro è nei prestiti passati
                                                    ClientNetwork.updateDatesLoan(User.id, book.id){ check ->
                                                        if(check){
                                                            showAlert("Prenotazione libro", "La prenotazione del libro è avvenuta con successo.")
                                                            val numCopie = (book.numCopie - 1)
                                                            when (numCopie) {
                                                                0 -> binding.tvCopie.text =
                                                                    "Nessuna copia disponibile"
                                                                1 -> binding.tvCopie.text =
                                                                    "Disponibile solo una copia"
                                                                else -> binding.tvCopie.text =
                                                                    "${numCopie} copie disponibili"
                                                            }
                                                            ClientNetwork.insertNotification(
                                                                User.id,
                                                                "${LocalDate.now()}: Hai effettuato la prenotazione di \"${book.titolo}\". Il libro è stato aggiunto ai tuoi prestiti"
                                                            ) { check ->
                                                                if (!check) {
                                                                    showAlert("ERRORE", "Si è verificato un problema durante la connessione con il server.")
                                                                }
                                                            }

                                                        }else{
                                                            showAlert("ERRORE", "Si è verificato un problema durante la connessione con il server.")
                                                        }
                                                    }
                                                }
                                            }else{
                                                showAlert("ERRORE", "Si è verificato un problema durante la connessione con il server.")
                                            }
                                        }

                                } else {
                                    //Il libro è già nei tuoi prestiti attuali
                                    showAlert("ATTENZIONE!", "Il libro è tra i tuoi prestiti attuali: hai già effettuato una prenotazione per questo libro.")
                                }
                            }else{
                                showAlert("ERRORE", "Si è verificato un problema durante la connessione con il server.")
                            }
                        }

                    }else{
                            //attesa disponibilità
                            ClientNetwork.checkWaitingNotifications(User.id, book.id){ value ->
                                when(value){
                                    0 ->    {
                                        showAlert("ATTENZIONE!", "Hai già effettuato la richiesta: sarai notificato quando il libro tornerà disponibile.")
                                    }
                                    1 -> {
                                        ClientNetwork.insertWaitingNotification(User.id, book.id){ flag ->
                                            if(flag == true){
                                                showAlert("Notifica disponibilità libro", "La tua richiesta è stata ricevuta con successo.")
                                            }else{
                                                showAlert("ERRORE", "Si è verificato un problema durante la connessione con il server.")
                                            }
                                        }
                                    }
                                    else -> {
                                        showAlert("ERRORE", "Si è verificato un problema durante la connessione con il server.")
                                    }
                                }
                            }
                    }
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

    private fun showAlert(title: String, message: String){
        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK"){dialog,_ ->
                dialog.dismiss()
            }.show()
    }

}