package com.pwm.biblioteca.fragment


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.akexorcist.snaptimepicker.SnapTimePickerDialog
import com.akexorcist.snaptimepicker.TimeRange
import com.akexorcist.snaptimepicker.TimeValue
import com.pwm.biblioteca.R
import com.pwm.biblioteca.utils.User
import com.pwm.biblioteca.databinding.FragmentPrenotaPostoBinding
import com.pwm.biblioteca.server.ClientNetwork
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

class BookSeatFragment : Fragment() {

    private lateinit var binding: FragmentPrenotaPostoBinding
    private lateinit var selectedDate: String
    private var selectedStartTime = ""
    private var selectedEndTime = ""
    private val MAXNUMSEAT = 10

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentPrenotaPostoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeViews()
    }

    private fun initializeViews() {

        binding.cvPrenotaGiorno.visibility = View.GONE

        binding.prenotaPostoGiornoEt.isClickable = false
        binding.prenotaPostoGiornoEt.isFocusable = false
        binding.prenotaPostoGiornoEt.isFocusableInTouchMode = false
        binding.prenotaPostoOraInizioEt.isClickable = false
        binding.prenotaPostoOraInizioEt.isFocusable = false
        binding.prenotaPostoOraInizioEt.isFocusableInTouchMode = false
        binding.prenotaPostoOraFineEt.isClickable = false
        binding.prenotaPostoOraFineEt.isFocusable = false
        binding.prenotaPostoOraFineEt.isFocusableInTouchMode = false



        binding.ibPrenotaGiorno.setOnClickListener {
            if (binding.cvPrenotaGiorno.visibility == View.VISIBLE){
                binding.cvPrenotaGiorno.visibility = View.GONE
            }
            else{
                binding.cvPrenotaGiorno.visibility = View.VISIBLE
            }
        }

        binding.ibOraInizio.setOnClickListener {
            showTimePickerDialog("13", "0") { hour, minute ->
                selectedStartTime = formatTime(hour, minute)
                binding.prenotaPostoOraInizioEt.setText(selectedStartTime)
                selectedEndTime=""
                binding.prenotaPostoOraFineEt.setText(selectedEndTime)
            }
        }

        binding.ibOraFine.setOnClickListener {
            if(binding.prenotaPostoOraInizioEt.text.isEmpty()){
                showAlert("ATTENZIONE!", "Seleziona prima l'ora di inizio.")
            }else{
                val startHour = selectedStartTime.substring(0,2)
                showTimePickerDialog(startHour,"0") { hour, minute ->
                    selectedEndTime = formatTime(hour, minute)
                    binding.prenotaPostoOraFineEt.setText(selectedEndTime)
                }
            }

        }

        binding.prenotaPostoBtn.setOnClickListener {
            if (::selectedDate.isInitialized && selectedStartTime != "" && selectedEndTime != "") {
                val formatter = DateTimeFormatter.ofPattern("HH:mm")
                val localTime1 = LocalTime.parse(selectedStartTime, formatter)
                val localTime2 = LocalTime.parse(selectedEndTime, formatter)
                if(localTime1 < localTime2){
                    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    val date = dateFormat.parse(selectedDate)
                    val sqlDate = java.sql.Date(date.time)
                    ClientNetwork.bookingCheckDate(User.id, sqlDate){ check ->
                        if(check == true){
                            ClientNetwork.getInfoSeat(sqlDate){ countSeatList ->
                                if(countSeatList.isEmpty()){
                                    showAlert("ERRORE", "Si è verificato un problema durante la connessione con il server.")
                                }else{
                                    if(bookingCheck(countSeatList, selectedStartTime, selectedEndTime)){
                                        ClientNetwork.insertBookingSeat(User.id, sqlDate, localTime1, localTime2){ check ->
                                            if(check == true){
                                                showAlert("Prenotazione sala", "La prenotazione della sala lettura è avvenuta con successo.")
                                                binding.prenotaPostoGiornoEt.text.clear()
                                                binding.prenotaPostoOraInizioEt.text.clear()
                                                binding.prenotaPostoOraFineEt.text.clear()
                                                ClientNetwork.insertNotification(User.id, "${LocalDate.now()}: Hai prenotato una postazione in sala lettura per giorno $selectedDate dalle ore $selectedStartTime alle ore $selectedEndTime"){ check ->
                                                    if(!check){
                                                        showAlert("ERRORE", "Si è verificato un problema durante il collegamento con il server.")
                                                    }
                                                }
                                            }else{
                                                showAlert("ERRORE", "Si è verificato un problema durante il collegamento con il server.")
                                            }
                                        }
                                    }else{
                                        showAlert("AVVISO", "La sala lettura ha raggiunto la capienza massima negli orari indicati.")
                                    }
                                }
                            }
                        }else{
                            showAlert("AVVISO", "Non è possibile prenotare due postazioni in sala lettura nella stessa giornata.")
                        }

                    }

                }
                else{
                    showAlert("ATTENZIONE!", "L'ora di fine deve essere successiva a quella di inizio.")
                }
            } else {
                showAlert("ATTENZIONE!", "Riempi tutti i campi per effettuare la prenotazione.")
            }
        }

        binding.cvPrenotaGiorno.setOnDateChangeListener { _, year, month, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(year, month, dayOfMonth)

            val currentDate = Calendar.getInstance()
            if (calendar.after(currentDate)) {
                selectedDate = formatDate(calendar.time)
                binding.prenotaPostoGiornoEt.setText(selectedDate)
            } else {
                binding.prenotaPostoGiornoEt.setText("")
                showAlert("ATTENZIONE!", "Seleziona una data successiva a quella attuale.")
            }
        }

    }

    private fun bookingCheck(countSeatList: ArrayList<Int>, selectedStartTime: String, selectedEndTime: String): Boolean {
        val start = selectedStartTime.substring(0,2).toInt()
        val end = selectedEndTime.substring(0,2).toInt()
        for (i in start until end){
            if (countSeatList[i-8] >= MAXNUMSEAT){
                return false
            }
        }
        return true
    }

    private fun showTimePickerDialog(startHour: String, startMinute: String, listener: (Int, Int) -> Unit) {
        val dialog = SnapTimePickerDialog.Builder()
            .setTitle(R.string.title)
            .setPositiveButtonText(R.string.positive_button_text)
            .setNegativeButtonText(R.string.negative_button_text)
            .setTitleColor(android.R.color.white)
            .setPreselectedTime(TimeValue(startHour.toInt() + 1, startMinute.toInt()))
            .setSelectableTimeRange(TimeRange(TimeValue(8,0), TimeValue(20,0)))
            .setTimeInterval(60)
            .build()


        dialog.show(parentFragmentManager, "SnapTimePickerDialog")

        dialog.setListener { hour, _ ->
            listener(hour, 0)
        }
    }

    private fun formatTime(hour: Int, minute: Int): String {
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        return timeFormat.format(calendar.time)
    }

    private fun formatDate(date: Date): String {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        return dateFormat.format(date)
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

