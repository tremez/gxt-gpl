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

import java.util.List;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import com.sencha.gxt.messages.client.DefaultMessages;

public class MaxNumberValidator<N extends Number> extends AbstractValidator<N> {

  public interface MaxNumberMessages {
    String numberMaxText(double min);
  }

  protected class DefaultMaxNumberMessages implements MaxNumberMessages {
    public String numberMaxText(double min) {
      return DefaultMessages.getMessages().numberField_maxText(min);
    }
  }
  
  protected N maxNumber;
  private MaxNumberMessages messages;
  
  public MaxNumberValidator(N maxNumber) {
    this.maxNumber = maxNumber;
  }

  public Number getMaxNumber() {
    return maxNumber;
  }

  public MaxNumberMessages getMessages() {
    if (messages == null) {
      messages = new  DefaultMaxNumberMessages();
    }
    return messages;
  }

  public void setMaxNumber(N maxNumber) {
    this.maxNumber = maxNumber;
  }

  public void setMessages(MaxNumberMessages messages) {
    this.messages = messages;
  }

  @Override
  public List<EditorError> validate(Editor<N> field, N value) {
    if (value != null && value.doubleValue() > maxNumber.doubleValue()) {
      return createError(field, getMessages().numberMaxText(maxNumber.doubleValue()), value);
    }
    return null;
  }
}
