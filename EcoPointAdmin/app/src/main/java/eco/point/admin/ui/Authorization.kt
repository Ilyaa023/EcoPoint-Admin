package eco.point.admin.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import eco.point.admin.data.FBAuth
import eco.point.admin.databinding.ActivityAuthorizationBinding

class Authorization : AppCompatActivity(){
    lateinit var loginResultHandler: ActivityResultLauncher<Intent>
    lateinit var fbConnectAuth: FBAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityAuthorizationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fbConnectAuth = FBAuth(this)
            loginResultHandler =
                registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                    if (it.resultCode == RESULT_OK) {
                        Toast.makeText(
                            this@Authorization,
                            "SignIn " + if (fbConnectAuth.getAccount(it) { result ->
                                    Toast.makeText(
                                        this@Authorization,
                                        when (result) {
                                            FBAuth.COMPLETE -> "Complete"
                                            FBAuth.CANCEL -> "Cancel"
                                            else -> "Fail"
                                                      },
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    if (result == FBAuth.COMPLETE)
                                        startActivity(
                                            Intent(
                                                this@Authorization,
                                                MainActivity::class.java
                                            ))
                                    finish()
                            }) "Successful!" else "Failed!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }

        binding.googleSigninButton.setOnClickListener{
            loginResultHandler.launch(fbConnectAuth.getGoogleSignInIntent())
        }
    }

    override fun onStart() {
        super.onStart()
        if (FirebaseAuth.getInstance().currentUser != null)
            fbConnectAuth.reload()
    }
}