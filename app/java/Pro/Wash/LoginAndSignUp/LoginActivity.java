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
import android.widget.ProgressBar;
import android.widget.Toast;

        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.Task;
        import com.google.firebase.auth.AuthResult;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Pro.Wash.HomeActivity.HomeActivity;
import Pro.Wash.HomeActivity.HomeActivityManager;
import Pro.Wash.HomeActivity.HomeActivityWashingCenter;
import Pro.Wash.R;

public class LoginActivity extends AppCompatActivity{

    private FirebaseAuth mAuth;
    private FirebaseUser mUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;

    private EditText EmailInput,PasswordInput;
    private Button LoginBtn,SignUpBtn,ForgotPasswordBtn;
    private String USER_TYPE;
    private ProgressBar LoginProgress;

    private Drawable errorIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //EditText
        EmailInput = findViewById(R.id.EmailID);
        PasswordInput = findViewById(R.id.Password);

        //Button
        LoginBtn = findViewById(R.id.LoginBtn);
        SignUpBtn = findViewById(R.id.SignUpBtn);
        ForgotPasswordBtn = findViewById(R.id.ForgotPasswordBtn);

        //Progress
        LoginProgress = findViewById(R.id.LoginProgress);
        LoginProgress.setVisibility(View.GONE);

        mAuth = FirebaseAuth.getInstance();

        errorIcon = getResources().getDrawable(R.drawable.ic_cancel_red_24dp);
        errorIcon.setBounds(new Rect(0, 0, errorIcon.getIntrinsicWidth(), errorIcon.getIntrinsicHeight()));
        
