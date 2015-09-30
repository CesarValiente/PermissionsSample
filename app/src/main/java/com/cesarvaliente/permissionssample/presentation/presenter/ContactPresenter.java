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

import android.net.Uri;

import com.cesarvaliente.permissionssample.action.SMSAction;
import com.cesarvaliente.permissionssample.action.usecase.ImageUseCase;
import com.cesarvaliente.permissionssample.presentation.view.contact.ContactView;


/**
 * Created by Cesar on 12/09/15.
 */
public class ContactPresenter {
    private ContactView contactView;
    private ImageUseCase useCase;
    private SMSAction smsAction;

    public ContactPresenter(ContactView contactView, ImageUseCase useCase, SMSAction smsAction) {
        this.contactView = contactView;
        this.useCase = useCase;
        this.smsAction = smsAction;
    }

    public void savePhoto(final Uri uri, final String name) {

        ImageUseCase.Callbacks callbacks = new ImageUseCase.Callbacks() {
            @Override
            public void onComplete(boolean result) {
                contactView.imageSaved(result);
            }

            @Override
            public void onError(String message) {
                contactView.imageSaved(false);
            }
        };
        useCase.execute(uri, name, callbacks);
    }

    public void sendSMS(String phoneNumber, String message) {
        //This operation doesn't need to create or to run implicity in another thread
        //since the OS will take care of this operation by itself.
        smsAction.sendSMS(phoneNumber, message);
    }
}
