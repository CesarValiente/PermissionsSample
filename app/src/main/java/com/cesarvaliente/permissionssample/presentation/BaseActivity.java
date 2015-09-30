/**
 * Copyright (C) 2015 Cesar Valiente (cesar.valiente@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.cesarvaliente.permissionssample.presentation;

import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewStub;
import android.widget.TextView;

import com.cesarvaliente.permissionssample.BuildConfig;
import com.cesarvaliente.permissionssample.R;

/**
 * Created by Cesar on 26/09/15.
 */
public class BaseActivity extends AppCompatActivity {

    protected View rationaleView;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
        case KeyEvent.KEYCODE_BACK:
            if (dismissPermissionRationale() != 0) {
                return true;
            }
        default:
            return super.onKeyDown(keyCode, event);
        }
    }

    protected void createAndShowPermissionRationale(int action, int titleResId, int subtitleResId) {
        if (rationaleView == null) {
            rationaleView = ((ViewStub) findViewById(R.id.permission_rationale_stub)).inflate();
        } else {
            rationaleView.setVisibility(View.VISIBLE);
        }
        ((TextView) rationaleView.findViewById(R.id.rationale_title)).setText(titleResId);
        ((TextView) rationaleView.findViewById(R.id.rationale_subtitle)).setText(subtitleResId);
        rationaleView.setTag(action);
    }

    protected void showSnackBarPermissionMessage(int message) {
        final CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.main_coordinator_layout);
        Snackbar snackbar = Snackbar.make(coordinatorLayout, getString(message), Snackbar.LENGTH_LONG)
                                    .setAction(getString(R.string.snackbar_settings), new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            Intent intent = new Intent(android.provider.Settings
                                                    .ACTION_APPLICATION_DETAILS_SETTINGS);
                                            intent.setData(Uri.parse("package:" + BuildConfig.APPLICATION_ID));
                                            startActivity(intent);
                                        }
                                    });
        snackbar.show();
    }

    /**
     * Dismiss and returns the action associated to the rationale
     * @return
     */
    protected int dismissPermissionRationale() {
        if (rationaleView != null && rationaleView.getVisibility() == View.VISIBLE) {
            rationaleView.setVisibility(View.GONE);
            return (int) rationaleView.getTag();
        }
        return 0;
    }

    public void onDismissRationaleClick(View view) {
        dismissPermissionRationale();
    }
}
