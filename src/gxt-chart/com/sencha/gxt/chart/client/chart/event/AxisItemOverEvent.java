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

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.Event;
import com.sencha.gxt.chart.client.chart.axis.Axis;
import com.sencha.gxt.chart.client.chart.event.AxisItemOverEvent.AxisItemOverHandler;

/**
 * Fired when an item in the {@link Axis} is moused over.
 */
public class AxisItemOverEvent extends GwtEvent<AxisItemOverHandler> {

  /**
   * Handler class for {@link AxisItemOverEvent} events.
   */
  public interface AxisItemOverHandler extends EventHandler {

    /**
     * Fired when an item in the {@link Axis} is moused over.
     * 
     * @param event the fired event
     */
    void onAxisOverItem(AxisItemOverEvent event);
  }

  /**
   * A widget that implements this interface is a public source of
   * {@link AxisItemOverEvent} events.
   */
  public interface HasAxisItemOverHandlers {

    /**
     * Adds a {@link AxisItemOverHandler} handler for {@link AxisItemOverEvent}
     * events.
     * 
     * @param handler the handler
     * @return the registration for the event
     */
    HandlerRegistration addAxisItemOverHandler(AxisItemOverHandler handler);
  }

  /**
   * Handler type.
   */
  private static Type<AxisItemOverHandler> TYPE;

  /**
   * Gets the type associated with this event.
   * 
   * @return returns the handler type
   */
  public static Type<AxisItemOverHandler> getType() {
    if (TYPE == null) {
      TYPE = new Type<AxisItemOverHandler>();
    }
    return TYPE;
  }

  private final String value;
  private final int index;
  private final Event event;

  /**
   * Creates a new event with the given value and index.
   * 
   * @param value the value of the axis item
   * @param index the index of the axis item
   */
  public AxisItemOverEvent(String value, int index, Event event) {
    this.value = value;
    this.index = index;
    this.event = event;
  }

  @Override
  public Type<AxisItemOverHandler> getAssociatedType() {
    return getType();
  }

  /**
   * Returns the browser event that initiated the selection event.
   * 
   * @return the browser event that initiated the selection event
   */
  public Event getBrowserEvent() {
    return event;
  }

  /**
   * Returns the index of the axis item.
   * 
   * @return the index of the axis item
   */
  public int getIndex() {
    return index;
  }

  /**
   * Returns the value of the axis item.
   * 
   * @return the value of the axis item
   */
  public String getValue() {
    return value;
  }

  @Override
  protected void dispatch(AxisItemOverHandler handler) {
    handler.onAxisOverItem(this);
  }

}
