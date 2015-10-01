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

import com.cesarvaliente.permissionssample.presentation.model.ContactModel;

/**
 * Created by Cesar on 13/09/15.
 */
public interface MainView {

    void configureActionBar();

    void setActionBarTitle(String title);

    void setActionBarHome(boolean home);

    void showProgressBar();

    void hideProgressBar();

    void showContactListFragment();

    void showContactFragment(ContactModel contact);

    void requestReadContacts();

    void requestSaveImage();

    void requestSendSMS();
}
