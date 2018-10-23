package com.mirkoalicastro.birdgame.physics;

import com.google.fpl.liquidfun.Contact;
import com.google.fpl.liquidfun.ContactListener;

public class MyContactListener extends ContactListener {
    public MyContactListener() {
        super();
    }
    public void beginContact(Contact contact) {
        super.beginContact(contact);

    }
}
