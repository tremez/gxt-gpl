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
package com.sencha.gxt.theme.base.client.field;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.theme.base.client.frame.Frame;
import com.sencha.gxt.theme.base.client.frame.NestedDivFrame;
import com.sencha.gxt.theme.base.client.tips.TipDefaultAppearance;
import com.sencha.gxt.widget.core.client.form.error.SideErrorHandler.SideErrorTooltipAppearance;

public class ErrorTipDefaultAppearance extends TipDefaultAppearance implements SideErrorTooltipAppearance {

  public interface ErrorTipFrameResources extends ClientBundle, TipDivFrameResources {
    @Source("com/sencha/gxt/core/public/clear.gif")
    @ImageOptions(repeatStyle = RepeatStyle.Both)
    ImageResource background();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("errorTipBottomBorder.gif")
    @Override
    ImageResource bottomBorder();

    @Override
    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("errorTipBottomLeftBorder.gif")
    ImageResource bottomLeftBorder();

    @Override
    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("errorTipBottomRightBorder.gif")
    ImageResource bottomRightBorder();

    @ImageOptions(repeatStyle = RepeatStyle.Vertical)
    @Source("errorTipLeftBorder.gif")
    @Override
    ImageResource leftBorder();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("errorTipRightBorder.gif")
    @Override
    ImageResource rightBorder();

    @Source({"com/sencha/gxt/theme/base/client/frame/NestedDivFrame.gss", "ErrorTipFrame.gss"})
    @Override
    ErrorTipNestedDivFrameStyle style();

    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    @Source("errorTipTopBorder.gif")
    @Override
    ImageResource topBorder();

    @Source("errorTipTopLeftBorder.gif")
    @Override
    ImageResource topLeftBorder();

    @Override
    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("errorTipTopRightBorder.gif")
    ImageResource topRightBorder();
  }

  public interface ErrorTipNestedDivFrameStyle extends TipNestedDivFrameStyle {
  }

  public interface ErrorTipResources extends TipResources {
    @Source("exclamation.gif")
    @ImageOptions(preventInlining = true)
    ImageResource errorIcon();

    @Source({"com/sencha/gxt/theme/base/client/tips/TipDefault.gss", "ErrorTip.gss"})
    ErrorTipStyle style();
  }

  public interface ErrorTipStyle extends TipStyle {
    String textWrap();
  }

  public interface ErrorTipTemplate extends XTemplates {
    @XTemplate(source = "ErrorTipDefault.html")
    SafeHtml render(ErrorTipStyle style);
  }

  private ErrorTipTemplate template;

  public ErrorTipDefaultAppearance() {
    this(GWT.<ErrorTipResources> create(ErrorTipResources.class));
  }

  public ErrorTipDefaultAppearance(ErrorTipResources resources) {
    super(resources);

    template = GWT.create(ErrorTipTemplate.class);
    frame = new NestedDivFrame(GWT.<TipDivFrameResources> create(ErrorTipFrameResources.class));
  }

  @Override
  public void render(SafeHtmlBuilder sb) {
    frame.render(sb, Frame.EMPTY_FRAME, template.render((ErrorTipStyle) style));
  }

}
