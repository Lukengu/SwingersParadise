package swingersparadise.app.solutions.novatech.pro.swingersparadise;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;

import swingersparadise.app.solutions.novatech.pro.swingersparadise.utils.BitmapUtils;


public class Login extends Activity {

    CallbackManager callbackManager;
    TextView register_link,reset_password;
    LoginButton loginButton;

    private static final String EMAIL = "email";
    private static final String PUBLIC_PROFILE = "public_profile";
    private static final String TAG = "LoginActivity";
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authStateListener;
    private GoogleSignInClient mGoogleSignInClient;
    private DatabaseReference users_db;
    private static final int RC_SIGN_IN = 9001;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        setContentView(R.layout.activity_login);
        register_link =  findViewById(R.id.register_link);
        reset_password =  findViewById(R.id.reset_password);

      //  register_link.setMovementMethod(LinkMovementMethod.getInstance());
       // reset_password.setMovementMethod(LinkMovementMethod.getInstance());
        users_db = FirebaseDatabase.getInstance().getReference().child("users");


        String text = "Don't have a account yet . Please <a href=\"app://register\">Register</a>";
        String reset = "<a href=\"app://reset_password\">Reset</a> Password";
        mAuth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {

                    startActivity(new Intent(Login.this, Content.class));
                    finish();
                }
            }
        };



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            register_link.setText(Html.fromHtml(text, Html.FROM_HTML_MODE_COMPACT));
            reset_password.setText(Html.fromHtml(reset, Html.FROM_HTML_MODE_COMPACT));
        } else {
            register_link.setText(Html.fromHtml(text));
            reset_password.setText(Html.fromHtml(reset));
        }
        callbackManager = CallbackManager.Factory.create();
        loginButton = findViewById(R.id.login_button);
        loginButton.setReadPermissions(Arrays.asList(EMAIL, PUBLIC_PROFILE));

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Log.d(TAG, "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                // App code
                //Toast.makeText(Login.this, "Cancelled", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(FacebookException exception) {
                // App code
                Log.e("FACEBOOK ERROR", exception.getMessage());

                //Toast.makeText(Login.this, exception.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        //Google
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

    }

    private void handleFacebookAccessToken(final AccessToken accessToken) {
        Log.d(TAG, "handleFacebookAccessToken:" + accessToken);

        AuthCredential credential = FacebookAuthProvider.getCredential(accessToken.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            //Save User Data in Firebase Db before starting  the intent

                            GraphRequest request =  GraphRequest.newMeRequest(accessToken, new  GraphRequest.GraphJSONObjectCallback(){

                                @Override
                                public void onCompleted(final JSONObject object, GraphResponse response) {
                                    final FirebaseUser user = mAuth.getCurrentUser();
                                   // Log.d("FacebookProfile", object.toString());
                                    users_db.child(user.getUid()).child("name").setValue(object.optString("name"));
                                    users_db.child(user.getUid()).child("email").setValue(object.optString("email"));
                                    users_db.child(user.getUid()).child("display_name").setValue(object.optString("email"));
                                    users_db.child(user.getUid()).child("gender").setValue(object.optString("gender"));
                                    users_db.child(user.getUid()).child("birthday").setValue(object.optString("birthday"));
                                    //Uri uri  = Uri.fromParts()

                                    new Thread( new Runnable(){

                                        @Override
                                        public void run() {
                                            try {

                                                StorageReference storageReference =  FirebaseStorage.getInstance().getReference().child("profiles/"+ user.getUid());
                                                BitmapUtils.saveBitmapToPath(getRemoteProfilePicture("https://graph.facebook.com/" + object.getString("id") + "/picture?type=large"), Login.this, "profile_img.jpg");
                                                String file_name = Login.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath().concat("/profile_img.jpg");
                                                final Uri file_path = Uri.fromFile(new File(file_name));
                                                if(file_path != null) {
                                                    storageReference.putFile(file_path);
                                                }

                                            } catch(IOException e) {
                                            } catch (JSONException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }).start();


                                }
                            });
                            Bundle parameters = new Bundle();
                            parameters.putString("fields", "id,name,email,gender, birthday,picture");
                            request.setParameters(parameters);
                            request.executeAsync();


                            //FirebaseUser user = mAuth.getCurrentUser();
                            //updateUI(user);
                            startActivity(new Intent(Login.this, Content.class));
                            finish();
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();


                        }


                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException e) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e);
                // ...
            }
        } else {

            callbackManager.onActivityResult(requestCode, resultCode, data);
        }

    }

    private void firebaseAuthWithGoogle(final GoogleSignInAccount account) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + account.getId());

        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "signInWithCredential:success");
                            final FirebaseUser user = mAuth.getCurrentUser();
                            // Log.d("FacebookProfile", object.toString());
                            users_db.child(user.getUid()).child("name").setValue(account.getGivenName().concat(" ").concat(account.getFamilyName()));

                            users_db.child(user.getUid()).child("email").setValue(account.getEmail());
                            users_db.child(user.getUid()).child("display_name").setValue(account.getDisplayName());
                            StorageReference storageReference =  FirebaseStorage.getInstance().getReference().child("profiles/"+ user.getUid());


                            new Thread( new Runnable(){

                                @Override
                                public void run() {
                                    try {

                                        StorageReference storageReference =  FirebaseStorage.getInstance().getReference().child("profiles/"+ user.getUid());
                                        BitmapUtils.saveBitmapToPath(getRemoteProfilePicture(account.getPhotoUrl().toString()), Login.this, "profile_img.jpg");
                                        String file_name = Login.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES).getPath().concat("/profile_img.jpg");
                                        final Uri file_path = Uri.fromFile(new File(file_name));
                                        if(file_path != null) {
                                            storageReference.putFile(file_path);
                                        }

                                    } catch(IOException e) {
                                    }
                                }
                            }).start();


                            startActivity(new Intent(Login.this, Content.class));
                          //  updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithCredential:failure", task.getException());
                            Snackbar.make(findViewById(R.id.main_content), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            //updateUI(null);
                        }

                        // ...
                    }
                });
    }

    public void facebook_login(View view) {
        loginButton.performClick();
    }

    @Override
    protected void onStart() {
        super.onStart();
       // FirebaseUser currentUser = mAuth.getCurrentUser();
        mAuth.addAuthStateListener(authStateListener);
        //updateUI(currentUser);
    }


    @Override
    protected void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(authStateListener);
    }

    public void open_register(View view) {
        startActivity(new Intent(Login.this, Register.class));
        
    }

    public void google_signin(View view) {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    private static Bitmap getRemoteProfilePicture(String url) throws IOException {
        URL imageURL = new URL(url);
        Bitmap bitmap = BitmapFactory.decodeStream(imageURL.openConnection().getInputStream());

        return bitmap;
    }

    public void email_sign_in(View view) {
        startActivity(new Intent(Login.this, Register.class));
    }
}
