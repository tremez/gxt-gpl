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
import java.util.Date;

import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.i18n.shared.DateTimeFormat.PredefinedFormat;

public class DateTimePropertyEditor extends PropertyEditor<Date>{

  protected DateTimeFormat format = DateTimeFormat.getFormat(PredefinedFormat.DATE_SHORT);
  protected boolean parseStrict = true;
  
  /**
   * Creates a new date time property editor.
   */
  public DateTimePropertyEditor() {

  }

  /**
   * Creates a new date time property editor.
   * 
   * @param format the date time format
   */
  public DateTimePropertyEditor(DateTimeFormat format) {
    this.format = format;
  }
  
  /**
   * Creates a new date time property editor.
   * 
   * @param pattern the pattern used to create a new @link
   *          {@link DateTimeFormat}.
   */
  public DateTimePropertyEditor(String pattern) {
    this.format = DateTimeFormat.getFormat(pattern);
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
   * Returns true if parsing strictly.
   * 
   * @return the parse strict state
   */
  public boolean isParseStrict() {
    return parseStrict;
  }

  @Override
  public Date parse(CharSequence text) throws ParseException {
    try {
      if (parseStrict) {
        return format.parseStrict(text.toString());
      } else {
        return format.parse(text.toString());
      }
    } catch (Exception ex) {
      throw new ParseException(ex.getMessage(), 0);
    }
  }

  @Override
  public String render(Date value) {
    return format.format(value);
  }

  /**
   * True to parse dates strictly (defaults to true). See @link
   * {@link DateTimeFormat#parseStrict(String)}.
   * 
   * @param parseStrict true to parse strictly
   */
  public void setParseStrict(boolean parseStrict) {
    this.parseStrict = parseStrict;
  }

}
