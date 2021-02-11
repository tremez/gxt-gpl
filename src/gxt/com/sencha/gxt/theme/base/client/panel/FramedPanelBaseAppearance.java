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
package com.sencha.gxt.theme.base.client.panel;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.core.client.Style.Side;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.resources.ThemeStyles;
import com.sencha.gxt.theme.base.client.frame.CollapsibleFrame;
import com.sencha.gxt.theme.base.client.frame.Frame;
import com.sencha.gxt.theme.base.client.frame.NestedDivFrame.NestedDivFrameResources;
import com.sencha.gxt.widget.core.client.FramedPanel.FramedPanelAppearance;

public abstract class FramedPanelBaseAppearance extends ContentPanelBaseAppearance implements FramedPanelAppearance {

  public interface FramedPanelDivFrameResources extends NestedDivFrameResources {

  }

  public interface FramedPanelTemplate extends ContentPanelTemplate {
    @XTemplate(source = "FramedPanel.html")
    SafeHtml render(ContentPanelStyle style);
  }

  protected CollapsibleFrame frame;


  public FramedPanelBaseAppearance(ContentPanelResources resources, FramedPanelTemplate template, CollapsibleFrame frame) {
    super(resources, template);

    this.frame = frame;
  }

  @Override
  public XElement getBodyWrap(XElement parent) {
    return frame.getCollapseElem(parent);
  }

  @Override
  public XElement getContentElem(XElement parent) {
    return parent.selectNode("." + style.body());
  }

  @Override
  public int getFrameHeight(XElement parent) {
    int h = frame.getFrameSize(parent).getHeight();
    h += frame.getContentElem(parent).getFrameSize().getHeight();
    return h;
  }

  @Override
  public int getFrameWidth(XElement parent) {
    int w = frame.getFrameSize(parent).getWidth();

    XElement content = getContentElem(parent);
    w += content.getFrameWidth(Side.LEFT, Side.RIGHT);

    return w;
  }

  @Override
  public XElement getHeaderElem(XElement parent) {
    return frame.getHeaderElem(parent);
  }

  @Override
  public void onBodyBorder(XElement parent, boolean border) {
    getContentElem(parent).setClassName(ThemeStyles.get().style().border(), border);
  }

  @Override
  public void onHideHeader(XElement parent, boolean hide) {
    parent.setClassName("noheader", hide);
    frame.onHideHeader(parent, hide);
  }

  @Override
  public void render(SafeHtmlBuilder sb) {
    frame.render(sb, Frame.EMPTY_FRAME, template.render(style));
  }

}
