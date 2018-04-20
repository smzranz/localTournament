package com.example.shamshir.localtournaments;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.concurrent.TimeUnit;

import static java.util.concurrent.TimeUnit.SECONDS;


public class SignUpActivity extends AppCompatActivity {

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;


    private FirebaseAuth mAuth;

    DatabaseReference databaseProfile;

    EditText nameTextField;
    EditText mobileTextField;
    EditText passwordTextField;
    EditText repeatpasswordTextField;
    EditText clubTextField;

    private String mVerificationId;
    private PhoneAuthProvider.ForceResendingToken mResendToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        authentication();
        nameTextField = (EditText) findViewById(R.id.fullName);
        mobileTextField = (EditText) findViewById(R.id.signUpUsername);
        passwordTextField = (EditText) findViewById(R.id.signUpPassword);
        repeatpasswordTextField = (EditText) findViewById(R.id.repeatPassword);
        clubTextField = (EditText) findViewById(R.id.clubName);
        Button signUpButton = (Button) findViewById(R.id.signup) ;

        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signupPressed();
            }
        });

        databaseProfile = FirebaseDatabase.getInstance().getReference("Users");
    }

    String fullName ;
    String mobileNumber ;
    String userpassword ;
    String userRepeatPassword ;
    String clubName;

    private void signupPressed(){

        fullName = nameTextField.getText().toString().trim();
        mobileNumber = mobileTextField.getText().toString().trim();
        userpassword = passwordTextField.getText().toString().trim();
        userRepeatPassword = repeatpasswordTextField.getText().toString().trim();
        clubName = clubTextField.getText().toString().trim();

        if (!TextUtils.isEmpty(fullName) || !TextUtils.isEmpty(mobileNumber) || !TextUtils.isEmpty(userpassword) || !TextUtils.isEmpty(userRepeatPassword)) {


    if (userpassword.equals(userRepeatPassword)){

                 //   addUserToFirebase();

        PhoneAuthProvider.getInstance().verifyPhoneNumber("+91"+mobileNumber,60, SECONDS,this,mCallbacks);



        }else{

        Toast.makeText(getApplicationContext(), "Passwords doesn't match", Toast.LENGTH_LONG).show();


    }



        } else {


            Toast.makeText(getApplicationContext(), "All Fields Required", Toast.LENGTH_LONG).show();

        }

    }

    private void addUserToFirebase(String userUUID){


        String uploadId = userUUID;


        ProfileDetails user = new ProfileDetails(uploadId,fullName,mobileNumber,userpassword,clubName);


        databaseProfile.child(uploadId).setValue(user);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("MyPref", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("userLogged",true);
        editor.putString("idName", uploadId);
        editor.apply();
            finish();
        startActivity(new Intent(SignUpActivity.this, AddTournamentActivity.class));






    }


    private void authentication(){

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                // This callback will be invoked in two situations:
                // 1 - Instant verification. In some cases the phone number can be instantly
                //     verified without needing to send or enter a verification code.
                // 2 - Auto-retrieval. On some devices Google Play services can automatically
                //     detect the incoming verification SMS and perform verification without
                //     user action.
                Log.i("lll", "onVerificationCompleted:" + credential);
                // [START_EXCLUDE silent]
                // mVerificationInProgress = false;
                // [END_EXCLUDE]

                // [START_EXCLUDE silent]
                // Update the UI and attempt sign in with the phone credential
                //  updateUI(STATE_VERIFY_SUCCESS, credential);
                // [END_EXCLUDE]
                  signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                // This callback is invoked in an invalid request for verification is made,
                // for instance if the the phone number format is not valid.
                Log.i("444", "onVerificationFailed", e);
                // [START_EXCLUDE silent]
                //   mVerificationInProgress = false;
                // [END_EXCLUDE]

                if (e instanceof FirebaseAuthInvalidCredentialsException) {
                    // Invalid request
                    // [START_EXCLUDE]
                    //   mPhoneNumberField.setError("Invalid phone number.");
                    // [END_EXCLUDE]
                } else if (e instanceof FirebaseTooManyRequestsException) {
                    // The SMS quota for the project has been exceeded
                    // [START_EXCLUDE]
                    //  Snackbar.make(findViewById(android.R.id.content), "Quota exceeded.",
                    //      Snackbar.LENGTH_SHORT).show();
                    // [END_EXCLUDE]
                }

                // Show a message and update the UI
                // [START_EXCLUDE]
                //   updateUI(STATE_VERIFY_FAILED);
                // [END_EXCLUDE]
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {
                // The SMS verification code has been sent to the provided phone number, we
                // now need to ask the user to enter the code and then construct a credential
                // by combining the code with a verification ID.
                Log.i("7777", "onCodeSent:" + verificationId);

                // Save verification ID and resending token so we can use them later
                 mVerificationId = verificationId;
                  mResendToken = token;
showOTPAlert();
                // [START_EXCLUDE]
                // Update UI
                // updateUI(STATE_CODE_SENT);
                // [END_EXCLUDE]
            }
        };




    }

    private void verifyPhoneNumberWithCode(String verificationId, String code) {
        // [START verify_with_code]
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, code);
        // [END verify_with_code]
        signInWithPhoneAuthCredential(credential);
    }
    public void goBack(){

        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStackImmediate();
        } else {
            super.onBackPressed();
        }

    }


    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("tag", "signInWithCredential:success");



                            FirebaseUser user = task.getResult().getUser();
                            addUserToFirebase(user.getUid());
                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
                            Log.w("tag", "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                            }
                        }
                    }
                });
    }


    private void resendVerificationCode(String phoneNumber,
                                        PhoneAuthProvider.ForceResendingToken token) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                phoneNumber,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                this,               // Activity (for callback binding)
                mCallbacks,         // OnVerificationStateChangedCallbacks
                token);             // ForceResendingToken from callbacks
    }


    private void showOTPAlert(){

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        final EditText edittext = new EditText(SignUpActivity.this);
        alert.setMessage("Enter Your Message");
        alert.setTitle("Enter Your Title");

        alert.setView(edittext);

        alert.setPositiveButton("Yes Option", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //What ever you want to do with the value

                String otpCode = edittext.getText().toString();

                verifyPhoneNumberWithCode(mVerificationId,otpCode);
            }
        });

        alert.setNegativeButton("No Option", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // what ever you want to do with No option.
            }
        });

        alert.show();

    }

}
