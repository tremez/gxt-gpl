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
import com.sencha.gxt.widget.core.client.event.ShowContextMenuEvent.ShowContextMenuHandler;
import com.sencha.gxt.widget.core.client.menu.Menu;

/**
 * Fires after a widget's context menu is shown.
 */
public class ShowContextMenuEvent extends GwtEvent<ShowContextMenuHandler> implements CancellableEvent {

  /**
   * Handler type.
   */
  private static Type<ShowContextMenuHandler> TYPE;

  /**
   * Gets the type associated with this event.
   * 
   * @return returns the handler type
   */
  public static Type<ShowContextMenuHandler> getType() {
    if (TYPE == null) {
      TYPE = new Type<ShowContextMenuHandler>();
    }
    return TYPE;
  }

  private Menu menu;
  private boolean cancelled;

  public ShowContextMenuEvent(Menu menu) {
    this.menu = menu;
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public Type<ShowContextMenuHandler> getAssociatedType() {
    return (Type) TYPE;
  }

  /**
   * Returns the context menu.
   * 
   * @return the context menu
   */
  public Menu getMenu() {
    return menu;
  }

  @Override
  public Component getSource() {
    return (Component)super.getSource();
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
  protected void dispatch(ShowContextMenuHandler handler) {
    handler.onShowContextMenu(this);
  }
  
  /**
   * Handler for {@link ShowContextMenuEvent} events.
   */
  public interface ShowContextMenuHandler extends EventHandler {

    /**
     * Called after a widget's context menu is shown.
     * 
     * @param event the {@link ShowContextMenuEvent} that was fired
     */
    public void onShowContextMenu(ShowContextMenuEvent event);

  }
  
  /**
   * A widget that implements this interface is a public source of
   * {@link ShowContextMenuEvent} events.
   */
  public interface HasShowContextMenuHandler {

    /**
     * Adds a {@link ShowContextMenuHandler} handler for
     * {@link ShowContextMenuEvent} events.
     * 
     * @param handler the handler
     * @return the registration for the event
     */
    public HandlerRegistration addShowContextMenuHandler(ShowContextMenuHandler handler);

  }

}
