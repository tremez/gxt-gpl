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
package com.sencha.gxt.widget.core.client.grid;

import com.sencha.gxt.core.client.ValueProvider;

/**
 * The GridViewConfig is used to return a CSS style name for rows in a Grid. See
 * {@link GridView#setViewConfig(GridViewConfig)}.
 */
public interface GridViewConfig<M> {

  /**
   * Returns one to many CSS style names separated by spaces.
   * 
   * @param model the model for the row
   * @param valueProvider the valueProvider for the col
   * @param rowIndex the row index
   * @param colIndex the row index
   * @return the CSS style name(s) separated by spaces.
   */
  public String getColStyle(M model, ValueProvider<? super M, ?> valueProvider, int rowIndex, int colIndex);

  /**
   * Returns one to many CSS style names separated by spaces.
   * 
   * @param model the model for the row
   * @param rowIndex the row index
   * @return the CSS style name(s) separated by spaces.
   */
  public String getRowStyle(M model, int rowIndex);

}
