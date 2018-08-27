package swingersparadise.app.solutions.novatech.pro.swingersparadise.utils;

import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Country implements Serializable {
    private  String name;
    private  String dial_code;
    private  String code;
    private  String flag;


    private  Country(String name, String dial_code, String code){
        this.name = name;
        this.dial_code = dial_code;
        this.code = code;
        this.flag = "https://www.countryflags.io/"+code.toLowerCase()+"/flat/64.png";
    }


    public static List<Country> getList(Context context) {
        List<Country> countries = new ArrayList<Country>();
        String country_code ="";
        JSONArray data = null;
        InputStream inputStream = context.getResources().openRawResource(
                context.getResources().getIdentifier("country_code",
                        "raw", context.getPackageName()));

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        byte buf[] = new byte[1024];
        int len;
        try {
            while ((len = inputStream.read(buf)) != -1) {
                outputStream.write(buf, 0, len);
            }
            country_code = outputStream.toString();

            data = new JSONArray(country_code);
            outputStream.close();
            inputStream.close();
        } catch (IOException e) {

        } catch (JSONException e) {
            e.printStackTrace();
        }
       // System.out.println(data.toString());
        for(int i =0; i < data.length(); i++){
            try {
                JSONObject obj = data.getJSONObject(i);
                countries.add( new Country(obj.getString("name"), obj.getString("dial_code"), obj.getString("code")));

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        Collections.sort(countries, new Comparator<Country>() {
            @Override
            public int compare(Country country1, Country country2) {
                return country1.getName().compareToIgnoreCase(country2.getName());
            }
        });


        return countries;
    }

    public String getName() {
        return name;
    }
    public String getDial_code() {
        return dial_code;
    }
    public String getCode() {
        return code;
    }
    public String getFlag() {
        return flag;
    }


}
