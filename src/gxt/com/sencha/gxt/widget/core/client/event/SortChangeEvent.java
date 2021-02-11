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
import com.sencha.gxt.data.shared.SortInfo;
import com.sencha.gxt.widget.core.client.event.SortChangeEvent.SortChangeHandler;

public final class SortChangeEvent extends GridEvent<SortChangeHandler> {

  public interface HasSortChangeHandlers extends HasHandlers {
    HandlerRegistration addSortChangeHandler(SortChangeHandler handler);
  }

  public interface SortChangeHandler extends EventHandler {
    void onSortChange(SortChangeEvent event);
  }

  private static GwtEvent.Type<SortChangeHandler> TYPE;

  public static GwtEvent.Type<SortChangeHandler> getType() {
    if (TYPE == null) {
      TYPE = new GwtEvent.Type<SortChangeHandler>();
    }
    return TYPE;
  }

  private SortInfo sortInfo;

  public SortChangeEvent(SortInfo sortInfo) {
    this.sortInfo = sortInfo;
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public GwtEvent.Type<SortChangeHandler> getAssociatedType() {
    return (GwtEvent.Type) TYPE;
  }

  public SortInfo getSortInfo() {
    return sortInfo;
  }

  @Override
  protected void dispatch(SortChangeHandler handler) {
    handler.onSortChange(this);
  }
}