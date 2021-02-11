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
package com.sencha.gxt.widget.core.client.tips;

import java.util.Date;
import java.util.logging.Logger;

import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.dom.client.MouseOutEvent;
import com.google.gwt.event.dom.client.MouseOutHandler;
import com.google.gwt.event.dom.client.MouseOverEvent;
import com.google.gwt.event.dom.client.MouseOverHandler;
import com.google.gwt.event.dom.client.TouchMoveEvent;
import com.google.gwt.event.dom.client.TouchMoveHandler;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.GXTLogConfiguration;
import com.sencha.gxt.core.client.Style.Anchor;
import com.sencha.gxt.core.client.Style.AnchorAlignment;
import com.sencha.gxt.core.client.Style.Side;
import com.sencha.gxt.core.client.dom.XDOM;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.gestures.LongPressOrTapGestureRecognizer;
import com.sencha.gxt.core.client.gestures.TouchData;
import com.sencha.gxt.core.client.gestures.TouchEventToGestureAdapter;
import com.sencha.gxt.core.client.util.Point;
import com.sencha.gxt.core.client.util.Region;
import com.sencha.gxt.core.client.util.Size;
import com.sencha.gxt.core.client.util.Util;
import com.sencha.gxt.core.shared.event.GroupingHandlerRegistration;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent.HideHandler;
import com.sencha.gxt.widget.core.client.event.XEvent;
import com.sencha.gxt.widget.core.client.tips.ToolTipConfig.ToolTipRenderer;

/**
 * A standard tooltip implementation for providing additional information when hovering over a target element.
 */
public class ToolTip extends Tip {

  private static Logger logger = Logger.getLogger(ToolTip.class.getName());

  private class Handler implements MouseOverHandler, MouseOutHandler, MouseMoveHandler, HideHandler,
      AttachEvent.Handler, FocusHandler, BlurHandler, KeyDownHandler, TouchMoveHandler {

    @Override
    public void onAttachOrDetach(AttachEvent event) {
      if (!event.isAttached()) {
        hide();
      }
    }

    @Override
    public void onBlur(BlurEvent event) {
    }

    @Override
    public void onFocus(FocusEvent event) {
    }

    @Override
    public void onHide(HideEvent event) {
      hide();
    }

    @Override
    public void onKeyDown(KeyDownEvent event) {
    }

    @Override
    public void onMouseMove(MouseMoveEvent event) {
      onTargetMouseMove(event);
    }

    @Override
    public void onMouseOut(MouseOutEvent event) {
      onTargetMouseOut(event);
    }

    @Override
    public void onMouseOver(MouseOverEvent event) {
      onTargetMouseOver(event);
    }

    @Override
    public void onTouchMove(TouchMoveEvent event) {
      onTargetTouchMove(event);
    }
  }

  protected XElement anchorEl;
  protected Timer dismissTimer;
  protected Timer hideTimer;
  protected Timer showTimer;
  protected Element target;
  protected Point targetXY = new Point(0, 0);
  protected SafeHtml title = SafeHtmlUtils.EMPTY_SAFE_HTML;
  protected SafeHtml body = SafeHtmlUtils.EMPTY_SAFE_HTML;

  private LongPressOrTapGestureRecognizer longPressOrTapGestureRecognizer;
  private GroupingHandlerRegistration handlerRegistration;
  private Date lastActive;

  /**
   * Creates a new tool tip.
   * 
   * @param target the target widget
   */
  public ToolTip(Widget target) {
    init();
    initTarget(target);
  }

  /**
   * Creates a new tool tip.
   *
   * @param appearance the appearance
   */
  public ToolTip(TipAppearance appearance) {
    super(appearance);
    init();
    initTarget(null);
  }

  /**
   * Creates a new tool tip.
   * 
   * @param target the target widget
   * @param appearance the appearance
   */
  public ToolTip(Widget target, TipAppearance appearance) {
    super(appearance);
    init();
    initTarget(target);
  }

  /**
   * Creates a new tool tip for the given target.
   * 
   * @param target the target widget
   * @param config the tool tip config
   */
  public ToolTip(Widget target, ToolTipConfig config) {
    init();
    updateConfig(config);
    initTarget(target);
  }

