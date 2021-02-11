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
package com.sencha.gxt.theme.base.client.frame;

import com.google.gwt.safecss.shared.SafeStyles;
import com.google.gwt.safecss.shared.SafeStylesUtils;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.util.Size;

/**
 * Defines the interface for classes which "frame" a given element and support
 * both a header and content section. In general, frames provide support for
 * rounded corners.
 * 
 * @see TableFrame
 * @see DivFrame
 */
public interface Frame {

  /**
   * Render options for Frames.
   */
  public static class FrameOptions {

    private static SafeStyles EMPTY = SafeStylesUtils.fromTrustedString("");

    private String tabIndex = "";
    private SafeStyles frameStyle;
    private String frameClasses = "";

    /**
     * Creates a new frame options instance.
     */
    public FrameOptions() {

    }

    /**
     * Creates a new frame options instance. In many cases, the Frame will be
     * the "outer" or "root" element and will need to have classes and styles
     * defined by content being "wrapped" by the frame.
     * 
     * @param tabIndex the tab index to be applied to frame
     * @param frameClasses a space separated list of CSS class names to be
     *          applied to frame
     * @param frameStyle a safe styles instance to be applied to the frame
     */
    public FrameOptions(Integer tabIndex, String frameClasses, SafeStyles frameStyle) {
      this.tabIndex = tabIndex == null ? "" : tabIndex.toString();
      this.frameClasses = frameClasses;
      this.frameStyle = frameStyle;
    }

    /**
     * Returns the frame classes.
     * 
     * @return the space separated list of CSS class names
     */
    public String getFrameClasses() {
      return frameClasses;
    }

    /**
     * Returns the frame style.
     * 
     * @return the style
     */
    public SafeStyles getFrameStyle() {
      if (frameStyle == null) {
        return EMPTY;
      }
      return frameStyle;
    }

    /**
     * Returns the tab index.
     * 
     * @return the tab index or "" if not specified
     */
    public String getTabIndex() {
      return tabIndex;
    }

    public void setFrameClasses(String frameClasses) {
      this.frameClasses = frameClasses;
    }

    public void setFrameStyle(SafeStyles frameStyle) {
      this.frameStyle = frameStyle;
    }

    public void setTabIndex(String tabIndex) {
      this.tabIndex = tabIndex;
    }

  }

  public static final FrameOptions EMPTY_FRAME = new FrameOptions();

  XElement getContentElem(XElement parent);

  /**
   * Returns the frame height and width. The parent element may be null in cases
   * where the method is called before the frame is rendered.
   * 
   * @param parent the parent element or null
   * @return the frame size
   */
  Size getFrameSize(XElement parent);

  XElement getHeaderElem(XElement parent);

  void onFocus(XElement parent, boolean focus);

  void onHideHeader(XElement parent, boolean hide);

  void onOver(XElement parent, boolean over);

  void onPress(XElement parent, boolean pressed);

  String overClass();

  String pressedClass();

  void render(SafeHtmlBuilder builder, FrameOptions options, SafeHtml content);

}
