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
import com.google.gwt.event.shared.HasHandlers;
import com.sencha.gxt.core.shared.event.CancellableEvent;
import com.sencha.gxt.widget.core.client.event.HeaderContextMenuEvent.HeaderContextMenuHandler;
import com.sencha.gxt.widget.core.client.menu.Menu;

public final class HeaderContextMenuEvent extends GridEvent<HeaderContextMenuHandler> implements CancellableEvent {

  public interface HasHeaderContextMenuHandlers extends HasHandlers {
    HandlerRegistration addHeaderContextMenuHandler(HeaderContextMenuHandler handler);
  }

  public interface HeaderContextMenuHandler extends EventHandler {
    void onHeaderContextMenu(HeaderContextMenuEvent event);
  }

  private static GwtEvent.Type<HeaderContextMenuHandler> TYPE;

  public static GwtEvent.Type<HeaderContextMenuHandler> getType() {
    if (TYPE == null) {
      TYPE = new GwtEvent.Type<HeaderContextMenuHandler>();
    }
    return TYPE;
  }

  private int columnIndex;
  private Menu menu;
  private boolean cancelled;

  public HeaderContextMenuEvent(int columnIndex, Menu menu) {
    this.columnIndex = columnIndex;
    this.menu = menu;
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public GwtEvent.Type<HeaderContextMenuHandler> getAssociatedType() {
    return (GwtEvent.Type) TYPE;
  }

  public int getColumnIndex() {
    return columnIndex;
  }

  public Menu getMenu() {
    return menu;
  }

  public boolean isCancelled() {
    return cancelled;
  }

  public void setCancelled(boolean cancelled) {
    this.cancelled = cancelled;
  }

  @Override
  protected void dispatch(HeaderContextMenuHandler handler) {
    handler.onHeaderContextMenu(this);
  }
}