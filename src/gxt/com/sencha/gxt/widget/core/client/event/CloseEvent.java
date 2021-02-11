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

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.event.CloseEvent.CloseHandler;

/**
 * Fires after a item is closed.
 */
public class CloseEvent<T> extends GwtEvent<CloseHandler<T>> {

  /**
   * Handler type.
   */
  private static Type<CloseHandler<?>> TYPE;

  /**
   * Gets the type associated with this event.
   * 
   * @return returns the handler type
   */
  public static Type<CloseHandler<?>> getType() {
    if (TYPE == null) {
      TYPE = new Type<CloseHandler<?>>();
    }
    return TYPE;
  }
  
  private T item;
  
  public CloseEvent(T item) {
    this.item = item;
  }
  
  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public Type<CloseHandler<T>> getAssociatedType() {
    return (Type) TYPE;
  }

  public T getItem() {
    return item;
  }

  public Component getSource() {
    return (Component) super.getSource();
  }

  @Override
  protected void dispatch(CloseHandler<T> handler) {
    handler.onClose(this);
  }
  
  /**
   * Handler class for {@link CloseEvent} events.
   * 
   * @param <T> the type being closed
   */
  public interface CloseHandler<T> extends EventHandler {

    /**
     * Called after tab item is closed.
     */
    void onClose(CloseEvent<T> event);
  }
  
  /**
   * A widget that implements this interface is a public source of
   * {@link CloseEvent} events.
   */
  public interface HasCloseHandlers<T> {

    /**
     * Adds a {@link CloseHandler} handler for {@link CloseEvent} events.
     * 
     * @param handler the handler
     * @return the registration for the event
     */
    HandlerRegistration addCloseHandler(CloseHandler<T> handler);
  }

}
