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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gwt.editor.client.EditorDelegate;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.editor.client.HasEditorDelegate;
import com.google.gwt.editor.client.HasEditorErrors;
import com.google.gwt.editor.client.ValueAwareEditor;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.impl.FocusImpl;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.event.InvalidEvent;
import com.sencha.gxt.widget.core.client.event.InvalidEvent.HasInvalidHandlers;
import com.sencha.gxt.widget.core.client.event.InvalidEvent.InvalidHandler;
import com.sencha.gxt.widget.core.client.event.ValidEvent;
import com.sencha.gxt.widget.core.client.event.ValidEvent.HasValidHandlers;
import com.sencha.gxt.widget.core.client.event.ValidEvent.ValidHandler;
import com.sencha.gxt.widget.core.client.form.error.DefaultEditorError;
import com.sencha.gxt.widget.core.client.form.error.ErrorHandler;
import com.sencha.gxt.widget.core.client.form.error.SideErrorHandler;

/**
 * Wraps a {@link Widget} so that it can be used like a {@link Field}.
 * 
 * @param <T> the field's data type
 */
public abstract class AdapterField<T> extends SimpleContainer implements IsField<T>, HasInvalidHandlers,
    HasValidHandlers, HasEditorErrors<T>, HasEditorDelegate<T>, ValueAwareEditor<T> {

  protected String forceInvalidText;
  protected boolean preventMark;

  private List<Validator<T>> validators = new ArrayList<Validator<T>>();
  private EditorDelegate<T> delegate;
  private ErrorHandler errorSupport;
  private List<EditorError> errors;

  /**
   * Creates an adapter field that wraps the specified widget so that it can be used like a {@link Field}.
   * 
   * @param widget the widget to wrap
   */
  public AdapterField(Widget widget) {
    setWidget(widget);
    setErrorSupport(new SideErrorHandler(this));
  }

  @Override
  public HandlerRegistration addInvalidHandler(InvalidHandler handler) {
    return addHandler(handler, InvalidEvent.getType());
  }

  /**
   * Adds a validator to be invoked when {@link #validateValue(Object)} is invoked.
   * 
   * @param validator the validator to add
   */
  public void addValidator(Validator<T> validator) {
    validators.add(validator);
  }

  @Override
  public HandlerRegistration addValidHandler(ValidHandler handler) {
    return addHandler(handler, ValidEvent.getType());
  }

  @Override
  public HandlerRegistration addValueChangeHandler(ValueChangeHandler<T> valueChangeHandler) {
    return addHandler(valueChangeHandler, ValueChangeEvent.getType());
  }

  /**
   * Clears the value from the field.
   */
  public void clear() {
    boolean restore = preventMark;
    preventMark = true;
    setValue(null);
    preventMark = restore;
    clearInvalid();
  }

  /**
   * Clear any invalid styles / messages for this field.
   */
  public void clearInvalid() {
    if (forceInvalidText != null) {
      forceInvalidText = null;
    }

    errorSupport.clearInvalid();

    fireEvent(new ValidEvent());
  }

  @Override
  public void disable() {
    super.disable();
    if (widget instanceof Component) {
      ((Component) widget).disable();
    }
  }

  @Override
  public void enable() {
    super.enable();
    if (widget instanceof Component) {
      ((Component) widget).enable();
    }
  }

  @Override
  public void finishEditing() {
  }

  @Override
  public void flush() {
    if (delegate == null) {
      return;
    }
    if (forceInvalidText != null) {
      delegate.recordError(forceInvalidText, "", this);
    } else {
      validate();
      if (errors != null) {
        for (EditorError e : errors) {
          delegate.recordError(e.getMessage(), e.getValue(), this);
        }
      }
    }
  }

  /**
   * Forces the field to be invalid using the given error message. When using this feature, {@link #clearInvalid()} must
   * be called to clear the error. Also, no other validation logic will execute.
   * 
   * @param msg the error text
   */
  public void forceInvalid(String msg) {
    forceInvalidText = msg;
    markInvalid(msg);
  }

  @Override
  public List<EditorError> getErrors() {
    return errors == null ? Collections.<EditorError> emptyList() : Collections.unmodifiableList(errors);
  }

  /**
   * Returns the field's error support instance.
   * 
   * @return the error support
   */
  public ErrorHandler getErrorSupport() {
    return errorSupport;
  }

  /**
   * Returns the field's validators.
   * 
   * @return the validators
   */
  public List<Validator<T>> getValidators() {
    return validators;
  }

  /**
   * Returns whether or not the field value is currently valid.
   * 
   * @return true if valid
   */
  public boolean isValid() {
    return isValid(false);
  }

  @Override
  public boolean isValid(boolean preventMark) {
    if (disabled) {
      return true;
    }
    boolean restore = this.preventMark;
    this.preventMark = preventMark;
    boolean result = validateValue(getValue());
    if (result) {
      // activeErrorMessage = null;
      errors = null;
    }
    this.preventMark = restore;
    return result;
  }

  /**
   * Marks this field as invalid. Validation will still run if called again, and the error message will be changed or
   * cleared based on validation. To set a error message that will not be cleared until manually cleared see
   * {@link #forceInvalid(String)}
   * 
   * Calling this will also register an error in the editor, if any.
   * 
   * @param msg the validation message
   */
  public void markInvalid(String msg) {
    markInvalid(Collections.<EditorError> singletonList(new DefaultEditorError(this, msg, getValue())));
  }

  @Override
  public void onPropertyChange(String... strings) {
    // no-op, subclasses can override if required
  }

  /**
   * Removes a validator from this list of validators that are run when {@link #validateValue(Object)} is invoked.
   * 
   * @param validator the validator to remove
   */
  public void removeValidator(Validator<T> validator) {
    validators.remove(validator);
  }

  /**
   * Resets the current field value to the originally loaded value and clears any validation messages.
   */
  public void reset() {
    boolean restore = preventMark;
    preventMark = true;
    setValue(null);
    preventMark = restore;
    clearInvalid();
  }

  @Override
  public void setDelegate(EditorDelegate<T> delegate) {
    this.delegate = delegate;
  }

  /**
   * Sets the error handler used to mark and query field errors.
   * 
   * @param error the error handler
   */
  public void setErrorSupport(ErrorHandler error) {
    this.errorSupport = error;
  }

  @Override
  public void showErrors(List<EditorError> errors) {
    for (EditorError error : errors) {
      assert error.getEditor() == this;
      // skip the error if sent by a field (this field)
      if (error.getUserData() == this) {
        continue;
      }
      error.setConsumed(true);
    }

    if (errors.size() > 0) {
      errorSupport.markInvalid(errors);
    } else {
      clearInvalid();
    }
  }

  /**
   * Validates the field value.
   * 
   * @return <code>true</code> if valid, otherwise <code>false</code>
   */
  public boolean validate() {
    return validate(false);
  }

  /**
   * Validates the field value.
   * 
   * @param preventMark true to not mark the field valid and fire invalid event when invalid
   * @return <code>true</code> if valid, otherwise <code>false</code>
   */
  public boolean validate(boolean preventMark) {
    if (disabled) {
      clearInvalid();
      return true;
    }
    boolean restore = this.preventMark;
    this.preventMark = preventMark;
    boolean result = validateValue(getValue());
    this.preventMark = restore;
    if (result) {
      clearInvalid();
      errors = null;
    }
    return result;
  }

  /**
   * Actual implementation of markInvalid, which bypasses recording an error in the editor peer.
   * 
   * @param msg the validation message
   */
  protected void markInvalid(List<EditorError> msg) {
    this.errors = msg;
    if (preventMark) {
      return;
    }

    errorSupport.markInvalid(msg);

    fireEvent(new InvalidEvent(msg));
  }

  @Override
  protected void onFocus(Event event) {
    super.onFocus(event);
    if (widget instanceof Component) {
      ((Component) widget).focus();
    } else {
      FocusImpl.getFocusImplForWidget().focus(widget.getElement());
    }
  }

  protected boolean validateValue(T value) {
    if (forceInvalidText != null) {
      markInvalid(forceInvalidText);
      return false;
    }
    List<EditorError> errors = new ArrayList<EditorError>();
    for (int i = 0; i < validators.size(); i++) {
      List<EditorError> temp = validators.get(i).validate(this, value);
      if (temp != null && temp.size() > 0) {
        errors.addAll(temp);
      }
    }
    if (errors.size() > 0) {
      markInvalid(errors);
      return false;
    }
    return true;
  }

}
