/**
 * Sencha GXT 4.0.0 - Sencha for GWT
 * Copyright (c) 2006-2015, Sencha Inc.
 *
 * licensing@sencha.com
 * http://www.sencha.com/products/gxt/license/
 *
 * ================================================================================
 * Open Source License
 * ================================================================================
 * This version of Sencha GXT is licensed under the terms of the Open Source GPL v3
 * license. You may use this license only if you are prepared to distribute and
 * share the source code of your application under the GPL v3 license:
 * http://www.gnu.org/licenses/gpl.html
 *
 * If you are NOT prepared to distribute and share the source code of your
 * application under the GPL v3 license, other commercial and oem licenses
 * are available for an alternate download of Sencha GXT.
 *
 * Please see the Sencha GXT Licensing page at:
 * http://www.sencha.com/products/gxt/license/
 *
 * For clarification or additional options, please contact:
 * licensing@sencha.com
 * ================================================================================
 *
 *
 * ================================================================================
 * Disclaimer
 * ================================================================================
 * THIS SOFTWARE IS DISTRIBUTED "AS-IS" WITHOUT ANY WARRANTIES, CONDITIONS AND
 * REPRESENTATIONS WHETHER EXPRESS OR IMPLIED, INCLUDING WITHOUT LIMITATION THE
 * IMPLIED WARRANTIES AND CONDITIONS OF MERCHANTABILITY, MERCHANTABLE QUALITY,
 * FITNESS FOR A PARTICULAR PURPOSE, DURABILITY, NON-INFRINGEMENT, PERFORMANCE AND
 * THOSE ARISING BY STATUTE OR FROM CUSTOM OR USAGE OF TRADE OR COURSE OF DEALING.
 * ================================================================================
 */
package com.sencha.gxt.widget.core.client.form;

import java.text.ParseException;
import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.HasChangeHandlers;
import com.google.gwt.event.dom.client.HasKeyDownHandlers;
import com.google.gwt.event.dom.client.HasKeyPressHandlers;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.AutoDirectionHandler;
import com.google.gwt.i18n.client.BidiPolicy;
import com.google.gwt.i18n.client.BidiUtils;
import com.google.gwt.i18n.shared.DirectionEstimator;
import com.google.gwt.i18n.shared.HasDirectionEstimator;
import com.google.gwt.user.client.ui.HasText;
import com.google.gwt.user.client.ui.impl.TextBoxImpl;
import com.sencha.gxt.cell.core.client.form.ValueBaseInputCell;
import com.sencha.gxt.core.client.GXT;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.messages.client.DefaultMessages;
import com.sencha.gxt.widget.core.client.event.ParseErrorEvent;
import com.sencha.gxt.widget.core.client.event.ParseErrorEvent.HasParseErrorHandlers;
import com.sencha.gxt.widget.core.client.event.ParseErrorEvent.ParseErrorHandler;
import com.sencha.gxt.widget.core.client.form.error.DefaultEditorError;
import com.sencha.gxt.widget.core.client.form.validator.EmptyValidator;

/**
 * Abstract base class for fields that have a single value.
 * 
 * @param <T> the field type
 */
