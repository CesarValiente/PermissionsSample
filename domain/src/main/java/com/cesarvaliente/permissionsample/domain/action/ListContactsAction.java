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
package com.cesarvaliente.permissionsample.domain.action;

import java.util.List;

import com.cesarvaliente.permissionsample.domain.entity.ContactEntity;

/**
 * In this case, that the data source can change completely the ListContactAction is good that we have here
 * instead in the action module, so if we change the data source for instance we add a database where we have
 * some contacts, this interface, in a possible module "data" it would be implemented there so the domain could
 * work with it even knowing anything of the implementation.
 */
public interface ListContactsAction {

    interface DataCallback {
        void onDataLoaded(List<ContactEntity> contactEntityList);
    }

    void getPhoneBookContacts(DataCallback dataCallback);
}
