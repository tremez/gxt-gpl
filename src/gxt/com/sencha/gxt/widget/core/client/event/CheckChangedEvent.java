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

import java.util.Collections;
import java.util.List;

import com.google.gwt.event.shared.EventHandler;
import com.google.gwt.event.shared.GwtEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.event.CheckChangedEvent.CheckChangedHandler;

/**
 * Fires after an objects check state is changed.
 */
public class CheckChangedEvent<T> extends GwtEvent<CheckChangedHandler<T>> {

  /**
   * Handler type.
   */
  private static Type<CheckChangedHandler<?>> TYPE;

  /**
   * Gets the type associated with this event.
   * 
   * @return returns the handler type
   */
  public static Type<CheckChangedHandler<?>> getType() {
    if (TYPE == null) {
      TYPE = new Type<CheckChangedHandler<?>>();
    }
    return TYPE;
  }

  private List<T> checked;

  public CheckChangedEvent(List<T> checked) {
    this.checked = Collections.unmodifiableList(checked);
  }

  public List<T> getItems() {
    return checked;
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public Type<CheckChangedHandler<T>> getAssociatedType() {
    return (Type) TYPE;
  }

  public Component getSource() {
    return (Component) super.getSource();
  }

  @Override
  protected void dispatch(CheckChangedHandler<T> handler) {
    handler.onCheckChanged(this);
  }
  
  /**
   * Handler class for {@link CheckChangeEvent} events.
   */
  public interface CheckChangedHandler<T> extends EventHandler {

    /**
     * Called when the checked selection changes.
     */
    void onCheckChanged(CheckChangedEvent<T> event);
  }
  
  /**
   * A widget that implements this interface is a public source of
   * {@link CheckChangedEvent} events.
   */
  public interface HasCheckChangedHandlers<T> {

    /**
     * Adds a {@link CheckChangedHandler} handler for {@link CheckChangedEvent}
     * events.
     * 
     * @param handler the handler
     * @return the registration for the event
     */
    HandlerRegistration addCheckChangedHandler(CheckChangedHandler<T> handler);
  }

}
