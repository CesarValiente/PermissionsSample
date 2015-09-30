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
package com.cesarvaliente.permissionssample.presentation.model.mapper;

import java.util.ArrayList;
import java.util.List;

import android.net.Uri;
import android.provider.ContactsContract;

import com.cesarvaliente.permissionsample.domain.entity.ContactEntity;
import com.cesarvaliente.permissionssample.presentation.model.ContactModel;

/**
 * Created by Cesar on 23/09/15.
 */
public class ContactModelMapper {

    public static ContactModel transform(ContactEntity contactEntity) {
        if (contactEntity == null) {
            throw new IllegalArgumentException("Contact can not be null");
        }
        ContactModel contactModel = new ContactModel(contactEntity.getName(), contactEntity.getPhone());
        contactModel.setEmail(contactEntity.getEmail());
        Uri uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, contactEntity.getId());
        contactModel.setImageUri(uri);

        return contactModel;
    }

    public static List<ContactModel> transform(List<ContactEntity> contactEntityList) {
        if (contactEntityList == null) {
            throw new IllegalArgumentException("Contact list can not be null");
        }
        List<ContactModel> contactModels = new ArrayList<>(contactEntityList.size());
        for (ContactEntity contact : contactEntityList) {
            ContactModel contactModel = transform(contact);
            contactModels.add(contactModel);
        }
        return contactModels;
    }
}
