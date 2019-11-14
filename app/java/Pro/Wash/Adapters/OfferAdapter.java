package Pro.Wash.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import Pro.Wash.R;

public class OfferAdapter extends BaseAdapter implements ListAdapter {

    private Context context;
    private ArrayList<String> UptoRs;
    private ArrayList<String> Promocode;

    public OfferAdapter(Context context, ArrayList<String> uptoRs, ArrayList<String> promocode) {
        this.context = context;
        UptoRs = uptoRs;
        Promocode = promocode;
    }

    @Override
    public int getCount() {
        return Promocode.size();
    }

    @Override
    public Object getItem(int position) {
        return Promocode.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;
        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert inflater != null;
            view = inflater.inflate(R.layout.custom_offer_manager, null);
        }

        TextView UptoRsTV = view.findViewById(R.id.UptoRO);
        TextView PromocodeTV = view.findViewById(R.id.ProCode);


        UptoRsTV.setText(UptoRs.get(position));
        PromocodeTV.setText(Promocode.get(position));

        return view;
    }
}
