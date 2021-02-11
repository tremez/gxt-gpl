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
package com.sencha.gxt.widget.core.client.container;

import com.sencha.gxt.core.client.util.Margins;

/**
 * Layout parameter that specifies a widget's margins.
 */
public class MarginData implements HasMargins {
  private Margins margins;

  /**
   * Creates a layout parameter that specifies a widget's margins.
   */
  public MarginData() {
  }

  /**
   * Creates a layout parameter with the specified margins.
   * 
   * @param margins the margins
   */
  public MarginData(int margins) {
    this.margins = new Margins(margins);
  }

  /**
   * Creates a layout parameter with the specified margins.
   * 
   * @param top the top margin
   * @param right the right margin
   * @param bottom the bottom margin
   * @param left the left margin
   */
  public MarginData(int top, int right, int bottom, int left) {
    this.margins = new Margins(top, right, bottom, left);
  }

  /**
   * Creates a layout parameter with the specified margins.
   * 
   * @param margins the margins
   */
  public MarginData(Margins margins) {
    this.margins = margins;
  }

  @Override
  public Margins getMargins() {
    return margins;
  }

  @Override
  public void setMargins(Margins margins) {
    this.margins = margins;

  }

}
