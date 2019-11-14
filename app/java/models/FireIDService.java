package models;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


public class FireIDService extends FirebaseInstanceIdService {

    FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
    @Override
    public void onTokenRefresh() {
        if(mUser != null) {
            String tkn = FirebaseInstanceId.getInstance().getToken();
            sendRegistrationToServer(tkn);
        }
    }

    private void sendRegistrationToServer(String tkn) {
        if(mUser != null) {
            DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("Tokens")
                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid());
            mRef.setValue(tkn);
        }
    }
}
