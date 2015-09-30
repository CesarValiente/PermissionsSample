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

package com.cesarvaliente.permissionssample.action.data.mapper;

import java.util.ArrayList;
import java.util.List;

import com.cesarvaliente.permissionsample.domain.entity.ContactEntity;
import com.cesarvaliente.permissionssample.action.data.ContactData;

/**
 * Created by Cesar on 26/09/15.
 */
public class ContactDataMapper {

    public static ContactEntity transform(ContactData contactData) {
        if (contactData == null) {
            throw new IllegalArgumentException("Contact can not be null");
        }
        ContactEntity contactEntity = new ContactEntity(contactData.getId(), contactData.getName(), contactData
                .getPhone());
        contactEntity.setEmail(contactData.getEmail());

        return contactEntity;
    }

    public static List<ContactEntity> transform(List<ContactData> contactDataList) {
        if (contactDataList == null) {
            throw new IllegalArgumentException("Contact list can not be null");
        }
        List<ContactEntity> contactModels = new ArrayList<>(contactDataList.size());
        for (ContactData contact : contactDataList) {
            ContactEntity contactModel = transform(contact);
            contactModels.add(contactModel);
        }
        return contactModels;
    }
}
