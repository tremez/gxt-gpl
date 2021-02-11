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

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.sencha.gxt.cell.core.client.form.RadioCell;
import com.sencha.gxt.core.client.util.ToggleGroup;

/**
 * Single radio field.
 * <p/>
 * {@link ValueChangeEvent}s are fired when the checkbox state is changed by the user, instead of waiting for a
 * {@link BlurEvent}.
 * <p/>
 * Group radios together using the {@link ToggleGroup}.
 */
public class Radio extends CheckBox {

  /**
   * Creates a new radio field.
   */
  public Radio() {
    this(new RadioCell());
  }

  /**
   * Creates a new radio field.
   *
   * @param cell the radio cell
   */
  public Radio(RadioCell cell) {
    super(cell);
  }

  /**
   * Sets the group name of the radios.
   * <ul>
   * <li>When grouping radios, also use {@link ToggleGroup} to group them.</li>
   * <li>Setting the name is not required in a {@link ToggleGroup}</li>
   * </ul>
   *
   * @param name is the group name of the radios.
   */
  @Override
  public void setName(String name) {
    super.setName(name);
  }

}