        OnUserIsLoggedIn();
        OnSignUpBtnClick();
        UserLogin();
        OnForgotPassword();

    }

    private void hideSoftKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    //If User is Logged In
    private void OnUserIsLoggedIn(){

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        if(mUser != null){

            mDatabase = FirebaseDatabase.getInstance();
            mReference = FirebaseDatabase.getInstance().getReference()
                    .child("UserType").child(mUser.getUid());

            mReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    USER_TYPE = dataSnapshot.getValue(String.class);

                    if(USER_TYPE != null){
                        switch (USER_TYPE) {
                            case "Client":
                                OpenClient();
                                break;
                            case "Customer":
                                OpenCustomer();
                                break;
                            case "Manager":
                                OpenManager();
                                break;
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    //On Forgot Password
    private void OnForgotPassword(){
        ForgotPasswordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!EmailInput.getText().toString().isEmpty())
                {
                    if(Patterns.EMAIL_ADDRESS.matcher(EmailInput.getText().toString()).matches()){
                        mAuth.sendPasswordResetEmail(EmailInput.getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext()
                                            , "Password Reset Email Sent"
                                            , Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(getApplicationContext()
                                            , "Error Occured Try Again!!!"
                                            , Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }

                    if(Patterns.PHONE.matcher(EmailInput.getText().toString()).matches()){

                        String UserPhone = EmailInput.getText().toString();
                        FirebaseDatabase PERDatabase = FirebaseDatabase.getInstance();
                        DatabaseReference PERRef = PERDatabase.getReference()
                                .child("Phone Email").child(UserPhone);
                        PERRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                String EmailId = dataSnapshot.getValue(String.class);
                                assert EmailId != null;
                                mAuth.sendPasswordResetEmail(EmailId)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    Toast.makeText(getApplicationContext()
                                                            , "Password Reset Email Sent"
                                                            , Toast.LENGTH_LONG).show();
                                                } else {
                                                    Toast.makeText(getApplicationContext()
                                                            , "Error Occured Try Again!!!"
                                                            , Toast.LENGTH_LONG).show();
                                                }
                                            }
                                        });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });

                    }
                }
                else{
                    EmailInput.setError("Fill Field and Press Forgot Password!!!");
                }

            }
        });
    }

    private void OnSignUpBtnClick(){
        //SignUp Activity SignUp Button
        SignUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, PhoneVerification.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);      // Start Sign In Form
                finish();
            }
        });
    }

    // On Clicking Login Button
    private void UserLogin() {

            LoginBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(CheckPassword() && CheckEmailId()){
                        SignInWithEmail(EmailInput.getText().toString());
                        LoginProgress.setVisibility(View.VISIBLE);
                        hideSoftKeyboard();
                    }
                    if(Patterns.PHONE.matcher("+91" + EmailInput.getText().toString()).matches() && CheckPassword()){
                        SignInWithPhone();
                        LoginProgress.setVisibility(View.VISIBLE);
                        hideSoftKeyboard();
                    }
                }
            });
        }

    // Check Password
    private boolean CheckPassword(){

        if(PasswordInput.getText().toString().isEmpty()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                PasswordInput.setError("Field Is Empty",errorIcon);
            }else
                PasswordInput.setError("Field Is Empty",errorIcon);
        }
        else if(PasswordInput.getText().toString().isEmpty()){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                PasswordInput.setError("Field Is Empty",errorIcon);
            }else
                PasswordInput.setError("Field Is Empty",errorIcon);
        }
        //If Entered Password is Not Equal TO Confirm Password
        else if(!PasswordInput.getText().toString().equals(PasswordInput.getText().toString())){
            PasswordInput.setText("");
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                PasswordInput.setError("Confirm Password is not equal to Entered Password",errorIcon);
            }else
                PasswordInput.setError("Confirm Password is not equal to Entered Password",errorIcon);
        }
        else {
            return true;
        }
        return false;
    }

    //Check Email Id
    private boolean CheckEmailId(){

        if(EmailInput.getText().toString().isEmpty()){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                EmailInput.setError("Please Enter EmailId",errorIcon);
            }else
                EmailInput.setError("Please Enter EmailId",errorIcon);

        }
        else{
            return true;
        }
        return false;
    }

    //Open Customer Activity
    private void OpenCustomer(){
        Intent Home = new Intent(LoginActivity.this,HomeActivity.class);
        Home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(Home);
        finish();
    }

    //Open Client Activity
    private void OpenClient(){
        Intent ClientHome = new Intent(LoginActivity.this,HomeActivityWashingCenter.class);
        ClientHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(ClientHome);
        finish();
    }

    //Sign In With Email
    private void SignInWithEmail(String Email){
        mAuth.signInWithEmailAndPassword(Email,PasswordInput.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    mUser = FirebaseAuth.getInstance().getCurrentUser();
                    mDatabase = FirebaseDatabase.getInstance();
                    mReference = mDatabase.getReference()
                            .child("UserType").child(mUser.getUid());

                    if(mUser != null){

                        mReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                USER_TYPE = dataSnapshot.getValue(String.class);

                                if(USER_TYPE != null){
                                    switch (USER_TYPE) {
                                        case "Client":
                                            OpenClient();
                                            break;
                                        case "Customer":
                                            OpenCustomer();
                                            break;
                                        case "Manager":
                                            OpenManager();
                                            break;
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }else
                {
                    if(task.getException() != null && !task.getException().getMessage().contains("The email address is badly formatted"))
                    {
                        Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                        LoginProgress.setVisibility(View.GONE);
                    }
                }
            }
        });

    }

    private void OpenManager() {
        Intent ManagerIntent = new Intent(LoginActivity.this,HomeActivityManager.class);
        startActivity(ManagerIntent);
        finish();
    }

    //Sign In With Phone
    private void SignInWithPhone(){

        String UserPhone = EmailInput.getText().toString();
        FirebaseDatabase PEDatabase = FirebaseDatabase.getInstance();
        DatabaseReference PERef = PEDatabase.getReference().child("Phone Email").child(UserPhone);
        PERef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String EmailId = dataSnapshot.getValue(String.class);
                SignInWithEmail(EmailId);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //Press Back Again To Exit
    private boolean isUserClickedBackButton = false;
    @Override
    public void onBackPressed() {

        if(!isUserClickedBackButton){

            Toast.makeText(getApplicationContext(),"Press Back Again To Exit"
                    ,Toast.LENGTH_SHORT).show();
            isUserClickedBackButton = true;

        }else {
            super.onBackPressed();
        }
    }
}
