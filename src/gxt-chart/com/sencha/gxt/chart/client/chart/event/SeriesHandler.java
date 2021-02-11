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
package com.sencha.gxt.chart.client.chart.event;

import com.google.gwt.event.shared.HandlerRegistration;
import com.sencha.gxt.chart.client.chart.event.SeriesItemOutEvent.HasSeriesItemOutHandlers;
import com.sencha.gxt.chart.client.chart.event.SeriesItemOutEvent.SeriesItemOutHandler;
import com.sencha.gxt.chart.client.chart.event.SeriesItemOverEvent.HasSeriesItemOverHandlers;
import com.sencha.gxt.chart.client.chart.event.SeriesItemOverEvent.SeriesItemOverHandler;
import com.sencha.gxt.chart.client.chart.event.SeriesItemUpEvent.HasSeriesItemUpHandlers;
import com.sencha.gxt.chart.client.chart.event.SeriesItemUpEvent.SeriesItemUpHandler;
import com.sencha.gxt.chart.client.chart.event.SeriesSelectionEvent.HasSeriesSelectionHandlers;
import com.sencha.gxt.chart.client.chart.event.SeriesSelectionEvent.SeriesSelectionHandler;

/**
 * Aggregating handler interface for:
 * 
 * <dl>
 * <dd>{@link SeriesSelectionEvent}</b></dd>
 * <dd>{@link SeriesItemOutEvent}</b></dd>
 * <dd>{@link SeriesItemOverEvent}</b></dd>
 * <dd>{@link SeriesItemUpEvent}</b></dd>
 * </dl>
 */
public interface SeriesHandler<M> extends SeriesSelectionHandler<M>, SeriesItemOutHandler<M>, SeriesItemOverHandler<M>,
    SeriesItemUpHandler<M> {

  /**
   * A widget that implements this interface is a public source of
   * {@link SeriesSelectionEvent}, {@link SeriesItemOutEvent},
   * {@link SeriesItemOverEvent} and {@link SeriesItemUpEvent} events.
   */
  public interface HasSeriesHandlers<M> extends HasSeriesSelectionHandlers<M>, HasSeriesItemOutHandlers<M>,
      HasSeriesItemOverHandlers<M>, HasSeriesItemUpHandlers<M> {

    /**
     * Adds a {@link SeriesHandler} handler for {@link SeriesSelectionEvent},
     * {@link SeriesItemOutEvent}, {@link SeriesItemOverEvent} and
     * {@link SeriesItemUpEvent} events.
     * 
     * @param handler the handler
     * @return the registration for the event
     */
    public HandlerRegistration addSeriesHandler(SeriesHandler<M> handler);

  }
}
