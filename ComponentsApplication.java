package org.lightframe.components;

import java.io.Serializable;

import com.vaadin.Application;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;

public class ComponentsApplication extends Application {
    private static final long serialVersionUID = -1134769947702907890L;
    private static final long SLEEP_TIME_IN_MILLIS = 500; // half a second

    private class CounterThread extends Thread implements Serializable {
        private static final long serialVersionUID = 6969871601928939400L;
        private final Label renderLabel;
        private boolean running = false;

        public CounterThread(Label renderLabel) {
            this.renderLabel = renderLabel;
            renderLabel.setData(1);
        }

        public void run() {
            final long startTime = System.currentTimeMillis();
            final long lifetime = 1000 * 60; // live for a minute.

            try {
                while (System.currentTimeMillis() < startTime + lifetime) {

                    if (running) {
                        final Integer number = (Integer) renderLabel.getData();
                        renderLabel.setValue(number);
                        renderLabel.setData(number + 1);
                    }

                    sleep(SLEEP_TIME_IN_MILLIS);
                }

                renderLabel.setValue("[ counter thread expired ]");
            } catch (InterruptedException e) {
                renderLabel.setValue("[ counter thread interrupted ]");
            }
        }

        public void startCounting() {
            running = true;
        }

        public void stopCounting() {
            running = false;
        }
    }

    @Override
    public void init() {
        final Window mainWindow = new Window("Components");
        setMainWindow(mainWindow);
        setTheme("componentstheme");

        mainWindow.addComponent(superImmediateExample());
        mainWindow.addComponent(windowManagerExample());
        mainWindow.addComponent(refresherExample());
        mainWindow.addComponent(swappablePasswordFieldExample());
    }

    private Component swappablePasswordFieldExample() {
        final Panel panel = new Panel("SwappablePasswordField example");
        final SwappablePasswordField field = new SwappablePasswordField();
        final String html = "<div style='margin-bottom:10px'>"
                + "This CustomComponent allows you to swap "
                + "between the 'text' and 'password' modes of a text input field.<br/>"
                + "It consists of a TextBox and a CheckBox with a neat API to control the pair of them.<br/>"
                + "I did this mainly inspired by "
                + "<a href='http://www.useit.com/alertbox/passwords.html' target='_blank'>a call</a>"
                + " against hidden passwords.</div>";

        panel.addComponent(new Label(html, Label.CONTENT_XHTML));
        panel.addComponent(field);

        return panel;
    }

    private Component refresherExample() {
        final Panel panel = new Panel("Refresher example");
        final HorizontalLayout layout = new HorizontalLayout();
        final Refresher refresher = new Refresher();
        final Label label = new Label("0");
        final CounterThread thread = new CounterThread(label);

        thread.start();

        label.setData(0);

        panel.addComponent(refresher);
        panel.addComponent(new Label("<div style='margin-bottom:10px'>"
                + "The Refresher allows you to affect the UI "
                + "from external threads without the "
                + "ProgressIndicator hack.</div>", Label.CONTENT_XHTML));
        panel.addComponent(layout);

        layout.setSpacing(true);
        layout.addComponent(new Button("Start Counting",
                new Button.ClickListener() {
                    private static final long serialVersionUID = 1L;

                    public void buttonClick(ClickEvent event) {
                        refresher.setRefreshInterval(SLEEP_TIME_IN_MILLIS);
                        thread.startCounting();
                    }
                }));

        layout.addComponent(new Button("Stop Counting",
                new Button.ClickListener() {
                    private static final long serialVersionUID = 1L;

                    public void buttonClick(ClickEvent event) {
                        refresher.setRefreshInterval(0);
                        thread.stopCounting();
                    }
                }));

        layout.addComponent(label);

        return panel;
    }

    private Component windowManagerExample() {
        final Panel panel = new Panel("WindowManager example");
        final HorizontalLayout layout = new HorizontalLayout();
        final WindowManager manager = new WindowManager();
        manager.setData(1);

        panel.addComponent(manager);

        panel.addComponent(new Label("<div style='margin-bottom:10px;'>"
                + "Press the button below to create a new managed Window.<br/>"
                + "Left-click the box in the footer to toggle "
                + "minimize/restore.<br/>"
                + "Right-click the box in the footer to close the window."
                + "</div>", Label.CONTENT_XHTML));

        panel.addComponent(layout);

        layout.setSpacing(true);
        layout.addComponent(new Button("Create New Window",
                new Button.ClickListener() {
                    private static final long serialVersionUID = 3120817389088026312L;

                    public void buttonClick(ClickEvent event) {
                        final Integer windowNumber = (Integer) manager
                                .getData();
                        final Window newWindow = manager.addWindow();
                        newWindow.setCaption("Managed Window " + windowNumber);
                        manager.setData(windowNumber + 1);
                    }
                }));

        layout.addComponent(new Button("Close All Windows",
                new Button.ClickListener() {
                    private static final long serialVersionUID = 7683402098695111188L;

                    public void buttonClick(ClickEvent event) {
                        manager.closeAllWindows();
                    }
                }));

        return panel;
    }

    private Component superImmediateExample() {
        final Panel panel = new Panel("SuperImmediateTextField examples");

        final HorizontalLayout semiSuperImmediate = new HorizontalLayout();
        final SuperImmediateTextField semiSuperImmediateTextField = new SuperImmediateTextField();
        final Label semiSuperImmediateLabel = new Label(
                "I'm a Label, mirroring the field with a second's delay.");

        semiSuperImmediate.addComponent(semiSuperImmediateTextField);
        semiSuperImmediate.addComponent(semiSuperImmediateLabel);

        semiSuperImmediateTextField.setDelay(1000);
        semiSuperImmediateTextField.setInputPrompt("type something");
        semiSuperImmediateTextField
                .addListener(new SuperImmediateTextField.KeyPressListener() {
                    private static final long serialVersionUID = -3549051979588281670L;

                    public void keyPressed(
                            SuperImmediateTextField.KeyPressEvent event) {
                        semiSuperImmediateLabel
                                .setValue(semiSuperImmediateTextField
                                        .getValue());
                    }
                });

        final HorizontalLayout verySuperImmediate = new HorizontalLayout();
        final SuperImmediateTextField verySuperImmediateTextField = new SuperImmediateTextField();
        final Label verySuperImmediateLabel = new Label(
                "I'm a Label, mirroring the field immediately.");

        verySuperImmediate.addComponent(verySuperImmediateTextField);
        verySuperImmediate.addComponent(verySuperImmediateLabel);

        verySuperImmediateTextField.setDelay(0);
        verySuperImmediateTextField.setInputPrompt("type something");
        verySuperImmediateTextField
                .addListener(new SuperImmediateTextField.KeyPressListener() {
                    private static final long serialVersionUID = 3729899805732151471L;

                    public void keyPressed(
                            SuperImmediateTextField.KeyPressEvent event) {
                        verySuperImmediateLabel
                                .setValue(verySuperImmediateTextField
                                        .getValue());
                    }
                });

        panel.addComponent(semiSuperImmediate);
        panel.addComponent(verySuperImmediate);

        return panel;
    }
}
