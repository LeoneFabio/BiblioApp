package com.pwm.biblioteca.utils

object User {

    var id: Int = 0
    var name: String = ""
    var surname: String = ""
    var age: Int = 0
    var gender: String = ""
    var address: String = ""
    var phone: String = ""
    var username: String = ""
    var password: String = ""
    var email: String = ""

    fun resetUser(){
         id = 0
         name = ""
         surname = ""
         age = 0
         gender = ""
         address = ""
         phone = ""
         username = ""
         password = ""
         email = ""
    }

    fun setUser(idUtente: Int, nome: String,cognome: String,sesso: String,indirizzo: String,eta: Int,email: String,cellulare: String, username: String, password:String){
        id = idUtente
        name = nome
        surname = cognome
        gender = sesso
        address = indirizzo
        age = eta
        User.email = email
        phone = cellulare
        User.username = username
        User.password = password
    }
}