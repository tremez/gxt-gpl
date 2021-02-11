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
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

/**
 * Fires after a item is selected.
 */
public class SelectEvent extends GwtEvent<SelectHandler> {

  /**
   * Handler type.
   */
  private static Type<SelectHandler> TYPE;

  /**
   * Gets the type associated with this event.
   * 
   * @return returns the handler type
   */
  public static Type<SelectHandler> getType() {
    if (TYPE == null) {
      TYPE = new Type<SelectHandler>();
    }
    return TYPE;
  }

  private Context context;

  public SelectEvent() {

  }

  public SelectEvent(Context context) {
    this.context = context;
  }

  /**
   * Returns the cell context.
   * 
   * @return the cell context or null if event not cell based
   */
  public Context getContext() {
    return context;
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public Type<SelectHandler> getAssociatedType() {
    return (Type) TYPE;
  }

  @Override
  protected void dispatch(SelectHandler handler) {
    handler.onSelect(this);
  }
  
  /**
   * Handler for {@link SelectEvent} events.
   */
  public interface SelectHandler extends EventHandler {

    /**
     * Called after a widget is selected.
     * 
     * @param event the {@link SelectEvent} that was fired
     */
    void onSelect(SelectEvent event);

  }
  
  /**
   * A widget that implements this interface is a public source of
   * {@link SelectEvent} events.
   */
  public interface HasSelectHandlers {

    /**
     * Adds a {@link SelectHandler} handler for {@link SelectEvent} events.
     * 
     * @param handler the handler
     * @return the registration for the event
     */
    HandlerRegistration addSelectHandler(SelectHandler handler);
  }

}
