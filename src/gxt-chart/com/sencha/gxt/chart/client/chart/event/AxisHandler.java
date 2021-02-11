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
import com.sencha.gxt.chart.client.chart.event.AxisItemOutEvent.AxisItemOutHandler;
import com.sencha.gxt.chart.client.chart.event.AxisItemOverEvent.AxisItemOverHandler;
import com.sencha.gxt.chart.client.chart.event.AxisItemUpEvent.AxisItemUpHandler;
import com.sencha.gxt.chart.client.chart.event.AxisSelectionEvent.AxisSelectionHandler;

/**
 * Aggregating handler interface for:
 * 
 * <dl>
 * <dd>{@link AxisSelectionEvent}</b></dd>
 * <dd>{@link AxisItemOutEvent}</b></dd>
 * <dd>{@link AxisItemOverEvent}</b></dd>
 * <dd>{@link AxisItemUpEvent}</b></dd>
 * </dl>
 */
public interface AxisHandler extends AxisSelectionHandler, AxisItemOutHandler, AxisItemOverHandler, AxisItemUpHandler {

  /**
   * A widget that implements this interface is a public source of
   * {@link AxisSelectionEvent}, {@link AxisItemOutEvent},
   * {@link AxisItemOverEvent} and {@link AxisItemUpEvent} events.
   */
  public interface HasAxisHandlers {

    /**
     * Adds a {@link AxisHandler} handler for {@link AxisSelectionEvent},
     * {@link AxisItemOutEvent}, {@link AxisItemOverEvent} and
     * {@link AxisItemUpEvent} events.
     * 
     * @param handler the handler
     * @return the registration for the event
     */
    public HandlerRegistration addAxisHandler(AxisHandler handler);

  }
}
