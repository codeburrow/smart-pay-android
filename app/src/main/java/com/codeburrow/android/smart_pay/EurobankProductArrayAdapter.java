package com.codeburrow.android.smart_pay;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * @author George Spiridakis <george@codeburrow.com>
 * @since Jun/26/2016.
 * ===================================================
 * ---------->    http://codeburrow.com    <----------
 * ===================================================
 */
public class EurobankProductArrayAdapter extends ArrayAdapter<EurobankProduct> {

    public EurobankProductArrayAdapter(Context context, ArrayList<EurobankProduct> eurobankProducts) {
        super(context, 0, eurobankProducts);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position.
        EurobankProduct eurobankProduct = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view.
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_account_list_view, parent, false);
        }
        // Lookup view for data population.
        TextView bankTextView = (TextView) convertView.findViewById(R.id.bank_text_view);
        TextView contractNumberTextView = (TextView) convertView.findViewById(R.id.contract_number_text_view);
        TextView balanceTextView = (TextView) convertView.findViewById(R.id.balance_text_view);
        // Populate the data into the template view using the EurobankProduct data access object.
        bankTextView.setText("EUR"); // It is hard-coded to "EUROBANK" for the purposes of the hackathon
        contractNumberTextView.setText(eurobankProduct.getContractNumber());
        balanceTextView.setText(String.valueOf(eurobankProduct.getBalance()));
        // Return the completed view to be rendered on screen.
        return convertView;
    }
}