  /**
   * Creates a new tool tip.
   *
   * @param config the tool tip config
   */
  public ToolTip(ToolTipConfig config) {
    init();
    updateConfig(config);
    initTarget(null);
  }

  /**
   * Returns the quick show interval.
   * 
   * @return the quick show interval
   */
  public int getQuickShowInterval() {
    return quickShowInterval;
  }

  /**
   * Returns the current tool tip config.
   * 
   * @return the tool tip config
   */
  public ToolTipConfig getToolTipConfig() {
    return toolTipConfig;
  }

  @Override
  public void hide() {
    clearTimers();
    lastActive = new Date();
    super.hide();
  }

  /**
   * Binds the tool tip to the target widget. Allows a tool tip to switch the target widget.
   *
   * @param widget the target widget
   */
  public void initTarget(final Widget widget) {
    if (handlerRegistration != null) {
      handlerRegistration.removeHandler();
    }

    if (widget != null) {
      this.target = widget.getElement();

      Handler handler = new Handler();
      handlerRegistration = new GroupingHandlerRegistration();
      handlerRegistration.add(widget.addDomHandler(handler, MouseOverEvent.getType()));
      handlerRegistration.add(widget.addDomHandler(handler, MouseOutEvent.getType()));
      handlerRegistration.add(widget.addDomHandler(handler, MouseMoveEvent.getType()));
      handlerRegistration.add(widget.addDomHandler(handler, TouchMoveEvent.getType()));
      handlerRegistration.add(widget.addHandler(handler, HideEvent.getType()));
      handlerRegistration.add(widget.addHandler(handler, AttachEvent.getType()));

      // handles displaying tooltip on long press
      longPressOrTapGestureRecognizer = new LongPressOrTapGestureRecognizer() {
        @Override
        protected void onLongPress(TouchData touchData) {
          super.onLongPress(touchData);

          onTargetOver(touchData.getLastNativeEvent().<Event>cast());
        }

        @Override
        public boolean handleEnd(NativeEvent endEvent) {
          // cancel preventing default in this recognizer.
          cancel();

          return super.handleEnd(endEvent);
        }
      };

      // listen for touch events on the widget
      new TouchEventToGestureAdapter(widget, longPressOrTapGestureRecognizer);
    }
  }

  /**
   * Sets the quick show interval (defaults to 250).
   * 
   * @param quickShowInterval the quick show interval
   */
  public void setQuickShowInterval(int quickShowInterval) {
    this.quickShowInterval = quickShowInterval;
  }

  @Override
  public void show() {
    if (disabled) return;
    Side origAnchor = null;
    boolean origConstrainPosition = false;

    if (toolTipConfig.getAnchor() != null) {
      origAnchor = toolTipConfig.getAnchor();
      // attach for good measure
      // getTarget, so the elements can be properly measured/sized.
      showAt(getTargetXY(0));
      origConstrainPosition = this.constrainPosition;
      constrainPosition = false;
    }

    // go to the method below, it has some added benefits before going to super
    Point xy = getTargetXY(0);
    showAt(xy.getX(), xy.getY());

    if (toolTipConfig.getAnchor() != null) {
      anchorEl.show();
      constrainPosition = origConstrainPosition;
      toolTipConfig.setAnchor(origAnchor);
    } else {
      anchorEl.hide();
    }
  }

  @Override
  public void showAt(int x, int y) {
    if (disabled) return;
    lastActive = new Date();
    clearTimers();

    // retain the position set so that show() can be used to reposition the tip
    // subtract mouse offset b/c getTarget(xy) sums to to targetXY
    if (!toolTipConfig.isTrackMouse()) {
      targetXY.setX(x - toolTipConfig.getMouseOffsetX());
      targetXY.setY(y - toolTipConfig.getMouseOffsetY());
    }

    super.showAt(x, y);
    if (toolTipConfig.getAnchor() != null) {
      anchorEl.show();
      syncAnchor();
    } else {
      anchorEl.hide();
    }

    if (toolTipConfig.getDismissDelay() > 0 && toolTipConfig.isAutoHide() && !toolTipConfig.isCloseable()) {
      dismissTimer = new Timer() {
        @Override
        public void run() {
          hide();
        }
      };
      dismissTimer.schedule(toolTipConfig.getDismissDelay());
    }
  }

