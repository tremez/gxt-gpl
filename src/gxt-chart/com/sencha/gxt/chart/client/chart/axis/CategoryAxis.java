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
package com.sencha.gxt.chart.client.chart.axis;

import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.ListStore;

/**
 * A type of axis that displays items in categories. This axis is generally used
 * to display categorical information like names of items, month names,
 * quarters, etc. but no quantitative values. For the other types of information
 * {@link NumericAxis} is more suitable.
 * 
 * @param <M> the data type of the axis
 * @param <V> the variable type of axis
 */
public class CategoryAxis<M, V> extends CartesianAxis<M, V> {

  protected ValueProvider<? super M, V> field;

  /**
   * Returns the {@link ValueProvider} used for labels on the axis.
   * 
   * @return the value provider used for labels on the axis
   */
  public ValueProvider<? super M, V> getField() {
    return field;
  }

  /**
   * Sets the {@link ValueProvider} used for labels on the axis.
   * 
   * @param field the value provider used for labels on the axis
   */
  public void setField(ValueProvider<? super M, V> field) {
    this.field = field;
  }

  @Override
  protected void applyData() {
    from = 0;
    to = chart.getCurrentStore().size();
    steps = (int) (to - 1);
  }

  @Override
  protected void createLabels() {
    labelNames.clear();
    ListStore<M> store = chart.getCurrentStore();
    for (int i = 0; i < store.size(); i++) {
      labelNames.add(field.getValue(store.get(i)));
    }
  }

}
