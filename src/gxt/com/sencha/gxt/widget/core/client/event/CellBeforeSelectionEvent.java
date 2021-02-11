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

import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.HasBeforeSelectionHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.sencha.gxt.core.shared.event.CancellableEvent;

/**
 * Fires before a selection occurs.
 * 
 * @param <T> the type about to be selected
 */
public class CellBeforeSelectionEvent<T> extends BeforeSelectionEvent<T> implements CancellableEvent {

  public static <T> CellBeforeSelectionEvent<T> fire(HandlerManager manager, Context context, T item) {
    if (manager.isEventHandled(BeforeSelectionEvent.getType())) {
      CellBeforeSelectionEvent<T> event = new CellBeforeSelectionEvent<T>(context, item);
      event.setItem(item);
      if (manager != null) {
        manager.fireEvent(event);
      }
      return event;
    }
    return null;
  }
  
  public static <T> CellBeforeSelectionEvent<T> fire(HasBeforeSelectionHandlers<T> handler, Context context, T item) {
    CellBeforeSelectionEvent<T> event = new CellBeforeSelectionEvent<T>(context, item);
    handler.fireEvent(event);
    return event;
  }

  private Context context;

  /**
   * Creates a new before selection event.
   * 
   * @param context the cell context
   * @param item the item to be selected
   */
  protected CellBeforeSelectionEvent(Context context, T item) {
    this.context = context;
    this.setItem(item);
  }

  /**
   * Returns the cell context if event fired from cell
   * 
   * @return the cell context if event fired via cell or null
   */
  public Context getContext() {
    return context;
  }

  @Override
  public boolean isCancelled() {
    return isCanceled();
  }

  @Override
  public void setCancelled(boolean cancel) {
    if (cancel) {
      cancel();
    }
  }

}
