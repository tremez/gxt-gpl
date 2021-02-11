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

import com.google.gwt.cell.client.ValueUpdater;

/**
 * The {@code ViewData} for this cell.
 */
public class ViewData {
  /**
   * The last value that was updated.
   */
  private String lastValue;

  /**
   * The current value.
   */
  private String curValue;

  /**
   * Construct a ViewData instance containing a given value.
   * 
   * @param value a String value
   */
  public ViewData(String value) {
    this.lastValue = value;
    this.curValue = value;
  }

  /**
   * Return true if the last and current values of this ViewData object are
   * equal to those of the other object.
   */
  @Override
  public boolean equals(Object other) {
    if (!(other instanceof ViewData)) {
      return false;
    }
    ViewData vd = (ViewData) other;
    return equalsOrNull(lastValue, vd.lastValue) && equalsOrNull(curValue, vd.curValue);
  }

  /**
   * Return the current value of the input element.
   * 
   * @return the current value String
   * @see #setCurrentValue(String)
   */
  public String getCurrentValue() {
    return curValue;
  }

  /**
   * Return the last value sent to the {@link ValueUpdater}.
   * 
   * @return the last value String
   * @see #setLastValue(String)
   */
  public String getLastValue() {
    return lastValue;
  }

  /**
   * Return a hash code based on the last and current values.
   */
  @Override
  public int hashCode() {
    return (lastValue + "_*!@HASH_SEPARATOR@!*_" + curValue).hashCode();
  }

  /**
   * Set the current value.
   * 
   * @param curValue the current value
   * @see #getCurrentValue()
   */
  protected void setCurrentValue(String curValue) {
    this.curValue = curValue;
  }

  /**
   * Set the last value.
   * 
   * @param lastValue the last value
   * @see #getLastValue()
   */
  protected void setLastValue(String lastValue) {
    this.lastValue = lastValue;
  }

  private boolean equalsOrNull(Object a, Object b) {
    return (a != null) ? a.equals(b) : ((b == null) ? true : false);
  }
}