/*
 * Copyright 2009 Henrik Paul.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package org.lightframe.components.client.ui;

import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Timer;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.UIDL;
import com.vaadin.terminal.gwt.client.ui.VTextField;

public class VSuperImmediateTextField extends VTextField implements
        KeyUpHandler {

    /** Set the tagname used to statically resolve widget from UIDL. */
    public static final String TAGNAME = "superimmediatetextfield";

    /** Set the CSS class name to allow styling. */
    public static final String CLASSNAME = "v-" + TAGNAME;

    public static final String PROPERTY_KEYPRESSED = "keypressed";
    public static final String ATTRIBUTE_DELAY = "delay";
    public static final int DEFAULT_DELAY = 300;

    private int delayMillis = DEFAULT_DELAY;
    private Timer timer = null;

    public VSuperImmediateTextField() {
        this(DOM.createInputText());
    }

    protected VSuperImmediateTextField(Element node) {
        super(node);

        setStyleName(CLASSNAME);
        addKeyUpHandler(this);
    }

    public void onKeyUp(KeyUpEvent event) {
        if (delayMillis == 0) {
            sendSuperImmediateEvent();
        }

        else if (delayMillis > 0) {
            if (timer == null) {
                timer = newTimer();
            } else {
                timer.cancel();
            }

            timer.schedule(delayMillis);
        }
    }

    @Override
    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        super.updateFromUIDL(uidl, client);

        if (client.updateComponent(this, uidl, true)) {
            return;
        }

        if (uidl.hasAttribute(ATTRIBUTE_DELAY)) {
            delayMillis = uidl.getIntAttribute(ATTRIBUTE_DELAY);
        }
    }

    private Timer newTimer() {
        return new Timer() {
            public void run() {
                sendSuperImmediateEvent();
                timer = null;
            }
        };
    }

    public void sendSuperImmediateEvent() {
        client.updateVariable(client.getPid(this), PROPERTY_KEYPRESSED, true,
                false);
        client.updateVariable(client.getPid(this), "text", getText(), false);
        client.sendPendingVariableChanges();
    }
}
