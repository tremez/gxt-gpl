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
package com.sencha.gxt.widget.core.client.button;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Event;
import com.sencha.gxt.cell.core.client.form.ToggleButtonCell;

/**
 * A 2-state toggle button.
 */
public class ToggleButton extends CellButtonBase<Boolean> {

  /**
   * Creates a new toggle button.
   */
  public ToggleButton() {
    this(GWT.<ToggleButtonCell> create(ToggleButtonCell.class));
  }

  /**
   * Creates a new toggle button.
   * 
   * @param text the button text
   */
  public ToggleButton(String text) {
    this();
    setText(text);
  }

  /**
   * Creates a new toggle button.
   * 
   * @param cell the toggle button cell
   */
  public ToggleButton(ToggleButtonCell cell) {
    super(cell, false);
  }

  @Override
  public ToggleButtonCell getCell() {
    return (ToggleButtonCell) super.getCell();
  }

  /**
   * Returns the allow depress state.
   * 
   * @return the allow depress state
   */
  public boolean isAllowDepress() {
    return getCell().isAllowDepress();
  }

  /**
   * True to allow a toggle item to be depressed (defaults to true).
   * 
   * @param allowDepress true to allow depressing
   */
  public void setAllowDepress(boolean allowDepress) {
    getCell().setAllowDepress(allowDepress);
  }

  @Override
  protected void onFocus(Event ce) {
    if (!getValue()) {
      super.onFocus(ce);
    }
  }

}
