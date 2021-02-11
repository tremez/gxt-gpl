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
import com.sencha.gxt.widget.core.client.SplitBar;

/**
 * Represents the after layout event.
 */
public class SplitBarDragEvent extends GwtEvent<SplitBarDragEvent.SplitBarDragHandler> {

  /**
   * Implemented by objects that handle {@link SplitBarDragEvent}.
   */
  public interface SplitBarDragHandler extends EventHandler {
    void onDragEvent(SplitBarDragEvent event);
  }
  
  /**
   * SplitBarDragStartHandler type.
   */
  private static Type<SplitBarDragEvent.SplitBarDragHandler> TYPE;

  /**
   * Gets the type associated with this event.
   * 
   * @return returns the handler type
   */
  public static Type<SplitBarDragEvent.SplitBarDragHandler> getType() {
    if (TYPE == null) {
      TYPE = new Type<SplitBarDragEvent.SplitBarDragHandler>();
    }
    return TYPE;
  }
  
  /**
   * A widget that implements this interface is a public source of
   * {@link SplitBarDragEvent} events.
   */
  public interface HasSplitBarDragHandlers {

    /**
     * Adds a {@link SplitBarDragHandler} handler for
     * {@link SplitBarDragEvent} events.
     * 
     * @param handler the handler
     * @return the registration for the event
     */
    HandlerRegistration addSplitBarDragHandler(SplitBarDragHandler handler);
  }

  private int size;
  private boolean start;

  public SplitBarDragEvent(boolean start, int size) {
    this.start = start;
    this.size = size;
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public Type<SplitBarDragEvent.SplitBarDragHandler> getAssociatedType() {
    return (Type) TYPE;
  }
  
  public boolean isStart() {
    return start;
  }

  public int getSize() {
    return size;
  }

  public SplitBar getSource() {
    return (SplitBar) super.getSource();
  }

  @Override
  protected void dispatch(SplitBarDragEvent.SplitBarDragHandler handler) {
    handler.onDragEvent(this);
  }

}
