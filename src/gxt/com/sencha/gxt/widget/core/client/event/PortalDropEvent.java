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
import com.sencha.gxt.widget.core.client.Portlet;
import com.sencha.gxt.widget.core.client.container.PortalLayoutContainer;
import com.sencha.gxt.widget.core.client.event.PortalDropEvent.PortalDropHandler;

/**
 * Fires after a portlet is dropped.
 */
public class PortalDropEvent extends GwtEvent<PortalDropHandler> {

  /**
   * Handler type.
   */
  private static Type<PortalDropHandler> TYPE;

  /**
   * Gets the type associated with this event.
   * 
   * @return returns the handler type
   */
  public static Type<PortalDropHandler> getType() {
    return TYPE != null ? TYPE : (TYPE = new Type<PortalDropHandler>());
  }

  private Portlet portlet;
  private int startColumn, startRow;
  private int column, row;

  public PortalDropEvent(Portlet portlet, int startColumn, int startRow, int column, int row) {
    this.portlet = portlet;
    this.startColumn = startColumn;
    this.startRow = startRow;
    this.column = column;
    this.row = row;
  }
  
  public int getStartColumn() {
    return startColumn;
  }

  public int getStartRow() {
    return startRow;
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public Type<PortalDropHandler> getAssociatedType() {
    return (Type) TYPE;
  }

  public int getColumn() {
    return column;
  }

  public Portlet getPortlet() {
    return portlet;
  }

  public int getRow() {
    return row;
  }

  @Override
  public PortalLayoutContainer getSource() {
    return (PortalLayoutContainer) super.getSource();
  }

  @Override
  protected void dispatch(PortalDropHandler handler) {
    handler.onDrop(this);
  }
  
  /**
   * Handler class for {@link PortalDropEvent} events.
   */
  public interface PortalDropHandler extends EventHandler {

    /**
     * Called after a portlet is dropped.
     */
    void onDrop(PortalDropEvent event);
  }
  
  /**
   * A widget that implements this interface is a public source of
   * {@link PortalDropEvent} events.
   */
  public interface HasPortalDropHandlers {

    /**
     * Adds a {@link PortalDropHandler} handler for {@link PortalDropEvent} events.
     * 
     * @param handler the handler
     * @return the registration for the event
     */
    HandlerRegistration addDropHandler(PortalDropHandler handler);
  }

}
