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

import java.util.logging.Logger;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.event.dom.client.HasScrollHandlers;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.uibinder.client.UiChild;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.IndexedPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.GXTLogConfiguration;
import com.sencha.gxt.core.client.Style.Side;
import com.sencha.gxt.core.client.dom.DefaultScrollSupport;
import com.sencha.gxt.core.client.dom.HasScrollSupport;
import com.sencha.gxt.core.client.dom.ScrollSupport;
import com.sencha.gxt.core.client.dom.ScrollSupport.ScrollMode;
import com.sencha.gxt.core.client.dom.XDOM;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.gestures.ScrollGestureRecognizer;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.core.client.util.Size;

/**
 * A layout container that lays out its children in a single column. The lay out
 * properties for each child are specified using {@link VerticalLayoutData}. 
 * <p/>
 * Note: This container must be given a size directly or by its parent. 
 * <p/>
 * <ul>
 * <li><a href="http://docs.sencha.com/gxt/latest/ui/layout/containers/VerticalLayoutContainer.html">Vertical Layout Container Guide</a>
 * - More on sizing.
 * </li>
 * <li><a href="http://docs.sencha.com/gxt/latest/ui/layout/LayoutContainers.html">Layouts Guide</a> - More on layouts.
 * </li>
 * </ul>
 * <p/>
 * Example:
 * <pre>
    VerticalLayoutContainer c = new VerticalLayoutContainer();
    c.add(new FieldLabel(new TextField(), "Home"), new VerticalLayoutData(1, .5));
    c.add(new FieldLabel(new TextField(), "Office"), new VerticalLayoutData(1, .5));
    RootPanel.get().add(c);
 * </pre>
 */
public class VerticalLayoutContainer extends InsertResizeContainer implements HasScrollHandlers, HasScrollSupport {

  /**
   * Specifies parameters that control the layout of the widget in the
   * container.
   */
  public static class VerticalLayoutData extends MarginData implements HasWidth, HasHeight, LayoutData {

    private double height = -1d;
    private double width = -1d;

    /**
     * Creates vertical layout parameters with default values for
     * <code>height</code> (-1) and <code>width</code> (-1) and
     * <code>margins</code> (none).
     */
    public VerticalLayoutData() {

    }

    /**
     * Creates vertical layout parameters with the specified values.
     * 
     * @param width the width specification (see {@link HasWidth})
     * @param height the height specification (see {@link HasHeight})
     */
    public VerticalLayoutData(double width, double height) {
      setWidth(width);
      setHeight(height);
    }

    /**
     * Creates ertical layout parameters with the specified values.
     * 
     * @param width the width specification (see {@link HasWidth})
     * @param height the height specification (see {@link HasHeight})
     * @param margins the margin specification (see {@link HasMargins})
     */
    public VerticalLayoutData(double width, double height, Margins margins) {
      super(margins);
      setWidth(width);
      setHeight(height);
    }

    @Override
    public double getHeight() {
      return height;
    }

    @Override
    public double getWidth() {
      return width;
    }

    @Override
    public void setHeight(double height) {
      this.height = height;
    }

    @Override
    public void setWidth(double width) {
      this.width = width;
    }

  }

  private boolean adjustForScroll;
  private boolean secondPassRequired;
  private ScrollSupport scrollSupport;
  private ScrollGestureRecognizer scrollGestureRecognizer;

  private static Logger logger = Logger.getLogger(VerticalLayoutContainer.class.getName());

  /**
   * Creates a vertical layout container.
   */
  public VerticalLayoutContainer() {
    setElement(Document.get().createDivElement());
    getContainerTarget().makePositionable(false);
  }

