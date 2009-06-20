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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.lightframe.components.client.ui.VWindowManager;


import com.vaadin.Application;
import com.vaadin.terminal.PaintException;
import com.vaadin.terminal.PaintTarget;
import com.vaadin.ui.AbstractComponent;
import com.vaadin.ui.Window;
import com.vaadin.ui.Window.CloseEvent;
import com.vaadin.ui.Window.CloseListener;

/**
 * A lightweight window manager.
 * 
 * @author Henrik Paul
 */
public class WindowManager extends AbstractComponent {

    /**
     * This {@link RuntimeException} is thrown if the {@link WindowManager
     * WindowManager's} {@link Window.CloseListener close listener} is called to
     * close a {@link Window} that is not managed by the WindowManager.
     * 
     * @author Henrik Paul
     */
    public class WindowNotManagedException extends RuntimeException {
        private static final long serialVersionUID = -6772803696335750369L;

        public WindowNotManagedException() {
            this("Window is not managed by " + WindowManager.this);
        }

        public WindowNotManagedException(String message, Throwable cause) {
            super(message, cause);
        }

        public WindowNotManagedException(String message) {
            super(message);
        }

        public WindowNotManagedException(Throwable cause) {
            super(cause);
        }

    }

    /**
     * Listen for closing {@link Window Windows}, that are managed by this
     * {@link WindowManager}. That Window will be removed from the
     * WindowManager's records and responsibilities.
     * 
     * @author Henrik Paul
     */
    private class ManagerWindowCloseListener implements CloseListener {
        private static final long serialVersionUID = 3288700222791880168L;

        public void windowClose(CloseEvent event) {
            final Window closedWindow = event.getWindow();

            if (managedWindows.contains(closedWindow)) {
                managedWindows.remove(event.getWindow());
            } else {
                throw new WindowNotManagedException();
            }

            requestRepaint();
        }
    }

    private static final long serialVersionUID = -3570380743273172124L;
    private static final Method WINDOW_CLOSE_METHOD;
    private static final String WINDOW_CLOSE_METHOD_NAME = "close";
    private static final Class<?>[] WINDOW_CLOSE_METHOD_ARGUMENTS = null;

    private final ManagerWindowCloseListener closeListener = new ManagerWindowCloseListener();
    private final List<Window> managedWindows = new ArrayList<Window>();

    static {
        try {
            // Because Window.close() is protected, it needs to be made
            // accessible for the WindowManager.

            WINDOW_CLOSE_METHOD = Window.class.getDeclaredMethod(
                    WINDOW_CLOSE_METHOD_NAME, WINDOW_CLOSE_METHOD_ARGUMENTS);
            WINDOW_CLOSE_METHOD.setAccessible(true);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(Window.class.getName() + "#"
                    + WINDOW_CLOSE_METHOD_NAME + "("
                    + WINDOW_CLOSE_METHOD_ARGUMENTS + ") not found", e);
        }
    }

    @Override
    public String getTag() {
        return VWindowManager.TAGNAME;
    }

    /**
     * Create a new {@link Window} that is managed by the {@link WindowManager}.
     * The Window will be immediately attached to the {@link Application}.
     * 
     * @return An attached {@link Window}, managed by this {@link WindowManager}
     *         . <code>null</code> if the {@link WindowManager} isn't, itself,
     *         attached to an {@link Application}.
     */
    public Window addWindow() {
        if (getApplication() != null) {
            final Window window = new Window();
            getApplication().getMainWindow().addWindow(window);
            window.addListener(closeListener);
            managedWindows.add(window);

            requestRepaint();
            return window;
        } else {
            return null;
        }
    }

    /**
     * Close a {@link Window} that is managed by this {@link WindowManager}.
     * 
     * @param window
     *            The {@link Window} to be closed.
     * @return <code>true</code> if the window was successfully closed.
     *         <code>false</code> if <code>window</code> is null, it's not
     *         attached to an {@link Application}, it's not managed by this
     *         {@link WindowManager} or this WindowManager isn't attached to an
     *         Application.
     */
    public boolean closeWindow(Window window) {
        if (getApplication() != null && window != null
                && managedWindows.contains(window)
                && window.getParent() != null) {

            try {
                // window.close() is protected, not public
                WINDOW_CLOSE_METHOD.invoke(window);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException(e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            } catch (InvocationTargetException e) {
                throw new RuntimeException(e);
            }

            return true;
        } else {
            return false;
        }
    }

    /**
     * Closes all {@link Window Windows} managed by this {@link WindowManager}.
     * 
     * @see #closeWindow(Window)
     */
    public void closeAllWindows() {
        for (final Window window : managedWindows
                .toArray(new Window[managedWindows.size()])) {
            closeWindow(window);
        }
    }

    @Override
    @SuppressWarnings("unchecked")
    public void changeVariables(Object source, Map variables) {
        super.changeVariables(source, variables);

        if (variables.containsKey(VWindowManager.ATTRIBUTE_CS_WINDOW_CLOSE)) {
            Integer closedWindowIndex = (Integer) variables
                    .get(VWindowManager.ATTRIBUTE_CS_WINDOW_CLOSE);
            closeWindow(managedWindows.get(closedWindowIndex));
            requestRepaint();
        }

        if (variables.containsKey(VWindowManager.ATTRIBUTE_CS_WINDOW_MINIMIZE)) {
            Integer minimizedWindowIndex = (Integer) variables
                    .get(VWindowManager.ATTRIBUTE_CS_WINDOW_MINIMIZE);
            Window window = managedWindows.get(minimizedWindowIndex);
            window.setVisible(!window.isVisible());
            requestRepaint();
        }
    }

    @Override
    public void paintContent(PaintTarget target) throws PaintException {
        super.paintContent(target);

        Object[] windowNames = new String[managedWindows.size()];
        Object[] minimizedWindows = new Integer[managedWindows.size()];

        for (int i = 0; i < managedWindows.size(); i++) {
            final Window window = managedWindows.get(i);
            windowNames[i] = window.getCaption();
            minimizedWindows[i] = window.isVisible() ? 0 : 1;
        }

        target.addAttribute(VWindowManager.ATTRIBUTE_SC_WINDOW_NAMES,
                windowNames);
        target.addAttribute(VWindowManager.ATTRIBUTE_SC_WINDOW_MINIMZED,
                minimizedWindows);
    }
}
