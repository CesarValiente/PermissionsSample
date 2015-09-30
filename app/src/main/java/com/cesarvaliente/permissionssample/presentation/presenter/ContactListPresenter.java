/**
 * Copyright (C) 2015 Cesar Valiente based in the code of Fernando Cejas
 * https://github.com/android10/Android-CleanArchitecture
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
package com.cesarvaliente.permissionssample.presentation.presenter;

import java.util.List;

import com.cesarvaliente.permissionsample.domain.entity.ContactEntity;
import com.cesarvaliente.permissionsample.domain.usecase.ListContactsUseCase;
import com.cesarvaliente.permissionssample.presentation.model.ContactModel;
import com.cesarvaliente.permissionssample.presentation.model.mapper.ContactModelMapper;
import com.cesarvaliente.permissionssample.presentation.view.contactlist.ContactListView;

/**
 * Created by Cesar on 06/09/15.
 */
public class ContactListPresenter {
    private ContactListView contactListView;
    private ListContactsUseCase listContactsUseCase;

    public ContactListPresenter(ContactListView contactListView, ListContactsUseCase listContactsUseCase) {
        this.contactListView = contactListView;
        this.listContactsUseCase = listContactsUseCase;
    }

    public void getPhoneBookContacts() {
        ListContactsUseCase.UiCallbacks uiCallbacks = new ListContactsUseCase.UiCallbacks() {
            @Override
            public void onComplete(List<ContactEntity> contacts) {
                //FIXME Is more correct if the transformation would have made it before to use the ui thread
                List<ContactModel> contactModels = ContactModelMapper.transform(contacts);
                contactListView.hideShowProgressDialog();
                contactListView.updateAdapter(contactModels);
            }

            @Override
            public void onError(String error) {
                contactListView.hideShowProgressDialog();
            }
        };
        contactListView.showProgressDialog();
        listContactsUseCase.execute(uiCallbacks);
    }
}
