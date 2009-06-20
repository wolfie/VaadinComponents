package org.lightframe.components;

import org.lightframe.components.SuperImmediateTextField.KeyPressEvent;

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

    private class CounterThread extends Thread {
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

        // getMainWindow().addComponent(manager);
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
        final Panel panel = new Panel("SuperImmediateTextField example");
        final SuperImmediateTextField textField = new SuperImmediateTextField();
        final Label superImmediateLabel = new Label(
                "I'm a Label, mirroring the field.");

        panel.addComponent(textField);
        panel.addComponent(superImmediateLabel);

        textField.setInputPrompt("type something");
        textField.addListener(new SuperImmediateTextField.KeyPressListener() {
            private static final long serialVersionUID = -3549051979588281670L;

            public void keyPressed(KeyPressEvent event) {
                superImmediateLabel.setValue(textField.getValue());
            }
        });

        return panel;
    }
}
