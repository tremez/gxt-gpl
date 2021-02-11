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
import com.google.gwt.user.client.Event;
import com.sencha.gxt.widget.core.client.event.RowDoubleClickEvent.RowDoubleClickHandler;

public final class RowDoubleClickEvent extends GridEvent<RowDoubleClickHandler> {

  public interface HasRowDoubleClickHandlers extends HasHandlers {
    HandlerRegistration addRowDoubleClickHandler(RowDoubleClickHandler handler);
  }

  public interface RowDoubleClickHandler extends EventHandler {
    void onRowDoubleClick(RowDoubleClickEvent event);
  }

  private static GwtEvent.Type<RowDoubleClickHandler> TYPE;

  public static GwtEvent.Type<RowDoubleClickHandler> getType() {
    if (TYPE == null) {
      TYPE = new GwtEvent.Type<RowDoubleClickHandler>();
    }
    return TYPE;
  }

  private int rowIndex;
  private int columnIndex = -1;
  private Event event;

  public RowDoubleClickEvent(int rowIndex, int columnIndex, Event event) {
    this.rowIndex = rowIndex;
    this.columnIndex = columnIndex;
    this.event = event;
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public GwtEvent.Type<RowDoubleClickHandler> getAssociatedType() {
    return (GwtEvent.Type) TYPE;
  }

  public int getRowIndex() {
    return rowIndex;
  }
  
  public int getColumnIndex() {
    return columnIndex;
  }

  public Event getEvent() {
    return event;
  }

  @Override
  protected void dispatch(RowDoubleClickHandler handler) {
    handler.onRowDoubleClick(this);
  }
}