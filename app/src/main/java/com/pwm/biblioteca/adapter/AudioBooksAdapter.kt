package com.pwm.biblioteca.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.pwm.biblioteca.utils.Book
import com.pwm.biblioteca.utils.User
import com.pwm.biblioteca.databinding.LayoutAudiolibroBinding
import com.pwm.biblioteca.server.ClientNetwork

class AudioBooksAdapter(private var bookList: List<Book>, private val audioItemClickListener: AudioItemClickListener, private var context: Context): RecyclerView.Adapter<AudioBooksAdapter.AudioBooksViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AudioBooksViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = LayoutAudiolibroBinding.inflate(layoutInflater, parent, false)
        return AudioBooksViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return bookList.size
    }

    override fun onBindViewHolder(holder: AudioBooksViewHolder, position: Int) {
        val book = bookList[position]
        holder.bind(book)
    }

    interface AudioItemClickListener {
        fun onAudioItem1Click(audioByteArray: ByteArray, titolo: String?)
        fun onAudioItem2Click(audioByteArray: ByteArray, titolo: String?)
    }

    inner class AudioBooksViewHolder(private val binding: LayoutAudiolibroBinding):
        RecyclerView.ViewHolder(binding.root) {

        fun bind(book: Book){
            binding.tvAutoreLibroPreferiti.text = "Autore: ${book.autore}"
            binding.tvNomeLibroPreferiti.text = "Titolo: ${book.titolo}"
            binding.ibCopertina.scaleType = ImageView.ScaleType.CENTER_CROP
            binding.ibCopertina.setImageBitmap(book.copertina)

            binding.ibDownload.setOnClickListener {
                if(User.id != 0){
                    ClientNetwork.cercaAUDIO(book.id){ audioByteArray ->
                        if(audioByteArray != null){
                            audioItemClickListener.onAudioItem1Click(audioByteArray, book.titolo)
                        }else{
                            showAlert("ERRORE", "Si è verificato un problema durante la connessione con il server.")
                        }
                    }
                }else{
                    showAlert("Download negato", "Per accedere alla funzionalità richiesta effettua il Login.")
                }

            }

            binding.ibRiproduci.setOnClickListener {
                if(User.id != 0){
                    ClientNetwork.cercaAUDIO(book.id){ audioByteArray ->
                        if(audioByteArray != null){
                            audioItemClickListener.onAudioItem2Click(audioByteArray, book.titolo)
                        }else{
                            showAlert("ERRORE", "Si è verificato un problema durante la connessione con il server.")

                        }
                    }
                }else{
                    showAlert("Riproduzione negata", "Per accedere alla funzionalità richiesta effettua il Login.")
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