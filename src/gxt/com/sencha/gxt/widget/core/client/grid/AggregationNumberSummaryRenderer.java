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

import com.google.gwt.i18n.client.NumberFormat;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.sencha.gxt.core.client.ValueProvider;

public class AggregationNumberSummaryRenderer<M, N> extends AggregationNumberFormatRenderer<M> {
  private final SummaryType<N, ? extends Number> summaryType;

  public AggregationNumberSummaryRenderer(NumberFormat format, SummaryType<N, ? extends Number> summaryType) {
    super(format);
    this.summaryType = summaryType;
  }

  public AggregationNumberSummaryRenderer(SummaryType<N, ? extends Number> summaryType) {
    super();
    this.summaryType = summaryType;
  }

  public SummaryType<N, ? extends Number> getSummaryType() {
    return summaryType;
  }

  @Override
  public SafeHtml render(int colIndex, Grid<M> grid) {
    ValueProvider<? super M, N> v = grid.getColumnModel().getValueProvider(colIndex);
    Number n = summaryType.calculate(grid.getStore().getAll(), v);
    return SafeHtmlUtils.fromString(getFormat().format(n));
  }

}
