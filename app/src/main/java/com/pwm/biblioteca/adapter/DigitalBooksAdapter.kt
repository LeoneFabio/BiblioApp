package com.pwm.biblioteca.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.pwm.biblioteca.utils.Book
import com.pwm.biblioteca.utils.User
import com.pwm.biblioteca.databinding.LayoutLibroDigitaleBinding
import com.pwm.biblioteca.server.ClientNetwork

class DigitalBooksAdapter(private var bookList: List<Book>, private val pdfItemClickListener: PdfItemClickListener, private var context: Context): RecyclerView.Adapter<DigitalBooksAdapter.DigitalBooksViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DigitalBooksViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = LayoutLibroDigitaleBinding.inflate(layoutInflater, parent, false)
        return DigitalBooksViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DigitalBooksViewHolder, position: Int) {
        val book = bookList[position]
        holder.bind(book)
    }

    override fun getItemCount(): Int {
        return bookList.size
    }

    interface PdfItemClickListener {
        fun onPdfItem1Click(pdfByteArray: ByteArray, titolo: String?)
        fun onPdfItem2Click(pdfByteArray: ByteArray, titolo: String?)
    }


    inner class DigitalBooksViewHolder(private val binding: LayoutLibroDigitaleBinding):
        RecyclerView.ViewHolder(binding.root) {

        fun bind(book: Book){
            binding.ibCopertina.scaleType = ImageView.ScaleType.CENTER_CROP
            binding.ibCopertina.setImageBitmap(book.copertina)
            binding.tvAutoreLibroPreferiti.text = "Autore: ${book.autore}"
            binding.tvNomeLibroPreferiti.text = "Titolo: ${book.titolo}"

            binding.ibDownloadPdf.setOnClickListener {
                if(User.id != 0){
                    ClientNetwork.cercaPDF(book.id) { pdfByteArray ->
                        if(pdfByteArray != null){
                            pdfItemClickListener.onPdfItem1Click(pdfByteArray, book.titolo)
                        }else{
                            showAlert("ERRORE", "Si è verificato un problema durante la connessione con il server.")

                        }
                    }
                }else{
                    showAlert("Download negato", "Per accedere alla funzionalità richiesta effettua il Login.")
                }
            }

            binding.ibLeggiPdf.setOnClickListener {
                if(User.id != 0){
                    ClientNetwork.cercaPDF(book.id) { pdfByteArray ->
                        if(pdfByteArray != null){
                            pdfItemClickListener.onPdfItem2Click(pdfByteArray, book.titolo)
                        }else{
                            showAlert("ERRORE", "Si è verificato un problema durante la connessione con il server.")
                        }
                    }
                }else{
                    showAlert("Lettura negata", "Per accedere alla funzionalità richiesta effettua il Login.")
                }
            }
        }

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