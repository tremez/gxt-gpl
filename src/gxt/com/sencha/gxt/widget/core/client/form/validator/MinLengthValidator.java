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

public class MinLengthValidator extends AbstractValidator<String> {

  public interface MinLengthMessages {
    String minLengthText(int length);
  }

  protected class DefaultMinLengthMessages implements MinLengthMessages {

    @Override
    public String minLengthText(int length) {
      return DefaultMessages.getMessages().textField_minLengthText(length);
    }

  }

  protected int minLength;
  private MinLengthMessages messages;

  public MinLengthValidator(int minLength) {
    this.minLength = minLength;
  }

  public MinLengthMessages getMessages() {
    if (messages == null) {
      messages = new DefaultMinLengthMessages();
    }
    return messages;
  }

  public void setMessages(MinLengthMessages messages) {
    this.messages = messages;
  }

  @Override
  public List<EditorError> validate(Editor<String> field, String value) {
    if (value == null) {
      return null;
    }
    int length = value.length();
    if (length < minLength) {
      return createError(new DefaultEditorError(field, getMessages().minLengthText(minLength), value));
    }
    return null;
  }

}
