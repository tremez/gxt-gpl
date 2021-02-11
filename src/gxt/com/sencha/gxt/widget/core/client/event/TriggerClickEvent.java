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
import com.sencha.gxt.widget.core.client.event.TriggerClickEvent.TriggerClickHandler;

/**
 * Fires after the trigger is clicked.
 */
public class TriggerClickEvent extends GwtEvent<TriggerClickHandler> {

  /**
   * Handler class for {@link TriggerClickEvent} events.
   */
  public interface TriggerClickHandler extends EventHandler {

    /**
     * Called after the source is expanded.
     */
    void onTriggerClick(TriggerClickEvent event);
  }

  /**
   * A widget that implements this interface is a public source of
   * {@link TriggerClickEvent} events.
   */
  public interface HasTriggerClickHandlers {

    /**
     * Adds a {@link TriggerClickHandler} handler for {@link TriggerClickEvent} events.
     * 
     * @param handler the handler
     * @return the registration for the event
     */
    HandlerRegistration addTriggerClickHandler(TriggerClickHandler handler);
  }

  /**
   * Handler type.
   */
  private static Type<TriggerClickHandler> TYPE;

  /**
   * Gets the type associated with this event.
   * 
   * @return returns the handler type
   */
  public static Type<TriggerClickHandler> getType() {
    return TYPE != null ? TYPE : (TYPE = new Type<TriggerClickHandler>());
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public Type<TriggerClickHandler> getAssociatedType() {
    return (Type) TYPE;
  }

  @Override
  protected void dispatch(TriggerClickHandler handler) {
    handler.onTriggerClick(this);
  }

}
