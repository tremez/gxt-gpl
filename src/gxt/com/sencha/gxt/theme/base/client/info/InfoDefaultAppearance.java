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
package com.sencha.gxt.theme.base.client.info;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.theme.base.client.frame.DivFrame;
import com.sencha.gxt.theme.base.client.frame.DivFrame.DivFrameResources;
import com.sencha.gxt.theme.base.client.frame.DivFrame.DivFrameStyle;
import com.sencha.gxt.theme.base.client.frame.Frame;
import com.sencha.gxt.widget.core.client.info.Info.InfoAppearance;

public class InfoDefaultAppearance implements InfoAppearance {

  public interface InfoResources extends ClientBundle, DivFrameResources {

    @Source("background.png")
    @ImageOptions(repeatStyle = RepeatStyle.Both)
    ImageResource background();

    @Source("bottomBorder.png")
    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    ImageResource bottomBorder();

    @Source("bottomLeftBorder.png")
    ImageResource bottomLeftBorder();

    @Source("bottomRightBorder.png")
    ImageResource bottomRightBorder();

    @Source("leftBorder.png")
    @ImageOptions(repeatStyle = RepeatStyle.Vertical)
    ImageResource leftBorder();

    @Source("rightBorder.png")
    @ImageOptions(repeatStyle = RepeatStyle.Vertical)
    ImageResource rightBorder();

    @Source({"com/sencha/gxt/theme/base/client/frame/DivFrame.gss", "Info.gss"})
    InfoStyle style();

    @Source("topBorder.png")
    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    ImageResource topBorder();

    @Source("topLeftBorder.png")
    ImageResource topLeftBorder();

    @Source("topRightBorder.png")
    ImageResource topRightBorder();
  }

  public interface InfoStyle extends DivFrameStyle {
    String info();
  }

  public interface Template extends XTemplates {
    @XTemplate(source = "InfoDefault.html")
    SafeHtml render(InfoStyle style);
  }

  private final Template template;
  private final Frame frame;
  private final InfoStyle style;

  public InfoDefaultAppearance() {
    this(GWT.<Template>create(Template.class), GWT.<InfoResources>create(InfoResources.class));
  }

  public InfoDefaultAppearance(Template template, InfoResources resources) {
    this.template = template;
    this.style = resources.style();
    this.style.ensureInjected();

    frame = new DivFrame(resources);
  }

  @Override
  public XElement getContentElement(XElement parent) {
    return parent.selectNode("." + style.info());
  }

  @Override
  public void render(SafeHtmlBuilder sb) {
    frame.render(sb, Frame.EMPTY_FRAME, template.render(style));
  }

}