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
package com.sencha.gxt.widget.core.client.selection;

import java.util.Collections;
import java.util.List;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.sencha.gxt.widget.core.client.selection.SelectionChangedEvent.SelectionChangedHandler;

/**
 * Fires after the selection changes.
 */
public class SelectionChangedEvent<M> extends GwtEvent<SelectionChangedHandler<M>> {

  /**
   * A widget that implements this interface is a public source of
   * {@link SelectionChangedEvent} events.
   * 
   * @param <M> the model type
   */
  public interface HasSelectionChangedHandlers<M> {

    /**
     * Adds a {@link SelectionChangedHandler} handler for
     * {@link SelectionChangedEvent} events.
     * 
     * @param handler the handler
     * @return the registration for the event
     */
    HandlerRegistration addSelectionChangedHandler(SelectionChangedHandler<M> handler);
  }

  /**
   * Handler class for {@link SelectionChangedEvent} events.
   */
  public interface SelectionChangedHandler<M> extends EventHandler {

    /**
     * Called after a widget's selections are changed. 
     */
    void onSelectionChanged(SelectionChangedEvent<M> event);
  }

  /**
   * Handler type.
   */
  private static Type<SelectionChangedHandler<?>> TYPE;

  /**
   * Gets the type associated with this event.
   * 
   * @return returns the handler type
   */
  public static Type<SelectionChangedHandler<?>> getType() {
    if (TYPE == null) {
      TYPE = new Type<SelectionChangedHandler<?>>();
    }
    return TYPE;
  }

  private List<M> selection;

  public SelectionChangedEvent(List<M> selection) {
    this.selection = Collections.unmodifiableList(selection);
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public Type<SelectionChangedHandler<M>> getAssociatedType() {
    return (Type) TYPE;
  }

  /**
   * Returns the selection.
   * 
   * @return the selection
   */
  public List<M> getSelection() {
    return selection;
  }
  
  @Override
  @SuppressWarnings("unchecked")
  public StoreSelectionModel<M> getSource() {
    return (StoreSelectionModel<M>) super.getSource();
  }
  
  @Override
  protected void dispatch(SelectionChangedHandler<M> handler) {
    handler.onSelectionChanged(this);
  }

}
