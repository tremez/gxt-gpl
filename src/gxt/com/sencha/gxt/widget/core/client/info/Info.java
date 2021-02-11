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
package com.sencha.gxt.widget.core.client.info;

import java.util.Stack;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Document;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.RootPanel;
import com.sencha.gxt.core.client.Style.HideMode;
import com.sencha.gxt.core.client.dom.XDOM;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.util.Point;
import com.sencha.gxt.core.client.util.Size;
import com.sencha.gxt.widget.core.client.Component;

/**
 * Displays a message in the bottom right region of the browser for a specified amount of time.
 */
public class Info extends Component {

  public abstract static interface InfoAppearance {

    XElement getContentElement(XElement parent);

    void render(SafeHtmlBuilder sb);

  }

  private static Stack<Info> infoCache = new Stack<Info>();

  private static Stack<Info> topLeft = new Stack<Info>();
  private static Stack<Info> topRight = new Stack<Info>();
  private static Stack<Info> bottomLeft = new Stack<Info>();
  private static Stack<Info> bottomRight = new Stack<Info>();

  /**
   * Displays a message using the specified configuration
   * 
   * @param config the info configuration
   */
  public static void display(InfoConfig config) {
    pop().show(config);
  }

  /**
   * Displays a message with the given title and text.
   * 
   * @param title the title as text
   * @param message the message as text
   */
  public static void display(String title, String message) {
    display(new DefaultInfoConfig(title, message));
  }

  /**
   * Displays a message with the given title and content.
   * 
   * @param title the title as HTML
   * @param message the message as HTML
   */
  public static void display(SafeHtml title, SafeHtml message) {
    display(new DefaultInfoConfig(title, message));
  }

  private static Info pop() {
    Info info = infoCache.size() > 0 ? (Info) infoCache.pop() : null;
    if (info == null) {
      info = new Info();
    }
    return info;
  }

  private static void push(Info info) {
    infoCache.push(info);
  }

  protected InfoConfig config;

  private InfoAppearance appearance;
  private HandlerRegistration showHandlerRegistration;
  private HandlerRegistration hideHandlerRegistration;

  /**
   * Creates a new info instance.
   */
  public Info() {
    this((InfoAppearance) GWT.create(InfoAppearance.class));
  }

  /**
   * Creates a new info appearance.
   * 
   * @param appearance the info appearance
   */
  public Info(InfoAppearance appearance) {
    this.appearance = appearance;
    setElement(Document.get().createDivElement());

    setHideMode(HideMode.OFFSETS);
  }

  /**
   * Returns the appearance.
   * 
   * @return the appearance
   */
  public InfoAppearance getAppearance() {
    return appearance;
  }

  @Override
  public void hide() {
    super.hide();
    afterHide();
  }

  /**
   * Displays the info.
   * 
   * @param config the info configuration.
   */
  public void show(InfoConfig config) {
    this.config = config;

    if (showHandlerRegistration != null) {
      showHandlerRegistration.removeHandler();
      showHandlerRegistration = null;
    }

    if (hideHandlerRegistration != null) {
      hideHandlerRegistration.removeHandler();
      hideHandlerRegistration = null;
    }

    if (config.getShowHandler() != null) {
      showHandlerRegistration = addShowHandler(config.getShowHandler());
    }

    if (config.getHideHandler() != null) {
      hideHandlerRegistration = addHideHandler(config.getHideHandler());
    }

    onShowInfo();
  }

  protected void afterHide() {
    RootPanel.get().remove(this);
    topLeft.remove(this);
    topRight.remove(this);
    bottomLeft.remove(this);
    bottomRight.remove(this);
    push(this);
  }

  protected void afterShow() {
    Timer t = new Timer() {
      public void run() {
        hide();
      }
    };
    t.schedule(config.getDisplay());
  }

  protected void onShowInfo() {
    SafeHtmlBuilder sb = new SafeHtmlBuilder();
    appearance.render(sb);
    getElement().setInnerSafeHtml(sb.toSafeHtml());

    XElement target = appearance.getContentElement(getElement());
    target.setInnerSafeHtml(config.render(this));

    getElement().makePositionable(true);

    RootPanel.get().add(this);

    setWidth(config.getWidth());
    setHeight(config.getHeight());

    getElement().updateZIndex(0);

    Point p = position();
    getElement().setLeftTop(p.getX(), p.getY());

    show();

    afterShow();
  }

  protected Point position() {
    Size viewport = XDOM.getViewportSize();

    int scrollLeft = XDOM.getBodyScrollLeft();
    int scrollTop = XDOM.getBodyScrollTop();

    int margin = config.getMargin();

    int left = 0;
    int top = 0;

    switch (config.getPosition()) {
      case TOP_LEFT:
        if (topLeft.size() == 0) {
          left = margin + scrollLeft;
          top = margin;

        } else {
          Info prev = topLeft.peek();
          top = prev.getAbsoluteTop() + prev.getOffsetHeight() + margin;
          left = prev.getAbsoluteLeft();

          int bottom = top + getOffsetHeight();

          if (bottom > viewport.getHeight()) {
            top = config.getMargin();
            left = prev.getAbsoluteLeft() + config.getWidth() + margin + scrollLeft;
          }

        }
        topLeft.add(this);
        break;
      case TOP_RIGHT:
        if (topRight.size() == 0) {
          left = viewport.getWidth() - config.getWidth() - margin + scrollLeft;
          top = config.getMargin();

        } else {
          Info prev = topRight.peek();
          top = prev.getAbsoluteTop() + prev.getOffsetHeight() + margin;
          left = prev.getAbsoluteLeft();

          int bottom = top + getOffsetHeight();

          if (bottom > viewport.getHeight()) {
            top = margin;
            left = prev.getAbsoluteLeft() - config.getWidth() - margin + scrollLeft;
          }
        }
        topRight.add(this);
        break;
      case BOTTOM_LEFT: {
        int height = config.getHeight() == -1 ? getOffsetHeight() : config.getHeight();

        if (bottomLeft.size() == 0) {
          top = viewport.getHeight() - height - margin + scrollTop;
          left = margin + scrollLeft;

        } else {
          Info prev = bottomLeft.peek();
          left = prev.getAbsoluteLeft() + scrollLeft;
          top = prev.getAbsoluteTop() - height - margin;

          if (top < 0) {
            top = viewport.getHeight() - height - margin + scrollTop;
            left = prev.getAbsoluteLeft() + prev.getOffsetWidth() + margin + scrollLeft;
          }
        }
        bottomLeft.add(this);
      }
        break;
      case BOTTOM_RIGHT: {
        int height = config.getHeight() == -1 ? getOffsetHeight() : config.getHeight();
        if (bottomRight.size() == 0) {
          top = viewport.getHeight() - height - margin - XDOM.getBodyScrollTop();
          left = viewport.getWidth() - config.getWidth() - margin + scrollLeft - XDOM.getScrollBarWidth();
        } else {
          Info prev = bottomRight.peek();
          top = prev.getAbsoluteTop() - height - margin;
          left = prev.getAbsoluteLeft() + scrollLeft;

          if (top < 0) {
            top = viewport.getHeight() - height - margin + scrollTop;
            left = prev.getAbsoluteLeft() - prev.getOffsetWidth() - margin + scrollLeft;
          }
        }
        bottomRight.add(this);
      }
        break;
    }

    return new Point(left, top);
  }

}
