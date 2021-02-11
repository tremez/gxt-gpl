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
import com.sencha.gxt.widget.core.client.container.HasLayout;
import com.sencha.gxt.widget.core.client.event.BeforeLayoutEvent.BeforeLayoutHandler;

/**
 * Fires before a layout is executed.
 */
public class BeforeLayoutEvent extends GwtEvent<BeforeLayoutHandler> implements CancellableEvent {

  /**
   * Handler type.
   */
  private static Type<BeforeLayoutHandler> TYPE;

  /**
   * Gets the type associated with this event.
   * 
   * @return returns the handler type
   */
  public static Type<BeforeLayoutHandler> getType() {
    if (TYPE == null) {
      TYPE = new Type<BeforeLayoutHandler>();
    }
    return TYPE;
  }

  private boolean cancelled;

  public BeforeLayoutEvent() {
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public Type<BeforeLayoutHandler> getAssociatedType() {
    return (Type) TYPE;
  }

  public HasLayout getSource() {
    return (HasLayout) super.getSource();
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
  protected void dispatch(BeforeLayoutHandler handler) {
    handler.onBeforeLayout(this);
  }
  
  /**
   * Handler for {@link BeforeLayoutEvent} events.
   */
  public interface BeforeLayoutHandler extends EventHandler {

    /**
     * Called before a container's layout is executed. The action can be cancelled
     * using {@link BeforeShowContextMenuEvent#setCancelled(boolean)}.
     * 
     * @param event the {@link BeforeLayoutEvent} that was fired
     */
    void onBeforeLayout(BeforeLayoutEvent event);

  }
  
  /**
   * A widget that implements this interface is a public source of
   * {@link BeforeLayoutEvent} events.
   */
  public interface HasBeforeLayoutHandlers {

    /**
     * Adds a {@link BeforeLayoutEvent} handler.
     * 
     * @param handler the handler
     * @return the registration for the event
     */
    public HandlerRegistration addBeforeLayoutHandler(BeforeLayoutHandler handler);

  }

}
