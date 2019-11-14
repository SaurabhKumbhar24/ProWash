package Pro.Wash.Customer;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Pro.Wash.Adapters.ImageAdapter;
import Pro.Wash.HomeActivity.HomeActivity;
import Pro.Wash.HomeActivity.HomeActivityWashingCenter;
import Pro.Wash.R;

public class MyAccount extends AppCompatActivity {

    //Toolbar
    private  android.support.v7.widget.Toolbar MyAccToolBar;

    //Button
    private Button Edit;
    private Button ChangeUNBtn;
    private Button ChangeUEBtn;


    //Text View
    private TextView UserFullName;
    private TextView UserEmailID;
    private TextView UserPhoneNo;
    private TextView UserNameTV;
    private TextView ChangeFNTV;
    private TextView ChangeLNTV;
    private TextView ChangeUETV ;

    //Edit Text
    private EditText ChangeFNET;
    private EditText ChangeLNET;
    private EditText ChangeUEET;

    //Image View
    public ImageView ProfilePhoto;

    //Relative Layout
    private RelativeLayout MainLayoutPart2;
    private RelativeLayout MainLayoutPart1;
    private RelativeLayout NameChangerLayout;
    private RelativeLayout EmailChangerLayout;

    //FireBase
    private FirebaseUser mUser;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseRef;
    private DatabaseReference CUserNameRef;

    //Strings
    private String FName,LName,EmailAddress,PhoneNum;

    private EditText mPassword ;
    private Button ConfirmPassword ;
    private Button CancelBtn;


    private AlertDialog dialog;

    private Pro.Wash.Adapters.ImageAdapter ImageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        ImageAdapter = new ImageAdapter();

        AlertDialog.Builder mBuilder;
        View mView;
        mBuilder = new AlertDialog.Builder(MyAccount.this);
        mView = getLayoutInflater().inflate(R.layout.confirm_password_dialog ,null);

        //Toolbar
        MyAccToolBar = findViewById(R.id.MyAccountToolbar);

        //Buttons
        Edit           =  findViewById(R.id.EditButton);

        ChangeUEBtn    =  findViewById(R.id.EChanger);
        ChangeUNBtn    =  findViewById(R.id.Change);

        ConfirmPassword = mView.findViewById(R.id.dialog_confirm);
        CancelBtn =  mView.findViewById(R.id.dialog_cancel);


        //Text View
        UserFullName =  findViewById(R.id.FullName);
        UserEmailID  =  findViewById(R.id.EmailIdChange);
        UserPhoneNo  =  findViewById(R.id.PhonenoChange);
        UserNameTV   =  findViewById(R.id.textView10);

        ChangeFNTV   = findViewById(R.id.FNTVChanger);
        ChangeLNTV   = findViewById(R.id.LNTVChanger);
        ChangeUETV   = findViewById(R.id.ETVChanger);

        //Edit View
        ChangeFNET   = findViewById(R.id.FNETChanger);
        ChangeLNET   = findViewById(R.id.LNETChanger);
        ChangeUEET   = findViewById(R.id.EETChanger);

        mPassword = mView.findViewById(R.id.confirm_password);

        //Image View
        ProfilePhoto     = findViewById(R.id.ProfileImage);

        //Layout
        MainLayoutPart2   = findViewById(R.id.MainLayout);
        MainLayoutPart1   = findViewById(R.id.ImageLayout);
        NameChangerLayout = findViewById(R.id.NameChangerLayout);
        EmailChangerLayout= findViewById(R.id.EmailLayoutChanger);


        MainLayoutPart1.setVisibility(View.VISIBLE);
        MainLayoutPart2.setVisibility(View.VISIBLE);
        NameChangerLayout.setVisibility(View.GONE);
        EmailChangerLayout.setVisibility(View.GONE);


        ProfilePhoto.setImageResource(R.drawable.defaulthuman);

        mBuilder.setView(mView);
        dialog = mBuilder.create();

