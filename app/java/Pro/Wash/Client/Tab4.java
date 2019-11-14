package Pro.Wash.Client;


import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Pro.Wash.Adapters.OrderAdaptertab4Client;
import Pro.Wash.R;

public class Tab4 extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private ListView mListView;
    private ArrayList<String> OID = new ArrayList<String>();
    private ArrayList<String> OType = new ArrayList<String>();
    private ArrayList<String> PDate = new ArrayList<String>();
    private ArrayList<String> PTime = new ArrayList<String>();
    private ArrayList<String> key = new ArrayList<String>();
    private OrderAdaptertab4Client orderAdapter;

    private OnFragmentInteractionListener mListener;

    public Tab4() {
        // Required empty public constructor
    }

    public static Tab4 newInstance(String param1, String param2) {
        Tab4 fragment = new Tab4();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tab4, container, false);

        mListView = view.findViewById(R.id.ListViewtab4);
        ShowOrders();

        return view;
    }
    //Show Orders To Client
    private void ShowOrders() {

        DatabaseReference mReference = FirebaseDatabase.getInstance().getReference().child("Orders");
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    if (ds.child("Order Status").getValue(String.class) != null
                            && ds.child("Order Status").getValue(String.class)
                            .equals("Processing Order")) {

                        key.add(ds.getKey());

                        OID.add("#" + ds.getKey());

                        OType.add(ds.child("Order Type").getValue(String.class));

                        PDate.add(ds.child("Pickup Date").getValue(String.class));

                        PTime.add(ds.child("Pickup Time").getValue(String.class));

                    }
                }

                if (getContext() != null) {
                    orderAdapter = new OrderAdaptertab4Client(getContext()
                            , OID, OType, PDate, PTime, key);
                    orderAdapter.notifyDataSetChanged();
                    mListView.setAdapter(orderAdapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
       void onFragmentInteraction(Uri uri);
    }
}