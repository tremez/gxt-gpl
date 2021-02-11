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
package com.sencha.gxt.chart.client.chart.series;

import com.google.gwt.i18n.client.NumberFormat;
import com.sencha.gxt.chart.client.chart.SeriesRoundNumberProvider;
import com.sencha.gxt.core.client.ValueProvider;
import com.sencha.gxt.data.shared.LabelProvider;
import com.sencha.gxt.widget.core.client.tips.ToolTipConfig;

/**
 * Configuration for {@link Series} tooltip.
 * 
 * @param <M> the data type used by the config
 */
public class SeriesToolTipConfig<M> extends ToolTipConfig {

  private SeriesLabelProvider<M> labelProvider = new SeriesRoundNumberProvider<M>();

  /**
   * Returns the custom label provider.
   * 
   * @return the custom label provider
   */
  public SeriesLabelProvider<M> getLabelProvider() {
    return labelProvider;
  }

  /**
   * Returns the custom label provider.
   * 
   * @param labelProvider the custom label provider
   */
  public void setLabelProvider(SeriesLabelProvider<M> labelProvider) {
    this.labelProvider = labelProvider;
  }

  /**
   * Creates a label provider using the given {@link NumberFormat}.
   * 
   * @param format the number formatter
   */
  public void setNumberFormatProvider(final NumberFormat format) {
    this.labelProvider = new SeriesLabelProvider<M>() {
      @Override
      public String getLabel(M item, ValueProvider<? super M, ? extends Number> valueProvider) {
        return format.format(valueProvider.getValue(item));
      }
    };
  }

  /**
   * Sets a custom {@link ValueProvider} for the tooltip.
   * 
   * @param customValueProvider the custom value provider
   * @param labelProvider the label provider used on the value provider
   */
  public <V> void setValueProvider(final ValueProvider<? super M, V> customValueProvider,
      final LabelProvider<? super V> labelProvider) {
    this.labelProvider = new SeriesLabelProvider<M>() {

      @Override
      public String getLabel(M item, ValueProvider<? super M, ? extends Number> valueProvider) {
        return labelProvider.getLabel(customValueProvider.getValue(item));
      }
    };
  }
}
