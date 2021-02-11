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

import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.sencha.gxt.cell.core.client.form.TimeFieldCell;
import com.sencha.gxt.messages.client.DefaultMessages;
import com.sencha.gxt.widget.core.client.event.ParseErrorEvent;

/**
 * Provides a time input field with a time dropdown and automatic time
 * validation.
 */
public class TimeField extends ComboBox<Date> {

  /**
   * Creates a new time field.
   */
  public TimeField() {
    this(new TimeFieldCell());
  }

  /**
   * Creates a new time field.
   * 
   * @param cell the time field cell
   */
  public TimeField(TimeFieldCell cell) {
    super(cell);
  }

  @Override
  public TimeFieldCell getCell() {
    return (TimeFieldCell) super.getCell();
  }

  /**
   * Returns the date time format.
   * 
   * @return the date time format
   */
  public DateTimeFormat getFormat() {
    return getCell().getFormat();
  }

  /**
   * Returns the number of minutes between each time value.
   * 
   * @return the increment
   */
  public int getIncrement() {
    return getCell().getIncrement();
  }

  /**
   * Returns the field's max value.
   * 
   * @return the max value
   */
  public Date getMaxValue() {
    return getCell().getMaxValue();
  }

  /**
   * Returns the fields minimum value.
   * 
   * @return the min value
   */
  public Date getMinValue() {
    return getCell().getMinValue();
  }

  /**
   * Sets the date time format used to format each entry (defaults to
   * {@link PredefinedFormat#TIME_SHORT}).
   * 
   * @param format the date time format
   */
  public void setFormat(DateTimeFormat format) {
    getCell().setFormat(format);
  }

  /**
   * Sets the number of minutes between each time value in the list (defaults to
   * 15).
   * 
   * @param increment the increment
   */
  public void setIncrement(int increment) {
    getCell().setIncrement(increment);
  }

  /**
   * Sets the field's max value.
   * 
   * @param value the max value
   */
  public void setMaxValue(Date value) {
    getCell().setMaxValue(value);
  }

  /**
   * The minimum allowed time (no default value).
   * 
   * @param value the minimum date
   */
  public void setMinValue(Date value) {
    getCell().setMinValue(value);
  }

  @Override
  protected void onCellParseError(ParseErrorEvent event) {
    super.onCellParseError(event);
    String value = event.getException().getMessage();
    String f = getFormat().getPattern();
    String msg = DefaultMessages.getMessages().timeField_invalidText(value, f);
    parseError = msg;
    forceInvalid(msg);
  }

}
