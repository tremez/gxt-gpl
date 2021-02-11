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
package com.sencha.gxt.widget.core.client.button;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.sencha.gxt.core.client.dom.XDOM;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.util.Size;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;

public class ButtonGroup extends SimpleContainer {

  public interface ButtonGroupAppearance {
    void render(SafeHtmlBuilder sb);

    void setHeading(XElement parent, SafeHtml html);
    
    void onHideHeader(XElement parent, boolean hide);

    XElement getHeaderElement(XElement parent);

    XElement getContentElem(XElement parent);

    int getFrameHeight(XElement parent);

    int getFrameWidth(XElement parent);
  }

  private final ButtonGroupAppearance appearance;
  private SafeHtml heading = SafeHtmlUtils.EMPTY_SAFE_HTML;

  public ButtonGroup() {
    this(GWT.<ButtonGroupAppearance> create(ButtonGroupAppearance.class));
  }

  public ButtonGroup(ButtonGroupAppearance appearance) {
    super(true);
    this.appearance = appearance;

    setDeferHeight(true);

    SafeHtmlBuilder builder = new SafeHtmlBuilder();
    this.appearance.render(builder);

    setElement((Element) XDOM.create(builder.toSafeHtml()));

//    addStyleName("x-toolbar-mark");
  }

  /**
   * Returns the heading html.
   *
   * @return the heading html
   */
  public SafeHtml getHeading() {
    return heading;
  }

  /**
   * Sets the heading html.
   *
   * @param html the heading html
   */
  public void setHeading(SafeHtml html) {
    this.heading = html;
    appearance.setHeading(getElement(), heading);
  }

  /**
   * Sets the heading text.
   *
   * Text that contains reserved html characters will be escaped.
   *
   * @param text the text
   */
  public void setHeading(String text) {
    setHeading(SafeHtmlUtils.fromString(text));
  }

  public ButtonGroupAppearance getAppearance() {
    return appearance;
  }

  public void setHeaderVisible(boolean visible) {
    appearance.onHideHeader(getElement(), !visible);
  }

  @Override
  protected void onResize(int width, int height) {
    Size frameSize = getFrameSize();

    if (isAutoWidth()) {
      getContainerTarget().getStyle().clearWidth();
    } else {
      width -= frameSize.getWidth();
      getContainerTarget().setWidth(width - frameSize.getWidth(), true);

    }

    if (isAutoHeight()) {
      getContainerTarget().getStyle().clearHeight();
    } else {
      height -= frameSize.getHeight();
      height -= appearance.getHeaderElement(getElement()).getOffsetHeight();
      getContainerTarget().setHeight(height - frameSize.getHeight(), true);
    }

    super.onResize(width, height);
  }

  @Override
  protected XElement getContainerTarget() {
    return appearance.getContentElem(getElement());
  }

  protected Size getFrameSize() {
    return new Size(appearance.getFrameWidth(getElement()), appearance.getFrameHeight(getElement()));
  }

}
