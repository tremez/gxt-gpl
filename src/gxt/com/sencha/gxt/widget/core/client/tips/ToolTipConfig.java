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

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.sencha.gxt.core.client.Style.Side;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.util.HasUiAttributes;

/**
 * Configuration information for a tool tip.
 */
public class ToolTipConfig implements HasUiAttributes {

  public interface ToolTipRenderer<T> extends XTemplates {
    SafeHtml renderToolTip(T data);
  }

  private Side anchor;
  private boolean anchorArrowEnabled = true;
  private int anchorOffset = 0;
  private boolean anchorToTarget = true;
  private boolean autoHide = true;
  private int dismissDelay = 5000;
  private boolean enabled = true;
  private int hideDelay = 200;
  private int maxWidth = 300;
  private int minWidth = 40;
  private int mouseOffsetX = 15;
  private int mouseOffsetY = 18;
  private Object data;
  private int showDelay = 500;
  private ToolTipRenderer<?> renderer;
  private SafeHtml body = SafeHtmlUtils.EMPTY_SAFE_HTML;
  private SafeHtml title = SafeHtmlUtils.EMPTY_SAFE_HTML;
  private boolean trackMouse;
  private boolean closeable;

  /**
   * Creates a new tool tip config.
   */
  public ToolTipConfig() {
  }

  /**
   * Creates a new tool tip config with the given body as text.
   *
   * @param text the tool tip body as text
   */
  public ToolTipConfig(String text) {
    this.setBody(text);
  }

  /**
   * Creates a new tool tip config with the given body as html.
   *
   * @param html the tool tip body as html
   */
  public ToolTipConfig(SafeHtml html) {
    this.setBody(html);
  }

  /**
   * Creates a new tool tip config with the given title and body text.
   *
   * @param titleText the tool tip title as text
   * @param bodyText the tool tip body as text
   */
  public ToolTipConfig(String titleText, String bodyText) {
    this.setTitle(titleText);
    this.setBody(bodyText);
  }

  /**
   * Creates a new tool tip config with the given title and body html.
   *
   * @param titleHtml the tool tip title as html
   * @param bodyHtml the tool tip body as html
   */
  public ToolTipConfig(SafeHtml titleHtml, SafeHtml bodyHtml) {
    setTitle(titleHtml);
    setBody(bodyHtml);
  }

  /**
   * Returns the anchor position.
   *
   * @return the anchor position
   */
  public Side getAnchor() {
    return anchor;
  }

  /**
   * Returns the distance in pixels of the tooltip and target element.
   *
   * @return the offset
   */
  public int getAnchorOffset() {
    return anchorOffset;
  }

  /**
   * Returns the tool tip body.
   *
   * @return the body
   */
  public SafeHtml getBody() {
    return body;
  }

  /**
   * Returns the data for the renderer.
   *
   * @return the data
   */
  public Object getData() {
    return data;
  }

  /**
   * Returns the dismiss delay.
   *
   * @return the dismiss delay
   */
  public int getDismissDelay() {
    return dismissDelay;
  }

  /**
   * Returns the hide delay in milliseconds.
   *
   * @return the delay
   */
  public int getHideDelay() {
    return hideDelay;
  }

  /**
   * Returns the tooltip's maximum width.
   *
   * @return the maximum width
   */
  public int getMaxWidth() {
    return maxWidth;
  }

  /**
   * Returns the tooltip's minimum width.
   *
   * @return the minimum width
   */
  public int getMinWidth() {
    return minWidth;
  }

  /**
   * Gets the X offset from the mouse position where the tooltip should be shown
   * @return the X offset from the mouse position from the tooltip
   */
  public int getMouseOffsetX() {
    return mouseOffsetX;
  }

  /**
   * Gets the Y offset from the mouse position where the tooltip should be shown
   * @return the Y offset from the mouse position from the tooltip
   */
  public int getMouseOffsetY() {
    return mouseOffsetY;
  }

  /**
   * Returns the renderer.
   *
   * @return the template
   */
  public ToolTipRenderer<?> getRenderer() {
    return renderer;
  }

  /**
   * Returns the show delay in milliseconds.
   *
   * @return the delay
   */
  public int getShowDelay() {
    return showDelay;
  }

  /**
   * Returns the tool tip title.
   *
   * @return the title
   */
  public SafeHtml getTitle() {
    return title;
  }

  /**
   * Returns the anchor arrow enabled.
   *
   * @return enabled true or false
   */
  public boolean isAnchorArrow() {
    return anchorArrowEnabled;
  }

  /**
   * Returns true if the tooltip is anchored to the target.
   *
   * @return true if anchored
   */
  public boolean isAnchorToTarget() {
    return anchorToTarget;
  }

  /**
   * Returns true if auto hide is enabled.
   *
   * @return the auto hide state
   */
  public boolean isAutoHide() {
    return autoHide;
  }

  /**
   * Returns true if the tip is closable.
   *
   * @return the closable state
   */
  public boolean isCloseable() {
    return closeable;
  }

  /**
   * Returns true if the tool tip is enabled.
   *
   * @return true for enabled
   */
  public boolean isEnabled() {
    return enabled;
  }

  /**
   * Returns true if mouse tracking is enabled.
   *
   * @return the mouse track state
   */
  public boolean isTrackMouse() {
    return trackMouse;
  }

