package com.pwm.biblioteca.fragment

import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.pwm.biblioteca.adapter.AudioBooksAdapter
import com.pwm.biblioteca.databinding.FragmentAudiolibriBinding
import com.pwm.biblioteca.server.ClientNetwork
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class AudioBooksFragment: Fragment(), AudioBooksAdapter.AudioItemClickListener {
    private lateinit var binding: FragmentAudiolibriBinding
    private lateinit var bookAdapter: AudioBooksAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAudiolibriBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tag1 = "fragmentMenu"
        val tag2 = "fragmentProfile"
        closeFragment(tag1)
        closeFragment(tag2)

        ClientNetwork.getAudioBooks(){ bookList->
            if (bookList.isEmpty()){
                showAlert("ERRORE", "Si è verificato un problema durante la connessione con il server.")
            }else{
                binding.rvAudioLibri.apply {
                    layoutManager = LinearLayoutManager(requireContext())
                    bookAdapter = AudioBooksAdapter(bookList, this@AudioBooksFragment, requireContext())
                    adapter = bookAdapter
                }
            }
        }

    }

    //Download audio
    override fun onAudioItem1Click(audioByteArray: ByteArray, titolo: String?){

        val fileName: String
        if(titolo != null){
            fileName = "${titolo}.mp3"
        }else{
            fileName = "audio.mp3"
        }
        val values = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "audio/*")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
        }

        val uri = requireContext().contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)

        uri?.let { downloadUri ->
            requireContext().contentResolver.openOutputStream(downloadUri)?.use { outputStream ->
                try {
                    outputStream.write(audioByteArray)
                    outputStream.close()
                    AlertDialog.Builder(requireContext())
                        .setTitle("DOWNLOAD AUDIO")
                        .setMessage("Il download è avvenuto con successo.")
                        .setPositiveButton("OK") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .setNeutralButton("Riproduci"){ dialog, _ ->
                            dialog.dismiss()
                            // Controlla se l'app possiede un output audio funzionante
                            if (requireContext().packageManager.hasSystemFeature(
                                    PackageManager.FEATURE_CAMERA_FRONT)) {
                                val intent = Intent(Intent.ACTION_VIEW)
                                intent.setDataAndType(downloadUri, "audio/*")
                                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

                                val chooserIntent = Intent.createChooser(intent, "Apri con")
                                chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

                                try {
                                    startActivity(chooserIntent)
                                } catch (e: ActivityNotFoundException) {
                                    // Nessuna app di riproduzione audio trovata sul dispositivo
                                    showAlert("ERRORE", "Nessuna app di riproduzione audio trovata sul dispositivo.")
                                }
                            } else {
                                showAlert("ERRORE", "Il tuo dispositivo non possiede un output audio funzionante.")
                            }
                        }
                        .show()
                } catch (e: IOException) {
                    showAlert("ERRORE", "Si è verificato un problema durante il download dell'audio.")
                    e.printStackTrace()
                    return
                }


            }
        }
    }

    //Riproduzione audio
    override fun onAudioItem2Click(audioByteArray: ByteArray, titolo: String?){

        // Controlla se il dispositivo ha un output audio funzionante
        if (requireContext().packageManager.hasSystemFeature(
                PackageManager.FEATURE_AUDIO_OUTPUT)) {
            val fileName: String
            if(titolo != null){
                fileName = "${titolo}.mp3"
            }else{
                fileName = "audio.mp3"
            }
            val filePath = requireContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)?.absolutePath + File.separator + fileName

            val outputStream: FileOutputStream
            try {
                outputStream = FileOutputStream(File(filePath))
                outputStream.write(audioByteArray)
                outputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
                return
            }

            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(FileProvider.getUriForFile(requireContext(), "${requireContext().packageName}.fileprovider", File(filePath)), "audio/mp3")
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

            val chooserIntent = Intent.createChooser(intent, "Apri con")
            chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            try {
                startActivity(chooserIntent)
            } catch (e: ActivityNotFoundException) {
                // Nessuna app di riproduzione audio trovata sul dispositivo
                showAlert("ERRORE", "Nessuna app di riproduzione audio trovata sul dispositivo.")
            }
        } else {
            showAlert("ERRORE", "Il tuo dispositivo non possiede un output audio funzionante.")
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