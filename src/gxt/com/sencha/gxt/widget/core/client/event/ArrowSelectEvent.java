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
package com.sencha.gxt.widget.core.client.event;

import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.sencha.gxt.widget.core.client.event.ArrowSelectEvent.ArrowSelectHandler;
import com.sencha.gxt.widget.core.client.menu.Menu;

/**
 * Fires after an arrow is selected.
 */
public class ArrowSelectEvent extends GwtEvent<ArrowSelectHandler> {

  /**
   * Handler for {@link ArrowSelectEvent} events.
   */
  public interface ArrowSelectHandler extends EventHandler {

    /**
     * Called after a arrow is selected.
     * 
     * @param event the {@link ArrowSelectEvent} that was fired
     */
    void onArrowSelect(ArrowSelectEvent event);

  }

  /**
   * A widget that implements this interface is a public source of
   * {@link ArrowSelectEvent} events.
   */
  public interface HasArrowSelectHandlers {

    /**
     * Adds a {@link ArrowSelectHandler} handler for {@link ArrowSelectEvent}
     * events.
     * 
     * @param handler the handler
     * @return the registration for the event
     */
    HandlerRegistration addArrowSelectHandler(ArrowSelectHandler handler);
  }

  /**
   * Handler type.
   */
  private static Type<ArrowSelectHandler> TYPE;

  /**
   * Gets the type associated with this event.
   * 
   * @return returns the handler type
   */
  public static Type<ArrowSelectHandler> getType() {
    if (TYPE == null) {
      TYPE = new Type<ArrowSelectHandler>();
    }
    return TYPE;
  }

  private final Menu menu;

  private final Context context;

  public ArrowSelectEvent(Context context, Menu menu) {
    this.context = context;
    this.menu = menu;
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public Type<ArrowSelectHandler> getAssociatedType() {
    return (Type) TYPE;
  }

  public Context getContext() {
    return context;
  }

  public Menu getMenu() {
    return menu;
  }

  @Override
  protected void dispatch(ArrowSelectHandler handler) {
    handler.onArrowSelect(this);
  }

}
