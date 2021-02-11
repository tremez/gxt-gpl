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
package com.sencha.gxt.widget.core.client.container;

import com.google.gwt.dom.client.Document;
import com.google.gwt.event.dom.client.HasScrollHandlers;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiChild;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.IsWidget;
import com.sencha.gxt.core.client.dom.DefaultScrollSupport;
import com.sencha.gxt.core.client.dom.HasScrollSupport;
import com.sencha.gxt.core.client.dom.ScrollSupport;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.core.client.gestures.ScrollGestureRecognizer;

/**
 * A flow layout container that supports scrolling and lays out its widgets
 * using the default HTML layout behavior.
 * 
 * <p/>
 * Code Snippet:
 * 
 * <pre>
    FlowLayoutContainer c = new FlowLayoutContainer();
    c.setScrollMode(ScrollMode.ALWAYS);
    MarginData layoutData = new MarginData(new Margins(0, 5, 0, 0));
    c.add(new TextButton("Button 1"), layoutData);
    c.add(new TextButton("Button 2"), layoutData);
    c.add(new TextButton("Button 3"), layoutData);
    Viewport v = new Viewport();
    v.add(c);
    RootPanel.get().add(v);
 * </pre>
 */
public class FlowLayoutContainer extends InsertContainer implements HasScrollHandlers, HasScrollSupport {

  private ScrollSupport scrollSupport;
  private ScrollGestureRecognizer scrollGestureRecognizer;

  /**
   * Creates a flow layout container.
   */
  public FlowLayoutContainer() {
    setElement(Document.get().createDivElement());
  }

  /**
   * Adds a widget to a flow layout container with the specified layout
   * parameters.
   * 
   * @param child the widget to add to the layout container
   * @param layoutData the parameters that describe how to lay out the widget
   */
  @UiChild(tagname = "child")
  public void add(IsWidget child, MarginData layoutData) {
    if (child != null) {
      child.asWidget().setLayoutData(layoutData);
    }
    super.add(child);
  }

  @Override
  public HandlerRegistration addScrollHandler(ScrollHandler handler) {
    DOM.sinkEvents(getContainerTarget(), Event.ONSCROLL | DOM.getEventsSunk(getContainerTarget()));
    return addDomHandler(handler, ScrollEvent.getType());
  }

  /**
   * Returns the scroll mode from the container's <code>ScrollSupport</code>
   * instance.
   * 
   * @return the scroll mode
   */
  public ScrollMode getScrollMode() {
    return getScrollSupport().getScrollMode();
  }

  @Override
  public ScrollSupport getScrollSupport() {
    if (scrollSupport == null) {
      scrollSupport = new DefaultScrollSupport(getContainerTarget());
    }
    initScrollGestureRecognizer();
    return scrollSupport;
  }

  /**
   * Inserts the widget at the specified index in the flow layout container.
   * 
   * @param w the widget to insert in the layout container
   * @param beforeIndex the insert index
   * @param layoutData the parameters that describe how to lay out the widget
   */
  public void insert(IsWidget w, int beforeIndex, MarginData layoutData) {
    if (w != null) {
      w.asWidget().setLayoutData(layoutData);
    }
    super.insert(w, beforeIndex);
  }

  /**
   * Sets the scroll mode on the container's <code>ScrollSupport</code>
   * instance. The scroll mode will not be preserved if
   * {@link #setScrollSupport(ScrollSupport)} is called AFTER calling this
   * method.
   * 
   * @param scrollMode the scroll mode
   */
  public void setScrollMode(ScrollMode scrollMode) {
    getScrollSupport().setScrollMode(scrollMode);
  }

  @Override
  public void setScrollSupport(ScrollSupport support) {
    this.scrollSupport = support;
    initScrollGestureRecognizer();
  }

  private void initScrollGestureRecognizer() {
    if (scrollGestureRecognizer == null) {
      scrollGestureRecognizer = new ScrollGestureRecognizer(getContainerTarget()) {
        @Override
        protected ScrollDirection getDirection() {
          ScrollMode scrollMode = getScrollMode();
          switch (scrollMode) {
            case AUTOX:
              return ScrollDirection.HORIZONTAL;
            case AUTOY:
              return ScrollDirection.VERTICAL;
          }
          return ScrollDirection.BOTH;
        }
      };
      addGestureRecognizer(scrollGestureRecognizer);
    }
  }
}
