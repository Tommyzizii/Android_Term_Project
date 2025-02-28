package com.example.pennytrack.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class AuthViewModel : ViewModel() {
    private val auth : FirebaseAuth = FirebaseAuth.getInstance()

    private val firestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    private val _authState = MutableLiveData<AuthState>()
    val authState : LiveData<AuthState> =_authState

    private val _username = MutableLiveData<String?>()
    val username: LiveData<String?> = _username

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        checkAuthStatus()
    }

    fun checkAuthStatus(){
        if (auth.currentUser == null ){
            _authState.value= AuthState.Unauthenticated
        }else{
            _authState.value= AuthState.Authenticated

        }
    }

    fun fetchUsername() {
        val userId = auth.currentUser?.uid ?: return
        _isLoading.value = true

        firestore.collection("users").document(userId)
            .get()
            .addOnSuccessListener { document ->
                val username = document.getString("username") ?: "Unknown"
                _username.value = username
                _isLoading.value = false
            }
            .addOnFailureListener { e ->
                _username.value = "Error fetching username"
                _isLoading.value = false
            }
    }

    fun login(email : String,password: String){
        if (email.isEmpty()|| password.isEmpty()){
            _authState.value= AuthState.Error("Email or Password can't be empty")
            return
        }

        _authState.value= AuthState.Loading
        auth.signInWithEmailAndPassword(email,password)
            .addOnCompleteListener{
                    task->
                if (task.isSuccessful){
                    _authState.value= AuthState.Authenticated

                }else{
                    _authState.value= AuthState.Error(task.exception?.message?:"Something went wrong")
                }
            }
    }

    fun signup(email: String, password: String, username: String) {
        if (email.isEmpty() || password.isEmpty() || username.isEmpty()) {
            _authState.value = AuthState.Error("Fields can't be empty")
            return
        }

        _authState.value = AuthState.Loading
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // Add user details to Firestore
                    val userId = auth.currentUser?.uid ?: return@addOnCompleteListener
                    val user = hashMapOf(
                        "username" to username,
                        "email" to email
                    )

                    firestore.collection("users").document(userId)
                        .set(user)
                        .addOnSuccessListener {
                            _authState.value = AuthState.Authenticated
                        }
                        .addOnFailureListener { e ->
                            _authState.value = AuthState.Error(e.message ?: "Failed to save user details")
                        }
                } else {
                    _authState.value = AuthState.Error(task.exception?.message ?: "Something went wrong")
                }
            }
    }
    fun signout(){
        auth.signOut()
        _authState.value= AuthState.Unauthenticated
    }
}

sealed class AuthState{
    object Authenticated : AuthState()
    object Unauthenticated : AuthState()
    object Loading : AuthState()
    data class Error(val message : String): AuthState()
}