package Pro.Wash.LoginAndSignUp;

import android.content.Intent;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;

import Pro.Wash.HomeActivity.HomeActivity;
import Pro.Wash.R;

public class TestSignUp extends AppCompatActivity {


    private Button CreateAccount;

    private EditText FullName;
    private EditText Email;
    private EditText PasswordEt;
    private EditText CPassword;


    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private DatabaseReference mReference;
    private DatabaseReference mRef;
    private DatabaseReference DRef;

    private String FName;
    private String EmailAddress;
    private String Password;

    private Drawable errorIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_sign_up);

        initialization();
        OnClickCreateAccount();

    }

    //INITIALIZATION OF DATA MEMBERS
    private void initialization() {

        //BUTTON
        CreateAccount = findViewById(R.id.CreateAccountBtn);

        //EDIT TEXT
        FullName = findViewById(R.id.FullNameRegister);
        Email = findViewById(R.id.EmailIdRegister);
        PasswordEt = findViewById(R.id.PasswordRegister);
        CPassword = findViewById(R.id.ConfirmPasswordRegister);

        //FIREBASE
        mAuth = FirebaseAuth.getInstance();

        //ERROR ICON
        errorIcon = getResources().getDrawable(R.drawable.ic_cancel_red_24dp);
        errorIcon.setBounds(new Rect(0, 0, errorIcon.getIntrinsicWidth(), errorIcon.getIntrinsicHeight()));


    }

    //ON CLICKING CREATE ACCOUNT BUTTON
    private void OnClickCreateAccount(){

        //EMAIL ALREADY EXITS THEN CREATE ACCOUNT
        CreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CheckName() && CheckEmailId() && CheckPassword())
                    RegisterUser();
            }
        });
    }

    // CHECK PASSWORD
    private boolean CheckPassword(){

        if(PasswordEt.getText().toString().isEmpty()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                PasswordEt.setError("Field Is Empty",errorIcon);
            }else{
                PasswordEt.setError("Field Is Empty",errorIcon);
            }
        }
        else if(CPassword.getText().toString().isEmpty()){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                CPassword.setError("Field Is Empty",errorIcon);
            }else{
                CPassword.setError("Field Is Empty",errorIcon);
            }
        }
        //If Entered Password is Not Equal TO Confirm Password
        else if(!PasswordEt.getText().toString().equals(CPassword.getText().toString())){
            CPassword.setText("");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                CPassword.setError("Confirm Password is not equal to Entered Password",errorIcon);
            }else{
                CPassword.setError("Confirm Password is not equal to Entered Password",errorIcon);
            }
        }
        else {
            Password = PasswordEt.getText().toString();
            return true;
        }
        return false;
    }

    //CHECK NAME
    private boolean CheckName(){

        if(FullName.getText().toString().isEmpty()){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                FullName.setError("Field Is Empty",errorIcon);
            }else{
                FullName.setError("Field Is Empty",errorIcon);
            }
        }
        else{
            FName = FullName.getText().toString();
            return true;
        }
        return false;
    }

    //CHECK EMAIL ID
    private boolean CheckEmailId(){

        if(Email.getText().toString().isEmpty()){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Email.setError("Field Is Empty",errorIcon);
            }else{
                Email.setError("Field Is Empty",errorIcon);
            }
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(Email.getText().toString()).matches()){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                Email.setError("Enter valid EmailId",errorIcon);
            }else{
                Email.setError("Enter valid EmailId",errorIcon);
            }
            Email.requestFocus();
        }
        else{
            EmailAddress = Email.getText().toString();
            return true;
        }
        return false;
    }


    //REGISTER USER
    private void RegisterUser(){
        mAuth.createUserWithEmailAndPassword(EmailAddress,Password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    String PhoneNumber = Objects.requireNonNull(getIntent().getExtras()).getString("PhoneNumber");
                    mUser = Objects.requireNonNull(task.getResult()).getUser();
                    mReference = FirebaseDatabase.getInstance().getReference().child("Users")
                            .child(mUser.getUid());
                    mReference.child("UserName").setValue(FName);
                    mReference.child("EmailId").setValue(EmailAddress);
                    mReference.child("PhoneNumber").setValue(PhoneNumber);

                    DatabaseReference Refer = FirebaseDatabase.getInstance().getReference()
                            .child("ReferralCode")
                            .child(mUser.getUid());
                    Refer.setValue("PRO"+PhoneNumber);


                    UserPhoneEmail(PhoneNumber,EmailAddress);

                    mUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(getApplicationContext(),"Verification Email Sent!!"
                                        ,Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                    mRef = FirebaseDatabase.getInstance().getReference()
                            .child("UserType").child(mUser.getUid());
                    mRef.setValue("Customer");

                    assert PhoneNumber != null;
                    DRef = FirebaseDatabase.getInstance().getReference()
                            .child("PhoneUID").child("91"+PhoneNumber);
                    DRef.setValue(mUser.getUid());

                    Toast.makeText(getApplicationContext(),"Account Created Successfully!!!",Toast.LENGTH_LONG).show();
                    hideSoftKeyboard();
                    Intent HomeAintent = new Intent(TestSignUp.this,HomeActivity.class);
                    HomeAintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(HomeAintent);
                    finish();
                }
                else if(task.getException() instanceof FirebaseAuthUserCollisionException){
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Email.setError("Email Id Already Exists",errorIcon);
                    }else{
                        Email.setError("Email Id Already Exists",errorIcon);
                    }
                    Email.setText("");
                }
                else{
                    Toast.makeText(getApplicationContext(),Objects.requireNonNull(task.getException()).getMessage(),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //User Phone Email Register
    private void UserPhoneEmail(String UserPhone , String UserEmail){
        FirebaseDatabase PEDatabase = FirebaseDatabase.getInstance();
        DatabaseReference PERef = PEDatabase.getReference().child("Phone Email").child(UserPhone);
        PERef.setValue(UserEmail);
    }

    //Press Back To Login Activity
    @Override
    public void onBackPressed() {
        startActivity(new Intent(TestSignUp.this,LoginActivity.class));
        finish();
    }

    private void hideSoftKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}