  /**
   * Updates the tool tip with the given config.
   * 
   * @param config the tool tip config
   */
  public void update(ToolTipConfig config) {
    updateConfig(config);
    if (isAttached()) {
      updateContent();
    }
    
    // When the tooltip text is updated reposition if the text runs the tooltip out of range/screen
    if (!showing && !config.isTrackMouse() && isAttached()) {
      show();
    }
  }

  protected void clearTimer(String timer) {
    if (timer.equals("hide")) {
      if (hideTimer != null) {
        hideTimer.cancel();
        hideTimer = null;
      }
    } else if (timer.equals("dismiss")) {
      if (dismissTimer != null) {
        dismissTimer.cancel();
        dismissTimer = null;
      }
    } else if (timer.equals("show")) {
      if (showTimer != null) {
        showTimer.cancel();
        showTimer = null;
      }
    }
  }

  protected void clearTimers() {
    clearTimer("show");
    clearTimer("dismiss");
    clearTimer("hide");
    if (longPressOrTapGestureRecognizer != null) {
      longPressOrTapGestureRecognizer.cancel();
    }
  }

  public void delayHide() {
    if (isAttached() && hideTimer == null && toolTipConfig.isAutoHide() && !toolTipConfig.isCloseable()) {
      if (toolTipConfig.getHideDelay() == 0) {
        hide();
        return;
      }
      hideTimer = new Timer() {
        @Override
        public void run() {
          hide();
        }
      };
      hideTimer.schedule(toolTipConfig.getHideDelay());
    }
  }

  protected void delayShow() {
    if (!isAttached() && showTimer == null) {
      if ((new Date().getTime() - lastActive.getTime()) < quickShowInterval) {
        show();
      } else {
        if (toolTipConfig.getShowDelay() > 0) {
          showTimer = new Timer() {
            @Override
            public void run() {
              show();
            }
          };
          showTimer.schedule(toolTipConfig.getShowDelay());
        } else {
          show();
        }
      }
    } else if (isAttached()) {
      show();
    }
  }

  protected AnchorAlignment getAnchorAlign() {
    switch (toolTipConfig.getAnchor()) {
      case TOP:
        return new AnchorAlignment(Anchor.TOP_LEFT, Anchor.BOTTOM_LEFT);
      case LEFT:
        return new AnchorAlignment(Anchor.TOP_LEFT, Anchor.TOP_RIGHT);
      case RIGHT:
        return new AnchorAlignment(Anchor.TOP_RIGHT, Anchor.TOP_LEFT);
      default:
        return new AnchorAlignment(Anchor.BOTTOM_LEFT, Anchor.TOP_LEFT);
    }
  }

  protected int[] getOffsets() {
    int[] offsets;
    if (toolTipConfig.isAnchorToTarget() && !toolTipConfig.isTrackMouse()) {
      switch (toolTipConfig.getAnchor()) {
        case TOP:
          offsets = new int[] {0, 9};
          break;
        case BOTTOM:
          offsets = new int[] {0, -9};
          break;
        case RIGHT:
          offsets = new int[] {-9, 0};
          break;
        default:
          offsets = new int[] {9, 0};
          break;
      }

    } else {
      int anchorOffset = toolTipConfig.getAnchorOffset();
      switch (toolTipConfig.getAnchor()) {
        case TOP:
          offsets = new int[] {-15 - anchorOffset, 30};
          break;
        case BOTTOM:
          offsets = new int[] {-19 - anchorOffset, -13 - getElement().getOffsetHeight()};
          break;
        case RIGHT:
          offsets = new int[] {-15 - getElement().getOffsetWidth(), -13 - anchorOffset};
          break;
        default:
          offsets = new int[] {25, -13 - anchorOffset};
          break;
      }

    }
    offsets[0] += toolTipConfig.getMouseOffsetX();
    offsets[1] += toolTipConfig.getMouseOffsetY();

    return offsets;
  }

  /**
   * Creates a new tool tip.
   */
  protected void init() {
    toolTipConfig = new ToolTipConfig();
    lastActive = new Date();
    monitorWindowResize = true;

    anchorEl = Document.get().createDivElement().cast();
    getAppearance().applyAnchorStyle(anchorEl);
    getElement().appendChild(anchorEl);
  }

  @Override
  protected void onAfterFirstAttach() {
    super.onAfterFirstAttach();
    anchorEl.setZIndex(getElement().getZIndex() + 1);
  }

