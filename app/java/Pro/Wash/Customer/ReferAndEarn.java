package Pro.Wash.Customer;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.provider.Telephony;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import Pro.Wash.HomeActivity.HomeActivity;
import Pro.Wash.R;

public class ReferAndEarn extends AppCompatActivity
        implements View.OnClickListener {

    private TextView ReferralCode;
    private EditText EnteredReferralCode;
    private RelativeLayout ReferLayout;
    private RelativeLayout ReferStatusLayout;
    private TextView ReferralStatus;

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ReferAndEarn.this,HomeActivity.class));
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer_and_earn);

        Button InviteContacts = findViewById(R.id.InviteContacts);
        Button Whatsapp = findViewById(R.id.WhatsApp);
        Button Facebook = findViewById(R.id.Facebook);
        Button MoreOptions = findViewById(R.id.MoreOptions);
        Toolbar mToolbar = findViewById(R.id.ReferAndEarnToolbar);
        TextView CopyToClipboard = findViewById(R.id.CopyReferralCode);
        ReferralCode = findViewById(R.id.ActualReferCode);
        EnteredReferralCode = findViewById(R.id.EnterReferralCode);
        TextView ConfirmReferralCode = findViewById(R.id.ConfirmReferralCode);
        ReferLayout = findViewById(R.id.ReferralcodeenterLayout);
        ReferStatusLayout = findViewById(R.id.IfRefer);
        ReferralStatus = findViewById(R.id.ReferralStatus);

        //Set Referral Code to Users Referral Code
        SetReferralCode();

        //If Referred Already Can't Use Another Referral Code
        CheckReferralCodeAlreadyEntered();

        //Hide keyboard
        hideSoftKeyboard();

        //On Click Toolbar Navigation
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ReferAndEarn.this,HomeActivity.class));
                finish();
            }});

        InviteContacts.setOnClickListener(this);
        Whatsapp.setOnClickListener(this);
        Facebook.setOnClickListener(this);
        MoreOptions.setOnClickListener(this);
        CopyToClipboard.setOnClickListener(this);
        ConfirmReferralCode.setOnClickListener(this);

        //TODO: Change The Text To App Link to share App
        //TODO: Earning Part (Our Currency)
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.InviteContacts:
                ShareViaSms();
                break;

            case R.id.WhatsApp:
                ShareVia("com.whatsapp");
                break;

            case R.id.Facebook:
                ShareVia("com.facebook.orca");
                break;

            case R.id.MoreOptions:
                Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT,
                        "Now say no to washing download ProWash Now \nUse Referral Code :"
                                + ReferralCode.getText().toString());
                startActivity(Intent.createChooser(sharingIntent,"Share using"));
                break;

            case R.id.CopyReferralCode:
                ClipboardManager clipboardManager = (ClipboardManager)
                        getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Referral Code:",
                        ReferralCode.getText().toString());
                assert clipboardManager != null;
                clipboardManager.setPrimaryClip(clipData);
                Toast.makeText(getApplicationContext(),"Refer Code Copied to clipboard",Toast.LENGTH_SHORT).show();
                break;

            case R.id.ConfirmReferralCode:
                CheckEnterReferralCode();
                break;
        }
    }
    //Share Text Via Social Media
    private void ShareVia(String AppName){
        try {
            Intent share = new Intent();
            share.setAction(Intent.ACTION_SEND);
            share.putExtra(Intent.EXTRA_TEXT, "Now say no to washing download ProWash Now \nUse Referral Code :"
                    + ReferralCode.getText().toString());
            share.setPackage(AppName);
            share.setType("text/plain");
            startActivity(share);

        }catch (ActivityNotFoundException e){
            //Start Play Store To Download Application
            Intent OpenPlayStore = new Intent(android.content.Intent.ACTION_VIEW);
            OpenPlayStore.setData(Uri.parse("https://play.google.com/store/apps/details?id="
                    + AppName));
            startActivity(OpenPlayStore);
        }
    }
    //Share Text Via SMS
    private void ShareViaSms() {

        String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(getApplicationContext());
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setType("text/plain");
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Now say no to washing download ProWash Now \nUse Referral Code :"
                + ReferralCode.getText().toString());
        sendIntent.setPackage(defaultSmsPackageName);
        startActivity(sendIntent);
    }

    //SetReferralCode
    private void SetReferralCode(){

        FirebaseUser UserA = FirebaseAuth.getInstance().getCurrentUser();
        assert UserA != null;
        DatabaseReference CountRef = FirebaseDatabase.getInstance().getReference()
                .child("ReferralCode")
                .child(UserA.getUid());

        CountRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String ReferCode = dataSnapshot.getValue(String.class);
                ReferralCode.setText(ReferCode);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    //Only One ReferralCode Can Be Use
    private void CheckEnterReferralCode() {
        final FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        assert mUser != null;
        DatabaseReference AllUsersReferral = FirebaseDatabase.getInstance().getReference()
                .child("ReferralCode");

        if(!EnteredReferralCode.getText().toString().isEmpty()){
            AllUsersReferral.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for(DataSnapshot ds : dataSnapshot.getChildren()){
                        if(ds.getValue(String.class)
                                .equals(EnteredReferralCode.getText().toString())){

                            if(!EnteredReferralCode.getText().toString().equals(
                                    ReferralCode.getText().toString())){

                                //TODO : Give Bonus Points
                                String ReferrerCode = EnteredReferralCode.getText().toString();
                                //The One Who Is Referring
                                String NewUserUID = mUser.getUid();
                                DatabaseReference Dref = FirebaseDatabase.getInstance()
                                        .getReference().child("WhoUsedWhoseReferralCode");
                                Dref.child(NewUserUID).setValue(ReferrerCode);

                                Toast.makeText(getApplicationContext(),"Referred Successfully " +
                                                "you will get bonus within 5 mins"
                                        ,Toast.LENGTH_SHORT).show();
                                break;
                            }else{
                                EnteredReferralCode.setError("You Entered Your Referral Code");
                            }
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        else{
            EnteredReferralCode.setError("Enter Code");
        }
    }

    private void CheckReferralCodeAlreadyEntered() {

        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        assert mUser != null;
        DatabaseReference AllUsersReferral = FirebaseDatabase.getInstance().getReference()
                .child("WhoUsedWhoseReferralCode").child(mUser.getUid());
        AllUsersReferral.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String value = dataSnapshot.getValue(String.class);
                if (value != null){
                    ReferLayout.setVisibility(View.GONE);
                    ReferStatusLayout.setVisibility(View.VISIBLE);
                    String Status = "Used Referral Code : " + value;
                    ReferralStatus.setText(Status);
                    ReferAndEarn.this.getWindow().setSoftInputMode
                            (WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
                }
                else{
                    ReferStatusLayout.setVisibility(View.GONE);
                    ReferLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void hideSoftKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }
}
