package eco.point.admin.legacy.firebase.auth

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.activity.result.ActivityResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.FirebaseDatabase

class FBConnectAuth(val activity: Activity) {
    companion object{
        val COMPLETE: Int = 0
        val CANCEL: Int = 1
        val FAILRULE: Int = 2
    }
    private val TAG = javaClass.name
    private val database = FirebaseDatabase.getInstance()
    private val usersDB = database.getReference("Users")
    private var auth = FirebaseAuth.getInstance()
    private var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(activity.getString(com.firebase.ui.auth.R.string.default_web_client_id))
        .requestEmail().build()
    private val googleSignInClient = GoogleSignIn.getClient(activity, gso)
    //private val loginResultHandler = activity.registerForActivityResult()

    fun getAccount(result: ActivityResult, fbAuthCallback: FBAuthCallback): Boolean{
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        return handleSignInResult(task, fbAuthCallback)
    }

    private fun handleSignInResult(task: Task<GoogleSignInAccount>, fbAuthCallback: FBAuthCallback): Boolean{
        try {
            val account = task.getResult(ApiException::class.java)
            firebaseGoogleAuth(account, fbAuthCallback)
            return true
        }catch (e: Exception){
            Log.e(TAG, "handleSignInResult: ${e.message}")
            return false
        }
    }

    private fun firebaseGoogleAuth(acccont: GoogleSignInAccount, fbAuthCallback: FBAuthCallback){
        val credential = GoogleAuthProvider.getCredential(acccont.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener(activity) {
            if (it.isSuccessful) {
                fbAuthCallback.onResult(COMPLETE)
            } else
                fbAuthCallback.onResult(FAILRULE)
        }.addOnCanceledListener(activity) {
            fbAuthCallback.onResult(CANCEL)
        }.addOnFailureListener(activity) {
            fbAuthCallback.onResult(FAILRULE)
        }
    }

    fun reload(){
        auth.currentUser?.reload()?.addOnCompleteListener {
            if (it.isSuccessful)
                Log.i(TAG, "reload: successful")
            else
                Log.e(TAG, "reload: failed")
        }
    }

    fun getGoogleSignInIntent(): Intent {
        return googleSignInClient.signInIntent
    }

    fun GoogleSignOut(){
        auth.signOut()
        googleSignInClient.signOut()
    }
}