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
package com.cesarvaliente.permissionssample.action.impl;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.text.TextUtils;

import com.cesarvaliente.permissionsample.domain.action.ListContactsAction;
import com.cesarvaliente.permissionsample.domain.entity.ContactEntity;
import com.cesarvaliente.permissionssample.action.data.ContactData;
import com.cesarvaliente.permissionssample.action.data.mapper.ContactDataMapper;

/**
 * Created by Cesar on 23/09/15.
 */
public class ListListContactsActionImpl implements ListContactsAction {

    private Context context;

    public ListListContactsActionImpl(Context context) {
        this.context = context;
    }

    @Override
    public void getPhoneBookContacts(DataCallback dataCallback) {
        List<ContactData> contactDataList = new ArrayList<>();
        Cursor contactsCursor = getContactsCursor();
        if (isCursorValid(contactsCursor)) {
            do {
                String contactId = contactsCursor.getString(contactsCursor.getColumnIndex(
                        ContactsContract.Contacts._ID));
                Cursor emails = getEmailsCursor(contactId);
                Cursor phones = getPHoneCursor(contactId);

                if (isCursorValid(emails) && isCursorValid(phones)) {
                    String name = contactsCursor.getString(contactsCursor.getColumnIndex(
                            ContactsContract.Data.DISPLAY_NAME));
                    String emailAddress = emails.getString(emails.getColumnIndex(ContactsContract
                            .CommonDataKinds.Email.DATA));
                    String phone = phones.getString(phones.getColumnIndex(
                            ContactsContract.CommonDataKinds.Phone.NUMBER));

                    if (!TextUtils.isEmpty(phone)) {
                        ContactData contactData = new ContactData(contactId, name, phone);
                        contactData.setEmail(emailAddress);
                        contactDataList.add(contactData);
                    }
                }
                closeCursor(emails);
                closeCursor(phones);
            } while (contactsCursor.moveToNext());
        }
        closeCursor(contactsCursor);

        notifyOnSuccess(contactDataList, dataCallback);
    }

    private void notifyOnSuccess(List<ContactData> contactDataList, DataCallback dataCallback) {
        List<ContactEntity> contactEntityList = ContactDataMapper.transform(contactDataList);
        dataCallback.onDataLoaded(contactEntityList);
    }

    private Cursor getContactsCursor() {
        String sortOrder = ContactsContract.Contacts.SORT_KEY_PRIMARY;
        String[] projection = { ContactsContract.Contacts._ID,
                ContactsContract.Contacts.LOOKUP_KEY,
                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
                ContactsContract.Contacts.PHOTO_THUMBNAIL_URI,
                sortOrder };
        String selection = ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + "<>''" + " AND " + ContactsContract
                .Contacts.IN_VISIBLE_GROUP + "=1";

        return context.getContentResolver().query(ContactsContract.Contacts.CONTENT_URI,
                projection, selection, null, sortOrder);
    }

    private Cursor getPHoneCursor(String contactId) {
        Uri uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
        String[] projection = new String[] { ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
                ContactsContract.CommonDataKinds.Phone.NUMBER };
        String selection = ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + contactId;

        return context.getContentResolver().query(uri, projection, selection, null, null);
    }

    private Cursor getEmailsCursor(String contactId) {
        String selection = ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + contactId;
        return context.getContentResolver().query(
                ContactsContract.CommonDataKinds.Email.CONTENT_URI, null, selection, null, null);
    }

    private boolean isCursorValid(Cursor cursor) {
        return cursor != null && !cursor.isClosed() && cursor.getCount() > 0 && cursor.moveToFirst();
    }

    private void closeCursor(Cursor cursor) {
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
    }
}
