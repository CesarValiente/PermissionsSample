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
package com.cesarvaliente.permissionssample.presentation.view.contact;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import com.cesarvaliente.permissionsample.domain.executor.PostExecutionThread;
import com.cesarvaliente.permissionsample.domain.executor.ThreadExecutor;
import com.cesarvaliente.permissionssample.R;
import com.cesarvaliente.permissionssample.action.ImageAction;
import com.cesarvaliente.permissionssample.action.SMSAction;
import com.cesarvaliente.permissionssample.action.executor.JobExecutor;
import com.cesarvaliente.permissionssample.action.impl.ImageActionImpl;
import com.cesarvaliente.permissionssample.action.impl.SMSActionImpl;
import com.cesarvaliente.permissionssample.action.usecase.ImageUseCase;
import com.cesarvaliente.permissionssample.action.usecase.ImageUseCaseImpl;
import com.cesarvaliente.permissionssample.presentation.model.ContactModel;
import com.cesarvaliente.permissionssample.presentation.presenter.ContactPresenter;
import com.cesarvaliente.permissionssample.presentation.utils.ImageUtils;
import com.cesarvaliente.permissionssample.presentation.view.MainActivity;
import com.cesarvaliente.permissionssample.presentation.view.MainView;
import com.cesarvaliente.permissionssample.presentation.view.broadcastreceiver.SMSReceiver;
import com.cesarvaliente.permissionssample.presentation.view.executor.UIThread;

/**
 * Created by Cesar on 10/09/15.
 */
public class ContactFragment extends Fragment implements ContactView {
    @Bind(R.id.photo) ImageView photo;
    @Bind(R.id.email) TextView email;
    @Bind(R.id.phone) TextView phone;
    @Bind(R.id.sms_edittext) EditText smsEditText;

    private MainView mainView;
    private ContactPresenter contactPresenter;

    private BroadcastReceiver sentSMSReceiver;
    private BroadcastReceiver deliveredSMSReceiver;

    public static ContactFragment newInstance(ContactModel contact) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(MainActivity.CURRENT_CONTACT_KEY, contact);

        ContactFragment fragment = new ContactFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public ContactModel getCurrentContact() {
        return getArguments().getParcelable(MainActivity.CURRENT_CONTACT_KEY);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Here we could a dependency injector like dagger to inject the dependencies.
        // I've decided not to use them since I want that this sample app is really to understand even for
        // beginners.
        ThreadExecutor threadExecutor = JobExecutor.getInstance();
        PostExecutionThread postExecutionThread = UIThread.getInstance();
        ImageAction imageAction = new ImageActionImpl(this.getContext());
        ImageUseCase imageUseCase = new ImageUseCaseImpl(imageAction, threadExecutor, postExecutionThread);
        SMSAction smsAction = new SMSActionImpl(this.getContext());
        contactPresenter = new ContactPresenter(this, imageUseCase, smsAction);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contact, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mainView = (MainView) getActivity();

        setupReceivers();
        setupActionBar();
        setFields();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(sentSMSReceiver);
        getActivity().unregisterReceiver(deliveredSMSReceiver);
    }

    private void setupReceivers() {
        sentSMSReceiver = SMSReceiver.getSMSSentReceiver();
        deliveredSMSReceiver = SMSReceiver.getSMSDeliveredReceiver();
        getActivity().registerReceiver(sentSMSReceiver, new IntentFilter(SMSActionImpl.SMS_SENT));
        getActivity().registerReceiver(deliveredSMSReceiver, new IntentFilter(SMSActionImpl.SMS_DELIVERED));
    }

    private void setFields() {
        ContactModel contact = getCurrentContact();
        int width = getResources().getDimensionPixelOffset(R.dimen.contact_photo_width);
        int height = getResources().getDimensionPixelOffset(R.dimen.contact_photo_height);
        ImageUtils.loadImage(getActivity(), contact.getImageUri(), photo, width, height);

        email.setText(contact.getEmail());
        phone.setText(contact.getPhone());
    }

    private void setupActionBar() {
        ContactModel contact = getCurrentContact();
        mainView.setActionBarTitle(contact.getName());
        mainView.setActionBarHome(true);
    }

    @OnClick(R.id.photo)
    public void onSaveImageClick() {
        mainView.requestWriteExternalStoragePermission();
    }

    public void saveImage() {
        contactPresenter.savePhoto(getCurrentContact().getImageUri(), getCurrentContact().getName());
    }

    @OnClick(R.id.sms_button)
    public void onSendSMSClick() {
        String message = smsEditText.getText().toString();
        if (!TextUtils.isEmpty(message)) {
            mainView.requestSendSMS();
        }
    }

    public void sendSMS() {
        contactPresenter.sendSMS(getCurrentContact().getPhone(), smsEditText.getText().toString());
    }

    @Override
    public void imageSaved(final boolean result) {
        Snackbar snackbar;
        if (result) {
            snackbar = Snackbar.make(photo, getString(R.string.image_saved), Snackbar.LENGTH_LONG);
        } else {
            snackbar = Snackbar.make(photo, getString(R.string.image_not_saved), Snackbar.LENGTH_LONG);
        }
        snackbar.show();
    }
}
