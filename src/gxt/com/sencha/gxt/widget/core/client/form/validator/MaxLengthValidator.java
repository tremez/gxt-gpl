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
import com.sencha.gxt.widget.core.client.form.error.DefaultEditorError;

public class MaxLengthValidator extends AbstractValidator<String> {

  public interface MaxLengthMessages {
    String maxLengthText(int length);
  }

  protected class DefaultMaxLengthMessages implements MaxLengthMessages {

    @Override
    public String maxLengthText(int length) {
      return DefaultMessages.getMessages().textField_maxLengthText(length);
    }

  }

  protected int maxLength;
  private MaxLengthMessages messages;

  public MaxLengthValidator(int maxLength) {
    this.maxLength = maxLength;
  }

  public MaxLengthMessages getMessages() {
    if (messages == null) {
      messages = new DefaultMaxLengthMessages();
    }
    return messages;
  }

  public void setMessages(MaxLengthMessages messages) {
    this.messages = messages;
  }

  @Override
  public List<EditorError> validate(Editor<String> field, String value) {
    if (value == null) return null;
    int length = value.length();
    if (length > maxLength) {
      return createError(new DefaultEditorError(field, getMessages().maxLengthText(maxLength), value));
    }
    return null;
  }

}
