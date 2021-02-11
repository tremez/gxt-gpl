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
package com.sencha.gxt.explorer.client.app.ui;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.RootPanel;
import com.sencha.gxt.core.client.GXT;
import com.sencha.gxt.core.client.dom.XDOM;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.gestures.ScrollGestureRecognizer;
import com.sencha.gxt.core.client.gestures.ScrollGestureRecognizer.ScrollDirection;
import com.sencha.gxt.core.client.util.Rectangle;
import com.sencha.gxt.explorer.client.model.Example;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.container.Viewport;

public class ExampleContainer extends SimpleContainer {

  public static final double DEFAULT_MAX_HEIGHT = -1;
  public static final double DEFAULT_MAX_WIDTH = -1;
  public static final double DEFAULT_MIN_HEIGHT = -1;
  public static final double DEFAULT_MIN_WIDTH = -1;
  public static final double DEFAULT_PREFERRED_HEIGHT = 0.5;
  public static final double DEFAULT_PREFERRED_MARGIN = 20;
  public static final double DEFAULT_PREFERRED_WIDTH = 0.5;

  private double maxHeight = DEFAULT_MAX_HEIGHT;
  private double maxWidth = DEFAULT_MAX_WIDTH;
  private double minHeight = DEFAULT_MIN_HEIGHT;
  private double minWidth = DEFAULT_MIN_WIDTH;
  private double preferredHeight = DEFAULT_PREFERRED_HEIGHT;
  private double preferredMargin = DEFAULT_PREFERRED_MARGIN;
  private double preferredWidth = DEFAULT_PREFERRED_WIDTH;

  private Rectangle cleanBounds;

  public ExampleContainer(Example example) {
    this(example != null ? example.getExample() : null);

    if (example != null) {
      maxHeight = example.getMaxHeight();
      maxWidth = example.getMaxWidth();

      minHeight = example.getMinHeight();
      minWidth = example.getMinWidth();

      preferredMargin = example.getPreferredMargin();
      preferredHeight = example.getPreferredHeight();
      preferredWidth = example.getPreferredWidth();
    }

    addGestureRecognizer(new ScrollGestureRecognizer(getElement(), ScrollDirection.BOTH));
  }

  public ExampleContainer(IsWidget widget) {
    super();

    addStyleName("explorer-example-body");
    widget.asWidget().addStyleName("explorer-example-demo");

    setWidget(widget);
  }

  public void doStandalone() {
    Viewport vp = new Viewport();
    vp.add(this);
    RootPanel.get().add(vp);
  }

  public ExampleContainer setMaxHeight(double maxHeight) {
    this.maxHeight = maxHeight;
    return this;
  }

  public ExampleContainer setMaxWidth(double maxWidth) {
    this.maxWidth = maxWidth;
    return this;
  }

  public ExampleContainer setMinHeight(double minHeight) {
    this.minHeight = minHeight;
    return this;
  }

  public ExampleContainer setMinWidth(double minWidth) {
    this.minWidth = minWidth;
    return this;
  }

  public ExampleContainer setPreferredHeight(double preferredHeight) {
    this.preferredHeight = preferredHeight;
    return this;
  }

  public ExampleContainer setPreferredMargin(double preferredMargin) {
    this.preferredMargin = preferredMargin;
    return this;
  }

  public ExampleContainer setPreferredWidth(double preferredWidth) {
    this.preferredWidth = preferredWidth;
    return this;
  }

  @Override
  protected void onAfterFirstAttach() {
    super.onAfterFirstAttach();

    // turn off the loading panel if there was one
    XElement loadingPanel = DOM.getElementById("explorer-loading-panel").cast();
    if (loadingPanel != null) {
      loadingPanel.removeFromParent();
    }
  }

