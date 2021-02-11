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

import java.util.HashMap;
import java.util.Map;

/**
 * Defines the configuration information for an aggregation row.
 * 
 * @param <M> the model type
 */
public class AggregationRowConfig<M> {

  private Map<ColumnConfig<M, ?>, String> cellStyle;
  private Map<ColumnConfig<M, ?>, AggregationRenderer<M>> renderers;

  /**
   * Creates a new aggregation row config.s
   */
  public AggregationRowConfig() {
    renderers = new HashMap<ColumnConfig<M, ?>, AggregationRenderer<M>>();
    cellStyle = new HashMap<ColumnConfig<M, ?>, String>();
  }

  /**
   * Returns the cell style for the given column.
   * 
   * @param id the column id
   * @return the CSS style name
   */
  public String getCellStyle(ColumnConfig<M, ?> id) {
    return cellStyle.get(id);
  }

  /**
   * Returns the aggregation renderer for the given column.
   * 
   * @param config the column
   * @return the aggregation renderer
   */
  public AggregationRenderer<M> getRenderer(ColumnConfig<M, ?> config) {
    return renderers.get(config);
  }

  /**
   * Sets the cell style for the given column.
   * 
   * @param config the column
   * @param style the CSS style name
   */
  public void setCellStyle(ColumnConfig<M, ?> config, String style) {
    cellStyle.put(config, style);
  }

  /**
   * Sets the aggregation renderer for the given column.
   * 
   * @param config the column
   * @param renderer the renderer
   */
  public void setRenderer(ColumnConfig<M, ?> config, AggregationRenderer<M> renderer) {
    renderers.put(config, renderer);
  }

}
