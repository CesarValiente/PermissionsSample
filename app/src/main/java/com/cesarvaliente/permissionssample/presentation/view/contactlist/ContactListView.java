package com.cesarvaliente.permissionssample.presentation.view.contactlist;

import java.util.List;

import com.cesarvaliente.permissionssample.presentation.model.ContactModel;

/**
 * Created by Cesar on 08/09/15.
 */
public interface ContactListView {

    void showProgressDialog();

    void hideShowProgressDialog();

    void updateAdapter(List<ContactModel> contacts);
}