  /**
   * Adds a widget to the vertical layout container with the specified layout
   * parameters.
   * 
   * @param child the widget to add to the layout container
   * @param layoutData the parameters that describe how to lay out the widget
   */
  @UiChild(tagname = "child")
  public void add(IsWidget child, VerticalLayoutData layoutData) {
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
   * Inserts the widget at the specified index in the vertical layout container.
   * 
   * @param w the widget to insert in the layout container
   * @param beforeIndex the insert index
   * @param layoutData the parameters that describe how to lay out the widget
   */
  public void insert(IsWidget w, int beforeIndex, VerticalLayoutData layoutData) {
    if (w != null) {
      w.asWidget().setLayoutData(layoutData);
    }
    super.insert(w, beforeIndex);
  }

  /**
   * Returns true if the container reserves space for the scroll bar.
   * 
   * @return true if the container reserves space for the scroll bar
   */
  public boolean isAdjustForScroll() {
    return adjustForScroll;
  }

  /**
   * True to request that the container reserve space for the scroll bar
   * (defaults to false).
   * 
   * @param adjustForScroll true to reserve space for the scroll bar
   */
  public void setAdjustForScroll(boolean adjustForScroll) {
    this.adjustForScroll = adjustForScroll;
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

  @Override
  protected void doLayout() {
    Size size = getContainerTarget().getStyleSize();

    if (GXTLogConfiguration.loggingIsEnabled()) {
      logger.finest(getId() + " doLayout size: " + size);
    }

    int w = size.getWidth() - (adjustForScroll ? XDOM.getScrollBarWidth() : 0);
    int h = size.getHeight();
    int ph = h;

    int count = getWidgetCount();

    // some columns can be percentages while others are fixed
    // so we need to make 2 passes
    for (int i = 0; i < count; i++) {
      Widget c = getWidget(i);
      if (!c.isVisible()) {
        continue;
      }
      c.getElement().getStyle().setPosition(Position.RELATIVE);
      double height = -1;
      Object d = c.getLayoutData();
      if (d instanceof HasHeight) {
        height = ((HasHeight) d).getHeight();
      }
      if (height > 1) {
        ph -= height;
      } else if (height == -1) {
        if ((c instanceof HasWidgets || c instanceof IndexedPanel) && !secondPassRequired) {
          secondPassRequired = true;
          Scheduler.get().scheduleDeferred(layoutCommand);
          return;
        }

        ph -= c.getOffsetHeight() + c.getElement().<XElement>cast().getMargins(Side.TOP, Side.BOTTOM);
        ph -= getTopBottomMargins(c);
      } else if (height < -1) {
        ph -= (h + height);
        ph -= getTopBottomMargins(c);
      } else {
        assert height >= 0 && height <= 1;
      }

    }

    secondPassRequired = false;

    ph = ph < 0 ? 0 : ph;
    double heightPercentageRemainder = 0;

    int yOffset = 0;
    for (int i = 0; i < count; i++) {
      Widget c = getWidget(i);
      if (!c.isVisible()) {
        continue;
      }
      double width = -1;
      double height = -1;

      Object d = c.getLayoutData();
      if (d instanceof HasWidth) {
        width = ((HasWidth) d).getWidth();
      }
      if (d instanceof HasHeight) {
        height = ((HasHeight) d).getHeight();
      }
      Margins margins = new Margins();
      if (d instanceof HasMargins) {
        if (((HasMargins) d).getMargins() != null) {
          margins = ((HasMargins) d).getMargins();
        }
      }

      if (width >= 0 && width <= 1) {
        //use value as a percentage
        width = width * w;
      } else if (width < -1) {
        //use value as a fixed pixel size
        width = w + width;
      } //else if w == -1, ignore, the widget already has the right width

      if (width != -1) {
        width -= margins.getRight() + margins.getLeft();
      }

      if (height >= 0 && height <= 1) {
        //use the value as a percentage of total available height
        height = height * ph;
        heightPercentageRemainder += (height - (int)Math.floor(height)); //add the new remainder bit
        assert heightPercentageRemainder >= 0;
        //if there is enough leftover at this point for a whole pixel, add it
        if (heightPercentageRemainder >= 1) {
          height++;
          heightPercentageRemainder--;
        }
      } else if (height < -1) {
        //height < -1, allocate all but 'height' space for this widget
        height = h + height;
      }

      if (height != -1) {
        height -= margins.getTop() + margins.getBottom();
      }

      applyLayout(c, (int) Math.floor(width), (int) Math.floor(height));
      c.getElement().<XElement>cast().setLeftTop(margins.getLeft(), yOffset + margins.getTop());

      //subtract out the margindata from the offset position so that position:relative behaves
      yOffset += margins.getTop() + margins.getBottom();
    }
  }

  @Override
  protected void doPhysicalAttach(Widget child, int beforeIndex) {
    //wrap in an outer div and attach that instead in case widget uses something other than display:block
    XElement wrap = DOM.createDiv().cast();
    wrap.appendChild(child.getElement());
    getContainerTarget().insertChild(wrap, beforeIndex);
  }

  @Override
  protected void doPhysicalDetach(Widget child) {
    assert child.getElement().getParentElement().getParentElement() == getContainerTarget();
    child.getElement().getParentElement().removeFromParent();
    child.getElement().removeFromParent();
  }

  @Override
  protected void onInsert(int index, Widget child) {
    //no op, super applies margins that doLayout deals with instead
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
