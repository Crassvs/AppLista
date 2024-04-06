package com.crassvs.pruebaapp.screens.login

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crassvs.applista.model.User
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

class LoginScreenViewModel: ViewModel() {

    private val auth : FirebaseAuth =  Firebase.auth
    private val _loading = MutableLiveData(false)

    fun signWithGoogleCredential(credential: AuthCredential, home: () -> Unit)
        = viewModelScope.launch{
            try {
                auth.signInWithCredential(credential)
                    .addOnCompleteListener(){ task ->
                        if (task.isSuccessful){
                            Log.d("ListaApp","Autorización exitósa Weon")
                            home()
                        }
                    }
                    .addOnFailureListener(){
                        Log.d("ListaApp","Autorización Falló Weon, Revisa que salió mal con Google")
                    }
            }catch (ex:Exception){
                Log.d("ListaApp","Falló Weon, Revisa que salió mal con Google" +
                                "${ex.localizedMessage}")
            }
    }

    fun signWithEmailAndPassword (email:String, password:String, home: ()-> Unit)
        = viewModelScope.launch {
             try {
                 auth.signInWithEmailAndPassword(email, password)
                     .addOnCompleteListener {  task ->
                         if (task.isSuccessful){
                                Log.d("ListaLabel","Esta Vaina ha autenticado credenciales")
                                home()
                         }else{
                                 Log.d("ListaLabel","Esta Vaina No Funciona: ${task.result.toString()}")
                         }
                     }
             }catch(ex:Exception){
                 Log.d("ListaLabel","signInWithEmailAndPassword ${ex.message}")
             }

        }

    fun createUserWithEmailAndPassword(
        email: String,
        password: String,
        home: () -> Unit
    ){
        if (_loading.value==false){
            _loading.value=true
            auth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener{ task ->
                    if (task.isSuccessful){
                        val displayName =
                            task.result.user?.email?.split("@")?.get(0)
                        createUser(displayName)
                        home()
                    }else{
                        Log.d("ListaLabel","Esta Vaina No Funciona: ${task.result.toString()}")
                    }
                    _loading.value = false
                }
        }
    }

    private fun createUser(displayName: String?) {
        val userId = auth.currentUser?.uid
        //val user = mutableMapOf<String, Any>()

       // user["user_id"] = userId.toString()
        //user["display_name"] = displayName.toString()

        val user = User(
            userId = userId.toString(),
            displayName = displayName.toString(),
            avatarUrl = "",
            quote = "Lo Dificil Ya Paso Weon",
            profession = "Android Dev",
            id = null
        )

        FirebaseFirestore.getInstance().collection("users")
            .add(user)
            .addOnSuccessListener {
                Log.d("ListaApp","Creao ${it.id}")
            }.addOnFailureListener{
                Log.d("ListaApp","Algo Salió Mal Weon ${it}")
            }
    }

}

