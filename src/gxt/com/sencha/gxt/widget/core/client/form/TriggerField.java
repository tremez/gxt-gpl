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

import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.sencha.gxt.cell.core.client.form.TriggerFieldCell;
import com.sencha.gxt.widget.core.client.event.TriggerClickEvent;
import com.sencha.gxt.widget.core.client.event.TriggerClickEvent.HasTriggerClickHandlers;
import com.sencha.gxt.widget.core.client.event.TriggerClickEvent.TriggerClickHandler;

/**
 * An abstract base class for an input field and a clickable trigger. The purpose of the trigger is defined by the
 * derived class (e.g. displaying a drop down or modifying the value of the input field).
 * 
 * @param <T> the field type
 */
public abstract class TriggerField<T> extends ValueBaseField<T> implements HasTriggerClickHandlers {

  protected TriggerField(PropertyEditor<T> propertyEditor) {
    this(new TriggerFieldCell<T>());
    setPropertyEditor(propertyEditor);
  }

  protected TriggerField(TriggerFieldCell<T> cell) {
    super(cell);
  }

  protected TriggerField(TriggerFieldCell<T> cell, PropertyEditor<T> propertyEditor) {
    this(cell);
    setPropertyEditor(propertyEditor);
  }

  @Override
  public HandlerRegistration addTriggerClickHandler(TriggerClickHandler handler) {
    return addHandler(handler, TriggerClickEvent.getType());
  }

  @Override
  public TriggerFieldCell<T> getCell() {
    return (TriggerFieldCell<T>) super.getCell();
  }

  /**
   * Returns {@code true} if the field is editable.
   * 
   * @return {@code true} if editable
   */
  public boolean isEditable() {
    return getCell().isEditable();
  }

  /**
   * Returns the finish on enter key state.
   * 
   * @return {@code true} if editing finished on enter key
   */
  public boolean isFinishEditOnEnter() {
    return getCell().isFinishEditOnEnter();
  }

  /**
   * Returns {@code true} if tab key events are being monitored.
   * 
   * @return {@code true} if monitoring
   */
  public boolean isMonitorTab() {
    return getCell().isMonitorTab();
  }

  /**
   * Allow or prevent the user from directly editing the field text (defaults to {@code true}). If {@code false} is
   * passed, the user will only be able to select from the items defined in the dropdown list.
   * 
   * @param editable {@code true} to allow the user to directly edit the field text
   */
  public void setEditable(boolean editable) {
    getCell().setEditable(getElement(), editable);
  }

  /**
   * Determines if the current edit should be completed when the enter key is pressed (defaults to {@code true}). When enabled,
   * the field will be blurred causing {@link TriggerFieldCell#finishEditing(Element, Object, Object, ValueUpdater)} to
   * be called.
   * 
   * @param finishEditOnEnter {@code true} to call
   *          {@link TriggerFieldCell#finishEditing(Element, Object, Object, ValueUpdater)} when enter key is pressed
   */
  public void setFinishEditOnEnter(boolean finishEditOnEnter) {
    getCell().setFinishEditOnEnter(finishEditOnEnter);
  }

  /**
   * Sets the visibility of the trigger.
   * 
   * @param hideTrigger {@code true} to hide the trigger
   */
  public void setHideTrigger(boolean hideTrigger) {
    getCell().setHideTrigger(hideTrigger);
    redraw();
  }

  /**
   * True to monitor tab key events to force the bluring of the field (defaults to {@code true}).
   * 
   * @param monitorTab {@code true} to monitor tab key events
   */
  public void setMonitorTab(boolean monitorTab) {
    getCell().setMonitorTab(monitorTab);
  }

  @Override
  protected void onBlur(Event be) {
    // no-op, triggerBlur will call super.onBlur in certain cases
  }

  @Override
  protected void onDetach() {
    super.onDetach();
    // TODO look into better solution with cancelled editing, cell is left with
    // mimicking true
    setMimicking(getCell());
  }

  @SuppressWarnings("rawtypes")
  private native void setMimicking(TriggerFieldCell cell) /*-{
		cell.@com.sencha.gxt.cell.core.client.form.TriggerFieldCell::mimicking = false;
  }-*/;

}
