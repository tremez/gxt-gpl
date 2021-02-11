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

import java.util.Date;

import com.google.gwt.cell.client.ValueUpdater;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.i18n.client.DateTimeFormat.PredefinedFormat;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.util.DateWrapper;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.widget.core.client.form.DateTimePropertyEditor;

public class TimeFieldCell extends ComboBoxCell<Date> {

  private static class Provider implements LabelProvider<Date> {

    private DateTimeFormat format = DateTimeFormat.getFormat(PredefinedFormat.TIME_SHORT);

    public DateTimeFormat getFormat() {
      return format;
    }

    @Override
    public String getLabel(Date item) {
      return getFormat().format(item);
    }

    public void setFormat(DateTimeFormat format) {
      this.format = format;
    }

  }

  private DateTimeFormat format = DateTimeFormat.getFormat(PredefinedFormat.TIME_SHORT);
  private int increment = 15;
  private Date maxValue;
  private Date minValue;
  private boolean initialized;

  public TimeFieldCell() {
    super(new ListStore<Date>(new ModelKeyProvider<Date>() {
      @Override
      public String getKey(Date item) {
        return "" + item.getTime();
      }
    }), new Provider());
    setPropertyEditor(new DateTimePropertyEditor(format));
  }

  @Override
  public void expand(Context context, XElement parent, ValueUpdater<Date> updater, Date value) {
    fillStore();
    super.expand(context, parent, updater, value);
  }

  /**
   * Returns the date time format.
   * 
   * @return the date time format
   */
  public DateTimeFormat getFormat() {
    return format;
  }

  /**
   * Returns the number of minutes between each time value.
   * 
   * @return the increment
   */
  public int getIncrement() {
    return increment;
  }

  /**
   * Returns the field's max value.
   * 
   * @return the max value
   */
  public Date getMaxValue() {
    return maxValue;
  }

  /**
   * Returns the fields minimum value.
   * 
   * @return the min value
   */
  public Date getMinValue() {
    return minValue;
  }

  /**
   * Sets the date time format used to format each entry (defaults to
   * {@link PredefinedFormat#TIME_SHORT}).
   * 
   * @param format the date time format
   */
  public void setFormat(DateTimeFormat format) {
    this.format = format;

    ((Provider) getLabelProvider()).setFormat(format);
    setPropertyEditor(new DateTimePropertyEditor(format));
  }

  /**
   * Sets the number of minutes between each time value in the list (defaults to
   * 15).
   * 
   * @param increment the increment
   */
  public void setIncrement(int increment) {
    this.increment = increment;
  }

  /**
   * Sets the field's max value.
   * 
   * @param value the max value
   */
  public void setMaxValue(Date value) {
    this.maxValue = value;
  }

  /**
   * The minimum allowed time (no default value).
   * 
   * @param value the minimum date
   */
  public void setMinValue(Date value) {
    this.minValue = value;
  }

  @Override
  protected void onTriggerClick(Context context, XElement parent, NativeEvent event,
      Date value, ValueUpdater<Date> updater) {
    fillStore();
    super.onTriggerClick(context, parent, event, value, updater);
  }

  private void fillStore() {
    if (!initialized) {
      this.initialized = true;
      DateWrapper min = minValue != null ? new DateWrapper(resetDate(minValue))
          : new DateWrapper(1970, 0, 1).clearTime();
      DateWrapper max = maxValue != null ? new DateWrapper(resetDate(maxValue))
          : new DateWrapper(1970, 0, 1).clearTime().addDays(1);

      store.clear();

      while (min.before(max)) {
        store.add(min.asDate());
        min = min.addMinutes(increment);
      }
    }
  }

  @SuppressWarnings("deprecation")
  private Date resetDate(Date date) {
    return new DateWrapper(1970, 0, 1).clearTime().addHours(date.getHours()).addMinutes(date.getMinutes()).addSeconds(
        date.getSeconds()).asDate();
  }
}
