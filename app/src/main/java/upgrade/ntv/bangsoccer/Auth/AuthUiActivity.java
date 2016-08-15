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
 *//*


package upgrade.ntv.bangsoccer.Auth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.MainThread;
import android.support.annotation.StringRes;
import android.support.annotation.StyleRes;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.RadioButton;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import upgrade.ntv.bangsoccer.R;

public class AuthUiActivity extends AppCompatActivity {

    private static final String UNCHANGED_CONFIG_VALUE = "SS0";

    private static final String GOOGLE_TOS_URL =
            "https://www.google.com/policies/terms/";
    private static final String FIREBASE_TOS_URL =
            "https://www.firebase.com/terms/terms-of-service.html";

    private static final int RC_SIGN_IN = 100;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

     */
/*
        if (auth.getCurrentUser() != null) {
            startActivity(SignedInActivity.createIntent(this));
            finish();
        }
*//*

        //setContentView(R.layout.auth_ui_layout);
        FirebaseAuth auth = FirebaseAuth.getInstance();
        ButterKnife.bind(this);

        startActivityForResult(
                AuthUI.getInstance().createSignInIntentBuilder()
                        .setTheme(AuthUI.getDefaultTheme())
                        .setLogo(R.drawable.mcancha)
                        .setProviders(getSelectedProviders())
                        .setTosUrl(FIREBASE_TOS_URL)
                        .setTheme(R.style.AppTheme)
                        .build(),
                RC_SIGN_IN);

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            handleSignInResponse(resultCode, data);
            return;
        }

        showSnackbar(R.string.unknown_response);
    }

    @MainThread
    private void handleSignInResponse(int resultCode, Intent data) {
        if (resultCode == RESULT_OK) {
            startActivity(SignedInActivity.createIntent(this));
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
            selectedProviders.add(AuthUI.FACEBOOK_PROVIDER);
            selectedProviders.add(AuthUI.GOOGLE_PROVIDER);


        return selectedProviders.toArray(new String[selectedProviders.size()]);
    }


    public static Intent createIntent(Context context) {
        Intent in = new Intent();
        in.setClass(context, AuthUiActivity.class);
        return in;
    }
}
*/
