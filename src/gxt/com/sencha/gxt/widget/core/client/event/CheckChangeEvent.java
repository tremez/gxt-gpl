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
import com.sencha.gxt.widget.core.client.event.CheckChangeEvent.CheckChangeHandler;
import com.sencha.gxt.widget.core.client.tree.Tree.CheckState;

/**
 * Fires after an objects check state is changed.
 */
public class CheckChangeEvent<T> extends GwtEvent<CheckChangeHandler<T>> {

  /**
   * Handler type.
   */
  private static Type<CheckChangeHandler<?>> TYPE;

  /**
   * Gets the type associated with this event.
   * 
   * @return returns the handler type
   */
  public static Type<CheckChangeHandler<?>> getType() {
    if (TYPE == null) {
      TYPE = new Type<CheckChangeHandler<?>>();
    }
    return TYPE;
  }

  private CheckState state;
  private T item;

  public CheckChangeEvent(T item, CheckState state) {
    this.item = item;
    this.state = state;
  }

  public T getItem() {
    return item;
  }

  /**
   * Returns the item's check state.
   * 
   * @return the check state
   */
  public CheckState getChecked() {
    return state;
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public Type<CheckChangeHandler<T>> getAssociatedType() {
    return (Type) TYPE;
  }

  public Component getSource() {
    return (Component) super.getSource();
  }

  @Override
  protected void dispatch(CheckChangeHandler<T> handler) {
    handler.onCheckChange(this);
  }

  /**
   * Handler class for {@link CheckChangeEvent} events.
   */
  public interface CheckChangeHandler<T> extends EventHandler {

    /**
     * Called after an item's check state changes.
     */
    void onCheckChange(CheckChangeEvent<T> event);
  }

  /**
   * A widget that implements this interface is a public source of
   * {@link CheckChangeEvent} events.
   */
  public interface HasCheckChangeHandlers<T> {

    /**
     * Adds a {@link CheckChangeHandler} handler for {@link CheckChangeEvent}
     * events.
     * 
     * @param handler the handler
     * @return the registration for the event
     */
    public HandlerRegistration addCheckChangeHandler(CheckChangeHandler<T> handler);

  }

}
