/* 
 * Copyright 2009 Henrik Paul
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

package org.lightframe.components;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.lightframe.components.client.ui.VSuperImmediateTextField;

import com.vaadin.data.Property;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.TextField;

/**
 * A text input widget that recognizes each keypress and informs the server side
 * about the event.
 * 
 * @author Henrik Paul
 * @see TextField
 */
public class SuperImmediateTextField extends TextField {

    /**
     * A class that is informed whenever a {@link SuperImmediateTextField} is
     * typed upon.
     * 
     * @author Henrik Paul
     * @see SuperImmediateTextField#addListener(KeyPressListener)
     * @see SuperImmediateTextField#removeListener(KeyPressListener)
     */
    public interface KeyPressListener extends Serializable {
        /**
         * Inform that a key has been pressed in an
         * {@link SuperImmediateTextField}.
         * 
         * @param event
         */
        public void keyPressed(KeyPressEvent event);
    }

    /**
     * An event that is sent whenever a key is pressed in a
     * {@link SuperImmediateTextField}.
     * 
     * @author Henrik Paul
     */
    public interface KeyPressEvent extends Serializable {
        /**
         * Get the source of the {@link KeyPressEvent}.
         * 
         * @return The {@link SuperImmediateTextField} that was typed upon.
         */
        public SuperImmediateTextField getSource();
    }

    private static final long serialVersionUID = -8423510242229989097L;

    private final KeyPressEvent keypressEvent = new KeyPressEvent() {
        private static final long serialVersionUID = 1L;

        public SuperImmediateTextField getSource() {
            return SuperImmediateTextField.this;
        }
    };

    private final Collection<KeyPressListener> listeners = new ArrayList<KeyPressListener>();

    private int delayMillis = VSuperImmediateTextField.DEFAULT_DELAY;

    /**
     * @see TextField#TextField()
     */
    public SuperImmediateTextField() {
        super();
    }

    /**
     * @see TextField#TextField(Property)
     */
    public SuperImmediateTextField(Property dataSource) {
        super(dataSource);
    }

    /**
     * @see TextField#TextField(String, Property)
     */
    public SuperImmediateTextField(String caption, Property dataSource) {
        super(caption, dataSource);
    }

    /**
     * @see TextField#TextField(String, String)
     */
    public SuperImmediateTextField(String caption, String value) {
        super(caption, value);
    }

    /**
     * @see TextField#TextField(String)
     */
    public SuperImmediateTextField(String caption) {
        super(caption);
    }

    @Override
    public String getTag() {
        return "superimmediatetextfield";
    }

    @Override
    @SuppressWarnings("unchecked")
    public void changeVariables(Object source, Map variables) {
        super.changeVariables(source, variables);

        if (variables.containsKey(VSuperImmediateTextField.PROPERTY_KEYPRESSED)) {
            fireKeyPressEvent();
        }
    }

    protected void fireKeyPressEvent() {
        for (KeyPressListener listener : listeners) {
            listener.keyPressed(keypressEvent);
        }
    }

    public void addListener(KeyPressListener listener) {
        if (listener != null) {
            listeners.add(listener);
        }
    }

    public void removeListener(KeyPressListener listener) {
        listeners.remove(listener);
    }

    /**
     * Set the delay to wait until the super immediate event is sent from when
     * the user starts typing.
     * 
     * @param delayInMillis
     *            How many milliseconds to wait. If negative, no event will be
     *            sent.
     */
    public void setDelay(final int delayInMillis) {
        delayMillis = delayInMillis;
        requestRepaint();
    }

    /**
     * Get the time waited between the user's start of typing and when the super
     * immediate event.
     * 
     * @return The delayed time in milliseconds. A negative result indicates
     *         that no events will be sent.
     */
    public int getDelay() {
        return delayMillis;
    }

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        super.paintContent(target);

        target.addAttribute(VSuperImmediateTextField.ATTRIBUTE_DELAY,
                delayMillis);
    }
}
