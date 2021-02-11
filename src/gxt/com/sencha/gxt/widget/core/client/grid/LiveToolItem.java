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
package com.sencha.gxt.widget.core.client.grid;

import com.google.gwt.event.shared.HandlerRegistration;
import com.sencha.gxt.core.client.util.Util;
import com.sencha.gxt.messages.client.DefaultMessages;
import com.sencha.gxt.widget.core.client.event.LiveGridViewUpdateEvent;
import com.sencha.gxt.widget.core.client.event.LiveGridViewUpdateEvent.LiveGridViewUpdateHandler;
import com.sencha.gxt.widget.core.client.toolbar.LabelToolItem;

/**
 * A specialized tool item for <code>>LiveGridView</code> that shows the current
 * location and total records.
 * 
 * @see LiveGridView
 */
public class LiveToolItem extends LabelToolItem {
  
  /**
   * LiveToolItem messages.
   */
  public interface LiveToolItemMessages {

    String displayMessage(int start, int end, int total);
  }

  protected static class DefaultLiveToolItemMessages implements LiveToolItemMessages {

    @Override
    public String displayMessage(int start, int end, int total) {
      return DefaultMessages.getMessages().pagingToolBar_displayMsg(start, end, total);
    }

  }

  private HandlerRegistration handlerRegistration;
  private LiveToolItemMessages messages;

  public LiveToolItem(Grid<?> grid) {
    bindGrid(grid);
    
    setLabel(Util.NBSP_SAFE_HTML);
  }

  /**
   * Binds the tool item to the specified grid, must be called.
   * 
   * @param grid the grid or null
   */
  public void bindGrid(Grid<?> grid) {
    if (handlerRegistration != null) {
      handlerRegistration.removeHandler();
      handlerRegistration = null;
    }
    if (grid != null) {
      @SuppressWarnings("rawtypes")
      LiveGridView view = (LiveGridView) grid.getView();
      view.addLiveGridViewUpdateHandler(new LiveGridViewUpdateHandler() {
        @Override
        public void onUpdate(LiveGridViewUpdateEvent event) {
          LiveToolItem.this.onUpdate(event);
        }
      });
    }
  }

  /**
   * Returns the tool item messages.
   * 
   * @return the messages
   */
  public LiveToolItemMessages getMessages() {
    if (messages == null) {
      messages = new DefaultLiveToolItemMessages();
    }
    return messages;
  }

  /**
   * Sets the tool item messages.
   * 
   * @param messages the messages
   */
  public void setMessages(LiveToolItemMessages messages) {
    this.messages = messages;
  }

  protected void onUpdate(LiveGridViewUpdateEvent be) {
    int pageSize = be.getRowCount();
    int viewIndex = be.getViewIndex();
    int totalCount = be.getTotalCount();
    int i = pageSize + viewIndex;
    if (i > totalCount) {
      i = totalCount;
    }
    setLabel(getMessages().displayMessage(totalCount == 0 ? 0 : viewIndex + 1, i, (int) totalCount));
  }
}
