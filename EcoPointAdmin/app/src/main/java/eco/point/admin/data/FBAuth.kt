package eco.point.admin.data

import android.app.Activity
import androidx.activity.result.ActivityResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class FBAuth(private val activity: Activity) {
    companion object{
        val COMPLETE = 0
        val CANCEL = 1
        val FAILED = 2
    }
    private val auth = FirebaseAuth.getInstance()
    private val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestIdToken(activity.getString(com.firebase.ui.auth.R.string.default_web_client_id))
        .requestEmail().build()
    private val googleSignInClient = GoogleSignIn.getClient(activity, gso)

    fun getAccount(result: ActivityResult, callback: (Int) -> Unit): Boolean {
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        return try {
            val account = task.getResult(ApiException::class.java)
            auth.signInWithCredential(
                GoogleAuthProvider.getCredential(account.idToken, null)
            )
                .addOnCompleteListener(activity) { result ->
                    if (result.isSuccessful)
                        FBUsersData().adminCheck { if (it) callback(COMPLETE) else callback(CANCEL) }
                    else callback(FAILED)
                }
                .addOnCanceledListener(activity) { callback(CANCEL) }
                .addOnFailureListener(activity) { FAILED }
            true
        } catch (e: Exception){
            false
        }
    }

    fun reload(){
        auth.currentUser?.reload()
    }

    fun getGoogleSignInIntent() = googleSignInClient.signInIntent

    fun googleSignOut(){
        auth.signOut()
        googleSignInClient.signOut()
    }
}