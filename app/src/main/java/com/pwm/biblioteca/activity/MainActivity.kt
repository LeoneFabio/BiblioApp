package com.pwm.biblioteca.activity


import android.annotation.SuppressLint
import android.graphics.Rect
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.pwm.biblioteca.databinding.ActivityMainBinding
import com.etebarian.meowbottomnavigation.MeowBottomNavigation
import androidx.fragment.app.FragmentManager
import com.pwm.biblioteca.R
import com.pwm.biblioteca.fragment.*
import com.pwm.biblioteca.server.ClientNetwork
import com.pwm.biblioteca.utils.User
import com.pwm.biblioteca.utils.UserSessionManager
import java.time.LocalDate


class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var bottomNavigation: MeowBottomNavigation
    private lateinit var userSessionManager: UserSessionManager




    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        userSessionManager = UserSessionManager(this)
        when (userSessionManager.isDarkModeEnabled()) {
            false -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            true -> AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        }



        // Verifica se l'utente ha già effettuato l'accesso
        if (userSessionManager.isUserLoggedIn()) {
            val username = userSessionManager.getUsername().toString()
            val password = userSessionManager.getPassword().toString()

            ClientNetwork.getUser(username, password){ found ->
                if (found != true){
                    Toast.makeText(this, "Caricamento utente loggato non andato a buon fine", Toast.LENGTH_SHORT).show()
                }else{
                    bookReturnNotification() // controlla se ci sono notifiche per libri da restituire
                    bookBackAvailable() // controlla se ci sono libri tornati disponibili
                }
            }
        }

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)


        setSupportActionBar(binding.toolbar)

        if (savedInstanceState == null) {
            // Sostituisci il contenuto del contenitore con l'HomeFragment
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HomeFragment())
                .commit()
        }



        bottomNavigation = binding.bottomNavigation
        bottomNavigation.show(2, true)
        bottomNavigation.add(MeowBottomNavigation.Model(1, R.drawable.baseline_menu_book_24))
        bottomNavigation.add(MeowBottomNavigation.Model(2, R.drawable.baseline_home_24))
        bottomNavigation.add(MeowBottomNavigation.Model(3, R.drawable.baseline_favorite_24))



        //Navigazione principale
        bottomNavigation.setOnClickMenuListener { model ->

            when (model.id) {
                1 -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, BookshopFragment())
                        .commit()
                    //Chiude il menu o il profilo se esiste
                    val tag1 = "fragmentMenu"
                    val tag2 = "fragmentProfile"
                    closeFragment(tag1)
                    closeFragment(tag2)
                }
                2 -> {
                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, HomeFragment())
                        .commit()
                    //Chiude il menu o il profilo se esiste
                    val tag1 = "fragmentMenu"
                    val tag2 = "fragmentProfile"
                    closeFragment(tag1)
                    closeFragment(tag2)
                }
                3 -> {
                    if(User.id != 0) {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.fragment_container, FavouritesFragment())
                            .commit()
                        //Chiude il menu o il profilo se esiste
                        val tag1 = "fragmentMenu"
                        val tag2 = "fragmentProfile"
                        closeFragment(tag1)
                        closeFragment(tag2)
                    }else{
                        AlertDialog.Builder(this)
                            .setTitle("Accesso Negato")
                            .setMessage("Per accede alla funzionalità richiesta effettua il Login")
                            .setPositiveButton("OK"){dialog,_ ->
                                dialog.dismiss()
                                when (getCurrentFragment()) {
                                    is BookshopFragment -> bottomNavigation.show(1, true)
                                    is HomeFragment -> bottomNavigation.show(2, true)
                                    else ->{ bottomNavigation.show(-1, false)
                                             bottomNavigation.clearAllCounts()
                                    }
                                }
                            }.show()
                    }
                }
            }
        }

        //Menu
        binding.ivMenu.setOnClickListener {
            val menuFragment = MenuFragment()
            val tag = "fragmentMenu"
            val tag1 = "fragmentProfile"
            if (!fragmentExists(supportFragmentManager, tag)) {
                //Chiude il fragment Profilo se esiste
                closeFragment(tag1)
                // Apre il fragment Menu
                binding.fragmentContainerMenu.visibility = View.VISIBLE

                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_menu, menuFragment, tag)
                    .commit()
            } else {
                // Chiude il fragment Menu
                closeFragment(tag)
            }
        }


        //Notifiche
        binding.ivNotifiche.setOnClickListener{
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.fragment_container,NotificationFragment())
                .commit()
            bottomNavigation.show(-1, false)
            bottomNavigation.clearAllCounts()
        }


        //Chiudi Menu o Profilo con un click in un punto qualsiasi sullo schermo
        binding.rlActivityMain.setOnTouchListener { _, event ->
            if (event.action == MotionEvent.ACTION_UP) {
                val touchX = event.x.toInt()
                val touchY = event.y.toInt()

                // Verifica se il tocco è all'interno del fragment menu
                if (binding.fragmentContainerMenu.visibility == View.VISIBLE) {
                    val fragmentRect = Rect()
                    binding.fragmentContainerMenu.getGlobalVisibleRect(fragmentRect)

                    // Verifica se il tocco è all'esterno del fragment menu
                    if (!fragmentRect.contains(touchX, touchY)) {
                        // Chiudi il menu
                        val tag = "fragmentMenu"
                        closeFragment(tag)
                    }
                }

                // Verifica se il tocco è all'interno del fragment profilo
                if (binding.fragmentContainerProfilo.visibility == View.VISIBLE) {
                    val fragmentRect = Rect()
                    binding.fragmentContainerProfilo.getGlobalVisibleRect(fragmentRect)

                    // Verifica se il tocco è all'esterno del fragment profilo
                    if (!fragmentRect.contains(touchX, touchY)) {
                        // Chiudi il fragment profilo
                        val tag = "fragmentProfile"
                        closeFragment(tag)
                    }
                }
            }
            true
        }


        //LoginRegistrazione
        binding.ivAccount.setOnClickListener {
            if(User.id == 0) {
                val tag = "fragmentProfile"
                val tag1 = "fragmentMenu"
                if (!fragmentExists(supportFragmentManager, tag)) {
                    //Chiude il fragment Menu se esiste
                    closeFragment(tag1)

                    // Apre il fragment Profilo
                    binding.fragmentContainerProfilo.visibility = View.VISIBLE

                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container_profilo, LoginSignUpFragment(), tag)
                        .commit()
                } else {
                    // Chiude il fragment Profilo
                    closeFragment(tag)
                }
            }else{
                val tag = "fragmentProfile"
                val tag1 = "fragmentMenu"
                if (!fragmentExists(supportFragmentManager, tag)) {
                    //Chiude il fragment Menu se esiste
                    closeFragment(tag1)

                    // Apre il fragment Profilo
                    binding.fragmentContainerProfilo.visibility = View.VISIBLE

                    supportFragmentManager.beginTransaction()
                        .replace(R.id.fragment_container_profilo, LogoutAccountFragment(), tag)
                        .commit()
                } else {
                    // Chiude il fragment Profilo
                    closeFragment(tag)
                }
            }
        }
    }

    override fun onBackPressed() {
        val tag1 = "fragmentMenu"
        val tag2 = "fragmentProfile"

        //Chiudi il menu se è aperto
        if (fragmentExists(supportFragmentManager, tag1)) {
            closeFragment(tag1)
        }
        //Controlli su fragment profilo se è aperto
        else if (fragmentExists(supportFragmentManager, tag2)) {
            val currentFragment1 = supportFragmentManager.findFragmentById(R.id.fragment_container_profilo)
            if(currentFragment1 is LoginFragment){
                //Se siamo nel fragment login, torna nel fragment LoginSignUp
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fragment_container_profilo, LoginSignUpFragment(), tag2)
                    .commit()
            }else{
                //Chiudi fragment profilo se non è LoginSignupFragment
                closeFragment(tag2)
            }
        } else {
            val currentFragment2 = supportFragmentManager.findFragmentById(R.id.fragment_container)
            when {
                (currentFragment2 is HomeFragment) -> finish() // Se siamo nella HomeFragment, chiudi l'app
                (currentFragment2 is FavouritesFragment) -> backHome()
                (currentFragment2 is BookshopFragment) -> backHome()
                (currentFragment2 is DonateBookFragment) -> backHome()
                (currentFragment2 is BookSeatFragment) -> backHome()
                (currentFragment2 is FeedbackFragment) -> backHome()
                (currentFragment2 is LoansFragment) -> backHome()
                (currentFragment2 is SettingsFragment) -> backHome()
                (currentFragment2 is NotificationFragment) -> backHome()
                (currentFragment2 is AccountFragment) -> backHome()
                else -> supportFragmentManager.popBackStack()

            }
        }
    }

    private fun backHome(){
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, HomeFragment())
            .commit()
    }


    private fun fragmentExists(fragmentManager: FragmentManager, tag: String): Boolean {
        val fragment = fragmentManager.findFragmentByTag(tag)
        return fragment != null
    }

    private fun closeFragment(tag: String){
        val existingFragment = supportFragmentManager.findFragmentByTag(tag)
        if (existingFragment != null) {
            supportFragmentManager.beginTransaction()
                .remove(existingFragment)
                .commit()
        }
    }

    fun showBottomNavigation():MeowBottomNavigation{
       return bottomNavigation
    }

    private fun getCurrentFragment(): Fragment? {
        val fragmentContainerId = R.id.fragment_container
        return supportFragmentManager.findFragmentById(fragmentContainerId)
    }

    private fun bookReturnNotification() {
        ClientNetwork.getLoanBooks(User.id, inCorso = true) { bookList, _ ->
            if(bookList.isNotEmpty()) {
                ClientNetwork.getNotifications(User.id) { notificationList ->
                    for (i in bookList.indices) {
                        if (bookList[i].giorniRestituzione in 0..5) {
                            var found = false
                            for (element in notificationList) {
                                if (element.testo == "${LocalDate.now()}: Rimangono soltanto ${bookList[i].giorniRestituzione} giorni alla scadenza della restituzione del libro \"${bookList[i].titolo}\"") {
                                    found = true
                                    break
                                }
                            }
                            if (!found) {
                                ClientNetwork.insertNotification(
                                    User.id,
                                    "${LocalDate.now()}: Rimangono soltanto ${bookList[i].giorniRestituzione} giorni alla scadenza della restituzione del libro \"${bookList[i].titolo}\""
                                ) { check ->
                                    if (!check) {
                                        showErrorAlert()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun bookBackAvailable() {
        ClientNetwork.getBookBackAvailable(User.id){ bookList, check ->
            if(!check){
                showErrorAlert()
            }else if(bookList.isNotEmpty()){
                ClientNetwork.removeBookBackAvailable(User.id){ check ->
                    if(check){
                        for (i in bookList.indices){
                            ClientNetwork.insertNotification(User.id, "${LocalDate.now()}: Il libro \"${bookList[i].titolo}\" è tornato disponibile!"){ check ->
                                if(!check){
                                    showErrorAlert()
                                }
                            }
                        }
                    }else{
                        showErrorAlert()
                    }
                }
            }
        }
    }

    private fun showErrorAlert(){
        AlertDialog.Builder(this)
            .setTitle("ERRORE")
            .setMessage("Si è verificato un problema durante il collegamento con il server.")
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}