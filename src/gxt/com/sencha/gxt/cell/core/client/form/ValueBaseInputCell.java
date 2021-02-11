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
package com.sencha.gxt.cell.core.client.form;

import java.util.Set;
import java.util.logging.Logger;

import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.InputElement;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.ui.impl.TextBoxImpl;
import com.sencha.gxt.cell.core.client.FocusableCell;
import com.sencha.gxt.core.client.GXT;
import com.sencha.gxt.core.client.GXTLogConfiguration;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.gestures.TapGestureRecognizer.CellTapGestureRecognizer;
import com.sencha.gxt.core.client.gestures.TouchData;
import com.sencha.gxt.widget.core.client.event.ParseErrorEvent;
import com.sencha.gxt.widget.core.client.event.ParseErrorEvent.HasParseErrorHandlers;
import com.sencha.gxt.widget.core.client.event.ParseErrorEvent.ParseErrorHandler;
import com.sencha.gxt.widget.core.client.form.PropertyEditor;

/**
 * A <code>FieldCell</code> which has an input element.
 * 
 * @param <T> the data type
 */
public abstract class ValueBaseInputCell<T> extends FieldCell<T> implements HasParseErrorHandlers, FocusableCell {

  private CellTapGestureRecognizer<T> cellTapGestureRecognizer = new CellTapGestureRecognizer<T>() {
    @Override
    protected void onTap(TouchData tap, Context context, Element parent, T value, ValueUpdater<T> valueUpdater) {
      ValueBaseInputCell.this.onTap(tap, context, parent, value, valueUpdater);
    }

    @Override
    protected void handlePreventDefault(NativeEvent event) {
      // don't prevent default on input cells
    }
  };

  public interface ValueBaseFieldAppearance extends FieldAppearance {
    XElement getInputElement(Element parent);
  }

  protected static TextBoxImpl impl = GWT.create(TextBoxImpl.class);

  /**
   * True to finish the edit if a blur is fired without a change event for browsers that do fire change events when
   * blurring the browser window.
   */
  protected boolean finishEditOnBlur = true;

  @SuppressWarnings("unchecked")
  protected PropertyEditor<T> propertyEditor = (PropertyEditor<T>) PropertyEditor.DEFAULT;
  protected String name;
  protected boolean allowBlank = true;

  private String emptyText;
  private boolean selectOnFocus;
  private static Logger logger = Logger.getLogger(ValueBaseInputCell.class.getName());
  private int cursorLocation;
  private boolean clearValueOnParseError = true;
  private boolean stopClick;

  /**
   * Creates a new cell instance.
   * 
   * @param appearance the appearance
   */
  public ValueBaseInputCell(ValueBaseFieldAppearance appearance) {
    super(appearance);
    addCellGestureAdapter(cellTapGestureRecognizer);
  }

  /**
   * Creates a new cell instance.
   * 
   * @param appearance the appearance
   * @param consumedEvents the consumed events
   */
  public ValueBaseInputCell(ValueBaseFieldAppearance appearance, Set<String> consumedEvents) {
    super(appearance, consumedEvents);
    addCellGestureAdapter(cellTapGestureRecognizer);
  }

  /**
   * Creates a new cell instance.
   * 
   * @param appearance the appearance
   * @param consumedEvents the consumed events
   */
  public ValueBaseInputCell(ValueBaseFieldAppearance appearance, String... consumedEvents) {
    super(appearance, consumedEvents);
    addCellGestureAdapter(cellTapGestureRecognizer);
  }

  @Override
  public HandlerRegistration addParseErrorHandler(ParseErrorHandler handler) {
    return addHandler(handler, ParseErrorEvent.getType());
  }

  @Override
  public void disable(XElement parent) {
    super.disable(parent);
    getAppearance().getInputElement(parent).disable();
  }

  @Override
  public void enable(XElement parent) {
    super.enable(parent);
    getAppearance().getInputElement(parent).enable();
  }

  /**
   * Returns the cell's appearance.
   * 
   * @return the appearance
   */
  public ValueBaseFieldAppearance getAppearance() {
    return (ValueBaseFieldAppearance) super.getAppearance();
  }

  /**
   * Gets the current position of the cursor (this also serves as the beginning of the text selection).
   * 
   * @param parent the parent
   * @return the cursor's position
   */
  public int getCursorPos(XElement parent) {
    return impl.getCursorPos(getInputElement(parent).<XElement> cast());
  }