        SetUserInfo();
        IfVerified();
        ToolBarSettings();
        EditAccout();
        TextViewChanger();
        ProfilePhotoManager();

        if(NameChangerLayout.getVisibility() == View.VISIBLE ||
                EmailChangerLayout.getVisibility() == View.VISIBLE){
            Edit.setVisibility(View.GONE);
        }
        else{
            Edit.setVisibility(View.VISIBLE);
            MyAccToolBar.setTitle("My Account");
        }
    }


    private void ProfilePhotoManager() {

        mDatabase = FirebaseDatabase.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference myProfileRef = mDatabase.getReference().child("Profile Photo").child(mUser.getUid());
        myProfileRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(dataSnapshot.getValue()!=null){
                    int position = Integer.parseInt(String.valueOf(dataSnapshot.getValue()));
                    ProfilePhoto.setImageResource(ImageAdapter.mThumbIds[position]);
            }}

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Failed To Retrive Data!!!",Toast.LENGTH_LONG).show();
            }
        });

    }

    //ToolBar
    private void ToolBarSettings(){
        setSupportActionBar(MyAccToolBar);
        //Back Button Setting
        if(getSupportActionBar()!=null){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        MyAccToolBar.setNavigationIcon(R.drawable.ic_left_arrow);
        //On Clicking Back Button
        MyAccToolBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                IfVerified();
                if(NameChangerLayout.getVisibility() == View.VISIBLE ||
                        EmailChangerLayout.getVisibility() == View.VISIBLE){
                    NameChangerLayout.setVisibility(View.GONE);
                    EmailChangerLayout.setVisibility(View.GONE);
                    MainLayoutPart1.setVisibility(View.VISIBLE);
                    MainLayoutPart2.setVisibility(View.VISIBLE);
                    Edit.setVisibility(View.VISIBLE);
                    MyAccToolBar.setTitle("My Account");
                }
                else{
                mDatabase = FirebaseDatabase.getInstance();
                mUser = FirebaseAuth.getInstance().getCurrentUser();
                mDatabaseRef = mDatabase.getReference().child("UserType").child(mUser.getUid());
                mDatabaseRef.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        String value = dataSnapshot.getValue(String.class);

                        assert value != null;
                        if(value.equals("Customer")){
                            Intent HomeAcc = new Intent(MyAccount.this,HomeActivity.class);
                            startActivity(HomeAcc);
                            finish();
                        }
                        if(value.equals("Client")){
                            Intent ClientHomeAcc = new Intent(MyAccount.this,HomeActivityWashingCenter.class);
                            startActivity(ClientHomeAcc);
                            finish();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }}
        });
    }

    //Verify Email Or Not
    private void IfVerified(){

        mDatabase = FirebaseDatabase.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabaseRef = mDatabase.getReference().child("Email Status").child(mUser.getUid());

        if(mUser.isEmailVerified()){
            mDatabaseRef.setValue("Verified");
        }
        else{
            mDatabaseRef.setValue("Not Verified");

        }

    }

    //Set User Info
    private void SetUserInfo(){
        mDatabase = FirebaseDatabase.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        if (mUser != null) {
            //User Name
            String pathUserName = "/Users/" + mUser.getUid() + "/UserName";
            DatabaseReference userNameRef = mDatabase.getReference(pathUserName);
            userNameRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String value = dataSnapshot.getValue(String.class);
                    UserFullName.setText(value);
                    UserNameTV.setText(value);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext(),"ERROR While Retriving UserName",
                            Toast.LENGTH_LONG).show();
                }
            });

            //Phone Number
            String pathPhoneNumber = "/Users/" + mUser.getUid() + "/PhoneNumber";
            DatabaseReference phoneRef = mDatabase.getReference(pathPhoneNumber);
            phoneRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String value = dataSnapshot.getValue(String.class);
                    String PN = "+91" + value;
                    PhoneNum = value;
                    UserPhoneNo.setText(PN);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext(),"ERROR While Retriving PhoneNumber",
                            Toast.LENGTH_LONG).show();
                }
            });

            //Email Id
            String pathEmailId = "/Users/" + mUser.getUid() + "/EmailId";
            DatabaseReference emailRef = mDatabase.getReference(pathEmailId);
            emailRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String Onvalue = dataSnapshot.getValue(String.class);
                    UserEmailID.setText(Onvalue);
                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext(),"ERROR While Retriving EmailId",
                            Toast.LENGTH_LONG).show();
                }
            });

        }
        else
        {
            Toast.makeText(getApplicationContext(),"NULL USER!!!",Toast.LENGTH_LONG).show();
        }

    }

    //Editing User Info
    private void EditAccout(){
        //On Clicking Edit Button
        Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Change User Name
                UserFullName.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        NameChangerLayout.setVisibility(View.VISIBLE);
                        MainLayoutPart2.setVisibility(View.GONE);
                        MainLayoutPart1.setVisibility(View.GONE);
                        EmailChangerLayout.setVisibility(View.GONE);
                        Edit.setVisibility(View.GONE);
                        MyAccToolBar.setTitle("Change Name");
                        ChangeUserName();
                    }
                });
                UserEmailID.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_mail,0,
                         R.drawable.ic_baseline_edit_24px,0);
                //Change User Email ID
                UserEmailID.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            NameChangerLayout.setVisibility(View.GONE);
                            MainLayoutPart2.setVisibility(View.GONE);
                            MainLayoutPart1.setVisibility(View.GONE);
                            EmailChangerLayout.setVisibility(View.VISIBLE);
                            Edit.setVisibility(View.GONE);
                            MyAccToolBar.setTitle("Change EmailID");
                            ChangeUserEmail();
                        }
                });

                UserFullName.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_name,0,
                        R.drawable.ic_baseline_edit_24px,0);

                //Profile Photo
                ProfilePhoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent ImageManagerIntent = new Intent(MyAccount.this,ImageManager.class);
                        startActivity(ImageManagerIntent);
                        finish();
                    }
                });
            }
        });
    }

    private void ChangeUserName(){
            ChangeUNBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(CheckName()){
                        String UserName = FName+ " " + LName;
                        CUserNameRef = FirebaseDatabase.getInstance().getReference().child("Users")
                                .child(mUser.getUid());
                        CUserNameRef.child("UserName").setValue(UserName);
                        NameChangerLayout.setVisibility(View.GONE);
                        MainLayoutPart2.setVisibility(View.VISIBLE);
                        MainLayoutPart1.setVisibility(View.VISIBLE);
                        EmailChangerLayout.setVisibility(View.GONE);
                        Edit.setVisibility(View.VISIBLE);
                        MyAccToolBar.setTitle("My Account");
                    }
                }
            });
    }

    private void ChangeUserEmail(){
            ChangeUEBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(CheckEmailId()) {
                        mUser = FirebaseAuth.getInstance().getCurrentUser();

                        EmailChanger();
                        Edit.setVisibility(View.VISIBLE);
                        MyAccToolBar.setTitle("My Account");
                    }
                }
            });
        }

    //Text View Changer Of Edit Account
    private void TextViewChanger(){

        if (ChangeFNTV != null) {
            ChangeFNTV.setVisibility(View.INVISIBLE);

            //On Selecting EditText Of First Name
            ChangeFNET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus){
                        ChangeFNET.setHint("");
                        ChangeFNTV.setVisibility(View.VISIBLE);
                    }
                }
            });
        }

        if (ChangeLNTV != null) {
            ChangeLNTV.setVisibility(View.INVISIBLE);

            //On Selecting EditText Of Last Name
            ChangeLNET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus){
                        ChangeLNET.setHint("");
                        ChangeLNTV.setVisibility(View.VISIBLE);
                    }
                }
            });
        }

        if (ChangeUETV != null) {
            ChangeUETV.setVisibility(View.INVISIBLE);

            //On Selecting EditText Of Email
            ChangeUEET.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(hasFocus){
                        ChangeUEET.setHint("");
                        ChangeUETV.setVisibility(View.VISIBLE);
                    }
                }
            });
        }
    }

    //CHECK NAME
    private boolean CheckName(){

        if(ChangeFNET.getText().toString().isEmpty()){
            ChangeFNET.setError("Field Is Empty");
        }
        else if(ChangeLNET.getText().toString().isEmpty()){
            ChangeLNET.setError("Field Is Empty");
        }
        else{
            FName = ChangeFNET.getText().toString();
            LName = ChangeLNET.getText().toString();
            return true;
        }
        return false;
    }

    //CHECK EMAIL ID
    private boolean CheckEmailId(){

        if(ChangeUEET.getText().toString().isEmpty()){
            ChangeUEET.setError("Please Enter EmailId");
        }
        else if(!Patterns.EMAIL_ADDRESS.matcher(ChangeUEET.getText().toString()).matches()){
            ChangeUEET.setError("Enter Valid Email Id");
            ChangeUEET.requestFocus();
        }
        else{
            EmailAddress = ChangeUEET.getText().toString();
            return true;
        }
        return false;
    }

    //CHANGE EMAIL
    private void EmailChanger(){

        dialog.show();

        //On Clicking Cancel Button
        CancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        //On Clicking Confirm Button
        ConfirmPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(CheckPassword()){

                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if(user != null && user.getEmail() != null){
                    AuthCredential credential = EmailAuthProvider
                            .getCredential(user.getEmail(), mPassword.getText().toString());
                    user.reauthenticate(credential)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                    FirebaseUser UpdateUser = FirebaseAuth.getInstance().getCurrentUser();
                                    UpdateUser.updateEmail(EmailAddress)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(getApplicationContext(),
                                                                "EMAIL ID UPDATED SUCCESSFULLY!!!"
                                                                ,Toast.LENGTH_LONG).show();
                                                        DatabaseReference emailRef =
                                                                FirebaseDatabase.getInstance().getReference().
                                                                        child("Phone Email").child(PhoneNum);
                                                                emailRef.setValue(EmailAddress);

                                                        dialog.dismiss();
                                                        CUserNameRef = FirebaseDatabase.getInstance().getReference().child("Users")
                                                                .child(mUser.getUid());
                                                        CUserNameRef.child("EmailId").setValue(EmailAddress);
                                                        NameChangerLayout.setVisibility(View.GONE);
                                                        MainLayoutPart2.setVisibility(View.VISIBLE);
                                                        MainLayoutPart1.setVisibility(View.VISIBLE);
                                                        EmailChangerLayout.setVisibility(View.GONE);
                                                    }
                                                    else{
                                                        Toast.makeText(getApplicationContext(),
                                                                "Error Occured While Updating!!!"
                                                                ,Toast.LENGTH_LONG).show();
                                                    }
                                                }
                                            });
                                }
                            });
                }}
            }
        });
    }

    // Check Password
    private boolean CheckPassword(){

        if(mPassword.getText().toString().isEmpty()) {
            mPassword.setError("Field Is Empty");
        }
        else if(mPassword.getText().toString().isEmpty()){
            mPassword.setError("Field Is Empty");
        }
        //If Entered Password Has Less Than 8 Characters
        else if(mPassword.getText().toString().length() < 8){
            mPassword.setText("");
            mPassword.setError("Password Entered Has Less Than 8 Characters");
        }
        else {
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {
        assert getIntent().getExtras() != null;
        IfVerified();
        String CHECK_ACTIVITY = getIntent().getExtras().getString("Home Activity User");
        if(CHECK_ACTIVITY != null){
            if(CHECK_ACTIVITY.equals("Home Activity User")){
                startActivity(new Intent(MyAccount.this,HomeActivity.class));
                finish();
            }
        }else{
            startActivity(new Intent(MyAccount.this,HomeActivityWashingCenter.class));
            finish();
        }
    }
}