  /**
   * Sets the anchor position (defaults to "bottom").
   *
   * @param anchor the anchor position (top, bottom, left, right)
   */
  public void setAnchor(Side anchor) {
    this.anchor = anchor;
  }

  /**
   * Turn on and off the anchor arrow. The arrow points from the tip to the target. Default is enabled true.
   *
   * @param anchorArrowEnabled enables the arrow
   */
  public void setAnchorArrow(boolean anchorArrowEnabled) {
    this.anchorArrowEnabled = anchorArrowEnabled;
  }

  /**
   * A numeric pixel value used to offset the default position of the anchor
   * arrow (defaults to 0). When the anchor position is on the top or bottom of
   * the tooltip, <code>anchorOffset</code> will be used as a horizontal offset.
   * Likewise, when the anchor position is on the left or right side,
   * <code>anchorOffset</code> will be used as a vertical offset.
   *
   *
   * @param anchorOffset the offset in pixels
   */
  public void setAnchorOffset(int anchorOffset) {
    this.anchorOffset = anchorOffset;
  }

  /**
   * True to anchor the tooltip to the target element, false to anchor it
   * relative to the mouse coordinates (defaults to true).
   *
   * @param anchorToTarget true to anchor the tooltip to the target element
   */
  public void setAnchorToTarget(boolean anchorToTarget) {
    this.anchorToTarget = anchorToTarget;
  }

  /**
   * True to automatically hide the tooltip after the mouse exits the target
   * element or after the {@link #dismissDelay} has expired if set (defaults to
   * true).
   *
   * @param autoHide the auto hide state
   */
  public void setAutoHide(boolean autoHide) {
    this.autoHide = autoHide;
  }

  /**
   * Sets the tool tip body as html.
   *
   * @param html the tip body html
   */
  public void setBody(SafeHtml html) {
    this.body = html;
  }

  /**
   * Sets the tool tip body as text.
   *
   * @param text the tip body text
   */
  public void setBody(String text) {
    this.body = SafeHtmlUtils.fromString(text);
  }

  /**
   * True to render a close tool button into the tooltip header (defaults to
   * false).
   *
   * @param closeable the closable state
   */
  public void setCloseable(boolean closeable) {
    this.closeable = closeable;
  }

  /**
   * The parameters to be used when a custom a {@link #renderer} is specified.
   *
   * @param data the data
   */
  public void setData(Object data) {
    this.data = data;
  }

  /**
   * Delay in milliseconds before the tooltip automatically hides (defaults to
   * 5000). To disable automatic hiding, set dismissDelay = 0.
   *
   * @param dismissDelay the dismiss delay
   */
  public void setDismissDelay(int dismissDelay) {
    this.dismissDelay = dismissDelay;
  }

  /**
   * Sets whether the tool tip is enabled (defaults to true).
   *
   * @param enabled true to enable
   */
  public void setEnabled(boolean enabled) {
    this.enabled = enabled;
  }

  /**
   * Delay in milliseconds after the mouse exits the target element but before
   * the tooltip actually hides (defaults to 200). Set to 0 for the tooltip to
   * hide immediately.
   *
   * @param hideDelay the hide delay
   */
  public void setHideDelay(int hideDelay) {
    this.hideDelay = hideDelay;
  }

  /**
   * Sets the tooltip's maximum width (defaults to 300).
   *
   * @param maxWidth the maximum width in pixels
   */
  public void setMaxWidth(int maxWidth) {
    this.maxWidth = maxWidth;
  }

  /**
   * Sets the tooltip's minimum width (defaults to 40).
   *
   * @param minWidth the minimum width
   */
  public void setMinWidth(int minWidth) {
    this.minWidth = minWidth;
  }

  /**
   * Sets the X offset from the mouse position where the tooltip should be shown
   * (defaults to 15)
   *
   * @param x the x axis offset
   */
  public void setMouseOffsetX(int x) {
    this.mouseOffsetX = x;
  }

  /**
   * Sets the Y offset from the mouse position where the tooltip should be shown
   * (defaults to 18)
   *
   * @param y the y axis offset
   */
  public void setMouseOffsetY(int y) {
    this.mouseOffsetY = y;
  }

  /**
   * A optional renderer to be used to render the tool tip. The
   * {@link #setData(Object)} will be applied to the template.
   *
   * @param renderer the renderer
   */
  public void setRenderer(ToolTipRenderer<?> renderer) {
    this.renderer = renderer;
  }

  /**
   * Delay in milliseconds before the tooltip displays after the mouse enters
   * the target element (defaults to 500).
   *
   * @param showDelay the show delay
   */
  public void setShowDelay(int showDelay) {
    this.showDelay = showDelay;
  }

  /**
   * Sets the tool tip title as html.
   *
   * @param html the title html
   */
  public void setTitle(SafeHtml html) {
    this.title = html;
  }

  /**
   * Sets the tool tip title as text
   * 
   * @param text the title text
   */
  public void setTitle(String text) {
    this.title = SafeHtmlUtils.fromString(text);
  }

  /**
   * True to have the tooltip follow the mouse as it moves over the target
   * element (defaults to false).
   *
   * @param trackMouse the track mouse state
   */
  public void setTrackMouse(boolean trackMouse) {
    this.trackMouse = trackMouse;
  }

}
