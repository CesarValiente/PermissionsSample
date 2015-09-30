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
package com.cesarvaliente.permissionssample.action.usecase;

import com.cesarvaliente.permissionsample.domain.usecase.UseCase;

/**
 * This use case makes use of the {@link UseCase} to be managed by the domain.
 * The reason the Use case is here, and its implementation too, is that to me is an action
 * that is attached to the framework, in this case Android, so it makes no sense create another layer of abstraction
 * in the domain, with its corresponding parsing. For data it makes sense, the data can evolve, can change, but
 * for an action that has to save an image in the device storage using a framework class as is the
 * {@link android.net.Uri} to me it makes no sense that this is in the domain.
 * So I would say, actions that can evolve and can have different implementations non attached to a framework, is
 * perfect using the domain (like getting user data, it can come from the phone book but also form a json file,
 * database, etc.) but an action like save an image, send a sms or permissions, that are completely attached to
 * the framework if they are in the domain, to me is making harder and more complex what is simpler.
 */
public interface ImageUseCase extends UseCase {

    interface Callbacks {
        void onComplete(boolean result);

        void onError(String message);
    }

    void execute(android.net.Uri imageUri, String contactName, Callbacks callbacks);
}
