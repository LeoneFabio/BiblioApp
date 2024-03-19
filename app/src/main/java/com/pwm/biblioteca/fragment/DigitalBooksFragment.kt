package com.pwm.biblioteca.fragment

import android.content.ActivityNotFoundException
import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.pwm.biblioteca.adapter.DigitalBooksAdapter
import com.pwm.biblioteca.databinding.FragmentLibriDigitaliBinding
import com.pwm.biblioteca.server.ClientNetwork
import java.io.IOException
import androidx.core.content.FileProvider
import java.io.File
import java.io.FileOutputStream


class DigitalBooksFragment: Fragment(), DigitalBooksAdapter.PdfItemClickListener {
    private lateinit var binding:FragmentLibriDigitaliBinding
    private lateinit var bookAdapter: DigitalBooksAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLibriDigitaliBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tag1 = "fragmentMenu"
        val tag2 = "fragmentProfile"
        closeFragment(tag1)
        closeFragment(tag2)

        ClientNetwork.getDigitalBooks(){ bookList ->
            if (bookList.isEmpty()){
                showAlert("ERRORE", "Si è verificato un problema durante la connessione con il server.")
            }else{
                binding.rvLibriDigitali.apply {
                    layoutManager = LinearLayoutManager(requireContext())
                    bookAdapter = DigitalBooksAdapter(bookList, this@DigitalBooksFragment, requireContext())
                    adapter = bookAdapter
                }

            }
        }

    }

    // Download PDF
    override fun onPdfItem1Click(pdfByteArray: ByteArray, titolo: String?){
        val fileName: String
        if(titolo != null){
            fileName = "${titolo}.pdf"
        }else{
            fileName = "file.pdf"
        }
        val values = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, fileName)
            put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
        }

        val uri = requireContext().contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, values)

        uri?.let { downloadUri ->
            requireContext().contentResolver.openOutputStream(downloadUri)?.use { outputStream ->
                try {
                    outputStream.write(pdfByteArray)
                    outputStream.close()
                    AlertDialog.Builder(requireContext())
                        .setTitle("DOWNLOAD PDF")
                        .setMessage("Il download è avvenuto con successo.")
                        .setPositiveButton("OK") { dialog, _ ->
                            dialog.dismiss()
                        }
                        .setNeutralButton("Apri"){ dialog, _ ->
                            dialog.dismiss()
                            val intent = Intent(Intent.ACTION_VIEW)
                            intent.setDataAndType(downloadUri, "application/pdf")
                            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

                            val chooserIntent = Intent.createChooser(intent, "Apri con")
                            chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

                            try {
                                startActivity(chooserIntent)
                            } catch (e: ActivityNotFoundException) {
                                // Nessuna app di visualizzazione PDF trovata sul dispositivo
                                showAlert("ERRORE", "Nessuna app di visualizzazione PDF trovata sul dispositivo.")
                            }
                        }
                        .show()
                } catch (e: IOException) {
                    showAlert("ERRORE", "Si è verificato un problema durante il download del pdf.")
                    e.printStackTrace()
                    return
                }
            }


        }
    }

    //Visualizzazione PDF senza Download
    override fun onPdfItem2Click(pdfByteArray: ByteArray, titolo: String?) {

        val fileName: String
        if(titolo != null){
            fileName = "${titolo}.pdf"
        }else{
            fileName = "file.pdf"
        }
            val filePath = requireContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)?.absolutePath + File.separator + fileName

            val outputStream: FileOutputStream
            try {
                outputStream = FileOutputStream(File(filePath))
                outputStream.write(pdfByteArray)
                outputStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
                return
            }

            val intent = Intent(Intent.ACTION_VIEW)
            intent.setDataAndType(FileProvider.getUriForFile(requireContext(), "${requireContext().packageName}.fileprovider", File(filePath)), "application/pdf")
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

            val chooserIntent = Intent.createChooser(intent, "Apri con")
            chooserIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)

            try {
                startActivity(chooserIntent)
            } catch (e: ActivityNotFoundException) {
                // Nessuna app di visualizzazione PDF trovata sul dispositivo
                showAlert("ERRORE", "Nessuna app di visualizzazione PDF trovata sul dispositivo.")
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