  protected void onMouseMove(Event event) {
    targetXY = event.<XEvent> cast().getXY();
    if (isAttached() && toolTipConfig.isTrackMouse()) {
      Side origAnchor = toolTipConfig.getAnchor();
      Point p = getTargetXY(0);
      toolTipConfig.setAnchor(origAnchor);
      if (constrainPosition) {
        p = getElement().adjustForConstraints(p);
      }
      super.showAt(p.getX(), p.getY());
    }
  }

  protected void onTargetMouseMove(MouseMoveEvent event) {
    onMouseMove(event.getNativeEvent().<Event> cast());
  }

  protected void onTargetMouseOut(MouseOutEvent event) {
    Element source = event.getNativeEvent().getEventTarget().cast();
    Element to = event.getNativeEvent().getRelatedEventTarget().cast();
    if (source != null && (to == null || !source.isOrHasChild(to.<Element> cast()))) {
      onTargetOut(event.getNativeEvent().<Event> cast());
    }
  }

  protected void onTargetMouseOver(MouseOverEvent event) {
    Element source = event.getNativeEvent().getEventTarget().cast();
    EventTarget from = event.getNativeEvent().getRelatedEventTarget();
    if (source != null && (from == null || !source.isOrHasChild(from.<Element> cast()))) {
      onTargetOver(event.getNativeEvent().<Event>cast());
    }
  }

  protected void onTargetOut(Event ce) {
    if (disabled) {
      return;
    }
    clearTimer("show");
    delayHide();
  }

  protected void onTargetOver(Event ce) {
    if (disabled || !target.isOrHasChild(ce.getEventTarget().<Element> cast())) {
      return;
    }
    clearTimer("hide");
    targetXY = ce.<XEvent>cast().getXY();
    delayShow();
  }

  protected void onTargetTouchMove(TouchMoveEvent event) {
    onMouseMove(event.getNativeEvent().<Event>cast());
  }

  @Override
  protected void onWindowResize(int width, int height) {
    super.onWindowResize(width, height);
    // this can only be reached if the tooltip is already visible, show it again
    // to sync anchor
    show();
  }