  /**
   * Returns the cell's empty text.
   * 
   * @return the empty text
   */
  public String getEmptyText() {
    return emptyText;
  }

  @Override
  public XElement getFocusElement(XElement parent) {
    return getInputElement(parent).cast();
  }

  @Override
  public InputElement getInputElement(Element parent) {
    return getAppearance().getInputElement(parent.<XElement> cast()).cast();
  }

  /**
   * Returns the field's name.
   * 
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the cell's property editor.
   * 
   * @return the property editor
   */
  public PropertyEditor<T> getPropertyEditor() {
    return propertyEditor;
  }

  /**
   * Returns the cell's raw text.
   * 
   * @param parent the parent element
   * @return text from the input field
   */
  public String getText(XElement parent) {
    String s = getInputElement(parent).getValue();
    return emptyText != null && emptyText.equals(s) ? "" : s;
  }

  /**
   * Returns the field's allow blank state.
   * 
   * @return true if blank values are allowed
   */
  public boolean isAllowBlank() {
    return allowBlank;
  }

  /**
   * Returns true if the current value is cleared on a parse error.
   * 
   * @return true if clearing on parse error
   */
  public boolean isClearValueOnParseError() {
    return clearValueOnParseError;
  }

  /**
   * Returns the select of focus state.
   * 
   * @return true if select on focus is enabled
   */
  public boolean isSelectOnFocus() {
    return selectOnFocus;
  }

  @Override
  public void onEmpty(XElement parent, boolean empty) {
    getAppearance().onEmpty(parent, empty);
  }

  @Override
  public boolean resetFocus(Context context, Element parent, T value) {
    getAppearance().getInputElement(parent).focus();
    if (cursorLocation > 0) {
      setCursorPos(parent.<XElement> cast(), cursorLocation);
    }
    return true;
  }

  /**
   * Selects the text.
   * 
   * @param parent the parent element
   * @param start the start index
   * @param length the selection length
   */
  public void select(XElement parent, int start, int length) {
    if (length < 0) {
      throw new IndexOutOfBoundsException("Length must be a positive integer. Length: " + length);
    }
    if (start < 0 || length + start > getText(parent).length()) {
      throw new IndexOutOfBoundsException("From Index: " + start + "  To Index: " + (start + length)
          + "  Text Length: " + getText(parent).length());
    }
    impl.setSelectionRange((Element) getInputElement(parent).cast(), start, length);
  }

  /**
   * Selects all the text.
   * 
   * @param parent the parent
   */
  @SuppressWarnings("deprecation")
  public void selectAll(XElement parent) {
    int length = getText(parent).length();
    if (length > 0) {
      select(parent, 0, length);
    } else {
      // EXTGWT-3391 no focus when starting an edit with an empty field in IE8
      if (GXT.isIE8()) {
        impl.setSelectionRange((com.google.gwt.user.client.Element) getInputElement(parent).cast(), 0, 0);
      }
    }
  }

  /**
   * Sets whether a field is valid when its value length = 0 (default to true).
   * 
   * @param allowBlank true to allow blanks, false otherwise
   */
  public void setAllowBlank(boolean allowBlank) {
    this.allowBlank = allowBlank;
  }

  /**
   * True to clear the current value when a parse error occurs (defaults to true).
   * 
   * @param clearValueOnParseError true to clean the value on parse error
   */
  public void setClearValueOnParseError(boolean clearValueOnParseError) {
    this.clearValueOnParseError = clearValueOnParseError;
  }

  /**
   * Sets the cursor position.
   * 
   * This will only work when the widget is attached to the document and not hidden.
   * 
   * @param parent the parent
   * @param pos the new cursor position
   */
  public void setCursorPos(XElement parent, int pos) {
    select(parent, pos, 0);
  }

  /**
   * Sets the default text to display in an empty field (defaults to null).
   * 
   * @param context the context
   * @param parent the parent
   * @param emptyText the empty text
   */
  public void setEmptyText(Context context, XElement parent, String emptyText) {
    removeEmptyText(parent);
    this.emptyText = emptyText;
    applyEmptyText(context, parent);
  }

  @Override
  public void setName(XElement parent, String name) {
    this.name = name;
    getInputElement(parent).setName(name);
  }

