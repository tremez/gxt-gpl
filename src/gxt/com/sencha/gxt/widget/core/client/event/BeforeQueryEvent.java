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
import com.sencha.gxt.widget.core.client.event.BeforeQueryEvent.BeforeQueryHandler;
import com.sencha.gxt.widget.core.client.form.ComboBox;

/**
 * Fires before a query is executed.
 * 
 * @param <T> the type about to be closed
 */
public class BeforeQueryEvent<T> extends GwtEvent<BeforeQueryHandler<T>> implements CancellableEvent {

  /**
   * Handler class for {@link BeforeQueryEvent} events.
   */
  public interface BeforeQueryHandler<T> extends EventHandler {

    /**
     * Called before query is executed. Listeners can cancel the action by
     * calling {@link BeforeQueryEvent#setCancelled(boolean)}.
     */
    void onBeforeQuery(BeforeQueryEvent<T> event);
  }

  /**
   * A widget that implements this interface is a public source of
   * {@link BeforeQueryEvent} events.
   */
  public interface HasBeforeQueryHandlers<T> {

    /**
     * Adds a {@link BeforeQueryHandler} handler for {@link BeforeQueryEvent}
     * events.
     * 
     * @param handler the handler
     * @return the registration for the event
     */
    HandlerRegistration addBeforeQueryHandler(BeforeQueryHandler<T> handler);
  }

  /**
   * Handler type.
   */
  private static Type<BeforeQueryHandler<?>> TYPE;

  /**
   * Gets the type associated with this event.
   * 
   * @return returns the handler type
   */
  public static Type<BeforeQueryHandler<?>> getType() {
    if (TYPE == null) {
      TYPE = new Type<BeforeQueryHandler<?>>();
    }
    return TYPE;
  }

  private String query;
  private boolean cancelled;

  public BeforeQueryEvent(String query) {
    this.query = query;
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public Type<BeforeQueryHandler<T>> getAssociatedType() {
    return (Type) TYPE;
  }

  public String getQuery() {
    return query;
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  public ComboBox<T> getSource() {
    return (ComboBox) super.getSource();
  }

  @Override
  public boolean isCancelled() {
    return cancelled;
  }

  @Override
  public void setCancelled(boolean cancelled) {
    this.cancelled = cancelled;
  }

  public void setQuery(String query) {
    this.query = query;
  }

  @Override
  protected void dispatch(BeforeQueryHandler<T> handler) {
    handler.onBeforeQuery(this);
  }

}
