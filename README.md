# PermissionsSample
Sample app that shows how the new permissions model introduced in Android 6.0 works, and how we can implement it and educate our users on a nice way (following clean architecture, using architectural patterns, etc.)

# Requirements
To use properly this app we first we have to create some contacts in our device/emulator, adding a name, a phone number and an image would be perfect so we can try all the stuff the app does.
If you are running this app in a device and you already have contacts, then you don't have to do anything else, just run it.

# Do you want to see first what this app does?
Sure! here you have some screenshots that show how the app requests permissions for 1st and 2nd (or N time), and how it educates users showing the rationale info panel and a snackbar when the permission has been denied "forever".

![Permission request 1st time][1]
![Permission request 2nd time][2]
![Showing rationale][3]
![Showing snackbar-permission denied][4]

[1]: ./art/permission-request-contacts.png
[2]: ./art/permission-request-contact-2nd.png
[3]: ./art/sampleapp_image_rationale.png
[4]: ./art/sampleapp_image_snackbar.png


And [here](https://www.youtube.com/watch?v=COiFMgbPzhg&feature=youtu.be) you can watch a small video.

# Architecture of the sample app
This app has been implemented based in the teachings of [Clean Architecture](https://blog.8thlight.com/uncle-bob/2012/08/13/the-clean-architecture.html) by [Uncle Bob](https://twitter.com/unclebobmartin). I would like to remark that is based, so is not strictly following it, since at the end, we are software developers who have to take these teachings and adjust them to our requirements, preferences, tastes, etc.

For an amazing and better example of Clean architecture on Android, or better said, the Clean approach followed by its author, check this amazing post of my friend [Fernando Cejas](http://fernandocejas.com/2014/09/03/architecting-android-the-clean-way/).

The app is divided in three modules, to make easier the decoupling between the different parts of the app.
- App (presentation): Is basically the UI, it has Android dependencies. Is built following the MVP pattern, so the views (Activity and Fragment) delegates (invokes) their presenters to deal with what the user tries to do.
- Domain: is where the user cases are, the presenter will use the use cases that are here to get what it wants. Is a simple Java module without Android dependencies.
- Action: Since this app sample is highly coupled to the Android framework, this module (an Android one) implements and declares actions that exposes outside. In this case with some of the actions that don't require a transformation model, that are just actions that even we couldn't return a result, the actions are used directly by the app.

# How the new permissions model is implemented?
Since the main purpose of this app is just to play with the new Android permissions model and show how to educate users I will cover here how this is implemented.

First, in the App module (the presentation layer) we have the [MainActivity](https://github.com/CesarValiente/PermissionsSample/blob/master/app/src/main/java/com/cesarvaliente/permissionssample/presentation/view/MainActivity.java) that has a [PermissionAction](https://github.com/CesarValiente/PermissionsSample/blob/master/action/src/main/java/com/cesarvaliente/permissionssample/action/PermissionAction.java) instance that is given by a Factory. This factory can implements and returns one of the two different PermissionAction implementations:
   
- Using the methods provided in the framework (targetSdk 23). [ActivityPermissionImpl](https://github.com/CesarValiente/PermissionsSample/blob/master/action/src/main/java/com/cesarvaliente/permissionssample/action/impl/permission/ActivityPermissionActionImpl.java)
- Using the support library v4 version 23. [SupportPermissionActionImpl](https://github.com/CesarValiente/PermissionsSample/blob/master/action/src/main/java/com/cesarvaliente/permissionssample/action/impl/permission/SupportPermissionActionImpl.java)

This implementation of the PermissionAction it will be injected to the [PermissionPresenter](https://github.com/CesarValiente/PermissionsSample/blob/master/app/src/main/java/com/cesarvaliente/permissionssample/presentation/presenter/PermissionPresenter.java) that will take care of interacting with the implementation in a transparent way for the View.

Here we would use a dependency injector like Dagger or a Service Locator, but I decided not to use it since this app is to teach about other stuff (adding the separation of concerns just has added enough difficult to a simple implementation). On next iterations of this sample app, I would use a depenency injector like Dagger2.

In the PermissionPresenter we also have passed to its constructor a [PermissionCallbacks](https://github.com/CesarValiente/PermissionsSample/blob/master/app/src/main/java/com/cesarvaliente/permissionssample/presentation/presenter/PermissionPresenter.java#L85) that the MainActivity is implementing; this callback will be used by the PermissionPresenter to inform the View the process has been finished.

# Testing
Testing the permissions functionality here is really easy. Since the PermissionPresenter's dependencies have been injected, we can mock both for being able to change their behaviour at glance, and checking the PermissionCallbacks methods wether they were called or not when some action was done.

You can check [PermissionPresenterTest](https://github.com/CesarValiente/PermissionsSample/blob/master/app/src/test/java/com/cesarvaliente/permissionssample/presentation/presenter/PermissionPresenterTest.java) to see how is implemented.

# UI
As we have seen here, the MainActivity actor is the one that is going to access and to interact with the permissions stuff, its fragments will just ask him to do some actions, but the fragments don't know anything about permissions, they are completely agnostic about that.

Then it makes perfectly sense that is the MainActivity the one that has the UI stuff of permissions.
Its layout has a CoordinatorLayout, used to make possible the dismiss action of the snackbar, and a ViewStub, that it will be used to inflate in runtime the View we want to use, to educate our users when the permission is already asked (It's called rationale).

# Slides
The slides of this sample app can be found in SpeackerDeck [here](https://goo.gl/MKYLbZ).

# Links and acknowledgements
I don't want to finish without linking to the awesome developers that have helped me a lot during the creation of this sample app. Their amazing posts and source code repos are a must that any developer should have a look.

New permissions model on Android:
- [New permissions model](https://developer.android.com/preview/features/runtime-permissions.html).
- [Android Patterns Permissions](https://www.google.com/design/spec/patterns/permissions.html#permissions-usage).
- [Exploring the new android permissions model (Joe Birch)](https://medium.com/ribot-labs/exploring-the-new-android-permissions-model-ba1d5d6c0610).
- [Everything every Android de must know about new Android’s runtime permission (nuuneoi)](http://inthecheesefactory.com/blog/things-you-need-to-know-about-android-m-permission-developer-edition/en).

Architecture:
- [The Clean Architecture (Uncle Bob)](http://blog.8thlight.com/uncle-bob/2012/08/13/the-clean-architecture.html).
- [Architecting Android… The clean way? (Fernando Cejas)](http://fernandocejas.com/2014/09/03/architecting-android-the-clean-way/).
- [MVP for Android: how to organise the presentation layer (Antonio Leiva)](http://antonioleiva.com/mvp-android/).
- [Effective Android UI (Pedro V. Gómez Sánchez)](https://github.com/pedrovgs/EffectiveAndroidUI).
- [Engineering Wunderlist for Android (César Valiente & Wunderlist Android team )](https://speakerdeck.com/cesarvaliente/engineering-wunderlist-for-android).

And I would like to thank as well to [Fine Cinnamon (OJTHandler)](https://github.com/FineCinnamon) for all the knowledge and good people that can be found there (and funny times too).

# One last thing...
This is an open source sample app released under [Apache License v2](http://www.apache.org/licenses/LICENSE-2.0), so it means that you can grab the code an use it in your open source projects that use compatible licenses with Apache, but also in your privatives ones.

This license requires attribution, so if you are going to use the code, respect the attribution, respect the class headers that mention the license terms and author name, and if you are going to release your app (that uses this code) to the world (the binary, the apk) then create a section in your app where you mention all the libraries your app uses including author name, code license and code source link.

And that's all, hope this approach on managing the new android permissions model helps you! Thanks :-)
