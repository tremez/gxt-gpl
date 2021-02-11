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
package com.sencha.gxt.theme.base.client.tips;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.sencha.gxt.core.client.Style.Side;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.resources.StyleInjectorHelper;
import com.sencha.gxt.theme.base.client.frame.Frame;
import com.sencha.gxt.theme.base.client.frame.NestedDivFrame;
import com.sencha.gxt.theme.base.client.frame.NestedDivFrame.NestedDivFrameResources;
import com.sencha.gxt.theme.base.client.frame.NestedDivFrame.NestedDivFrameStyle;
import com.sencha.gxt.widget.core.client.tips.Tip.TipAppearance;

public class TipDefaultAppearance implements TipAppearance {

  public interface TipDefaultTemplate extends XTemplates {

    @XTemplate(source = "TipDefault.html")
    SafeHtml render(TipStyle style);

  }

  public interface TipDivFrameResources extends ClientBundle, NestedDivFrameResources {

    ImageResource anchorBottom();

    ImageResource anchorLeft();

    ImageResource anchorRight();

    ImageResource anchorTop();

    @Source("background.gif")
    @ImageOptions(repeatStyle = RepeatStyle.Both)
    ImageResource background();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Override
    ImageResource bottomBorder();

    @Override
    @ImageOptions(repeatStyle = RepeatStyle.Both)
    ImageResource bottomLeftBorder();

    @Override
    @ImageOptions(repeatStyle = RepeatStyle.Both)
    ImageResource bottomRightBorder();

    @ImageOptions(repeatStyle = RepeatStyle.Vertical)
    @Override
    ImageResource leftBorder();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Override
    ImageResource rightBorder();

    @Source({"com/sencha/gxt/theme/base/client/frame/NestedDivFrame.gss", "TipDivFrame.gss"})
    @Override
    TipNestedDivFrameStyle style();

    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    @Override
    ImageResource topBorder();

    @Override
    ImageResource topLeftBorder();

    @Override
    @ImageOptions(repeatStyle = RepeatStyle.Both)
    ImageResource topRightBorder();

  }

  public interface TipNestedDivFrameStyle extends NestedDivFrameStyle {
  }

  public interface TipResources extends ClientBundle {

    ImageResource anchorBottom();

    ImageResource anchorLeft();

    ImageResource anchorRight();

    ImageResource anchorTop();

    @Source("TipDefault.gss")
    TipStyle style();

  }

  public interface TipStyle extends CssResource {

    String anchor();

    String anchorBottom();

    String anchorLeft();

    String anchorRight();

    String anchorTop();

    String heading();

    String text();

    String tools();

    String tip();
    
  }

  protected final TipResources resources;
  protected final TipStyle style;
  protected TipDefaultTemplate template;

  protected Frame frame;

  public TipDefaultAppearance() {
    this(GWT.<TipResources> create(TipResources.class));
  }

  public TipDefaultAppearance(TipResources resources) {
    this(resources, GWT.<TipDefaultTemplate> create(TipDefaultTemplate.class));
  }

  public TipDefaultAppearance(TipResources resources, TipDefaultTemplate template) {
    this.resources = resources;
    this.style = resources.style();

    StyleInjectorHelper.ensureInjected(style, true);

    this.template = template;

    frame = new NestedDivFrame(GWT.<TipDivFrameResources> create(TipDivFrameResources.class));
  }

  @Override
  public void applyAnchorDirectionStyle(XElement anchorEl, Side anchor) {
    anchorEl.setClassName(style.anchorTop(), anchor == Side.TOP);
    anchorEl.setClassName(style.anchorBottom(), anchor == Side.BOTTOM);
    anchorEl.setClassName(style.anchorRight(), anchor == Side.RIGHT);
    anchorEl.setClassName(style.anchorLeft(), anchor == Side.LEFT);
  }

  @Override
  public void applyAnchorStyle(XElement anchorEl) {
    anchorEl.addClassName(style.anchor());
  }

  public XElement getHeaderElement(XElement parent) {
    return parent.selectNode("." + style.heading());
  }

  @Override
  public XElement getBodyElement(XElement parent) {
    return parent.selectNode("." + style.text());
  }

  @Override
  public XElement getToolsElement(XElement parent) {
    return parent.selectNode("." + style.tools());
  }

  @Override
  public void removeAnchorStyle(XElement anchorEl) {
    anchorEl.removeClassName(style.anchor());
  }

  @Override
  public void render(SafeHtmlBuilder sb) {
    frame.render(sb, Frame.EMPTY_FRAME, template.render(style));
  }

  protected TipDivFrameResources getDivFrameResources() {
    return GWT.create(TipDivFrameResources.class);
  }

  @Override
  public void updateContent(XElement parent, SafeHtml title, SafeHtml body) {
    XElement header = getHeaderElement(parent);
    if (title == SafeHtmlUtils.EMPTY_SAFE_HTML) {
      header.getParentElement().getStyle().setDisplay(Display.NONE);
    } else {
      header.setInnerSafeHtml(title);
      header.getParentElement().getStyle().setDisplay(Display.BLOCK);
    }

    getBodyElement(parent).setInnerSafeHtml(body);
  }

}
