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
import com.sencha.gxt.widget.core.client.event.LiveGridViewUpdateEvent.LiveGridViewUpdateHandler;

/**
 * Fires after the rows in the live grid have been updated.
 */
public final class LiveGridViewUpdateEvent extends GwtEvent<LiveGridViewUpdateHandler> {
  
  public interface LiveGridViewUpdateHandler extends EventHandler {
    void onUpdate(LiveGridViewUpdateEvent event);
  }
  
  /**
   * A widget that implements this interface is a public source of
   * {@link LiveGridViewUpdateEvent} events.
   */
  public interface HasLiveGridViewUpdateHandlers {

    /**
     * Adds a {@link LiveGridViewUpdateHandler} handler for {@link LiveGridViewUpdateEvent} events.
     * 
     * @param handler the handler
     * @return the registration for the event
     */
    public HandlerRegistration addLiveGridViewUpdateHandler(LiveGridViewUpdateHandler handler);

  }

  private static GwtEvent.Type<LiveGridViewUpdateHandler> TYPE;

  public static GwtEvent.Type<LiveGridViewUpdateHandler> getType() {
    if (TYPE == null) {
      TYPE = new GwtEvent.Type<LiveGridViewUpdateHandler>();
    }
    return TYPE;
  }

  private final int liveStoreOffset;
  private final int rowCount;
  private final int totalCount;
  private final int viewIndex;

  public LiveGridViewUpdateEvent(int liveStoreOffset, int viewIndex, int totalCount, int rowCount) {
    this.liveStoreOffset = liveStoreOffset;
    this.viewIndex = viewIndex;
    this.totalCount = totalCount;
    this.rowCount = rowCount;
  }

  @Override
  public GwtEvent.Type<LiveGridViewUpdateHandler> getAssociatedType() {
    return TYPE;
  }

  /**
   * Returns the offset.
   * 
   * @return the offset
   */
  public int getLiveStoreOffset() {
    return liveStoreOffset;
  }

  /**
   * Returns the row count.
   * 
   * @return the row count
   */
  public int getRowCount() {
    return rowCount;
  }

  /**
   * Returns the total count.
   * 
   * @return the total count
   */
  public int getTotalCount() {
    return totalCount;
  }

  /**
   * Returns the view index.
   * 
   * @return the view index
   */
  public int getViewIndex() {
    return viewIndex;
  }

  @Override
  protected void dispatch(LiveGridViewUpdateHandler handler) {
    handler.onUpdate(this);

  }
}
