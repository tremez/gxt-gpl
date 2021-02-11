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
package com.sencha.gxt.widget.core.client.form.error;

import com.google.gwt.editor.client.Editor;
import com.google.gwt.editor.client.EditorError;
import com.sencha.gxt.core.client.util.Format;

public class DefaultEditorError implements EditorError {

  protected Editor<?> editor;
  protected String message;
  protected boolean consumed;
  protected Object value;
  
  public DefaultEditorError(Editor<?> editor, String message, Object value) {
    this.editor = editor;
    this.message = message;
    this.value = value;
  }
  
  @Override
  public boolean isConsumed() {
    return consumed;
  }

  @Override
  public String getAbsolutePath() {
    return null;
  }

  @Override
  public Editor<?> getEditor() {
    return editor;
  }

  @Override
  public String getMessage() {
    return Format.substitute(message, value);
  }

  @Override
  public String getPath() {
    return null;
  }

  @Override
  public Object getUserData() {
    return null;
  }

  @Override
  public Object getValue() {
    return value;
  }

  @Override
  public void setConsumed(boolean consumed) {
    this.consumed = consumed;
  }
}
