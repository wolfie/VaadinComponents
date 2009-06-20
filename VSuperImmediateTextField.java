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

package net.lightframe.components.client.ui;

import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.vaadin.terminal.gwt.client.ui.VTextField;

public class VSuperImmediateTextField extends VTextField implements
        KeyUpHandler {

    /** Set the tagname used to statically resolve widget from UIDL. */
    public static final String TAGNAME = "superimmediatetextfield";

    /** Set the CSS class name to allow styling. */
    public static final String CLASSNAME = "v-" + TAGNAME;

    public static final String PROPERTY_KEYPRESSED = "keypressed";

    public VSuperImmediateTextField() {
        this(DOM.createInputText());
    }

    protected VSuperImmediateTextField(Element node) {
        super(node);

        setStyleName(CLASSNAME);
        addKeyUpHandler(this);
    }

    public void onKeyUp(KeyUpEvent event) {
        client.updateVariable(client.getPid(this), PROPERTY_KEYPRESSED, true,
                false);
        client.updateVariable(client.getPid(this), "text", getText(), false);
        client.sendPendingVariableChanges();
    }
}
