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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Style;
import com.google.gwt.dom.client.Style.Clear;
import com.google.gwt.event.dom.client.HasScrollHandlers;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.dom.client.ScrollHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.LocaleInfo;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiChild;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Event;
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
 * A layout container that uses the CSS float style to enable widgets to float
 * around other widgets.
 * 
 * <p />
 * Code Snippet:
 * 
 * <pre>
    CssFloatLayoutContainer c = new CssFloatLayoutContainer();
    HTML rectangle = new HTML("I'm a Red<br/>Rectangle");
    Label text = new Label("This text will flow around the Red Rectangle because that's the way things work in CssFloatLayoutContainer. You may need to resize the browser window to see the effect.");
    c.add(rectangle, new CssFloatData(100));
    c.add(text);
    rectangle.getElement().getStyle().setBackgroundColor("red");
    text.getElement().getStyle().setFloat(Float.NONE);
    text.getElement().getStyle().setDisplay(Display.INLINE);
    Viewport v = new Viewport();
    v.add(c);
    RootPanel.get().add(v);
 * </pre>
 */
public class CssFloatLayoutContainer extends InsertResizeContainer implements HasScrollHandlers, HasScrollSupport {

  /**
   * Specifies widget layout parameters that control the size of the widget.
   */
  public static class CssFloatData implements HasSize, HasMargins, LayoutData {

    private double width = -1;
    private Margins margins;
    private boolean clear;

    /**
     * Creates layout data for the CSS float layout container with the default
     * value for <code>width</code> (-1 = use widget's default width).
     */
    public CssFloatData() {

    }

    /**
     * Creates layout data for the CSS float layout container using the
     * specified width. Values <= 1 are treated as percentages.
     * 
     * @param width the width of the widget
     */
    public CssFloatData(double width) {
      this.width = width;
    }

    public CssFloatData(double width, Margins margins) {
      this(width);
      this.margins = margins;
    }

    @Override
    public double getSize() {
      return width;
    }

    /**
     * Sets the width of the column.
     * 
     * @param size the width, values <= 1 treated a percentages.
     */
    @Override
    public void setSize(double size) {
      this.width = size;
    }

    @Override
    public Margins getMargins() {
      return margins;
    }

    @Override
    public void setMargins(Margins margins) {
      this.margins = margins;
    }

    /**
     * Indicates if this item should begin a new line (similar to {@code clear:both} in CSS). Defaults to {@code false}.
     * @return true if this layout data should begin a new line
     */
    public boolean isClear() {
      return clear;
    }

    /**
     * Configures whether or not this child should begin a new line (similar to {@code clear:both} in CSS). Defaults to
     * {@code false}.
     * @param clear true if this item should begin a new line
     */
    public void setClear(boolean clear) {
      this.clear = clear;
    }
  }

  public interface CssFloatLayoutAppearance {

    XElement getContainerTarget(XElement parent);

    void onInsert(Widget child);

    void onRemove(Widget child);

    void render(SafeHtmlBuilder sb);

  }

  protected boolean adjustForScroll = false;
  private final CssFloatLayoutAppearance appearance;

  private Style.Float styleFloat = LocaleInfo.getCurrentLocale().isRTL() ? Style.Float.RIGHT : Style.Float.LEFT;
  private ScrollSupport scrollSupport;
  private ScrollGestureRecognizer scrollGestureRecognizer;

  private static Logger logger = Logger.getLogger(CssFloatLayoutContainer.class.getName());

  /**
   * Creates a CSS float layout container with the default appearance.
   */
  public CssFloatLayoutContainer() {
    this(GWT.<CssFloatLayoutAppearance> create(CssFloatLayoutAppearance.class));
  }

  /**
   * Creates a CSS float layout container with the specified appearance.
   * 
   * @param appearance the appearance of the CSS float layout container
   */
  public CssFloatLayoutContainer(CssFloatLayoutAppearance appearance) {
    this.appearance = appearance;
    SafeHtmlBuilder sb = new SafeHtmlBuilder();
    appearance.render(sb);
    setElement((Element) XDOM.create(sb.toSafeHtml()));
  }

