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
import com.sencha.gxt.core.shared.event.CancellableEvent;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.event.BeforeExpandItemEvent.BeforeExpandItemHandler;

/**
 * Event type for widgets that can be expanded.
 * 
 * @param <T> the type about to be expanded
 */
public class BeforeExpandItemEvent<T> extends GwtEvent<BeforeExpandItemHandler<T>> implements CancellableEvent {

  /**
   * Handler class for {@link BeforeCollapseEvent} events.
   */
  public interface BeforeExpandItemHandler<T> extends EventHandler {

    /**
     * Called before a content panel is expanded. Listeners can cancel the action
     * by calling {@link BeforeExpandItemEvent#setCancelled(boolean)}.
     */
    void onBeforeExpand(BeforeExpandItemEvent<T> event);
  }
  
  /**
   * A widget that implements this interface is a public source of
   * {@link BeforeExpandItemEvent} events.
   */
  public interface HasBeforeExpandItemHandlers<T> {

    /**
     * Adds a {@link BeforeExpandItemHandler} handler for {@link BeforeExpandItemEvent}
     * events.
     * 
     * @param handler the handler
     * @return the registration for the event
     */
    HandlerRegistration addBeforeExpandHandler(BeforeExpandItemHandler<T> handler);
  }
  
  /**
   * Handler type.
   */
  private static Type<BeforeExpandItemHandler<?>> TYPE;

  /**
   * Gets the type associated with this event.
   * 
   * @return returns the handler type
   */
  public static Type<BeforeExpandItemHandler<?>> getType() {
    return TYPE != null ? TYPE : (TYPE = new Type<BeforeExpandItemHandler<?>>());
  }

  private boolean cancelled;
  private T item;
  
  public BeforeExpandItemEvent(T item) {
    this.item = item;
  }
  
  public T getItem() {
    return item;
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public Type<BeforeExpandItemHandler<T>> getAssociatedType() {
    return (Type) TYPE;
  }

  @Override
  public Component getSource() {
    return (Component) super.getSource();
  }

  @Override
  public boolean isCancelled() {
    return cancelled;
  }

  @Override
  public void setCancelled(boolean cancelled) {
    this.cancelled = cancelled;
  }

  @Override
  protected void dispatch(BeforeExpandItemHandler<T> handler) {
    handler.onBeforeExpand(this);
  }

}