  @Override
  protected void doLayout() {
    if (widget != null) {
      final XElement container = getContainerTarget();
      final XElement element = widget.getElement().cast();

      // determine if the widget is clean by checking if current bounds are clean
      final Rectangle currentBounds = element.getBounds(true);
      final boolean cleanWidget = cleanBounds == null || (
          currentBounds.getX() == cleanBounds.getX()
              && currentBounds.getY() == cleanBounds.getY()
              && currentBounds.getWidth() == cleanBounds.getWidth()
              && currentBounds.getHeight() == cleanBounds.getHeight()
      );

      // only reposition and resize when the widget is clean
      if (cleanWidget) {
        // flags for testing if we prefer 100% width or height
        final boolean fullWidth = preferredWidth == 1;
        final boolean fullHeight = preferredHeight == 1;

        // the widget needs to use relative positioning for container scrolling to work
        element.makePositionable(false);

        // enable container scrolling appropriate depending on type of device
        if (GXT.isTouch()) {
          container.getStyle().setOverflow(Overflow.SCROLL);
        } else {
          // widgets wanting 100% must handle scrolling themselves to avoid extraneous scroll bars
          container.getStyle().setOverflowX(fullWidth ? Overflow.HIDDEN : Overflow.AUTO);
          container.getStyle().setOverflowY(fullHeight ? Overflow.HIDDEN : Overflow.AUTO);
        }

        // fetch available container size
        final int containerWidthPx = container.getComputedWidth();
        final int containerHeightPx = container.getComputedHeight();

        // calculate preferred margin as pixels
        final int preferredMarginPx;
        if (preferredMargin < -1) {
          preferredMarginPx = -1;
        } else if (preferredMargin >= 0 && preferredMargin <= 1) {
          preferredMarginPx = (int) (containerWidthPx * preferredMargin);
        } else {
          preferredMarginPx = (int) preferredMargin;
        }

        // constrain widget margin
        final int singleMargin;
        if (preferredMarginPx < 0) {
          singleMargin = 0;
        } else {
          singleMargin = preferredMarginPx;
        }
        final int bothMargins = singleMargin * 2;

        // calculate minimum size as pixels
        final int minWidthPx;
        if (minWidth < -1) {
          minWidthPx = -1;
        } else if (minWidth >= 0 && minWidth <= 1) {
          minWidthPx = (int) ((containerWidthPx - bothMargins) * minWidth);
        } else {
          minWidthPx = (int) minWidth;
        }

        final int minHeightPx;
        if (minHeight < -1) {
          minHeightPx = -1;
        } else if (minHeight >= 0 && minHeight <= 1) {
          minHeightPx = (int) ((containerHeightPx - bothMargins) * minHeight);
        } else {
          minHeightPx = (int) minHeight;
        }

        // calculate maximum size as pixels
        final int maxWidthPx;
        if (maxWidth < -1) {
          maxWidthPx = -1;
        } else if (maxWidth >= 0 && maxWidth <= 1) {
          maxWidthPx = (int) ((containerWidthPx - bothMargins) * maxWidth);
        } else {
          maxWidthPx = (int) maxWidth;
        }

        final int maxHeightPx;
        if (maxHeight < -1) {
          maxHeightPx = -1;
        } else if (maxHeight >= 0 && maxHeight <= 1) {
          maxHeightPx = (int) ((containerHeightPx - bothMargins) * maxHeight);
        } else {
          maxHeightPx = (int) maxHeight;
        }

        // calculate preferred size as pixels
        final int preferredWidthPx;
        if (preferredWidth < -1) {
          preferredWidthPx = -1;
        } else if (preferredWidth >= 0 && preferredWidth <= 1) {
          preferredWidthPx = (int) ((containerWidthPx - bothMargins) * preferredWidth);
        } else {
          preferredWidthPx = (int) preferredWidth;
        }

        final int preferredHeightPx;
        if (preferredHeight < -1) {
          preferredHeightPx = -1;
        } else if (preferredHeight >= 0 && preferredHeight <= 1) {
          preferredHeightPx = (int) ((containerHeightPx - bothMargins) * preferredHeight);
        } else {
          preferredHeightPx = (int) preferredHeight;
        }

        // constrain widget size
        final int widgetWidthPx;
        if (preferredWidthPx < minWidthPx && minWidthPx > -1) {
          widgetWidthPx = minWidthPx;
        } else if (preferredWidthPx > maxWidthPx && maxWidthPx > -1) {
          widgetWidthPx = maxWidthPx;
        } else {
          widgetWidthPx = preferredWidthPx;
        }

        final int widgetHeightPx;
        if (preferredHeightPx < minHeightPx && minHeightPx > -1) {
          widgetHeightPx = minHeightPx;
        } else if (preferredHeightPx > maxHeightPx && maxHeightPx > -1) {
          widgetHeightPx = maxHeightPx;
        } else {
          widgetHeightPx = preferredHeightPx;
        }

        // calculate widget position for centering
        final int left = (containerWidthPx - bothMargins - widgetWidthPx) / 2;
        final int top = (containerHeightPx - bothMargins - widgetHeightPx) / 2;

        // constrain widget to top-left
        final int widgetLeftPx;
        if (left < 0) {
          widgetLeftPx = 0;
        } else {
          widgetLeftPx = left;
        }

        final int widgetTopPx;
        if (top < 0) {
          widgetTopPx = 0;
        } else {
          widgetTopPx = top;
        }

        // set the widget margin
        element.setMargins(singleMargin);

        // layout the widget at its calculated position & size
        applyLayout(widget, new Rectangle(widgetLeftPx, widgetTopPx, widgetWidthPx, widgetHeightPx));

        Scheduler.get().scheduleFinally(new Command() {
          @Override
          public void execute() {
            // after the layout has finished, fetch the adjusted widget size (including any possible overflow & scrollbars)
            final int adjustedWidthPx;
            if (fullWidth) {
              adjustedWidthPx = widgetWidthPx;
            } else if (element.isScrollableX()) {
              adjustedWidthPx = element.getScrollWidth() + XDOM.getScrollBarWidth();
            } else {
              adjustedWidthPx = element.getComputedWidth();
            }

            final int adjustedHeightPx;
            if (fullHeight) {
              adjustedHeightPx = widgetHeightPx;
            } else if (element.isScrollableY()) {
              adjustedHeightPx = element.getScrollHeight() + XDOM.getScrollBarWidth();
            } else {
              adjustedHeightPx = element.getComputedHeight();
            }

            // if the actual size differs from the calculated size, position the widget again because it's not right
            if (adjustedWidthPx != widgetWidthPx || adjustedHeightPx != widgetHeightPx) {
              // recalculate widget position for centering
              final int adjustedLeft = (containerWidthPx - bothMargins - adjustedWidthPx) / 2;
              final int adjustedTop = (containerHeightPx - bothMargins - adjustedHeightPx) / 2;

              // constrain widget to top-left
              final int adjustedLeftPx;
              if (adjustedLeft < 0) {
                adjustedLeftPx = 0;
              } else {
                adjustedLeftPx = adjustedLeft;
              }

              final int adjustedTopPx;
              if (adjustedTop < 0) {
                adjustedTopPx = 0;
              } else {
                adjustedTopPx = adjustedTop;
              }

              // layout the widget at its recalculated position & size
              applyLayout(widget, new Rectangle(adjustedLeftPx, adjustedTopPx, adjustedWidthPx, adjustedHeightPx));
            }

            Scheduler.get().scheduleFinally(new Command() {
              @Override
              public void execute() {
                // after the layout has finished, preserve the clean bounds for next time
                cleanBounds = element.getBounds(true);
              }
            });
          }
        });
      } else {
        // once the widget is dirty, ensure it can't ever be clean again even if the user restores original bounds
        cleanBounds = new Rectangle(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
      }


      Scheduler.get().scheduleFinally(new Command() {
        @Override
        public void execute() {
          // after the layout has finished, focus may be off screen or covered by a virtual keyboard
          // so we need to ensure focus is on screen or the user may get confused
          ensureFocusIsOnScreen();
        }
      });
    }
  }

  private static native void ensureFocusIsOnScreen() /*-{
    var e = $doc.activeElement;

    if (e) {
      var t = e.tagName;
      if (t) {
        t = t.toLowerCase();
        if (t == "input" || t == "textarea") {
          if (e.scrollIntoViewIfNeeded) {
            e.scrollIntoViewIfNeeded(true)
          } else if (e.scrollIntoView) {
            e.scrollIntoView(false);
          }
        }
      }
    }
  }-*/;

}
