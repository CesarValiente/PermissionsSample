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
package com.cesarvaliente.permissionssample.presentation.view;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import butterknife.Bind;
import butterknife.ButterKnife;
import com.cesarvaliente.permissionssample.R;
import com.cesarvaliente.permissionssample.action.PermissionAction;
import com.cesarvaliente.permissionssample.action.impl.permission.PermissionActionFactory;
import com.cesarvaliente.permissionssample.presentation.BaseActivity;
import com.cesarvaliente.permissionssample.presentation.model.ContactModel;
import com.cesarvaliente.permissionssample.presentation.presenter.PermissionPresenter;
import com.cesarvaliente.permissionssample.presentation.presenter.PermissionPresenter.PermissionCallbacks;
import com.cesarvaliente.permissionssample.presentation.view.contact.ContactFragment;
import com.cesarvaliente.permissionssample.presentation.view.contactlist.ContactListFragment;

import static com.cesarvaliente.permissionssample.action.impl.permission.PermissionActionFactory.SUPPORT_IMPL;
import static com.cesarvaliente.permissionssample.presentation.presenter.PermissionPresenter.ACTION_READ_CONTACTS;
import static com.cesarvaliente.permissionssample.presentation.presenter.PermissionPresenter.ACTION_SAVE_IMAGE;
import static com.cesarvaliente.permissionssample.presentation.presenter.PermissionPresenter.ACTION_SEND_SMS;


public class MainActivity extends BaseActivity implements MainView, PermissionCallbacks {
    public final static String CURRENT_CONTACT_KEY = "current_contact";

    @Bind(R.id.toolbar) Toolbar toolbar;
    @Bind(R.id.progress) ProgressBar progressBar;

    private PermissionPresenter permissionPresenter;

    private ContactListFragment contactListFragment;
    private ContactFragment contactFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        PermissionActionFactory permissionActionFactory = new PermissionActionFactory(this);
        PermissionAction permissionAction = permissionActionFactory.getPermissionAction(SUPPORT_IMPL);

        permissionPresenter = new PermissionPresenter(permissionAction, this);

        configureActionBar();
        showContactListFragment();
    }

    @Override
    public void configureActionBar() {
        if (toolbar != null) {
            setSupportActionBar(toolbar);
            setActionBarHome(false);
            setActionBarTitle(getString(R.string.app_name));
        }
    }

    @Override
    public void showContactListFragment() {
        contactListFragment = new ContactListFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.content, contactListFragment);
        transaction.commit();
    }

    @Override
    public void showContactFragment(ContactModel contact) {
        contactFragment = ContactFragment.newInstance(contact);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content, contactFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void setActionBarTitle(String title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public void setActionBarHome(boolean home) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(home);
    }

    @Override
    public void showProgressBar() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
        }
        return true;
    }

    //---- Requesting permissions. Fragments will call this functions -----//

    @Override
    public void requestReadContactsPermission() {
        permissionPresenter.requestReadContactsPermission(ACTION_READ_CONTACTS);
    }

    @Override
    public void requestWriteExternalStoragePermission() {
        permissionPresenter.requestWriteExternalStorangePermission(PermissionPresenter
                .ACTION_SAVE_IMAGE);
    }

    @Override
    public void requestSendSMS() {
        permissionPresenter.requestSendSMS(PermissionPresenter.ACTION_SEND_SMS);
    }

    //----- Permission management ----//

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
        case ACTION_READ_CONTACTS:
        case ACTION_SAVE_IMAGE:
        case ACTION_SEND_SMS:
            if (permissionPresenter.verifyGrantedPermission(grantResults)) {
                permissionAccepted(requestCode);
            } else {
                permissionDenied(requestCode);
            }
        }
    }

    @Override
    public void permissionAccepted(int action) {
        switch (action) {
        case ACTION_READ_CONTACTS:
            contactListFragment.getPhoneBookContacts();
            break;
        case ACTION_SAVE_IMAGE:
            contactFragment.saveImage();
            break;
        case ACTION_SEND_SMS:
            contactFragment.sendSMS();
            break;
        }
    }

    @Override
    public void permissionDenied(int action) {
        if (dismissPermissionRationale() == 0) {
            showSnackBarPermissionMessage(action);
        }
    }

    @Override
    public void showRationale(int action) {
        switch (action) {
        case ACTION_READ_CONTACTS:
            createAndShowPermissionRationale(action, R.string.rationale_read_contacts_title, R.string
                    .rationale_read_contacts_subtitle);
            break;
        case ACTION_SAVE_IMAGE:
            createAndShowPermissionRationale(action, R.string.rationale_save_image_title, R.string
                    .rationale_save_image_subtitle);
            break;
        case ACTION_SEND_SMS:
            createAndShowPermissionRationale(action, R.string.rationale_send_sms_title, R.string
                    .rationale_send_sms_subtitle);
            break;
        }
    }

    public void onAcceptRationaleClick(View view) {
        int action = dismissPermissionRationale();
        switch (action) {
        case ACTION_READ_CONTACTS:
            permissionPresenter.requestReadContactsPermissionAfterRationale(action);
            break;
        case ACTION_SAVE_IMAGE:
            permissionPresenter.requestWriteExternalStorangePermissionAfterRationale(action);
            break;
        case ACTION_SEND_SMS:
            permissionPresenter.requestSendSMSAfterRationale(action);
            break;
        }
    }

    @Override
    protected void showSnackBarPermissionMessage(int action) {
        switch (action) {
        case ACTION_READ_CONTACTS:
            super.showSnackBarPermissionMessage(R.string.snackbar_read_contacts);
            break;
        case ACTION_SAVE_IMAGE:
            super.showSnackBarPermissionMessage(R.string.snackbar_save_image);
            break;
        case ACTION_SEND_SMS:
            super.showSnackBarPermissionMessage(R.string.snackbar_sms);
            break;
        }
    }
}
