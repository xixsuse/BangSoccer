/*
 * Copyright 2016 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the
 * License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
 * express or implied. See the License for the specific language governing permissions and
 * limitations under the License.
 */

package upgrade.ntv.bangsoccer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginManager;
import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FacebookAuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.firebase.ui.auth.AuthUI.FACEBOOK_PROVIDER;


public class SignedInActivity extends AppCompatActivity {

    @BindView(R.id.user_profile_picture)
    ImageView mUserProfilePicture;

    @BindView(R.id.user_email)
    TextView mUserEmail;

    @BindView(R.id.user_display_name)
    TextView mUserDisplayName;

    @BindView(R.id.user_enabled_providers)
    TextView mEnabledProviders;

    @BindView(android.R.id.content)
    View mRootView;

    private static final String FIREBASE_TOS_URL =
            "https://www.firebase.com/terms/terms-of-service.html";

    private static final int RC_SIGN_IN = 100;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser == null) {
            startActivityForResult(
                    AuthUI.getInstance().createSignInIntentBuilder()
                            .setIsSmartLockEnabled(false)
                            //.setTheme(AuthUI.getDefaultTheme())
                            .setLogo(R.drawable.mcancha)
                            .setProviders(FACEBOOK_PROVIDER)
                            .setTosUrl(FIREBASE_TOS_URL)
                            .setTheme(R.style.AppTheme)
                            //.setIsSmartLockEnabled(!BuildConfig.DEBUG)
                            .build(),
                    RC_SIGN_IN);
            return;
        }else{

            FacebookSdk.sdkInitialize(getApplicationContext());
            facebookPermissions();
            finish();
            startActivity(ActivityMain.createIntent(this));

        }

    }

    @OnClick(R.id.sign_out)
    public void signOut() {
        AuthUI.getInstance()
                .signOut(this)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            startActivityForResult(
                                    AuthUI.getInstance().createSignInIntentBuilder()
                                            .setTheme(R.style.AppTheme)
                                            .setLogo(R.drawable.mcancha)
                                            .setProviders(FACEBOOK_PROVIDER)
                                            .setTosUrl(FIREBASE_TOS_URL)
                                            .setTheme(R.style.AppTheme)
                                            .build(),
                                    RC_SIGN_IN);
                        } else {
                            showSnackbar(R.string.sign_out_failed);
                        }
                    }
                });
    }

    //@OnClick(R.id.delete_account)
    public void deleteAccountClicked() {

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to delete this account?")
                .setPositiveButton("Yes, nuke it!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        deleteAccount();
                    }
                })
                .setNegativeButton("No", null)
                .create();

        dialog.show();
    }

    private void deleteAccount() {
        FirebaseAuth.getInstance()
                .getCurrentUser()
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            startActivityForResult(
                                    AuthUI.getInstance().createSignInIntentBuilder()
                                            .setTheme(AuthUI.getDefaultTheme())
                                            .setLogo(R.drawable.mcancha)
                                            .setProviders(FACEBOOK_PROVIDER)
                                            .setTosUrl(FIREBASE_TOS_URL)
                                            .setTheme(R.style.AppTheme)
                                            .build(),
                                    RC_SIGN_IN);
                        } else {
                            showSnackbar(R.string.delete_account_failed);
                        }
                    }
                });
    }

    @MainThread
    private void populateProfile() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user.getPhotoUrl() != null) {
            Glide.with(this)
                    .load(user.getPhotoUrl())
                    .fitCenter()
                    .into(mUserProfilePicture);
        }

        mUserEmail.setText(
                TextUtils.isEmpty(user.getEmail()) ? "No email" : user.getEmail());
        mUserDisplayName.setText(
                TextUtils.isEmpty(user.getDisplayName()) ? "No display name" : user.getDisplayName());

        StringBuilder providerList = new StringBuilder();

        providerList.append("Providers used: ");

        if (user.getProviders() == null || user.getProviders().isEmpty()) {
            providerList.append("none");
        } else {
            Iterator<String> providerIter = user.getProviders().iterator();
            while (providerIter.hasNext()) {
                String provider = providerIter.next();
                /*if (GoogleAuthProvider.PROVIDER_ID.equals(provider)) {
                    providerList.append("Google");
                } else */if (FacebookAuthProvider.PROVIDER_ID.equals(provider)) {
                    providerList.append("Facebook");
                } /*else if (EmailAuthProvider.PROVIDER_ID.equals(provider)) {
                    providerList.append("Password");*/
                else {
                    providerList.append(provider);
                }

                if (providerIter.hasNext()) {
                    providerList.append(", ");
                }
            }
        }

        mEnabledProviders.setText(providerList);
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            FacebookSdk.sdkInitialize(getApplicationContext());
            facebookPermissions();

            handleSignInResponse(resultCode, data);
            return;
        }

        showSnackbar(R.string.unknown_response);
    }
    private void facebookPermissions(){

        //Permission for Likes

        LoginManager.getInstance().
                logInWithPublishPermissions(
                this,
                Arrays.asList("publish_actions"));

    }

    @MainThread
    private void handleSignInResponse(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            startActivity(ActivityMain.createIntent(this));
            finish();
            return;
        }

        if (resultCode == RESULT_CANCELED) {
            showSnackbar(R.string.sign_in_cancelled);
            return;
        }

        showSnackbar(R.string.unknown_sign_in_response);
    }


    @MainThread
    private String[] getSelectedProviders() {
        ArrayList<String> selectedProviders = new ArrayList<>();

        //add providers
        selectedProviders.add(AuthUI.EMAIL_PROVIDER);
        selectedProviders.add(FACEBOOK_PROVIDER);
        selectedProviders.add(AuthUI.GOOGLE_PROVIDER);


        return selectedProviders.toArray(new String[selectedProviders.size()]);
    }

    @MainThread
    private void showSnackbar(@StringRes int errorMessageRes) {
        try{
            Snackbar.make(mRootView, errorMessageRes, Snackbar.LENGTH_LONG)
                    .show();
        }catch (Exception e){
            Log.e("signinactivity: ", e.getMessage());
        }
    }

    public static Intent createIntent(Context context) {
        Intent in = new Intent();
        in.setClass(context, SignedInActivity.class);
        return in;
    }
}
