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
package com.cesarvaliente.permissionssample.presentation.view.contactlist;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.Bind;
import butterknife.ButterKnife;
import com.cesarvaliente.permissionsample.domain.action.ListContactsAction;
import com.cesarvaliente.permissionsample.domain.executor.PostExecutionThread;
import com.cesarvaliente.permissionsample.domain.executor.ThreadExecutor;
import com.cesarvaliente.permissionsample.domain.usecase.ListContactsUseCase;
import com.cesarvaliente.permissionsample.domain.usecase.impl.ListContactsUseCaseImpl;
import com.cesarvaliente.permissionssample.R;
import com.cesarvaliente.permissionssample.action.executor.JobExecutor;
import com.cesarvaliente.permissionssample.action.impl.ListListContactsActionImpl;
import com.cesarvaliente.permissionssample.presentation.model.ContactModel;
import com.cesarvaliente.permissionssample.presentation.presenter.ContactListPresenter;
import com.cesarvaliente.permissionssample.presentation.view.MainActivity;
import com.cesarvaliente.permissionssample.presentation.view.MainView;
import com.cesarvaliente.permissionssample.presentation.view.contactlist.recyclerview.Adapter;
import com.cesarvaliente.permissionssample.presentation.view.contactlist.recyclerview.ItemClickListener;
import com.cesarvaliente.permissionssample.presentation.view.executor.UIThread;

/**
 * Created by Cesar on 05/09/15.
 */
public class ContactListFragment extends Fragment implements ContactListView {
    @Bind(R.id.recycler_view) RecyclerView recyclerView;

    private MainView mainView;
    private ContactListPresenter contactListPresenter;

    private RecyclerView.LayoutManager layoutManager;
    private Adapter adapter;
    private List<ContactModel> contactList;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null && !savedInstanceState.isEmpty()) {
            contactList = savedInstanceState.getParcelableArrayList(MainActivity.CURRENT_CONTACT_KEY);
        }

        //Here we could use a ServiceLocator or a dependency injector to inject the depenencies.
        // I've decided not to use them since I want that this sample app is really to understand even for
        // beginners.
        ThreadExecutor threadExecutor = JobExecutor.getInstance();
        PostExecutionThread postExecutionThread = UIThread.getInstance();
        ListContactsAction listContactsAction = new ListListContactsActionImpl(this.getContext());
        ListContactsUseCase listContactsUseCase = new ListContactsUseCaseImpl(listContactsAction, threadExecutor,
                postExecutionThread);
        contactListPresenter = new ContactListPresenter(this, listContactsUseCase);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contact_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupList();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mainView = (MainView) getActivity();
        setActionBar();
    }

    @Override
    public void onStart() {
        super.onStart();
        mainView.requestReadContactsPermission();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(MainActivity.CURRENT_CONTACT_KEY, (ArrayList) contactList);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    private void setActionBar() {
        mainView.setActionBarHome(false);
        mainView.setActionBarTitle(getString(R.string.app_name));
    }

    public void getPhoneBookContacts() {
        if (contactList == null || contactList.isEmpty()) {
            contactListPresenter.getPhoneBookContacts();
        } else {
            refreshAdapter();
        }
    }

    private void setupList() {
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity().getBaseContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.addOnItemTouchListener(
                new ItemClickListener(getActivity(), new ItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        showContactFragment(contactList.get(position));
                    }
                }));
    }

    private void showContactFragment(ContactModel contact) {
        mainView.showContactFragment(contact);
    }

    @Override
    public void showProgressDialog() {
        //FIXME Change that for a nicer way to do it
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mainView.showProgressBar();
            }
        });
    }

    @Override
    public void hideShowProgressDialog() {
        mainView.hideProgressBar();
    }

    @Override
    public void updateAdapter(final List<ContactModel> contacts) {
        contactList = contacts;
        refreshAdapter();
    }

    private void refreshAdapter() {
        adapter = new Adapter(getActivity(), contactList);
        recyclerView.setAdapter(adapter);
    }
}
