package swingersparadise.app.solutions.novatech.pro.swingersparadise;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

import swingersparadise.app.solutions.novatech.pro.swingersparadise.register.adapters.ViewPagerAdapter;
import swingersparadise.app.solutions.novatech.pro.swingersparadise.register.fragments.Credentials;
import swingersparadise.app.solutions.novatech.pro.swingersparadise.register.fragments.Details;
import swingersparadise.app.solutions.novatech.pro.swingersparadise.register.fragments.Preferences;

public class Register extends AppCompatActivity {

    private ViewPager viewPager;
    private TextView page_1, page_2, page_3;
    private int position = 0;
    private Button btn_next, btn_prev;
    private SharedPreferences spref;
    private SharedPreferences.Editor editor;
    private ViewPagerAdapter adapter;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private FirebaseStorage storage;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        spref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = spref.edit();

        storage = FirebaseStorage.getInstance();
        storageReference =  storage.getReference();

        mAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user != null) {


                }


            }
        };
        setTitle("Register");

      /*  viewPager = findViewById(R.id.viewPager);
        page_1 = findViewById(R.id.page_1);
        page_2 = findViewById(R.id.page_2);
        page_3 = findViewById(R.id.page_3);
        btn_next = findViewById(R.id.btn_next);
        btn_prev = findViewById(R.id.btn_prev);

        spref = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        editor = spref.edit();
        adapter = new ViewPagerAdapter(getSupportFragmentManager());

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                if(i == 1 && !validateNext()) {
                    viewPager.setCurrentItem(0, true);
                    ((Credentials) adapter.getItem(0)).setError();
                }
            }

            @Override
            public void onPageSelected(int i) {
                switch(i)
                {
                    case 0:
                        page_1.setEnabled(true);
                        page_2.setEnabled(false);
                        page_3.setEnabled(false);
                        break;
                    case 1:
                        page_1.setEnabled(false);
                        page_2.setEnabled(true);
                        page_3.setEnabled(false);
                        break;
                    case 2:
                        page_1.setEnabled(false);
                        page_2.setEnabled(false);
                        page_3.setEnabled(true);
                        break;
                }
                position = i;
                //  btn_next.setEnabled(position  != 2);
                btn_prev.setEnabled(position  != 0);
                if(position == 2) {
                    btn_next.setText("Finish");
                } else {
                    btn_next.setText("Next");
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });

        btn_next.setOnClickListener(next);
        btn_prev.setOnClickListener(previous);



        adapter.addFragment(new Credentials(), "Credentials");
        adapter.addFragment(new Details(), "Details");
        adapter.addFragment(new Preferences(), "Preferences");

        viewPager.setAdapter(adapter);

        storage = FirebaseStorage.getInstance();
        storageReference =  storage.getReference();

        firebaseAuth = FirebaseAuth.getInstance();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user != null) {


                }


            }
        };
        */

    }
    protected  View.OnClickListener next= new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if(position != 2){
                if( position < 3 && validateNext()) {
                    ++position;
                    viewPager.setCurrentItem(position, true);
                } else{
                    Fragment f = adapter.getItem(position);
                    switch(position){
                        case 0:
                            ((Credentials) f).setError();
                            break;
                        case 1 :
                            ((Details) f).setError();
                            break;
                    }

                }
            } else {
                Fragment f = adapter.getItem(2);
                ((Preferences) f).setData();
                if(validateFinish()) {

                    ((Preferences) f).finishRegistration();
                    //btn_next.setEnabled(false);

                } else {

                    ((Preferences) f).setError();
                }
            }
        }
    };

    protected  View.OnClickListener previous= new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Fragment f = adapter.getItem(2);
            if( position != 0) {
                --position;
                viewPager.setCurrentItem(position, true);
                ((Preferences) f).setData();
            } else {

            }

        }
    };




    protected boolean validateFinish(){
        return  spref.contains("selected_image") ;
    }

    protected boolean validateNext(){
        if(position == 0)
            return (spref.contains("display_name") && spref.contains("email_address") && spref.contains("password") && spref.contains("conf_password") && spref.contains("mobile_number"));

        if(position == 1)
            return spref.contains("gender");

        return true;
    }


    public void firebaseRegistration() {

        //firebaseAuth.signOut();

        final String email = spref.getString("email_address", "");
        final String password = spref.getString("password", "");
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()) {
                    Log.e("FireBaseRegistration", "UnSuccessfull");
                } else {
                    final String uuid = mAuth.getCurrentUser().getUid();
                    DatabaseReference db = FirebaseDatabase.getInstance().getReference().child("Users").child(spref.getString("gender", "")).child(uuid);

                    db.child("AboutMe").setValue(spref.getString("about_me",""));
                    db.child("BodyPart").setValue(spref.getString("body_part",""));
                    db.child("Build").setValue(spref.getString("build",""));
                    db.child("CellphoneNumber").setValue(spref.getString("dial_code","").concat(spref.getString("mobile_number","")));
                    db.child("Country").setValue(spref.getString("country",""));
                    db.child("DisplayName").setValue(spref.getString("display_name",""));
                    db.child("Drinking").setValue(spref.getString("drinking",""));
                    db.child("Ethnicity").setValue(spref.getString("ethnicity",""));
                    db.child("HairColor").setValue(spref.getString("hair_colour",""));
                    db.child("MaritalStatus").setValue(spref.getString("marital_status",""));
                    db.child("Name").setValue(spref.getString("name",""));
                    db.child("Age").setValue(spref.getInt("age",0));
                    db.child("RefferedBy").setValue(spref.getString("reffered_by",""));
                    db.child("SexualPrefs").setValue(spref.getString("sexual_preferences",""));
                    db.child("Smoking").setValue(spref.getString("smoking",""));
                    db.child("Surname").setValue(spref.getString("surname",""));

                    final ProgressDialog progressDialog = new ProgressDialog(Register.this);
                    progressDialog.setTitle("Logging in...");
                    progressDialog.show();

                    StorageReference ref = storageReference.child("profiles/"+ uuid);
                    final String file_name  = spref.getString("selected_image", "");
                    final Uri file_path = Uri.fromFile(new File(file_name));
                    if(file_path != null) {
                        ref.putFile(file_path)
                                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        progressDialog.dismiss();
                                        editor.putBoolean("has_registered", true).commit();
                                        editor.putString("uuid", uuid).commit();
                                        editor.remove("about_me").commit();
                                        editor.remove("body_part").commit();
                                        editor.remove("build").commit();
                                        editor.remove("country_code").commit();
                                        editor.remove("mobile_number").commit();
                                        editor.remove("country").commit();
                                        editor.remove("display_name").commit();
                                        editor.remove("drinking").commit();
                                        editor.remove("ethnicity").commit();
                                        editor.remove("hair_colour").commit();
                                        editor.remove("marital_status").commit();
                                        editor.remove("name").commit();
                                        editor.remove("reffered_by").commit();
                                        editor.remove("sexual_preferences").commit();
                                        editor.remove("smoking").commit();
                                        editor.remove("selected_image").commit();
                                        editor.remove("email_address").commit();
                                        editor.remove("password").commit();
                                        editor.remove("conf_password").commit();

                                        startActivity(new Intent(Register.this, Content.class));
                                    }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        progressDialog.dismiss();
                                        Toast.makeText(Register.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                                        double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot
                                                .getTotalByteCount());
                                        progressDialog.setMessage("Loging in " + (int) progress + "%");
                                    }
                                });
                    }







                }

            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(authStateListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(authStateListener);
    }
}