  /**
   * Sets the field's property editor which is used to translate typed values to string, and string values back to typed
   * values.
   * 
   * @param propertyEditor the property editor
   */
  public void setPropertyEditor(PropertyEditor<T> propertyEditor) {
    this.propertyEditor = propertyEditor;
  }

  /**
   * True to automatically select any existing field text when the field receives input focus (defaults to false). This
   * only applies when the field becomes focused via a click. This setting has no effect when tabbing into the field as
   * the browser will select all text.
   * 
   * @param selectOnFocus true to focus
   */
  public void setSelectOnFocus(boolean selectOnFocus) {
    this.selectOnFocus = selectOnFocus;
  }

  /**
   * Sets the underlying DOM field's value directly, bypassing validation. This method does not update the field's
   * value. To set the value with validation see {@link #setValue}.
   * 
   * @param parent the parent element
   * @param text the text
   */
  public void setText(XElement parent, String text) {
    getInputElement(parent).setValue(text != null ? text : "");
  }

  protected void applyEmptyText(Context context, XElement parent) {
    if (!hasFocus(context, parent) && emptyText != null && getText(parent).length() < 1) {
      if (GXT.isIE8() || GXT.isIE9()) {
        setText(parent, emptyText);
      }
      onEmpty(parent, true);
    }
    // can set this whether IE or not
    getInputElement(parent).setAttribute("placeholder", emptyText == null ? "" : emptyText);
  }

  @Override
  protected void onBlur(Context context, XElement parent, T value, NativeEvent event, ValueUpdater<T> valueUpdater) {
    // all browsers fire change the blur event on a 'normal' blur
    // when blurring the browser window does the browser fire a change event
    // browsers that do NOT fire a change event on browser window blur
    // Safari, Chrome, IE9, IE8
    //
    // Browsers should fire the change event
    // http://www.w3.org/TR/DOM-Level-2-Events/events.html#Events-eventgroupings-htmlevents-h3

    if (finishEditOnBlur && (GXT.isSafari() || GXT.isChrome() || GXT.isIE9() || GXT.isIE10() || GXT.isMSEdge())) {
      String text = getText(parent);
      String sValue = getPropertyEditor().render(value);
      if (!text.equals(sValue)) {
        if (GXTLogConfiguration.loggingIsEnabled()) {
          logger.finest("onBlur input value not equal to value, the browser CHANGE not fired, manually call finishEditing");
        }
        finishEditing(parent, value, context.getKey(), valueUpdater);
      }
    }

    super.onBlur(context, parent, value, event, valueUpdater);
    applyEmptyText(context, parent);
  }

  @Override
  protected void onClick(XElement parent, NativeEvent event) {
    if (stopClick) {
      event.preventDefault();
    }
    stopClick = false;
    super.onClick(parent, event);
  }

  @Override
  protected void onFocus(Context context, XElement parent, T value, NativeEvent event, ValueUpdater<T> updater) {
    super.onFocus(context, parent, value, event, updater);
    if (GXTLogConfiguration.loggingIsEnabled()) {
      logger.finest("onFocus");
    }

    if (emptyText != null) {
      removeEmptyText(parent);
    }
    if (selectOnFocus) {
      selectAll(parent);
    }
  }

  protected void onKeyUp(Context context, XElement parent, T value, NativeEvent event, ValueUpdater<T> valueUpdater) {
    super.onKeyUp(context, parent, value, event, valueUpdater);

    cursorLocation = getCursorPos(parent.<XElement> cast());
  }

  @Override
  protected void onMouseDown(XElement parent, NativeEvent event) {
    super.onMouseDown(parent, event);

    // There is a bug in Edge that will not focus the textarea
    // the caret is there, but never receives the focus event and document.activeElement is always body
    if (GXT.isMSEdge()) {
      getInputElement(parent).focus();
    }

    stopClick = selectOnFocus && !hasFocus(null, parent);
  }

  protected void onTap(TouchData t, Context context, Element parent, T value, ValueUpdater<T> valueUpdater) {
  }

  protected void removeEmptyText(XElement parent) {
    onEmpty(parent, false);

    String v = getText(parent);
    if (((emptyText != null && emptyText.equals(v)) || "".equals(v)) && (GXT.isIE8() || GXT.isIE9())) {
      setText(parent, "");
      select(parent, 0, 0);
    }
  }

}
