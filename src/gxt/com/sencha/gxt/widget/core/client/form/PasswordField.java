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

import com.sencha.gxt.cell.core.client.form.PasswordInputCell;

/**
 * A single line input field where the characters are masked to prevent them
 * from being visible to others.
 */
public class PasswordField extends ValueBaseField<String> {

  /**
   * Creates a password field that allows entering a single line of text where
   * the characters are masked to prevent them from being visible to others.
   */
  public PasswordField() {
    this(new PasswordInputCell());
  }

  /**
   * Creates a new password text field.
   */
  public PasswordField(PasswordInputCell cell) {
    super(cell);
    redraw();
  }

  /**
   * Creates a new password text field.
   * 
   * @param cell the input cell
   * @param propertyEditor the property editor
   */
  public PasswordField(PasswordInputCell cell, PropertyEditor<String> propertyEditor) {
    this(cell);
    setPropertyEditor(propertyEditor);
  }

}
