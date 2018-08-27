package swingersparadise.app.solutions.novatech.pro.swingersparadise.register.fragments;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;


import swingersparadise.app.solutions.novatech.pro.swingersparadise.R;
import swingersparadise.app.solutions.novatech.pro.swingersparadise.Register;
import swingersparadise.app.solutions.novatech.pro.swingersparadise.register.fragments.listeners.SetErrorListener;
import swingersparadise.app.solutions.novatech.pro.swingersparadise.ui.MultiSelectionSpinner;


public class Preferences extends Fragment implements SetErrorListener {

    private ImageView profile_image;

    private MultiSelectionSpinner sexual_preferences;
    private CheckedTextView terms_conditions;
    private boolean ACCEPT_TERMS_CONDITIONS = true;
    private static final int GET_FROM_GALLERY = 0;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    Bitmap selectedImage;
    EditText about_me, registered_pin;
    SharedPreferences spref;
    SharedPreferences.Editor editor;
    LinearLayout parent;
    Button resend_sms;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.preferences, container, false);
            sexual_preferences = view.findViewById(R.id.sexual_preferences);
            profile_image = view.findViewById(R.id.profile_image);
            terms_conditions = view.findViewById(R.id.terms_conditions);
            about_me  = view.findViewById(R.id.about_me);
            parent = view.findViewById(R.id.parent);
            spref = PreferenceManager.getDefaultSharedPreferences(getActivity().getApplicationContext());
            editor = spref.edit();
            registered_pin = view.findViewById(R.id.registered_pin);
            registered_pin.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if( editable.length() == 5) {
                        int pin = Integer.parseInt(registered_pin.getText().toString());

                        if( pin == spref.getInt("otp",0)){
                           // editor.putBoolean("has_registered", true).commit();
                            // editor.putBoolean("is_login", true).commit();
                            ((Register) getActivity()).firebaseRegistration();
                        } else {
                            registered_pin.setError("Invalid pin Entered");
                        }
                    }

                }
            });
            resend_sms = view.findViewById(R.id.resend_sms);
            resend_sms.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendOTP();
                }
            });





            sexual_preferences.setItems(getActivity().getResources().getStringArray(R.array.sexual_preferences));





          //  sexual_preferences.setAdapter(adapter);




            terms_conditions.setCheckMarkDrawable(R.drawable.ic_check_black_24dp);
            if(spref.contains("terms_conditions"))
               editor.remove("terms_conditions").commit();

            editor.putBoolean("terms_conditions", ACCEPT_TERMS_CONDITIONS).commit();


            terms_conditions.setOnClickListener(new View.OnClickListener(){

                    @Override
                    public void onClick(View view) {
                       ACCEPT_TERMS_CONDITIONS = !ACCEPT_TERMS_CONDITIONS;
                       //terms_conditions.setChecked(ACCEPT_TERMS_CONDITIONS);
                        if(ACCEPT_TERMS_CONDITIONS) {
                            terms_conditions.setCheckMarkDrawable(R.drawable.ic_check_black_24dp);
                        } else {
                            terms_conditions.setCheckMarkDrawable(null);
                        }

                        if(spref.contains("terms_conditions"))
                            editor.remove("terms_conditions").commit();
                        
                        editor.putBoolean("terms_conditions", ACCEPT_TERMS_CONDITIONS).commit();


                        //Toast.makeText(getActivity(), "Selected Item: " +ACCEPT_TERMS_CONDITIONS, Toast.LENGTH_SHORT).show();

                    }
                });

            about_me.setOnFocusChangeListener(new  View.OnFocusChangeListener(){

                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    if(!hasFocus) {
                        if(spref.contains("about_me"))
                            editor.remove("about_me").commit();

                        editor.putString("about_me",  about_me.getText().toString()).commit();
                    }
                }
            });
            registerForContextMenu(profile_image);


            loadData();
            /***
             *  String s = spinner.getSelectedItemsAsString();
             Log.e("getSelected", s);
             */
            return view;
    }

    private void loadData(){
        Log.e("Sexual Prerences", spref.getString("sexual_preferences",""));
       if(spref.contains("selected_image")) {
        /*   Glide
                   .with(this)

                   .load(spref.getString("selected_image",""))
                   .into(profile_image);*/
           BitmapFactory.Options options = new BitmapFactory.Options();
           options.inPreferredConfig = Bitmap.Config.ARGB_8888;
           Bitmap bitmap = BitmapFactory.decodeFile(spref.getString("selected_image",""), options);
           profile_image.setImageBitmap(Bitmap.createScaledBitmap(bitmap, 136, 136, false));


       }
        if(spref.contains("sexual_preferences")) {
            sexual_preferences.setSelection(spref.getString("sexual_preferences","").split(","));
        }
        about_me.setText(spref.contains("about_me")?  spref.getString("about_me",""):"");

        if(spref.contains("terms_conditions") && spref.getBoolean("terms_conditions", false) ) {
            terms_conditions.setCheckMarkDrawable(R.drawable.ic_check_black_24dp);
        } else {
            terms_conditions.setCheckMarkDrawable(null);
        }

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu,v,menuInfo);
        menu.setHeaderTitle("Profile Photo");
        menu.add(0, v.getId(), 0, "Take Photo");
        menu.add(1, v.getId(), 0, "From Gallery");
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if("From Gallery".equals(item.getTitle())){
            startActivityForResult(
                    new Intent(
                            Intent.ACTION_PICK,
                            MediaStore.Images.Media.INTERNAL_CONTENT_URI
                    ),
                    GET_FROM_GALLERY
            );
        }
        if("Take Photo".equals(item.getTitle())){
            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }

        }
        return true;
    }
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==GET_FROM_GALLERY && resultCode == Activity.RESULT_OK) {

            Bitmap bitmap = null;
            try {
               // Uri profileImage = data.getData();

              //  ((Register) getActivity()).setProfileImage(profileImage);

                selectedImage = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(),data.getData());
                profile_image.setImageBitmap(Bitmap.createScaledBitmap(selectedImage, 136, 136, false));
                if(spref.contains("selected_image"))
                    editor.remove("selected_image").commit();

                editor.putString("selected_image",   getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath().concat("/profile_img.jpg"));
                saveBitmapToPath(selectedImage);

            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK) {
            Bundle extras = data.getExtras();
            selectedImage  = (Bitmap) extras.get("data");

            profile_image.setImageBitmap(Bitmap.createScaledBitmap(selectedImage, 136, 136, false));
            editor.putString("selected_image",   getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath().concat("/profile_img.jpg"));
            saveBitmapToPath(selectedImage);

           // ((Register) getActivity()).setProfileImage( Uri.fromFile( new File(getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath().concat("/profile_img.jpg"))));
            //saveBitmapToPath(imageBitmap);
        }






    }
    protected void saveBitmapToPath(Bitmap bitmap) {
        //String root = Environment.getExternalStorageDirectory().toString();
        //File myDir = new File(root +  get);
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        storageDir.mkdirs();
        File file = new File(storageDir, "profile_img.jpg");
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setError() {
        if(!spref.contains("selected_image")){
            Snackbar snackbar = Snackbar.make(parent, "Profile Image Missing", Snackbar.LENGTH_LONG);

            View sbView = snackbar.getView();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                sbView.setBackground(getActivity().getDrawable(R.drawable.snackbar_error));
            } else {
                sbView.setBackground(getActivity().getResources().getDrawable(R.drawable.snackbar_error));
            }



            snackbar.show();

        }

    }

    @Override
    public void setData() {
        if (spref.contains("sexual_preferences"))
            editor.remove("sexual_preferences").commit();
           editor.putString("sexual_preferences", sexual_preferences.getSelectedItemsAsString()).commit();
    }


    public void finishRegistration() {


        registered_pin.setVisibility(View.VISIBLE);
        resend_sms.setVisibility(View.VISIBLE);
        sendOTP();

    }



    private void sendOTP() {
       /* int min = 11111;
        int max = 99999;

        Random r = new Random();
        int  otp = r.nextInt((max - min) + 1) + min;

        if(spref.contains("otp"))
            editor.remove("otp").commit();

        editor.putInt("otp",otp).commit();

        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("Sending Message...");
        progressDialog.show();

        SMSPortal sms = new SMSPortal(getActivity(), new OnServiceResponseListener() {
            @Override
            public void onSuccess(Object object) {
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(ClientException e) {
                progressDialog.dismiss();
            }
        });
        ArrayList<String> numbers =  new ArrayList<String>();
        String cell_no = spref.getString("dial_code","").replace("+","").concat(
                spref.getString("mobile_number",""));
        System.out.println("Cell_no"+cell_no);
        numbers.add(cell_no);
        sms.sendMS(   "Welcome "+spref.getString("display_name","")+" your otp to login is " +otp ,numbers);*/

    }
}
