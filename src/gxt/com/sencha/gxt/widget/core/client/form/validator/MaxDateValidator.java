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
package com.sencha.gxt.widget.core.client.form.validator;

import java.util.Date;
import java.util.List;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.i18n.shared.DateTimeFormat;
import com.google.gwt.i18n.shared.DateTimeFormat.PredefinedFormat;
import com.sencha.gxt.core.client.util.DateWrapper;
import com.sencha.gxt.messages.client.DefaultMessages;

/**
 * Tests if the value is on the same day or earlier than the specified max date.
 */
public class MaxDateValidator extends AbstractValidator<Date> {

  public interface MaxDateMessages {
    String dateMaxText(String max);
  }

  protected class DefaultMaxDateMessages implements MaxDateMessages {

    @Override
    public String dateMaxText(String max) {
      return DefaultMessages.getMessages().dateField_maxText(max);
    }

  }

  protected Date maxDate;
  private MaxDateMessages messages;
  private DateTimeFormat format = DateTimeFormat.getFormat(PredefinedFormat.DATE_SHORT);

  public MaxDateValidator(Date maxDate) {
    setMaxDate(maxDate);
  }

  public MaxDateValidator(Date maxDate, DateTimeFormat format) {
    this(maxDate);
    this.format = format;
  }

  public Date getMaxDate() {
    return maxDate;
  }

  public MaxDateMessages getMessages() {
    if (messages == null) {
      messages = new DefaultMaxDateMessages();
    }
    return messages;
  }

  /**
   * Sets the max date. Hours, minutes, seconds, and milliseconds are cleared.
   * 
   * @param maxDate the max date
   */
  public void setMaxDate(Date maxDate) {
    this.maxDate = new DateWrapper(maxDate).clearTime().asDate();
  }

  public void setMessages(MaxDateMessages messages) {
    this.messages = messages;
  }

  @Override
  public List<EditorError> validate(Editor<Date> editor, Date value) {
    if (value != null && value.after(maxDate)) {
      return createError(editor, getMessages().dateMaxText(format.format(maxDate)), value);
    }
    return null;
  }
}
