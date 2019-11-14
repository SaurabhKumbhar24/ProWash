package Pro.Wash.Customer;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Pro.Wash.Adapters.ImageAdapter;
import Pro.Wash.R;

public class ImageManager extends AppCompatActivity {

    private Button mConfirm , mCancel;
    private ImageView mDImage;
    private AlertDialog DialogBox;

    private Pro.Wash.Adapters.ImageAdapter ImageAdapter;

    private FirebaseDatabase mDatabase;
    private FirebaseUser mUser;
    private DatabaseReference mReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_manager);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabase = FirebaseDatabase.getInstance();

        AlertDialog.Builder mBuilder;
        View mView;
        mBuilder = new AlertDialog.Builder(ImageManager.this);
        mView = getLayoutInflater().inflate(R.layout.image_dialog_confirm,null);

        mDImage  = mView.findViewById(R.id.image_view);
        mConfirm = mView.findViewById(R.id.dialog_confirm_Btn);
        mCancel  = mView.findViewById(R.id.dialog_cancel_Btn);

        mBuilder.setView(mView);
        DialogBox = mBuilder.create();

        GridView ImageGridView;
        ImageGridView = findViewById(R.id.GridView);
        ImageGridView.setAdapter(new ImageAdapter(this));

        ImageGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                Toast.makeText(getApplicationContext(), "Position :" + position,
                        Toast.LENGTH_SHORT).show();

                ImageAdapter = new ImageAdapter();
                final int Image = ImageAdapter.mThumbIds[position];

                DialogBox.show();
                mDImage.setImageResource(Image);
                mConfirm.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogBox.dismiss();
                        mReference =  mDatabase.getReference().child("Profile Photo").child(mUser.getUid());
                        mReference.setValue(position);
                        startActivity(new Intent(ImageManager.this,MyAccount.class));
                        finish();
                    }
                });
                mCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DialogBox.dismiss();
                    }
                });
            }
        });

    }
}
