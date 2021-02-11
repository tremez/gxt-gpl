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
package com.sencha.gxt.widget.core.client.menu;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.logical.shared.HasValueChangeHandlers;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.sencha.gxt.widget.core.client.DatePicker;

public class DateMenu extends Menu implements HasValueChangeHandlers<Date>{

  protected DatePicker picker;

  public DateMenu() {
    this(new DatePicker());
  }

  public DateMenu(DatePicker datePicker) {
    this(datePicker, GWT.<MenuAppearance>create(MenuAppearance.class));
  }

  public DateMenu(DatePicker datePicker, MenuAppearance menuAppearance) {
    super(menuAppearance);

    this.picker = datePicker;

    picker.addValueChangeHandler(new ValueChangeHandler<Date>() {
      @Override
      public void onValueChange(ValueChangeEvent<Date> event) {
        onPickerSelect(event);
      }
    });

    add(picker);
    getAppearance().applyDateMenu(getElement());
    plain = true;
    showSeparator = false;
    setEnableScrolling(false);
  }

  @Override
  public HandlerRegistration addValueChangeHandler(ValueChangeHandler<Date> handler) {
    return addHandler(handler, ValueChangeEvent.getType());
  }

  @Override
  public void focus() {
    super.focus();
    picker.getElement().focus();
  }

  /**
   * Returns the selected date.
   * 
   * @return the date
   */
  public Date getDate() {
    return picker.getValue();
  }

  /**
   * Returns the date picker.
   * 
   * @return the date picker
   */
  public DatePicker getDatePicker() {
    return picker;
  }

  /**
   * Sets the menu's date.
   * 
   * @param date the date
   */
  public void setDate(Date date) {
    picker.setValue(date);
  }

  protected void onPickerSelect(ValueChangeEvent<Date> event) {
    fireEvent(event);
  }

}
