package mx.cetys.jorgepayan.whatsonsale.Controllers.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import mx.cetys.jorgepayan.whatsonsale.Models.Location;
import mx.cetys.jorgepayan.whatsonsale.R;

/**
 * Created by fidel on 12/2/2017.
 */

public class LocationAdapter extends ArrayAdapter<Location> {
    public LocationAdapter(Context context){
        super(context, R.layout.location_row, R.id.text_view_locationName);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = super.getView(position, convertView, parent);

        TextView locationName = (TextView) view.findViewById(R.id.text_view_locationName);
        TextView locationAddress = (TextView) view.findViewById(R.id.text_view_locationAddress);

        Location location = this.getItem(position);

        locationName.setText(location.getName());
        locationAddress.setText(location.getAddress());

        return view;
    }

}