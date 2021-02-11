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

import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.sencha.gxt.cell.core.client.ButtonCell.ButtonCellAppearance;
import com.sencha.gxt.cell.core.client.SplitButtonCell;
import com.sencha.gxt.widget.core.client.event.ArrowClickEvent;
import com.sencha.gxt.widget.core.client.event.ArrowClickEvent.ArrowClickHandler;
import com.sencha.gxt.widget.core.client.event.ArrowClickEvent.HasArrowClickHandlers;

/**
 * A split button that provides a built-in dropdown arrow that can fire an event
 * separately from the default click event of the button.
 */
public class SplitButton extends TextButton implements HasArrowClickHandlers {

  public interface SplitButtonAppearance extends ButtonCellAppearance<String> {

  }

  /**
   * Creates a new split button.
   */
  public SplitButton() {
    this(new SplitButtonCell());
  }

  /**
   * Creates a new split button.
   * 
   * @param cell the button's cell
   */
  public SplitButton(SplitButtonCell cell) {
    super(cell);
  }

  /**
   * Creates a new split button.
   * 
   * @param cell the button's cell
   * @param text the button's text
   */
  public SplitButton(SplitButtonCell cell, String text) {
    super(cell, text);
  }

  /**
   * Creates a new split button.
   * 
   * @param text the button's text
   */
  public SplitButton(String text) {
    this(new SplitButtonCell(), text);
  }

  @Override
  public HandlerRegistration addArrowClickHandler(ArrowClickHandler handler) {
    return addHandler(handler, ArrowClickEvent.getType());
  }

  @Override
  protected void onClick(Event e) {
    e.preventDefault();
    hideToolTip();
  }

}
