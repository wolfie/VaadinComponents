package org.lightframe.components;

import java.util.Collection;

import com.vaadin.data.Property;
import com.vaadin.data.Validator;
import com.vaadin.data.Validator.InvalidValueException;
import com.vaadin.ui.AbstractOrderedLayout;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.CustomComponent;
import com.vaadin.ui.Field;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.TextField;
import com.vaadin.ui.Button.ClickEvent;

public class SwappablePasswordField extends CustomComponent implements Field {
    private static final long serialVersionUID = 8169375628436894292L;

    final private TextField textField;
    final private CheckBox checkBox = new CheckBox("secret", this,
            "checkBoxClick");
    final private AbstractOrderedLayout layout = new HorizontalLayout();

    public SwappablePasswordField() {
        textField = new TextField();
        initCompositionLayout();
    }

    public SwappablePasswordField(String caption) {
        textField = new TextField(caption);
        initCompositionLayout();
    }

    public SwappablePasswordField(Property dataSource) {
        textField = new TextField(dataSource);
        initCompositionLayout();
    }

    public SwappablePasswordField(String caption, Property dataSource) {
        textField = new TextField(caption, dataSource);
        initCompositionLayout();
    }

    public SwappablePasswordField(String caption, String value) {
        textField = new TextField(caption, value);
        initCompositionLayout();
    }

    private void initCompositionLayout() {
        setCompositionRoot(layout);
        layout.addComponent(textField);
        layout.addComponent(checkBox);
        layout.setExpandRatio(textField, 1f);
        layout.setComponentAlignment(textField, Alignment.BOTTOM_RIGHT);
        layout.setComponentAlignment(checkBox, Alignment.BOTTOM_LEFT);

        checkBox.setImmediate(true);
        setSecret(true);
    }

    public void setInputPrompt(String inputPrompt) {
        textField.setInputPrompt(inputPrompt);
    }

    public String getInputPrompt() {
        return textField.getInputPrompt();
    }

    public int getMaxLength() {
        return textField.getMaxLength();
    }

    public void setMaxLength(int maxLength) {
        textField.setMaxLength(maxLength);
    }

    public String getNullRepresentation() {
        return textField.getNullRepresentation();
    }

    public void setNullRepresentation(String nullRepresentation) {
        textField.setNullRepresentation(nullRepresentation);
    }

    public boolean isNullSettingAllowed() {
        return textField.isNullSettingAllowed();
    }

    public void setNullSettingAllowed(boolean isAllowed) {
        textField.setNullSettingAllowed(isAllowed);
    }

    public void checkBoxClick(ClickEvent event) {
        setSecret(checkBox.booleanValue());
    }

    public boolean isSecret() {
        return textField.isSecret();
    }

    public void setSecret(boolean isSecret) {
        textField.setSecret(isSecret);
        checkBox.setValue(isSecret);
    }

    // delegate methods for TextField
    public String getRequiredError() {
        return textField.getRequiredError();
    }

    public boolean isRequired() {
        return textField.isRequired();
    }

    public void setRequired(boolean required) {
        textField.setRequired(required);
    }

    public void setRequiredError(String requiredMessage) {
        textField.setRequiredError(requiredMessage);
    }

    public boolean isInvalidCommitted() {
        return textField.isInvalidCommitted();
    }

    public void setInvalidCommitted(boolean isCommitted) {
        textField.setInvalidCommitted(isCommitted);
    }

    public void commit() throws SourceException, InvalidValueException {
        textField.commit();
    }

    public void discard() throws SourceException {
        textField.discard();
    }

    public boolean isModified() {
        return textField.isModified();
    }

    public boolean isReadThrough() {
        return textField.isReadThrough();
    }

    public boolean isWriteThrough() {
        return textField.isWriteThrough();
    }

    public void setReadThrough(boolean readThrough) throws SourceException {
        textField.setReadThrough(readThrough);
    }

    public void setWriteThrough(boolean writeThrough) throws SourceException,
            InvalidValueException {
        textField.setWriteThrough(writeThrough);
    }

    public void addValidator(Validator validator) {
        textField.addValidator(validator);
    }

    public Collection<?> getValidators() {
        return textField.getValidators();
    }

    public boolean isInvalidAllowed() {
        return textField.isInvalidAllowed();
    }

    public boolean isValid() {
        return textField.isValid();
    }

    public void removeValidator(Validator validator) {
        textField.removeValidator(validator);
    }

    public void setInvalidAllowed(boolean invalidValueAllowed)
            throws UnsupportedOperationException {
        textField.setInvalidAllowed(invalidValueAllowed);
    }

    public void validate() throws InvalidValueException {
        textField.validate();
    }

    public Class<?> getType() {
        return textField.getType();
    }

    public Object getValue() {
        return textField.getValue();
    }

    public void setValue(Object newValue) throws ReadOnlyException,
            ConversionException {
        textField.setValue(newValue);
    }

    public void addListener(ValueChangeListener listener) {
        textField.addListener(listener);
    }

    public void removeListener(ValueChangeListener listener) {
        textField.removeListener(listener);
    }

    public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
        textField.valueChange(event);
    }

    public Property getPropertyDataSource() {
        return textField.getPropertyDataSource();
    }

    public void setPropertyDataSource(Property newDataSource) {
        textField.setPropertyDataSource(newDataSource);
    }

    public void focus() {
        textField.focus();
    }

    public int getTabIndex() {
        return textField.getTabIndex();
    }

    public void setTabIndex(int tabIndex) {
        textField.setTabIndex(tabIndex);
    }
}