  protected void syncAnchor() {
    Anchor anchorPos, targetPos;
    final int offsetX, offsetY;
    int anchorOffset = toolTipConfig.getAnchorOffset();
    switch (toolTipConfig.getAnchor()) {
      case TOP:
        anchorPos = Anchor.BOTTOM;
        targetPos = Anchor.TOP_LEFT;
        offsetX = 20 + anchorOffset;
        offsetY = 2;
        break;
      case RIGHT:
        anchorPos = Anchor.LEFT;
        targetPos = Anchor.TOP_RIGHT;
        offsetX = -2;
        offsetY = 11 + anchorOffset;
        break;
      case BOTTOM:
        anchorPos = Anchor.TOP;
        targetPos = Anchor.BOTTOM_LEFT;
        offsetX = 20 + anchorOffset;
        offsetY = -2;
        break;
      default:
        anchorPos = Anchor.RIGHT;
        targetPos = Anchor.TOP_LEFT;
        offsetX = 2;
        offsetY = 11 + anchorOffset;
        break;
    }
    anchorEl.alignTo(getElement(), new AnchorAlignment(anchorPos, targetPos, false), offsetX, offsetY);
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  protected void updateContent() {
    SafeHtml bh = SafeHtmlUtils.EMPTY_SAFE_HTML;

    if (toolTipConfig.getRenderer() != null) {
      Object data = toolTipConfig.getData();
      ToolTipRenderer r = toolTipConfig.getRenderer();
      bh = r.renderToolTip(data);
    } else {
      bh = body;
    }

    if (bh == SafeHtmlUtils.EMPTY_SAFE_HTML) {
      getAppearance().updateContent(getElement(), title, Util.NBSP_SAFE_HTML);
    } else {
      getAppearance().updateContent(getElement(), title, bh);
    }
  }

  /**
   * Add to tooltip anchor to the range for overall measurement calculations.
   * 
   * @param offsets return what was given with added anchor amount depending on side. Adds a couple pixels for spacing.
   * @return offsets
   */
  private int[] getAddAnchorToRange(int[] offsets) {
    Region ar = XElement.as(anchorEl).getRegion();
    // Note things are inverted from screen to code
    switch (toolTipConfig.getAnchor()) {
      case TOP:
        offsets[0] = 0;
        offsets[1] += ar.getBottom() - ar.getTop() + 2;
        break;
      case BOTTOM:
        offsets[0] = 0;
        offsets[1] += ar.getBottom() - ar.getTop() + 2;
        break;
      case RIGHT:
        offsets[0] = ar.getRight() - ar.getLeft() + 2;
        offsets[1] = 0;
        break;
      case LEFT:
        offsets[0] = ar.getRight() - ar.getLeft() + 2;
        offsets[1] = 0;
        break;
    }
    return offsets;
  }

  protected Point getTargetXY(int targetCounter) {
    if (toolTipConfig.getAnchor() != null) {
      targetCounter++;
      int[] offsets = getOffsets();

      // base pin point, depending on anchor side designation
      Point xy = targetXY;
      if (toolTipConfig.isAnchorToTarget() && !toolTipConfig.isTrackMouse()) {
        // Note: Don't set the scroll offset in x and y
        xy = getElement().getAlignToXY(target, getAnchorAlign(), 0, 0);
      }

      int dw = XDOM.getViewWidth(false);
      int dh = XDOM.getViewHeight(false);
      int scrollX = XDOM.getBodyScrollLeft();
      int scrollY = XDOM.getBodyScrollTop();

      int[] axy = new int[] {xy.getX() + offsets[0], xy.getY() + offsets[1]};

      // getElement().getAlignToXY adds scroll offset, this takes it away, b/c Tip showAt translates/adds it again
      // This takes out the scroll offset given by 'getElement().getAlignToXY'
      if (toolTipConfig.isAnchorToTarget() && !toolTipConfig.isTrackMouse()) {
        axy[0] -= XDOM.getBodyScrollLeft();
        axy[1] -= XDOM.getBodyScrollTop();
        
        if (scrollY > 0) {
          // bottom offset correct is double
          scrollY -= XDOM.getBodyScrollTop() * 2;
        }
      }

      Size sz = getElement().getSize();
      Region r = XElement.as(target).getRegion();

      // Anchor range is not factored in on overall size, which overall size has to be used to flip to another side
      // Note, things are inverted from display to programattical code like RIGHT means screen display LEFT
      offsets = getAddAnchorToRange(offsets);

      // When tip is screen top hits the ceiling out of range/site, this is the offset that fixes it.  
      if (toolTipConfig.isTrackMouse() && toolTipConfig.getAnchor() == Side.BOTTOM) {
          offsets[1] = XDOM.getBodyScrollTop() * 2;
      }
      
      // When tip is screen bottom during a scroll needs an offset correction so not to flip to early
      if (toolTipConfig.isTrackMouse() && toolTipConfig.getAnchor() == Side.TOP) {
        offsets[1] -= XDOM.getBodyScrollTop() * 2;
      }
      
      if (GXTLogConfiguration.loggingIsEnabled()) {
        String s = "dw=" + dw + ",dh=" + dh + " ";
        s += "scroll=" + scrollX + ",=" + scrollY + " ";
        s += "offsets=" + offsets[0] + "," + offsets[1] + " ";
        s += "axy=" + axy[0] + "," + axy[1] + " ";
        s += "sz=" + sz + " ";
        s += "r=" + r + " ";
        s += "isTrackMouse=" + toolTipConfig.isTrackMouse() + " ";
        s += "targetXY=" + targetXY + " ";
        logger.finest(s);

        if (toolTipConfig.getAnchor() == Side.TOP) {
          String s1 = "TOP CALC: " + "sz.getHeight()=" + sz.getHeight() + " " + "+ offsets[1]=" + offsets[1] + " "
              + "+ scrollY " + scrollY + " " + "< dh=" + dh + " " + "- r.getBottom()=" + r.getBottom();
          String s2 = "TOP CALC: " + (sz.getHeight() + offsets[1] + scrollY) + " < " + (dh - r.getBottom()) + " ";
          String s3 = "TOP CALC: " + (sz.getHeight() + offsets[1] + scrollY < dh - r.getBottom());
          logger.finest(s1);
          logger.finest(s2);
          logger.finest(s3);
        }

        if (toolTipConfig.getAnchor() == Side.BOTTOM) {
          String s1 = "BOTTOM CALC: sz.getHeight()=" + sz.getHeight() + " + offsets[1]=" + offsets[1] + " - scrollY="
              + scrollY + " < r.getTop()=" + r.getTop();
          String s2 = "BOTTOM CALC: " + (sz.getHeight() + offsets[1] - scrollY) + " <  " + r.getTop();
          String s3 = "BOTTOM CALC: " + (sz.getHeight() + offsets[1] - scrollY < r.getTop());
          logger.finest(s1);
          logger.finest(s2);
          logger.finest(s3);
        }

        if (toolTipConfig.getAnchor() == Side.LEFT) {
          String s1 = "LEFT CALC: sz.getWidth()=" + sz.getWidth() + " + offsets[0]=" + offsets[0] + " + scrollX="
              + scrollX + " < dw=" + dw + " - r.getRight()=" + r.getRight();
          String s2 = "LEFT CALC: " + (sz.getWidth() + offsets[0] + scrollX) + " < " + (dw - r.getRight());
          String s3 = "LEFT CALC: " + (sz.getWidth() + offsets[0] + scrollX < dw - r.getRight());
          logger.finest(s1);
          logger.finest(s2);
          logger.finest(s3);
        }

        if (toolTipConfig.getAnchor() == Side.RIGHT) {
          String s1 = "RIGHT CALC: sz.getWidth()=" + sz.getWidth() + " + offsets[0]=" + offsets[0] + " + scrollX="
              + scrollX + " < r.getLeft()=" + r.getLeft();
          String s2 = "RIGHT CALC: " + (sz.getWidth() + offsets[0] + scrollX) + " < " + r.getLeft();
          String s3 = "RIGHT CALC: " + (sz.getWidth() + offsets[0] + scrollX < r.getLeft());
          logger.finest(s1);
          logger.finest(s2);
          logger.finest(s3);
        }
      }

      // if we are not inside valid ranges we try to switch the anchor
      if (!((toolTipConfig.getAnchor() == Side.TOP && (sz.getHeight() + offsets[1] + scrollY < dh - r.getBottom()))
          || (toolTipConfig.getAnchor() == Side.RIGHT && (sz.getWidth() + offsets[0] + scrollX < r.getLeft()))
          || (toolTipConfig.getAnchor() == Side.BOTTOM && (sz.getHeight() + offsets[1] - scrollY < r.getTop())) 
          || (toolTipConfig.getAnchor() == Side.LEFT && (sz.getWidth() + offsets[0] - scrollX < dw - r.getRight())))
          && targetCounter < 4) {
        targetCounter++;

        if (sz.getWidth() + offsets[0] + scrollX < r.getLeft()) {
          toolTipConfig.setAnchor(Side.RIGHT);
          return getTargetXY(targetCounter);
        }
        if (sz.getWidth() + offsets[0] + scrollX < r.getLeft()) {
          toolTipConfig.setAnchor(Side.LEFT);
          return getTargetXY(targetCounter);
        }
        if (sz.getHeight() + offsets[1] + scrollY < dh - r.getBottom()) {
          toolTipConfig.setAnchor(Side.TOP);
          return getTargetXY(targetCounter);
        }
        if (sz.getHeight() + offsets[1] + scrollY < r.getTop()) {
          toolTipConfig.setAnchor(Side.BOTTOM);
          return getTargetXY(targetCounter);
        }
      }

      // sets the direction of the anchor <^>
      if (toolTipConfig.isAnchorArrow()) {
        getAppearance().applyAnchorDirectionStyle(anchorEl, toolTipConfig.getAnchor());
      }

      // reset recursion check counter
      targetCounter = 0;
      return new Point(axy[0], axy[1]);

    } else {
      int x = targetXY.getX() + toolTipConfig.getMouseOffsetX();
      int y = targetXY.getY() + toolTipConfig.getMouseOffsetY();

      return new Point(x, y);
    }
  }

  private void updateConfig(ToolTipConfig config) {
    this.toolTipConfig = config;
    if (!config.isEnabled()) {
      clearTimers();
      hide();
    }
    setMinWidth(config.getMinWidth());
    setMaxWidth(config.getMaxWidth());
    setClosable(config.isCloseable());
    body = config.getBody();
    title = config.getTitle();
  }

}
