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

import com.google.gwt.resources.client.ImageResource;
import com.sencha.gxt.cell.core.client.TextButtonCell;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

/**
 * A text button.
 */
public class TextButton extends CellButtonBase<String> {

  /**
   * Creates a new text button.
   */
  public TextButton() {
    this(new TextButtonCell());
  }

  /**
   * Creates a new text button.
   * 
   * @param text the button's text
   */
  public TextButton(String text) {
    this(new TextButtonCell(), text);
  }

  /**
   * Creates a new text button.
   * 
   * @param cell the button cell
   * @param text the button's text
   */
  public TextButton(TextButtonCell cell, String text) {
    super(cell, text);
    setText(text);
  }

  /**
   * Creates a new text button.
   * 
   * @param text the button's text
   * @param handler the select handler
   */
  public TextButton(String text, SelectHandler handler) {
    this(text);
    addSelectHandler(handler);
  }

  /**
   * Creates a new text button.
   * 
   * @param text the button's text
   * @param icon the button's icon
   */
  public TextButton(String text, ImageResource icon) {
    this(text);
    setIcon(icon);
  }

  /**
   * Creates a new text button.
   * 
   * @param cell the button's cell
   */
  public TextButton(TextButtonCell cell) {
    super(cell, null);
  }

}
