package Pro.Wash.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import Pro.Wash.Customer.TrackOrder;
import Pro.Wash.R;

public class CustomerMyOrdersAdapter extends BaseAdapter implements ListAdapter {

    private Context context;
    private ArrayList<String> OrderId;
    private ArrayList<String> OrderType;
    private ArrayList<String> OrderDate;
    private ArrayList<String> OrderStatus;

    private Button ViewReceipt;

    public CustomerMyOrdersAdapter(Context context,
                                   ArrayList<String> orderId,
                                   ArrayList<String> orderType,
                                   ArrayList<String> orderDate,
                                   ArrayList<String> orderStatus) {
        this.context = context;
        OrderId = orderId;
        OrderType = orderType;
        OrderDate = orderDate;
        OrderStatus = orderStatus;
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert inflater != null;
            view = inflater.inflate(R.layout.customer_myorders_new, null);


            TextView SetOrderId = view.findViewById(R.id.OrderIdTV);
            TextView SetOrderType = view.findViewById(R.id.toSetOrderType);
            TextView OrderPlacedDate = view.findViewById(R.id.toSetPlacedDateAndTime);
            TextView OrderStat = view.findViewById(R.id.OrderStatus);
            Button OrderDetails = view.findViewById(R.id.ViewOrderDetails);

            ViewReceipt = view.findViewById(R.id.ViewReceipt);

            String OID = "Order #" + OrderId.get(position);
            SetOrderId.setText(OID);
            SetOrderType.setText(OrderType.get(position));
            OrderPlacedDate.setText(OrderDate.get(position));
            OrderStat.setText(OrderStatus.get(position));

            OrderDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent OrderDet = new Intent(context, TrackOrder.class);
                    OrderDet.putExtra("Order Id", OrderId.get(position));
                    context.startActivity(OrderDet);
                }
            });

            ViewReceipt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    GenerateReceipt();
                }
            });
        }
        return view;
    }

    private void GenerateReceipt(){
        //TODO: Generate Receipt Dialog Box
    }

}
