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

import com.google.gwt.event.shared.HandlerRegistration;
import com.sencha.gxt.widget.core.client.event.AddEvent.AddHandler;
import com.sencha.gxt.widget.core.client.event.AddEvent.HasAddHandlers;
import com.sencha.gxt.widget.core.client.event.BeforeAddEvent.BeforeAddHandler;
import com.sencha.gxt.widget.core.client.event.BeforeAddEvent.HasBeforeAddHandlers;
import com.sencha.gxt.widget.core.client.event.BeforeRemoveEvent.BeforeRemoveHandler;
import com.sencha.gxt.widget.core.client.event.BeforeRemoveEvent.HasBeforeRemoveHandlers;
import com.sencha.gxt.widget.core.client.event.RemoveEvent.HasRemoveHandlers;
import com.sencha.gxt.widget.core.client.event.RemoveEvent.RemoveHandler;


/**
 * Aggregating handler interface for:
 * 
 * <dl>
 * <dd>{@link BeforeAddEvent}</b></dd>
 * <dd>{@link AddEvent}</b></dd>
 * <dd>{@link BeforeRemoveEvent}</b></dd>
 * <dd>{@link RemoveEvent}</b></dd>
 * </dl>
 */
public interface ContainerHandler extends BeforeAddHandler, AddHandler, BeforeRemoveHandler,
    RemoveHandler {

  /**
   * A widget that implements this interface is a public source of
   * {@link BeforeAddEvent}, {@link AddEvent}, {@link BeforeRemoveEvent},
   * {@link RemoveEvent} events.
   */
  public interface HasContainerHandlers extends HasBeforeAddHandlers,
          HasAddHandlers, HasBeforeRemoveHandlers, HasRemoveHandlers {

      /**
       * Adds a {@link ContainerHandler} handler for {@link BeforeAddEvent} ,
       * {@link AddEvent}, {@link BeforeRemoveEvent}, {@link RemoveEvent} events.
       * 
       * @param handler
       *            the handler
       * @return the registration for the event
       */
      HandlerRegistration addContainerHandler(ContainerHandler handler);
  }
}
