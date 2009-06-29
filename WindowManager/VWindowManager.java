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

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;
import com.vaadin.terminal.gwt.client.ApplicationConnection;
import com.vaadin.terminal.gwt.client.Paintable;
import com.vaadin.terminal.gwt.client.UIDL;

public class VWindowManager extends Widget implements Paintable {

    /** Set the tagname used to statically resolve widget from UIDL. */
    public static final String TAGNAME = "windowmanager";

    /** Set the CSS class name to allow styling. */
    public static final String CLASSNAME = "v-" + TAGNAME;

    public static final String WINDOWBOX_CLASSNAME = CLASSNAME + "-windowbox";
    public static final String WINDOWBOX_CLASSNAME_MINIMIZED = WINDOWBOX_CLASSNAME
            + "-minimized";
    public static final String WINDOWBOX_CLOSE_CLASSNAME = WINDOWBOX_CLASSNAME
            + "-close";

    public static final String ATTRIBUTE_SC_WINDOW_NAMES = "windowNames";
    public static final String ATTRIBUTE_SC_WINDOW_MINIMZED = "minimizedWindows";
    public static final String ATTRIBUTE_CS_WINDOW_CLOSE = "close";
    public static final String ATTRIBUTE_CS_WINDOW_MINIMIZE = "minimize";

    private int windowCount = 0;

    /** Component identifier in UIDL communications. */
    String uidlId;

    /** Reference to the server connection object. */
    ApplicationConnection client;

    /**
     * The constructor should first call super() to initialize the component and
     * then handle any initialization relevant to Vaadin.
     */
    public VWindowManager() {
        setElement(Document.get().createDivElement());
        // This method call of the Paintable interface sets the component
        // style name in DOM tree
        setStyleName(CLASSNAME);
    }

    public void updateFromUIDL(UIDL uidl, ApplicationConnection client) {
        // This call should be made first. Ensure correct implementation,
        // and let the containing layout manage caption, etc.
        if (client.updateComponent(this, uidl, true)) {
            return;
        }

        // Save reference to server connection object to be able to send
        // user interaction later
        this.client = client;

        // Save the UIDL identifier for the component
        uidlId = uidl.getId();

        if (uidl.hasAttribute(ATTRIBUTE_SC_WINDOW_NAMES)) {
            // ugly way to clear all elements.
            getElement().setInnerHTML("");

            final String[] names = uidl
                    .getStringArrayAttribute(ATTRIBUTE_SC_WINDOW_NAMES);
            for (final String name : names) {
                final Element windowBox = DOM.createDiv();

                windowBox.setInnerHTML(name);
                windowBox.setClassName(WINDOWBOX_CLASSNAME);
                DOM.sinkEvents(windowBox, Event.ONCLICK | Event.ONCONTEXTMENU);

                DOM.appendChild(getElement(), windowBox);
            }

            windowCount = names.length;

        }

        if (uidl.hasAttribute(ATTRIBUTE_SC_WINDOW_MINIMZED)) {
            final int[] minimized = uidl
                    .getIntArrayAttribute(ATTRIBUTE_SC_WINDOW_MINIMZED);
            final NodeList<Node> windowBoxes = getElement().getChildNodes();

            for (int i = 0; i < windowBoxes.getLength(); i++) {
                final Element windowBox = (Element) windowBoxes.getItem(i);
                windowBox
                        .setClassName(minimized[i] == 1 ? WINDOWBOX_CLASSNAME_MINIMIZED
                                : WINDOWBOX_CLASSNAME);
            }
        }
    }

    @Override
    public void onBrowserEvent(Event event) {
        Element target = DOM.eventGetCurrentTarget(event);

        switch (event.getTypeInt()) {
        case Event.ONCLICK:
            minimizeWindow(target);
            break;
        case Event.ONCONTEXTMENU:
            event.preventDefault();
            event.stopPropagation();
            closeWindow(getWindowBoxIndex(target));
            break;
        }
    }

    private void minimizeWindow(Element windowBox) {
        final int index = getWindowBoxIndex(windowBox);
        if (isValidWindowIndex(index)) {
            client.updateVariable(client.getPid(this),
                    ATTRIBUTE_CS_WINDOW_MINIMIZE, index, true);
        }
    }

    public void closeWindow(int index) {
        if (isValidWindowIndex(index)) {
            client.updateVariable(client.getPid(this),
                    ATTRIBUTE_CS_WINDOW_CLOSE, index, true);
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    private int getWindowBoxIndex(Element windowBox) {
        final NodeList<Node> siblings = windowBox.getParentNode()
                .getChildNodes();

        for (int i = 0; i < siblings.getLength(); i++) {
            final Node sibling = siblings.getItem(i);
            if (sibling.equals(windowBox)) {
                return i;
            }
        }

        return -1;
    }

    private boolean isValidWindowIndex(int index) {
        return 0 <= index && index < windowCount;
    }
}
