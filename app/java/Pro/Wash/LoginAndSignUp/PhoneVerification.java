package Pro.Wash.LoginAndSignUp;

import android.content.Intent;
import android.support.annotation.NonNull;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;

import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

import Pro.Wash.R;

public class PhoneVerification extends AppCompatActivity{

    private EditText PhoneNumber;
    private Button GetOtp, VerifyOtp, ResendOtp,LoginBTN;
    private String mVerificationId;
    private ProgressBar PhoneBar;
    private EditText OneTimePassword;
    private FirebaseAuth FBAuth;

    private PhoneAuthProvider.ForceResendingToken mResendToken;
    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks;

    @Override
    protected void onCreate(Bundle savedInstanceState) throws RuntimeException {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_verification);


        //Progress Bar
        PhoneBar     =  findViewById(R.id.progressBar3);

        //Edit Text
        PhoneNumber =  findViewById(R.id.PhoneNumber);

        //Button
        GetOtp      =      findViewById(R.id.GetOtp);
        VerifyOtp   =     findViewById(R.id.verifyOtp);
        ResendOtp   =    findViewById(R.id.resendOtp);

        //Edit Text
        OneTimePassword  =   findViewById(R.id.OneTimePassword);


        //INVISIBLE Progress Bar 3
        PhoneBar.setVisibility(View.INVISIBLE);

        //FireBase Get Instance
        FBAuth = FirebaseAuth.getInstance();


        OnClickingGetOtpBtn();      //  On Clicking GetOtp Button
        OnClickingVerifyOtpBtn();   //  On Clicking Verify Otp Button
        OnClickingResendOtpBtn();   //  On Clicking Resend Otp Button

        mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            @Override
            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
                signInWithPhoneAuthCredential(phoneAuthCredential);

            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Toast.makeText(getApplicationContext(),
                        "There Was Some Error In Verification!!", Toast.LENGTH_LONG).show();
                PhoneBar.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onCodeSent(String verificationID
                    , PhoneAuthProvider.ForceResendingToken token) {

                mVerificationId = verificationID;
                mResendToken = token;
                PhoneBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getApplicationContext(), "Code Send Successfully"
                        , Toast.LENGTH_LONG).show();

            }
        };
    }

    //SignInWithPhoneAuthCredential
    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        FBAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            //TODO :afnaldnfae fasjnkdan
                            if(PhoneNumber != null) {
                                String UserPhoneNumber = PhoneNumber.getText().toString();
                                Intent intentP = new Intent(PhoneVerification.this,
                                        TestSignUp.class);
                                intentP.putExtra("PhoneNumber", UserPhoneNumber);
                                startActivity(intentP);
                                finish();
                            }
                        } else {
                            // Sign in failed, display a message
                            Toast.makeText(getApplicationContext(), "There Was Some Error!!"
                                    , Toast.LENGTH_LONG).show();
                            if (task.getException()
                                    instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(getApplicationContext(),
                                        "Invalid Code Entered!!"
                                        , Toast.LENGTH_LONG).show();
                            }

                        }
                    }
                });
    }

    private void OnClickingGetOtpBtn(){
        // On Clicking Get Otp Button
        GetOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    PhoneBar.setVisibility(View.VISIBLE);

                    PhoneAuthProvider.getInstance().verifyPhoneNumber(
                            "+91" + PhoneNumber.getText().toString(), // Phone number to verify
                            60,                      // Timeout duration
                            TimeUnit.SECONDS,           // Unit of timeout
                            PhoneVerification.this,      // Activity (for callback binding)
                            mCallbacks
                    );
            }
        });
    }

    private void OnClickingVerifyOtpBtn(){
        //On Clicking Verify Button
        VerifyOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhoneAuthCredential credential = PhoneAuthProvider.getCredential(mVerificationId,
                        OneTimePassword.getText().toString());
                signInWithPhoneAuthCredential(credential);

            }
        });
    }

    private void OnClickingResendOtpBtn(){
        //On Clicking Resend Button
        ResendOtp.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                PhoneAuthProvider.getInstance().verifyPhoneNumber(
                        "+91" + PhoneNumber.getText().toString(), // Phone number to verify
                        60,                  // Timeout duration
                        TimeUnit.SECONDS,       // Unit of timeout
                        PhoneVerification.this,  // Activity (for callback binding)
                        mCallbacks,             // OnVerificationStateChangedCallbacks
                        mResendToken);          // ForceResendingToken from callbacks
            }
        });
    }

    @Override
    public void onBackPressed() {
        Intent Loginintent = new Intent(PhoneVerification.this , LoginActivity.class);
        startActivity(Loginintent);
        finish();
    }
}

