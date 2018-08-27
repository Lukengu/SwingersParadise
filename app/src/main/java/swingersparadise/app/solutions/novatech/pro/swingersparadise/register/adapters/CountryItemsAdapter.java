package swingersparadise.app.solutions.novatech.pro.swingersparadise.register.adapters;

import android.content.Context;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import swingersparadise.app.solutions.novatech.pro.swingersparadise.R;
import swingersparadise.app.solutions.novatech.pro.swingersparadise.utils.Country;


public class CountryItemsAdapter extends BaseAdapter {
    private ArrayList<Country> countries;
    private ArrayList<String> country_list ;
    private Context context;
    private boolean no_bg;

    public CountryItemsAdapter(Context context, ArrayList<Country> countries, boolean no_bg) {

        this.countries =countries;
        this.context = context;
        this.no_bg =no_bg;

    }

    @Override
    public int getCount() {
        return countries.size();
    }

    @Override
    public @Nullable
    Country getItem(int position) {
        return countries.get(position);
    }

    /**
     * Returns the position of the specified item in the array.
     *
     * @param item The item to retrieve the position of.
     *
     * @return The position of the specified item.
     */
    public int getPosition(@Nullable Country item) {
        return countries.indexOf(item);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Get the data item for this position
        final  View view;
        Country country = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            int res = no_bg ? R.layout.spinner_country_textview_no_bg : R.layout.spinner_country_textview;
            convertView =  LayoutInflater.from(context).inflate(res, parent, false);
            view =convertView;
        } else {
            view = convertView;
        }
        // Lookup view for data population

        TextView textView =  view.findViewById(R.id.country_name);
        ImageView imageView = view.findViewById(R.id.flag);;

        // Populate the data into the template view using the data object

        textView.setText(country.getName());
        Glide
                .with(context)
                .load(country.getFlag())
                .into(imageView);
        //Log.d("Country Flag", country.getFlag());


        // Return the completed view to render on screen

        return convertView;

    }

}
