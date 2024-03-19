package com.pwm.biblioteca.server

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.pwm.biblioteca.utils.Book
import com.pwm.biblioteca.utils.News
import com.pwm.biblioteca.utils.Notification
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*
import java.sql.Date
import com.pwm.biblioteca.utils.User
import java.time.LocalDate
import java.time.LocalTime

//Definizione di un service retrofit
object ClientNetwork {
    val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8000/webmobile/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(UserAPI::class.java)
    }

    //TOP10 HOME
    fun getTop10(callback: (List<Book>) -> Unit) {

        val bookList = mutableListOf<Book>()

        val query = "SELECT LC.*, AVG(R.valutazione) AS media_valutazione\n" +
                "FROM Recensioni R\n" +
                "JOIN LibriCartacei LC ON R.refLibroCartaceo = LC.idLibroCartaceo\n" +
                "GROUP BY LC.idLibroCartaceo\n" +
                "ORDER BY media_valutazione DESC\n" +
                "LIMIT 10;"

        retrofit.select(query).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val result = response.body()?.get("queryset") as JsonArray
                    if (result.size() != 0){
                        // esiste almeno un libro
                        for (i in 0 until result.size()){
                            val idLibro = result[i].asJsonObject.get("idLibroCartaceo").asInt
                            val titolo = result[i].asJsonObject.get("titolo").asString
                            val autore = result[i].asJsonObject.get("autore").asString
                            val genere = result[i].asJsonObject.get("genere").asString
                            val numCopie = result[i].asJsonObject.get("numCopie").asInt
                            val dataCaricamento = result[i].asJsonObject.get("dataCaricamento").asString
                            val descrizione = result[i].asJsonObject.get("descrizione").asString
                            val copertina = result[i].asJsonObject.get("copertina").asString
                            getCopertina(copertina) { check, image ->
                                if(check){
                                    // Copertine trovate
                                    bookList.add(Book(image, idLibro, titolo, autore, genere, numCopie, dataCaricamento, descrizione))

                                }else{
                                    // Copertine non trovate
                                    bookList.add(Book(null, idLibro, titolo, autore, genere, numCopie, dataCaricamento, descrizione))
                                }

                                if (bookList.size == result.size()) {
                                    // Tutti i libri sono stati aggiunti alla lista
                                    callback(bookList)
                                }

                            }
                        }
                    }else{
                        // non esiste nessun libro
                        callback(bookList)
                    }
                } else {
                    // la risposta non è andata a buon fine
                    callback(bookList)
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                // la richiesta non è andata a buon fine
                callback(bookList)
            }
        })
    }

    //NUOVE USCITE HOME
    fun getNuoveUscite (callback: (List<Book>) -> Unit){

        val bookList = mutableListOf<Book>()

        val query = "SELECT *\n" +
                "FROM LibriCartacei\n" +
                "ORDER BY dataCaricamento DESC\n" +
                "LIMIT 10;\n"

        retrofit.select(query).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val result = response.body()?.get("queryset") as JsonArray
                    if (result.size() != 0){
                        // esiste almeno un libro
                        for (i in 0 until result.size()){
                            val idLibro = result[i].asJsonObject.get("idLibroCartaceo").asInt
                            val titolo = result[i].asJsonObject.get("titolo").asString
                            val autore = result[i].asJsonObject.get("autore").asString
                            val genere = result[i].asJsonObject.get("genere").asString
                            val numCopie = result[i].asJsonObject.get("numCopie").asInt
                            val dataCaricamento = result[i].asJsonObject.get("dataCaricamento").asString
                            val descrizione = result[i].asJsonObject.get("descrizione").asString
                            val copertina = result[i].asJsonObject.get("copertina").asString
                            getCopertina(copertina) { check, image ->
                                if(check){
                                    // Copertine trovate
                                    bookList.add(Book(image, idLibro, titolo, autore, genere, numCopie, dataCaricamento, descrizione))

                                }else{
                                    // Copertine non trovate
                                    bookList.add(Book(null, idLibro, titolo, autore, genere, numCopie, dataCaricamento, descrizione))
                                }

                                if (bookList.size == result.size()) {
                                    // Tutti i libri sono stati aggiunti alla lista
                                    callback(bookList)
                                }

                            }
                        }
                    }else{
                        // non esiste nessun libro
                        callback(bookList)
                    }
                } else {
                    // la risposta non è andata a buon fine
                    callback(bookList)
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                // la richiesta non è andata a buon fine
                callback(bookList)
            }
        })



    }


    private fun getCopertina(url: String, callback: (Boolean, Bitmap?) -> Unit){

        var image: Bitmap? = null

        retrofit.file(url).enqueue(
            object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if(response.isSuccessful) {

                        if (response.body()!=null) {
                            image = BitmapFactory.decodeStream(response.body()?.byteStream())
                            callback(true, image)
                        }
                    }else{
                        callback(false, image)
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    callback(false, image)
                }

            }
        )
    }

    //LibriCartacei filtrati per genere
    fun getPaperBooks (genere: String ="Avventura", callback: (List<Book>) -> Unit){

        val bookList = mutableListOf<Book>()

        val query = "SELECT *\n" +
                "FROM LibriCartacei\n" +
                "WHERE genere = '${genere}';"

        retrofit.select(query).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val result = response.body()?.get("queryset") as JsonArray
                    if (result.size() != 0){
                        // esiste almeno un libro
                        for (i in 0 until result.size()){
                            val idLibro = result[i].asJsonObject.get("idLibroCartaceo").asInt
                            val titolo = result[i].asJsonObject.get("titolo").asString
                            val autore = result[i].asJsonObject.get("autore").asString
                            val genere = result[i].asJsonObject.get("genere").asString
                            val numCopie = result[i].asJsonObject.get("numCopie").asInt
                            val dataCaricamento = result[i].asJsonObject.get("dataCaricamento").asString
                            val descrizione = result[i].asJsonObject.get("descrizione").asString
                            val copertina = result[i].asJsonObject.get("copertina").asString
                            getCopertina(copertina) { check, image ->
                                if(check){
                                    // Copertine trovate
                                    bookList.add(Book(image, idLibro, titolo, autore, genere, numCopie, dataCaricamento, descrizione))

                                }else{
                                    // Copertine non trovate
                                    bookList.add(Book(null, idLibro, titolo, autore, genere, numCopie, dataCaricamento, descrizione))
                                }

                                if (bookList.size == result.size()) {
                                    // Tutti i libri sono stati aggiunti alla lista
                                    callback(bookList)
                                }

                            }
                        }
                    }else{
                        // non esiste nessun libro
                        callback(bookList)
                    }
                } else {
                    // la risposta non è andata a buon fine
                    callback(bookList)
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                // la richiesta non è andata a buon fine
                callback(bookList)
            }
        })

    }

    fun getLoanBooks (id: Int, inCorso: Boolean, callback: (List<Book>, Boolean) -> Unit){
        val currentDate = LocalDate.now()
        val bookList = mutableListOf<Book>()
        var giorniRestituzione = -1
        lateinit var query: String

        if(inCorso) {
            query = "select l.*, DATEDIFF(p.dataFinePrestito, '${currentDate}') AS daysDifference\n" +
                    "from Prestiti p, Utenti u, LibriCartacei l\n" +
                    "where p.refLibroCartaceo = l.idLibroCartaceo && p.refUtente = u.idUtente && u.idUtente = ${id} && '${currentDate}' <= p.dataFinePrestito;"
        }else{
            query = "select * \n" +
                    "from LibriCartacei l1\n" +
                    "where l1.idLibroCartaceo in (select p.refLibroCartaceo\n" +
                    "                           from Prestiti p, Utenti u\n" +
                    "                           where p.refUtente = u.idUtente && u.idUtente = ${id} && '${currentDate}' > p.dataFinePrestito);"
        }
        retrofit.select(query).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val result = response.body()?.get("queryset") as JsonArray
                    if (result.size() != 0){
                        // esiste almeno un libro
                        for (i in 0 until result.size()){
                            val idLibro = result[i].asJsonObject.get("idLibroCartaceo").asInt
                            val titolo = result[i].asJsonObject.get("titolo").asString
                            val autore = result[i].asJsonObject.get("autore").asString
                            val genere = result[i].asJsonObject.get("genere").asString
                            val numCopie = result[i].asJsonObject.get("numCopie").asInt
                            val dataCaricamento = result[i].asJsonObject.get("dataCaricamento").asString
                            val descrizione = result[i].asJsonObject.get("descrizione").asString
                            val copertina = result[i].asJsonObject.get("copertina").asString
                            getCopertina(copertina) { check, image ->
                                if(check){
                                    // Copertine trovate
                                    if(inCorso){
                                        giorniRestituzione = result[i].asJsonObject.get("daysDifference").asInt

                                    }else{
                                        giorniRestituzione = -1
                                    }
                                    bookList.add(Book(image, idLibro, titolo, autore, genere, numCopie, dataCaricamento, descrizione, giorniRestituzione))
                                }else{
                                    // Copertine non trovate
                                    bookList.add(Book(null, idLibro, titolo, autore, genere, numCopie, dataCaricamento, descrizione, giorniRestituzione))
                                }

                                if (bookList.size == result.size()) {
                                    // Tutti i libri sono stati aggiunti alla lista
                                    callback(bookList, true)
                                }

                            }
                        }
                    }else{
                        // non esiste nessun libro
                        callback(bookList, true)
                    }
                } else {
                    // la risposta non è andata a buon fine
                    callback(bookList, false)
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                // la richiesta non è andata a buon fine
                callback(bookList, false)
            }
        })

    }


    fun getFavouriteBooks (id:Int, callback: (List<Book>) -> Unit){

        val favList = mutableListOf<Book>()

        val query = "SELECT *\n" +
                "FROM LibriCartacei\n" +
                "WHERE idLibroCartaceo in (SELECT refLibroCartaceo\n" +
                                        "FROM Preferiti\n" +
                                        "WHERE refUtente = '${id}');"

        retrofit.select(query).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val result = response.body()?.get("queryset") as JsonArray
                    if (result.size() != 0){
                        // esiste almeno un libro
                        for (i in 0 until result.size()){
                            val idLibro = result[i].asJsonObject.get("idLibroCartaceo").asInt
                            val titolo = result[i].asJsonObject.get("titolo").asString
                            val autore = result[i].asJsonObject.get("autore").asString
                            val genere = result[i].asJsonObject.get("genere").asString
                            val numCopie = result[i].asJsonObject.get("numCopie").asInt
                            val dataCaricamento = result[i].asJsonObject.get("dataCaricamento").asString
                            val descrizione = result[i].asJsonObject.get("descrizione").asString
                            val copertina = result[i].asJsonObject.get("copertina").asString
                            getCopertina(copertina) { check, image ->
                                if(check){
                                    // Copertine trovate
                                    favList.add(Book(image, idLibro, titolo, autore, genere, numCopie, dataCaricamento, descrizione))

                                }else{
                                    // Copertine non trovate
                                    favList.add(Book(null, idLibro, titolo, autore, genere, numCopie, dataCaricamento, descrizione))
                                }

                                if (favList.size == result.size()) {
                                    // Tutti i libri sono stati aggiunti alla lista
                                    callback(favList)
                                }

                            }
                        }
                    }else{
                        // non esiste nessun libro
                        callback(favList)
                    }
                } else {
                    // la risposta non è andata a buon fine
                    callback(favList)
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                // la richiesta non è andata a buon fine
                callback(favList)
            }
        })

    }

    //AudioLibri
    fun getAudioBooks(callback: (List<Book>) -> Unit){

        val bookList = mutableListOf<Book>()

        val query = "SELECT *\n" +
                "FROM AudioLibri;"

        retrofit.select(query).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val result = response.body()?.get("queryset") as JsonArray
                    if (result.size() != 0){
                        // esiste almeno un libro
                        for (i in 0 until result.size()){
                            val idLibro = result[i].asJsonObject.get("idAudioLibro").asInt
                            val titolo = result[i].asJsonObject.get("titolo").asString
                            val autore = result[i].asJsonObject.get("autore").asString
                            val copertina = result[i].asJsonObject.get("copertina").asString
                            getCopertina(copertina) { check, image ->
                                if(check){
                                    // Copertine trovate
                                    bookList.add(Book(image, idLibro, titolo, autore, null, 0, null, null))

                                }else{
                                    // Copertine non trovate
                                    bookList.add(Book(null, idLibro, titolo, autore, null, 0, null, null))
                                }

                                if (bookList.size == result.size()) {
                                    // Tutti i libri sono stati aggiunti alla lista
                                    callback(bookList)
                                }

                            }
                        }
                    }else{
                        // non esiste nessun libro
                        callback(bookList)
                    }
                } else {
                    // la risposta non è andata a buon fine
                    callback(bookList)
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                // la richiesta non è andata a buon fine
                callback(bookList)
            }
        })

    }

    //LibriDigitali
    fun getDigitalBooks(callback: (List<Book>) -> Unit){

        val bookList = mutableListOf<Book>()

        val query = "SELECT *\n" +
                "FROM LibriDigitali;"

        retrofit.select(query).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val result = response.body()?.get("queryset") as JsonArray
                    if (result.size() != 0){
                        // esiste almeno un libro
                        for (i in 0 until result.size()){
                            val idLibro = result[i].asJsonObject.get("idLibroDigitale").asInt
                            val titolo = result[i].asJsonObject.get("titolo").asString
                            val autore = result[i].asJsonObject.get("autore").asString
                            val copertina = result[i].asJsonObject.get("copertina").asString
                            getCopertina(copertina) { check, image ->
                                if(check){
                                    // Copertine trovate
                                    bookList.add(Book(image, idLibro, titolo, autore, null, 0, null, null))

                                }else{
                                    // Copertine non trovate
                                    bookList.add(Book(null, idLibro, titolo, autore, null, 0, null, null))
                                }

                                if (bookList.size == result.size()) {
                                    // Tutti i libri sono stati aggiunti alla lista
                                    callback(bookList)
                                }

                            }
                        }
                    }else{
                        // non esiste nessun libro
                        callback(bookList)
                    }
                } else {
                    // la risposta non è andata a buon fine
                    callback(bookList)
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                // la richiesta non è andata a buon fine
                callback(bookList)
            }
        })

    }

    //ValutazioneMedia di un libro
    fun getValutazioneMedia(book: Book, callback: (String?) -> Unit) {
        val idLibro = book.id
        val query = "SELECT AVG(R.valutazione) AS media_valutazione\n" +
                "FROM Recensioni R\n" +
                "JOIN LibriCartacei LC ON R.refLibroCartaceo = LC.idLibroCartaceo\n" +
                "WHERE LC.idLibroCartaceo = '${idLibro}'\n" +
                "GROUP BY LC.idLibroCartaceo;"

        retrofit.select(query).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val result = response.body()?.get("queryset") as JsonArray
                    if (result.size() == 1){
                        val media = result[0].asJsonObject.get("media_valutazione").asString
                        callback(media)
                    }else{
                        callback(null)
                    }


                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                callback(null)
            }
        })
    }

    //ottieni le notifiche
    fun getNotifications(id: Int, callback: (List<Notification>) -> Unit){
        val notificationList = mutableListOf<Notification>()

        val query = "SELECT *\n" +
                "FROM Notifiche\n" +
                "WHERE refUtente = '${id}'\n" +
                "ORDER BY idNotifica DESC;"

        retrofit.select(query).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val result = response.body()?.get("queryset") as JsonArray
                    if (result.size() != 0){
                        for (i in 0 until result.size()){
                            val idNotifica = result[i].asJsonObject.get("idNotifica").asInt
                            val refUtente = result[i].asJsonObject.get("refUtente").asInt
                            val testo = result[i].asJsonObject.get("testo").asString
                            notificationList.add(Notification(idNotifica, refUtente, testo))
                        }
                        callback(notificationList)
                    }else{
                        callback(notificationList)
                    }
                } else {
                    // la risposta non è andata a buon fine
                    callback(notificationList)
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                // la richiesta non è andata a buon fine
                callback(notificationList)
            }
        })

    }


    fun cercaPDF(idLibro: Int, callback: (ByteArray?) -> Unit) {

        val query = "select URL from LibriDigitali where idLibroDigitale = '${idLibro}';"

        retrofit.select(query).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val result = response.body()?.get("queryset") as JsonArray
                    if (result.size() != 0){
                        val url = result[0].asJsonObject.get("URL").asString
                        getPDF(url){pdfByteArray ->
                            callback(pdfByteArray)
                        }
                    }else{
                        callback(null)
                    }


                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                callback(null)
            }
        })

    }

    private fun getPDF(url: String, callback: (ByteArray?) -> Unit){

        var pdfByteArray: ByteArray?

        retrofit.file(url).enqueue(
            object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if(response.isSuccessful) {

                        pdfByteArray = response.body()?.bytes()
                        if (pdfByteArray != null) {
                            // Il PDF è stato ottenuto come ByteArray
                            callback(pdfByteArray)

                        }else{
                            //il PDF non esiste
                            callback(pdfByteArray)
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {

                    callback(null)
                }

            }
        )

    }

    fun cercaAUDIO(idLibro: Int, callback: (ByteArray?) -> Unit) {

        val query = "select URL from AudioLibri where idAudioLibro = '${idLibro}';"

        retrofit.select(query).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val result = response.body()?.get("queryset") as JsonArray
                    if (result.size() == 1){
                        val url = result[0].asJsonObject.get("URL").asString
                        getAUDIO(url){audioByteArray ->
                            callback(audioByteArray)
                        }
                    }else{
                        callback(null)
                    }


                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                callback(null)
            }
        })

    }

    fun getAUDIO(url: String, callback: (ByteArray?) -> Unit){

        var audioByteArray: ByteArray?

        retrofit.file(url).enqueue(
            object : Callback<ResponseBody> {
                override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                    if(response.isSuccessful) {

                        audioByteArray = response.body()?.bytes()

                        if (audioByteArray != null) {
                            callback(audioByteArray)

                        }else{
                            //non esiste audio relativo al libro cercato
                            callback(audioByteArray)
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    /*
                    * gestisci qui il fallimento della richiesta
                    */
                    callback(null)
                }

            }
        )
    }

    fun getUser(username:String, password:String, callback: (Boolean?) -> Unit){
        val user = username.replace("'", "''")
        val pass = password.replace("'", "''")
        val query = "SELECT * FROM Utenti WHERE username = '${user}' && password = '${pass}'"

        retrofit.select(query).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val result = response.body()?.get("queryset") as JsonArray
                    if (result.size() == 1){
                        val idUtente = result[0].asJsonObject.get("idUtente").asInt
                        val nome = result[0].asJsonObject.get("nome").asString
                        val cognome = result[0].asJsonObject.get("cognome").asString
                        val sesso = result[0].asJsonObject.get("sesso").asString
                        val indirizzo = result[0].asJsonObject.get("indirizzo").asString
                        val eta = result[0].asJsonObject.get("eta").asInt
                        val email = result[0].asJsonObject.get("email").asString
                        val cellulare = result[0].asJsonObject.get("cellulare").asString
                        User.setUser(idUtente, nome, cognome, sesso, indirizzo, eta, email, cellulare, username, password)
                        callback(true)
                    }else {
                        callback(false)
                    }
                } else {
                    callback(null)
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                callback(null)
            }
        })
    }

    fun insertUser(nome: String, cognome: String, sesso: String, indirizzo: String, eta: Int, email: String,
                   cellulare: String, username: String, password:String, callback: (Boolean?) -> Unit){
        val name = nome.replace("'", "''")
        val surname = cognome.replace("'", "''")
        val gender = sesso.replace("'", "''")
        val address = indirizzo.replace("'", "''")
        val mail = email.replace("'", "''")
        val number = cellulare.replace("'", "''")
        val user = username.replace("'", "''")
        val pass = password.replace("'", "''")

        val query = "INSERT INTO `Utenti` (`nome`, `cognome`, `sesso`, `indirizzo`, `eta`, `cellulare`, `email`, `username`, `password`) " +
                "VALUES ('${name}', '${surname}', '${gender}', '${address}', '${eta}', '${number}', '${mail}', '${user}', '${pass}');\n"

        retrofit.insert(query).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    callback(true)
                } else {
                    callback(false)
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                callback(false)
            }
        })

    }

    fun updateUser(id: Int, nome: String, cognome: String, sesso: String, indirizzo: String,
                   eta: Int, email: String, cellulare: String, callback: (Boolean?) -> Unit){
        val name = nome.replace("'", "''")
        val surname = cognome.replace("'", "''")
        val gender = sesso.replace("'", "''")
        val address = indirizzo.replace("'", "''")
        val mail = email.replace("'", "''")
        val number = cellulare.replace("'", "''")
        val query ="            " +
                "UPDATE `Utenti` \n" +
                "SET \n" +
                "    `nome` = '${name}',\n" +
                "    `cognome` = '${surname}',\n" +
                "    `sesso` = '${gender}',\n" +
                "    `indirizzo` = '${address}',\n" +
                "    `eta` = '${eta}',\n" +
                "    `cellulare` = '${number}',\n" +
                "    `email` = '${mail}'\n" +
                "WHERE\n" +
                "    (`idUtente` = '${id}');\n"


        retrofit.update(query).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    callback(true)
                } else {
                    callback(false)
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                callback(false)
            }
        })

    }

    fun updateCredentials(id:Int, username: String, password: String, callback: (Boolean?) -> Unit){
        val user = username.replace("'", "''")
        val pass = password.replace("'", "''")
        val query ="            " +
                "UPDATE `Utenti` \n" +
                "SET \n" +
                "    `username` = '${user}',\n" +
                "    `password` = '${pass}'\n" +
                "WHERE\n" +
                "    (`idUtente` = '${id}');\n"


        retrofit.update(query).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    callback(true)
                } else {
                    callback(false)
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                callback(false)
            }
        })
    }
    fun getNews(callback: (List<News>) -> Unit) {

        val newsList = mutableListOf<News>()

        val query = "SELECT *\n" +
                    "FROM News;"

        retrofit.select(query).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val result = response.body()?.get("queryset") as JsonArray
                    if (result.size() != 0){
                        // esiste almeno una news
                        for (i in 0 until result.size()){
                            val idNews = result[i].asJsonObject.get("idNews").asInt
                            val titolo = result[i].asJsonObject.get("titolo").asString
                            val dataPubblicazione = result[i].asJsonObject.get("dataPubblicazione").asString
                            val descrizione = result[i].asJsonObject.get("descrizione").asString
                            val copertina = result[i].asJsonObject.get("immagine").asString
                            getCopertina(copertina) { check, image ->
                                if(check){
                                    // Copertine trovate
                                    newsList.add(News(idNews, image, descrizione, dataPubblicazione, titolo))

                                }else{
                                    // Copertine non trovate
                                    newsList.add(News(idNews, null, descrizione, dataPubblicazione, titolo))
                                }

                                if (newsList.size == result.size()) {
                                    // Tutti le news sono state aggiunte alla lista
                                    callback(newsList)
                                }

                            }
                        }
                    }else{
                        // non esiste nessuna news
                        callback(newsList)
                    }
                } else {
                    // la risposta non è andata a buon fine
                    callback(newsList)
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                // la richiesta non è andata a buon fine
                callback(newsList)
            }
        })
    }

    fun insertRating(idUtente: Int, idLibro: Int, rating: Float, callback: (Boolean?) -> Unit){

        val query = "INSERT INTO `Recensioni` (`refUtente`, `refLibroCartaceo`, `valutazione`) " +
                    "VALUES ('${idUtente}', '${idLibro}', '${rating}');"

        retrofit.insert(query).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    callback(true)
                } else {
                    callback(false)
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                callback(false)
            }
        })
    }

    fun updateRating(idUtente: Int, idLibro: Int, rating: Float, callback: (Boolean?) -> Unit){

        val query ="UPDATE `Recensioni` \n" +
                   "SET `valutazione` = '${rating}'\n" +
                   "WHERE\n" +
                   "(`refUtente` = '${idUtente}') and (`refLibroCartaceo` = '${idLibro}');"


        retrofit.update(query).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    callback(true)
                } else {
                    callback(false)
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                callback(false)
            }
        })

    }

    fun getRating(idUtente: Int, idLibro: Int, callback: (Float?) -> Unit) {

        val query = "SELECT `valutazione` FROM `Recensioni` WHERE `refUtente` = '$idUtente' AND `refLibroCartaceo` = '$idLibro';"

        retrofit.select(query).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val result = response.body()?.asJsonObject?.get("queryset")?.asJsonArray
                    if (result != null && result.size() > 0) {
                        val rating = result[0].asJsonObject.get("valutazione").asFloat
                        callback(rating)
                    } else {
                        callback(-1.0f) // Nessuna recensione trovata
                    }
                } else {
                    callback(null) // Errore nella risposta del server
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                callback(null) // Richiesta fallita
            }
        })
    }

    fun getInfoFavourite(idUtente: Int, idLibro: Int, callback: (Int?) -> Unit) {

        val query = "SELECT * FROM `Preferiti` WHERE `refUtente` = '$idUtente' AND `refLibroCartaceo` = '$idLibro';"

        retrofit.select(query).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val result = response.body()?.asJsonObject?.get("queryset")?.asJsonArray
                    if (result != null && result.size() > 0) {
                        callback(1)
                    } else {
                        callback(2) // Il libro non è tra i preferiti dell'utente
                    }
                } else {
                    callback(-1) // Errore nella risposta del server
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                callback(-1) // Richiesta fallita
            }
        })
    }

    fun insertFavouriteBook(idUtente: Int, idLibro: Int, callback: (Boolean?) -> Unit) {

        val query = "INSERT INTO `Preferiti` (`refUtente`, `refLibroCartaceo`) " +
                    "VALUES ('${idUtente}', '${idLibro}');"

        retrofit.insert(query).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    callback(true)
                } else {
                    callback(false)
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                callback(false)
            }
        })

    }

    fun deleteFavouriteBook(idUtente: Int, idLibro: Int, callback: (Boolean?) -> Unit) {

        val query = "DELETE FROM Preferiti\n" +
                    "WHERE refUtente = '${idUtente}' AND refLibroCartaceo = '${idLibro}';\n"

        retrofit.remove(query).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    callback(true)
                } else {
                    callback(false)
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                callback(false)
            }
        })

    }

    fun insertLoan(idUtente: Int, idLibro: Int, callback: (Boolean) -> Unit){
        val query = "INSERT INTO `Prestiti` (`refUtente`, `refLibroCartaceo`, `dataInizioPrestito`, `dataFinePrestito`) VALUES ('${idUtente}', '${idLibro}', '${LocalDate.now()}', '${LocalDate.now().plusMonths(1)}');"

        retrofit.insert(query).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    callback(true)
                } else {
                    callback(false)
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                callback(false)
            }
        })
    }


    fun insertWaitingNotification(idUtente: Int, idLibro: Int, callback: (Boolean?) -> Unit) {
        val query =
            "INSERT INTO `AttesaDisponibilita` (`refUtente`, `refLibroCartaceo`) VALUES ('${idUtente}', '${idLibro}');"

        retrofit.insert(query).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    callback(true)
                } else {
                    callback(false)
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                callback(false)
            }
        })
    }

    fun checkWaitingNotifications(idUtente: Int, idLibro: Int, callback: (Int) -> Unit){

        val query = "SELECT *\n" +
                "FROM AttesaDisponibilita \n" +
                "WHERE refUtente = '${idUtente}' AND refLibroCartaceo = '${idLibro}';"

        retrofit.select(query).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val result = response.body()?.get("queryset") as JsonArray
                    if (result.size() != 0){
                        // l'utente è in attesa che il libro torni disponibile
                        callback(0)
                    }else{
                        // l'utente NON è in attesa che il libro torni disponibile
                        callback(1)
                    }
                } else {
                    // la risposta non è andata a buon fine
                    callback(-1)
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                // la richiesta non è andata a buon fine
                callback(-1)
            }
        })

    }

    fun updateBookCopies(id:Int, copie:Int, callback: (Boolean) -> Unit){
        val query = "UPDATE `LibriCartacei` SET `numCopie`= '${copie}' WHERE (`idLibroCartaceo` = '${id}');\n"

        retrofit.update(query).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                     callback(true)
                } else {
                     callback(false)
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                 callback(false)
            }
        })
    }

    fun insertNotification(idUtente: Int, testo:String, callback: (Boolean) -> Unit){
        val text = testo.replace("'", "''")
        val query = "INSERT INTO `Notifiche` (`refUtente`, `testo`) VALUES ('${idUtente}', '${text}');"

        retrofit.insert(query).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    callback(true)
                } else {
                    callback(false)
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                callback(false)
            }
        })
    }


    fun getInfoSeat(date: Date, callback: (ArrayList<Int>) -> Unit) {
        val countSeatList = ArrayList<Int>()

        queryNext(8, date, countSeatList, callback)
    }

    private fun queryNext(index: Int, date: Date, countSeatList: ArrayList<Int>, callback: (ArrayList<Int>) -> Unit) {
        val query = "SELECT count(oraInizio) as count\n" +
                "FROM PrenotazioniPosto\n" +
                "WHERE giorno = '${date}' AND oraInizio <= '${index}:00:00' AND oraFine > '${index}:00:00';"

        retrofit.select(query).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val result = response.body()?.asJsonObject?.get("queryset")?.asJsonArray
                    if (result != null && result.size() > 0) {
                        countSeatList.add(result[0].asJsonObject.get("count").asInt)
                    } else {
                        countSeatList.add(0)
                    }
                }
                if (index < 19) {
                    queryNext(index + 1, date, countSeatList, callback)
                } else {
                    callback(countSeatList)
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                if (index < 19) {
                    queryNext(index + 1, date, countSeatList, callback)
                } else {
                    callback(countSeatList)
                }
            }
        })
    }

    fun insertBookingSeat(idUtente: Int, date: Date, startTime: LocalTime?, endTime: LocalTime?, callback: (Boolean?) -> Unit) {

        val query = "INSERT INTO `PrenotazioniPosto` (`refUtente`, `giorno`, `oraInizio`, `oraFine`) " +
                "VALUES ('${idUtente}', '${date}', '${startTime}', '${endTime}');"

        retrofit.insert(query).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    callback(true)
                } else {
                    callback(false)
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                callback(false)
            }
        })
    }

    fun bookingCheckDate(idUtente: Int, date: Date, callback: (Boolean?) -> Unit) {

        val query = "SELECT * FROM `PrenotazioniPosto` WHERE `refUtente` = '$idUtente' AND `giorno` = '$date';"

        retrofit.select(query).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val result = response.body()?.asJsonObject?.get("queryset")?.asJsonArray
                    if (result != null && result.size() > 0) {
                        callback(false)
                    } else {
                        callback(true)
                    }
                } else {
                    callback(false) // Errore nella risposta del server
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                callback(false) // Richiesta fallita
            }
        })

    }

    fun insertDonateBook(idUtente: Int, titolo: String, autore: String, stato: String, descrizione: String, callback: (Boolean?) -> Unit) {
        val title = titolo.replace("'", "''")
        val author = autore.replace("'", "''")
        val condition = stato.replace("'", "''")
        val description = descrizione.replace("'", "''")
        val query = "INSERT INTO `RichiesteDonazioni` (`refUtente`, `titolo`, `autore`, `stato`, `descrizione`) " +
                    "VALUES ('${idUtente}', '${title}', '${author}', '${condition}', '${description}');"

        retrofit.insert(query).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    callback(true)
                } else {
                    callback(false)
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                callback(false)
            }
        })
    }

    fun insertFeedback(idUtente: Int, feedback: String, callback: (Boolean?) -> Unit) {
        val f = feedback.replace("'", "''")
        val query = "INSERT INTO `Feedback` (`refUtente`, `testo`) " +
                    "VALUES ('${idUtente}', '${f}');"

        retrofit.insert(query).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    callback(true)
                } else {
                    callback(false)
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                callback(false)
            }
        })
    }

    fun updateDaysLoan(idUtente: Int, idLibro: Int, giorni: Int, callback: (Boolean) -> Unit) {

        val date = LocalDate.now().plusDays(giorni.toLong() + 15)
        val query = "UPDATE `Prestiti` SET `dataFinePrestito`= '${date}' WHERE (`refUtente` = '${idUtente}' AND `refLibroCartaceo` = '${idLibro}');\n"

        retrofit.update(query).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    callback(true)
                } else {
                    callback(false)
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                callback(false)
            }
        })
    }

    fun updateDatesLoan(idUtente: Int, idLibro: Int, callback: (Boolean) -> Unit) {

        val query = "UPDATE `Prestiti` SET `dataInizioPrestito`= '${LocalDate.now()}', `dataFinePrestito`= '${LocalDate.now().plusMonths(1)}'  WHERE (`refUtente` = '${idUtente}' AND `refLibroCartaceo` = '${idLibro}');\n"

        retrofit.update(query).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    callback(true)
                } else {
                    callback(false)
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                callback(false)
            }
        })
    }

    fun getBookBackAvailable(idUtente: Int, callback: (List<Book>, Boolean) -> Unit) {

        val bookList = mutableListOf<Book>()
        val query = "SELECT idLibroCartaceo, titolo\n" +
                    "FROM AttesaDisponibilita , LibriCartacei \n" +
                    "WHERE refLibroCartaceo=idLibroCartaceo AND refUtente=${idUtente} AND numCopie>0;"

        retrofit.select(query).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    val result = response.body()?.get("queryset") as JsonArray
                    if (result.size() != 0){
                        // esiste almeno un libro
                        for (i in 0 until result.size()){
                            val idLibro = result[i].asJsonObject.get("idLibroCartaceo").asInt
                            val titolo = result[i].asJsonObject.get("titolo").asString
                            bookList.add(Book(null,idLibro,titolo,null,null,0,null,null,0))
                            if (bookList.size == result.size()) {
                                // Tutti i libri sono stati aggiunti alla lista
                                callback(bookList, true)
                            }
                        }
                    }else{
                        // non esiste nessun libro
                        callback(bookList, true)
                    }
                } else {
                    // la risposta non è andata a buon fine
                    callback(bookList, false)
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                // la richiesta non è andata a buon fine
                callback(bookList, false)
            }
        })
    }

    fun removeUser(idUtente: Int, callback: (Boolean) -> Unit) {

        val query = "DELETE FROM AttesaDisponibilita WHERE refUtente = ${idUtente};\n" +
                    "DELETE FROM Feedback WHERE refUtente = ${idUtente};\n" +
                    "DELETE FROM Notifiche WHERE refUtente = ${idUtente};\n" +
                    "DELETE FROM Preferiti WHERE refUtente = ${idUtente};\n" +
                    "DELETE FROM PrenotazioniPosto WHERE refUtente = ${idUtente};\n" +
                    "DELETE FROM Prestiti WHERE refUtente = ${idUtente};\n" +
                    "DELETE FROM Recensioni WHERE refUtente = ${idUtente};\n" +
                    "DELETE FROM RichiesteDonazioni WHERE refUtente = ${idUtente};\n" +
                    "DELETE FROM Utenti WHERE idUtente = ${idUtente};"


        retrofit.remove(query).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    callback(true)
                } else {
                    callback(false)
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                callback(false)
            }
        })
    }

    fun removeBookBackAvailable(idUtente: Int, callback: (Boolean) -> Unit) {

        val query = "DELETE A1\n" +
                    "FROM AttesaDisponibilita A1\n" +
                    "JOIN LibriCartacei l ON A1.refLibroCartaceo = l.idLibroCartaceo\n" +
                    "WHERE A1.refUtente = ${idUtente} AND l.numCopie > 0;"

        retrofit.remove(query).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                if (response.isSuccessful) {
                    callback(true)
                } else {
                    callback(false)
                }
            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                callback(false)
            }
        })
    }

}