  /**
   * Adds a widget to the CSS float layout container with the specified layout
   * parameters.
   * 
   * @param child the widget to add to the layout container
   * @param layoutData the parameters that describe how to lay out the widget
   */
  @UiChild(tagname = "child")
  public void add(IsWidget child, CssFloatData layoutData) {
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
   * Returns the value of the CSS float property.
   * 
   * @return the value of the CSS float property
   */
  public Style.Float getStyleFloat() {
    return styleFloat;
  }

  /**
   * Inserts the widget at the specified index in the CSS float layout
   * container.
   * 
   * @param w the widget to insert in the layout container
   * @param beforeIndex the insert index
   * @param layoutData the parameters that describe how to lay out the widget
   */
  public void insert(IsWidget w, int beforeIndex, CssFloatData layoutData) {
    if (w != null) {
      w.asWidget().setLayoutData(layoutData);
    }
    super.insert(w, beforeIndex);
  }

  /**
   * Returns true if adjust for scroll is enabled.
   * 
   * @return the adjust for scroll state
   */
  public boolean isAdjustForScroll() {
    return adjustForScroll;
  }

  /**
   * True to adjust the container width calculations to account for the scroll
   * bar (defaults to false).
   * 
   * @param adjustForScroll the adjust for scroll state
   */
  public void setAdjustForScroll(boolean adjustForScroll) {
    this.adjustForScroll = adjustForScroll;
  }

  /**
   * Sets the scroll mode on the container's <code>ScrollSupport</code>
   * instance. The scroll mode will not be preserved if
   * {@link #setScrollSupport(ScrollSupport)} is called after calling this
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

  /**
   * Sets the value of the CSS float property.
   * 
   * @param styleFloat the value of the CSS float property
   */
  public void setStyleFloat(Style.Float styleFloat) {
    this.styleFloat = styleFloat;
  }

  @Override
  protected void doLayout() {
    Size size = getContainerTarget().getStyleSize();

    if (GXTLogConfiguration.loggingIsEnabled()) {
      logger.finest(getId() + " doLayout size: " + size);
    }

    int w = size.getWidth() - (adjustForScroll ? XDOM.getScrollBarWidth() : 0);
    w -= getContainerTarget().getFrameWidth(Side.LEFT, Side.RIGHT);

    int count = getWidgetCount();

    // some columns can be percentages while others are fixed
    // so we need to make 2 passes
    double linePercentageRemaining = 1.0;
    double lineRemainingFixed = w;

    List<Widget> line = new ArrayList<Widget>();
    for (int i = 0; i < count; i++) {
      Widget widget = getWidget(i);

      Object d = widget.getLayoutData();
      CssFloatData layoutData;
      if (d instanceof CssFloatData) {
        layoutData = (CssFloatData) d;
      } else {
        layoutData = new CssFloatData();
      }
      if (layoutData.isClear()) {
        appendLine(line, (int) lineRemainingFixed);
        lineRemainingFixed = w;
        linePercentageRemaining = 1.0;
        widget.getElement().getStyle().setClear(Clear.BOTH);
      } else {
        widget.getElement().getStyle().clearClear();
      }
      if (layoutData.getSize() > 1) {
        if (lineRemainingFixed < layoutData.getSize() + getLeftRightMargins(widget)) {
          appendLine(line, (int) lineRemainingFixed);
          lineRemainingFixed = w;
          linePercentageRemaining = 1.0;
        }
        lineRemainingFixed -= layoutData.getSize() + getLeftRightMargins(widget);
        if (GXTLogConfiguration.loggingIsEnabled()) {
          logger.finest(getId() + " child " + i + " consuming " + layoutData.getSize() + "px");
        }
      } else if (layoutData.getSize() > 0) {
        if (linePercentageRemaining < layoutData.getSize()) {
          appendLine(line, (int) (lineRemainingFixed));
          lineRemainingFixed = w;
          linePercentageRemaining = 1.0;
        }
        linePercentageRemaining -= layoutData.getSize();
        if (GXTLogConfiguration.loggingIsEnabled()) {
          logger.finest(getId() + " child " + i + " consuming " + layoutData.getSize() * 100 + "%");
        }
      } else {
        //assume layoutData.getSize() == -1, measure the child to see where we put it
        int width = widget.getOffsetWidth();
        if (lineRemainingFixed < width + getLeftRightMargins(widget)) {
          appendLine(line, (int) lineRemainingFixed);
          lineRemainingFixed = w;
          linePercentageRemaining = 1.0;
        }
        lineRemainingFixed -= width + getLeftRightMargins(widget);
        if (GXTLogConfiguration.loggingIsEnabled()) {
          logger.finest(getId() + " child " + i + " consuming " + width + "px (as -1)");
        }
      }
      line.add(widget);
    }
    appendLine(line, (int) (lineRemainingFixed));
  }

  private void appendLine(List<Widget> line, int percentageWidth) {
    for (int i = 0; i < line.size(); i++) {
      Widget widget = line.get(i);

      Object d = widget.getLayoutData();
      double s = -1;
      if (d instanceof CssFloatData) {
        CssFloatData layoutData = (CssFloatData) d;
        s = layoutData.getSize();
      }

      final int width;
      if (s > 0 && s <= 1) {
        assert percentageWidth >= 0;//don't need a non-negative percentage unless this line has percentage items
        width = (int) (s * percentageWidth) - getLeftRightMargins(widget);//%
      } else {
        width = (int) s;//-1 or px
      }
      applyLayout(widget, width, -1);
    }
    line.clear();
    if (GXTLogConfiguration.loggingIsEnabled()) {
      logger.finest(getId() + " line done");
    }
  }

  @Override
  protected XElement getContainerTarget() {
    return appearance.getContainerTarget(getElement());
  }

  @Override
  protected void onInsert(int index, Widget child) {
    super.onInsert(index, child);
    child.getElement().getStyle().setFloat(styleFloat);
    appearance.onInsert(child);
  }

  @Override
  protected void onRemove(Widget child) {
    super.onRemove(child);
    appearance.onRemove(child);
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
