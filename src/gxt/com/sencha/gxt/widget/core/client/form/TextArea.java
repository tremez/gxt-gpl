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

import com.sencha.gxt.cell.core.client.form.TextAreaInputCell;
import com.sencha.gxt.cell.core.client.form.TextAreaInputCell.Resizable;
import com.sencha.gxt.core.client.dom.XElement;

/**
 * A multiple line text input field.
 */
public class TextArea extends ValueBaseField<String> {

  /**
   * Creates a new TextArea
   */
  public TextArea() {
    this(new TextAreaInputCell());
  }

  /**
   * Creates a new text area with the specified cell.
   * 
   * @param cell a text area input cell that renders the text area
   */
  public TextArea(TextAreaInputCell cell) {
    super(cell);
    redraw();
  }

  /**
   * Gets the cursor position.
   * 
   * @return returns the cursor position.
   */
  @Override
  public int getCursorPos() {
    return getImpl().getTextAreaCursorPos(getInputEl());
  }

  /**
   * Returns the resizable value.
   * 
   * @return the resize value
   */
  public Resizable getResizable() {
    return ((TextAreaInputCell) getCell()).getResizable();
  }

  /**
   * Gets the selection length
   * 
   * @return returns the selection length.
   */
  @Override
  public int getSelectionLength() {
    return getImpl().getTextAreaSelectionLength(getInputEl());
  }

  /**
   * Returns true if scroll bars are disabled.
   * 
   * @return the scroll bar state
   */
  public boolean isPreventScrollbars() {
    return ((TextAreaInputCell) getCell()).isPreventScrollbars();
  }
  
  @Override
  protected void onRedraw() {
    super.onRedraw();
    XElement input = getElement().selectNode("textarea");
    if (input != null) {
      input.setId(getId() + "-input");
    }
  }

  /**
   * True to prevent scrollbars from appearing regardless of how much text is in
   * the field (equivalent to setting overflow: hidden, defaults to false.
   * 
   * @param preventScrollbars true to disable scroll bars
   */
  public void setPreventScrollbars(boolean preventScrollbars) {
    ((TextAreaInputCell) getCell()).setPreventScrollbars(getElement(), preventScrollbars);
  }

  /**
   * Sets whether the field can be resized (defaults to NONE). This method uses
   * the CSS resize property which is only supported on browsers that support
   * CSS3.
   * 
   * @param resizable the resizable value
   */
  public void setResizable(Resizable resizable) {
    ((TextAreaInputCell) getCell()).setResizable(getElement(), resizable);
  }
}