public abstract class ValueBaseField<T> extends Field<T> implements HasKeyPressHandlers, HasKeyDownHandlers,
    HasChangeHandlers, HasParseErrorHandlers, HasDirectionEstimator, AutoDirectionHandler.Target, HasText {

  protected static TextBoxImpl impl = GWT.create(TextBoxImpl.class);

  private final AutoDirectionHandler autoDirHandler;
  private EmptyValidator<T> emptyValidator;

  protected ValueBaseField(ValueBaseInputCell<T> cell) {
    super(cell);
    autoDirHandler = AutoDirectionHandler.addTo(this, BidiPolicy.isBidiEnabled());

    getCell().addParseErrorHandler(new ParseErrorHandler() {

      @Override
      public void onParseError(ParseErrorEvent event) {
        onCellParseError(event);
      }
    });

    setWidth(150);
  }

  protected ValueBaseField(ValueBaseInputCell<T> cell, PropertyEditor<T> propertyEditor) {
    this(cell);
    setPropertyEditor(propertyEditor);
  }

  @Override
  public HandlerRegistration addChangeHandler(ChangeHandler handler) {
    return addDomHandler(handler, ChangeEvent.getType());
  }

  @Override
  public HandlerRegistration addKeyDownHandler(KeyDownHandler handler) {
    return addDomHandler(handler, KeyDownEvent.getType());
  }

  @Override
  public HandlerRegistration addKeyPressHandler(KeyPressHandler handler) {
    return addDomHandler(handler, KeyPressEvent.getType());
  }

  @Override
  public void clear() {
    // EXTGWT-2856 calling clear when the field is focused is causing a blur event to fire when the field is being
    // redrawn. ValueBaseInputCell thinks the input value does not equal value as the input element still has the
    // non-cleared value while the actual value is null
    setText("");
    // make sure emptyText is added back for ie8 or ie9 - as other browsers utilize placeholder attribute
    if (GXT.isIE8() || GXT.isIE9()) {
      setEmptyText(getEmptyText());
    }
    super.clear();
  }

  @Override
  public HandlerRegistration addKeyUpHandler(KeyUpHandler handler) {
    return addDomHandler(handler, KeyUpEvent.getType());
  }

  @Override
  public HandlerRegistration addParseErrorHandler(ParseErrorHandler handler) {
    return addHandler(handler, ParseErrorEvent.getType());
  }

  @Override
  public ValueBaseInputCell<T> getCell() {
    return (ValueBaseInputCell<T>) super.getCell();
  }

  /**
   * Returns the field's current value. The value of the field is not updated until the field is blurred when editing.
   * 
   * @return the current value
   */
  public T getCurrentValue() {
    try {
      return getValueOrThrow();
    } catch (Exception e) {
      return null;
    }
  }

  /**
   * Gets the current position of the cursor (this also serves as the beginning of the text selection).
   * 
   * @return the cursor's position
   */
  public int getCursorPos() {
    return impl.getCursorPos(getInputEl());
  }

  @Override
  public Direction getDirection() {
    return BidiUtils.getDirectionOnElement(getElement());
  }

  @Override
  public DirectionEstimator getDirectionEstimator() {
    return autoDirHandler.getDirectionEstimator();
  }

  /**
   * Returns the field's empty text.
   * 
   * @return the empty text
   */
  public String getEmptyText() {
    return getCell().getEmptyText();
  }

  /**
   * Returns the field's property editor.
   * 
   * @return the property editor
   */
  public PropertyEditor<T> getPropertyEditor() {
    return getCell().getPropertyEditor();
  }

  /**
   * Gets the text currently selected within this text box.
   * 
   * @return the selected text, or an empty string if none is selected
   */
  public String getSelectedText() {
    int start = getCursorPos();
    if (start < 0) {
      return "";
    }
    int length = getSelectionLength();
    return getText().substring(start, start + length);
  }

  /**
   * Gets the length of the current text selection.
   * 
   * @return the text selection length
   */
  public int getSelectionLength() {
    return impl.getSelectionLength(getInputEl());
  }

  /**
   * Gets this object's text.
   * 
   * @return the object's new text
   */
  @Override
  public String getText() {
    return getInputEl().getPropertyString("value");
  }

  /**
   * Return the parsed value, or null if the field is empty.
   * 
   * @return the parsed value
   * @throws ParseException if the value cannot be parsed
   */
  public T getValueOrThrow() throws ParseException {
    String text = getText();

    if (text == null || "".equals(text)) {
      return null;
    }

    if (getEmptyText() != null && text.equals(getEmptyText())) {
      return null;
    }

    T parseResult = getCell().getPropertyEditor().parse(text);

    return parseResult;
  }

  /**
   * Returns the field's allow blank state.
   * 
   * @return {@code true} if blank values are allowed
   */
  public boolean isAllowBlank() {
    return getCell().isAllowBlank();
  }

  /**
   * Returns {@code true} if the current value is cleared on a parse error.
   * 
   * @return {@code true} if clearing on parse error
   */
  public boolean isClearValueOnParseError() {
    return getCell().isClearValueOnParseError();
  }

  /**
   * Returns the valid state of the current value. This method will not mark the field invalid if validation fails.
   * 
   * @return the current valid state
   */
  public boolean isCurrentValid() {
    return isCurrentValid(false);
  }

  /**
   * Returns the valid state of the current value. If a ParseException is thrown trying to convert the raw value to a
   * typed value, a EditError will be returned.
   * 
   * @param preventMark {@code true} to not mark the field invalid
   * @return the current valid state
   */
  public boolean isCurrentValid(boolean preventMark) {
    if (disabled) {
      return true;
    }
    boolean restore = this.preventMark;
    this.preventMark = preventMark;

    T currentValue = null;

    try {
      currentValue = getValueOrThrow();
    } catch (ParseException e) {

      String current = getText();
      EditorError error = new DefaultEditorError(this, DefaultMessages.getMessages().field_parseExceptionText(current),
          current);

      ArrayList<EditorError> errors = new ArrayList<EditorError>();
      errors.add(error);

      setErrors(errors);

      if (!preventMark) {
        markInvalid(errors);
      }

      this.preventMark = restore;
      return false;
    }
    boolean result = validateValue(currentValue);
    this.preventMark = restore;

    if (result) {
      clearInvalid();
    }
    return result;
  }

  /**
   * Returns the select of focus state.
   * 
   * @return {@code true} if select on focus is enabled
   */
  public boolean isSelectOnFocus() {
    return getCell().isSelectOnFocus();
  }

  /**
   * Selects text in the field.
   * 
   * @param start the index where the selection should start.
   * @param length the number of characters to be selected
   */
  public void select(int start, int length) {
    getCell().select(getElement(), start, length);
  }

  /**
   * Selects all the text.
   */
  public void selectAll() {
    getCell().selectAll(getElement());
  }

  /**
   * Sets whether a field is valid when its value length = 0 (default to {@code true}).
   * 
   * @param allowBlank {@code true} to allow blanks, {@code false} otherwise
   */
  public void setAllowBlank(boolean allowBlank) {
    getCell().setAllowBlank(allowBlank);
    if (!allowBlank) {
      if (emptyValidator == null) {
        emptyValidator = new EmptyValidator<T>();
      }
      if (!getValidators().contains(emptyValidator)) {
        getValidators().add(0, emptyValidator);
      }
    } else if (emptyValidator != null) {
      removeValidator(emptyValidator);
    }
  }

  /**
   * Clears the current value when a parse error occurs (defaults to {@code true}).
   * 
   * @param clearValueOnParseError {@code true} to clean the value on parse error
   */
  public void setClearValueOnParseError(boolean clearValueOnParseError) {
    getCell().setClearValueOnParseError(clearValueOnParseError);
  }

  /**
   * Sets the cursor position.
   * 
   * This will only work when the widget is attached to the document and not hidden.
   * 
   * @param pos the new cursor position
   */
  public void setCursorPos(int pos) {
    setSelectionRange(pos, 0);
  }

  /**
   * Sets the directionality for a widget.
   * 
   * @param direction RTL if the directionality should be set to right-to-left, LTR if the directionality should be set
   *          to left-to-right DEFAULT if the directionality should not be explicitly set
   */
  @Override
  public void setDirection(Direction direction) {
    BidiUtils.setDirectionOnElement(getElement(), direction);
  }

  /**
   * Toggles on / off direction estimation.
   * 
   * @param enabled Whether to enable direction estimation. If {@code true}, sets the DirectionEstimator object to a
   *          default DirectionEstimator.
   */
  @Override
  public void setDirectionEstimator(boolean enabled) {
    autoDirHandler.setDirectionEstimator(enabled);
  }

  /**
   * Sets the {link DirectionEstimator} object.
   * 
   * @param directionEstimator The {code DirectionEstimator} to be set. null means turning off direction estimation.
   */
  @Override
  public void setDirectionEstimator(DirectionEstimator directionEstimator) {
    autoDirHandler.setDirectionEstimator(directionEstimator);
  }

  /**
   * Sets the default text to display in an empty field (defaults to null).
   * 
   * @param emptyText the empty text
   */
  public void setEmptyText(String emptyText) {
    getCell().setEmptyText(createContext(), getElement(), emptyText);
  }

  @Override
  public void setId(String id) {
    super.setId(id);
    getCell().getInputElement(getElement()).setId(id + "-input");
  }

  /**
   * Sets the field's property editor which is used to translate typed values to string, and string values back to typed
   * values.
   * 
   * @param propertyEditor the property editor
   */
  public void setPropertyEditor(PropertyEditor<T> propertyEditor) {
    getCell().setPropertyEditor(propertyEditor);
  }

  /**
   * Sets the field's read only state. Relevant only when type has the value "text" or "password".
   * 
   * @param readOnly the read only state
   */
  public void setReadOnly(boolean readOnly) {
    getCell().setReadOnly(readOnly);
    getCell().getInputElement(getElement()).setReadOnly(readOnly);
  }

  /**
   * Returns the read only state.
   * 
   * @return {@code true} if read only, otherwise {@code false}
   */
  public boolean isReadOnly() {
    return getCell().isReadOnly();
  }

  /**
   * Sets the range of text to be selected.
   * 
   * This will only work when the widget is attached to the document and not hidden.
   * 
   * @param pos the position of the first character to be selected
   * @param length the number of characters to be selected
   */
  public void setSelectionRange(int pos, int length) {
    // Setting the selection range will not work for unattached elements.
    if (!isAttached()) {
      return;
    }

    if (length < 0) {
      throw new IndexOutOfBoundsException("Length must be a positive integer. Length: " + length);
    }
    if (pos < 0 || length + pos > getText().length()) {
      throw new IndexOutOfBoundsException("From Index: " + pos + "  To Index: " + (pos + length) + "  Text Length: "
          + getText().length());
    }
    impl.setSelectionRange(getInputEl(), pos, length);
  }

  /**
   * Automatically select any existing field text when the field receives input focus (defaults to {@code false}).
   * 
   * @param selectOnFocus {@code true} to focus
   */
  public void setSelectOnFocus(boolean selectOnFocus) {
    getCell().setSelectOnFocus(selectOnFocus);
  }

  @Override
  public void setTabIndex(int tabIndex) {
    this.tabIndex = tabIndex;
    getInputEl().setTabIndex(tabIndex);
  }

  /**
   * Sets the underlying DOM field's value directly, bypassing validation. This method does not update the field's
   * value. To set the value with validation see {@link #setValue}.
   * 
   * @param text the text
   */
  @Override
  public void setText(String text) {
    getCell().setText(getElement(), text);
    autoDirHandler.refreshDirection();
  }

  /**
   * Sets this object's value without firing any events. This should be identical to calling setValue(value, false).
   * 
   * It is acceptable to fail assertions or throw (documented) unchecked exceptions in response to bad values.
   * 
   * Widgets must accept null as a valid value. By convention, setting a widget to null clears value, calling getValue()
   * on a cleared widget returns null. Widgets that can not be cleared (e.g. CheckBox) must find another valid meaning
   * for null input.
   * 
   * 
   * @param value the parsed value
   */
  @Override
  public void setValue(T value) {
    setValue(value, false);
  }

  public boolean validateCurrent(boolean preventMark) {
    if (disabled) {
      clearInvalid();
      return true;
    }

    boolean restore = this.preventMark;
    this.preventMark = preventMark;
    boolean result = validateValue(getCurrentValue());
    this.preventMark = restore;
    if (result) {
      clearInvalid();
      // activeErrorMessage = null;
    }
    return result;
  }

  @Override
  protected void doAutoValidate() {
    // handle any parse errors before calling validateCurrent as parse exceptions are swallowed.
    try {
      getValueOrThrow();
    } catch (ParseException e) {
      onCellParseError(new ParseErrorEvent(e.getLocalizedMessage(), e));
      return;
    }
    validateCurrent(false);
  }

  @Override
  protected XElement getFocusEl() {
    return XElement.as(getCell().getInputElement(getElement()));
  }

  protected TextBoxImpl getImpl() {
    return impl;
  }

  /**
   * Provides the actual input element used by this field.
   * 
   * @return the input element
   */
  protected XElement getInputEl() {
    return XElement.as(getCell().getInputElement(getElement()));
  }

  protected void onCellParseError(ParseErrorEvent event) {
    // fire again through field
    fireEvent(event);
  }

  @Override
  protected void onRedraw() {
    super.onRedraw();
    XElement input = getElement().selectNode("input");
    if (input != null) {
      input.setId(getId() + "-input");
    }
    getInputEl().setTabIndex(getTabIndex());
  }

}
