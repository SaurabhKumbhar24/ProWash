package Pro.Wash.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import Pro.Wash.R;

public class ClientMyOrdersAdapter extends BaseAdapter implements ListAdapter {

    private Context context;
    private ArrayList<String> OrderId;
    private ArrayList<String> OrderType;
    private ArrayList<String> PickupDate;
    private ArrayList<String> PickupTime;

    public ClientMyOrdersAdapter(Context context,
                                   ArrayList<String> orderId,
                                   ArrayList<String> orderType,
                                   ArrayList<String> pickupDate,
                                   ArrayList<String> pickupTime) {
        this.context = context;
        OrderId = orderId;
        OrderType = orderType;
        PickupDate = pickupDate;
        PickupTime = pickupTime;
    }

    @Override
    public int getCount() {
        return OrderId.size();
    }

    @Override
    public Object getItem(int position) {
        return OrderId.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert inflater != null;
            view = inflater.inflate(R.layout.client_myorder, null);
        }

        TextView SetOrderId = view.findViewById(R.id.COrderIdTV);
        TextView SetOrderType = view.findViewById(R.id.CtoSetOrderType);
        TextView SetPickupDate = view.findViewById(R.id.CtoSetPickupDate);
        TextView SetPickupTime = view.findViewById(R.id.CtoSetPickupTime);

        String OID ="#" + OrderId.get(position);
        SetOrderId.setText(OID);
        SetOrderType.setText(OrderType.get(position));
        SetPickupDate.setText(PickupDate.get(position));
        SetPickupTime.setText(PickupTime.get(position));

        return view;
    }
}
