package swingersparadise.app.solutions.novatech.pro.swingersparadise.register.fragments;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

import swingersparadise.app.solutions.novatech.pro.swingersparadise.R;
import swingersparadise.app.solutions.novatech.pro.swingersparadise.register.fragments.listeners.SetErrorListener;

import static swingersparadise.app.solutions.novatech.pro.swingersparadise.utils.ArrayUtils.getPosition;


public class Details extends Fragment implements SetErrorListener {
    private Spinner marital_status,gender,build,ethnicity,body_part,hair_colour,smoking,drinking;
    EditText name, surname,age;
    SharedPreferences spref;
    SharedPreferences.Editor editor;
    LinearLayout parent;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.details, container, false);

        spref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
        editor = spref.edit();


        name = view.findViewById(R.id.name);
        surname = view.findViewById(R.id.surname);
        age = view.findViewById(R.id.age);

        marital_status = view.findViewById(R.id.marital_status);
        gender = view.findViewById(R.id.gender);
        build = view.findViewById(R.id.build);
        ethnicity = view.findViewById(R.id.ethnicity);
        body_part = view.findViewById(R.id.body_part);
        hair_colour = view.findViewById(R.id.hair_colour);
        smoking = view.findViewById(R.id.smoking);
        drinking = view.findViewById(R.id.drinking);
        parent = view.findViewById(R.id.parent);


        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_textview,
                getActivity().getResources().getStringArray(R.array.marital_status));
        marital_status.setAdapter(adapter);


        adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_textview,
                getActivity().getResources().getStringArray(R.array.gender));
        gender.setAdapter(adapter);

        adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_textview,
                getActivity().getResources().getStringArray(R.array.build));
        build.setAdapter(adapter);

        adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_textview,
                getActivity().getResources().getStringArray(R.array.ethnicity));
        ethnicity.setAdapter(adapter);

        adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_textview,
                getActivity().getResources().getStringArray(R.array.body_part));
        body_part.setAdapter(adapter);

        adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_textview,
                getActivity().getResources().getStringArray(R.array.hair_colour));
        hair_colour.setAdapter(adapter);


        adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_textview,
                getActivity().getResources().getStringArray(R.array.smoking));
        smoking.setAdapter(adapter);


        adapter = new ArrayAdapter<>(getActivity(), R.layout.spinner_textview,
                getActivity().getResources().getStringArray(R.array.drinking));
        drinking.setAdapter(adapter);

        name.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean  hasfocus) {
                if(!hasfocus && !TextUtils.isEmpty(name.getText()) ){

                    if(spref.contains("name"))
                        editor.remove("name").commit();

                    editor.putString("name", name.getText().toString()).commit();

                }

            }
        });
        surname.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean  hasfocus) {
                if(!hasfocus && !TextUtils.isEmpty(surname.getText()) ){

                    if(spref.contains("surname"))
                        editor.remove("surname").commit();

                    editor.putString("surname", surname.getText().toString()).commit();

                }

            }
        });
        age.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean  hasfocus) {
                if(!hasfocus && !TextUtils.isEmpty(age.getText()) ){

                    if(spref.contains("age"))
                        editor.remove("age").commit();

                    editor.putInt("age", Integer.parseInt(age.getText().toString())).commit();

                }

            }
        });

        marital_status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = marital_status.getSelectedItem().toString();


                if(!"Marital Status".equals(selected)){
                    if(spref.contains("marital_status"))
                        editor.remove("marital_status").commit();

                    editor.putString("marital_status", selected).commit();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });




        gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = gender.getSelectedItem().toString();


                if(spref.contains("gender"))
                    editor.remove("gender").commit();

                //editor.putString("gender",  gender.getSelectedItem().toString()).commit();

                editor.putString("gender", gender.getSelectedItem().toString()).commit();

                editor.putString("gender", gender.getSelectedItem().toString()).commit();


            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        build.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = build.getSelectedItem().toString();


                if(!"Build".equals(selected)){
                    if(spref.contains("build"))
                        editor.remove("build").commit();

                    editor.putString("build", selected).commit();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        ethnicity.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = ethnicity.getSelectedItem().toString();


                if(!"Ethnicity".equals(selected)){
                    if(spref.contains("ethnicity"))
                        editor.remove("ethnicity").commit();

                    editor.putString("ethnicity", selected).commit();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        body_part.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = ethnicity.getSelectedItem().toString();


                if(!"Body Parts".equals(selected)){
                    if(spref.contains("body_part"))
                        editor.remove("body_part").commit();

                    editor.putString("body_part", selected).commit();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        hair_colour.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = hair_colour.getSelectedItem().toString();


                if(!"Hair Colour".equals(selected)){
                    if(spref.contains("hair_colour"))
                        editor.remove("hair_colour").commit();

                    editor.putString("hair_colour", selected).commit();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        smoking.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = smoking.getSelectedItem().toString();


                if(!"Smoking".equals(selected)){
                    if(spref.contains("smoking"))
                        editor.remove("smoking").commit();


                    editor.putString("smoking", selected).commit();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        drinking.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String selected = drinking.getSelectedItem().toString();
                if(!"Drinking".equals(selected)){
                    if(spref.contains("drinking"))
                        editor.remove("drinking").commit();

                    editor.putString("drinking", selected).commit();

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        loadData();
        return view;
    }
    private void loadData() {


        name.setText(spref.contains("name") ? spref.getString("name","") :"");
        surname.setText(spref.contains("surname") ? spref.getString("surname","") :"");
        age.setText(spref.contains("age") ? String.valueOf(spref.getInt("age",0) ) :"");

        gender.setSelection(!spref.contains("gender") ? 0: getPosition(spref.getString("gender",""), getActivity().getResources().getStringArray(R.array.gender)), true );
        marital_status.setSelection(!spref.contains("marital_status") ? 0: getPosition(spref.getString("marital_status",""), getActivity().getResources().getStringArray(R.array.marital_status)), true );
        build.setSelection(!spref.contains("build") ? 0: getPosition(spref.getString("build",""), getActivity().getResources().getStringArray(R.array.build)), true);

        ethnicity.setSelection(!spref.contains("ethnicity") ? 0: getPosition(spref.getString("ethnicity",""), getActivity().getResources().getStringArray(R.array.ethnicity)), true);
        body_part.setSelection(!spref.contains("body_part") ? 0: getPosition(spref.getString("body_part",""), getActivity().getResources().getStringArray(R.array.body_part)), true);
        hair_colour.setSelection(!spref.contains("hair_colour") ? 0:  getPosition(spref.getString("hair_colour",""), getActivity().getResources().getStringArray(R.array.hair_colour)), true);
        smoking.setSelection(!spref.contains("smoking") ? 0:  getPosition(spref.getString("smoking",""),
                getActivity().getResources().getStringArray(R.array.smoking)), true);
        drinking.setSelection(!spref.contains("drinking") ? 0:
                getPosition(spref.getString("drinking",""), getActivity().getResources().getStringArray(R.array.drinking)), true);


    }



    @Override
    public void setError() {

        Snackbar snackbar = Snackbar.make(parent, "Gender Missing", Snackbar.LENGTH_LONG);

        View sbView = snackbar.getView();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            sbView.setBackground(getActivity().getDrawable(R.drawable.snackbar_error));
        } else {
            sbView.setBackground(getActivity().getResources().getDrawable(R.drawable.snackbar_error));
        }



        snackbar.show();
    }

    @Override
    public void setData() {

    }
}
