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
package com.sencha.gxt.theme.gray.client.window;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import com.sencha.gxt.theme.base.client.frame.NestedDivFrame;
import com.sencha.gxt.theme.base.client.frame.NestedDivFrame.NestedDivFrameStyle;
import com.sencha.gxt.theme.base.client.panel.FramedPanelBaseAppearance;
import com.sencha.gxt.theme.base.client.widget.HeaderDefaultAppearance;
import com.sencha.gxt.theme.base.client.widget.HeaderDefaultAppearance.HeaderResources;
import com.sencha.gxt.theme.base.client.widget.HeaderDefaultAppearance.HeaderStyle;
import com.sencha.gxt.theme.gray.client.panel.GrayFramedPanelAppearance.FramedPanelStyle;
import com.sencha.gxt.widget.core.client.Window.WindowAppearance;

public class GrayWindowAppearance extends FramedPanelBaseAppearance implements WindowAppearance {

  public interface GrayWindowDivFrameStyle extends NestedDivFrameStyle {

  }

  public interface GrayWindowDivFrameResources extends FramedPanelDivFrameResources, ClientBundle {

    @Source({"com/sencha/gxt/theme/base/client/frame/NestedDivFrame.gss", "GrayWindowDivFrame.gss"})
    @Override
    GrayWindowDivFrameStyle style();

    @Source("com/sencha/gxt/core/public/clear.gif")
    ImageResource background();

    @Override
    ImageResource topLeftBorder();

    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    @Override
    ImageResource topBorder();

    @Override
    @ImageOptions(repeatStyle = RepeatStyle.Both)
    ImageResource topRightBorder();

    @ImageOptions(repeatStyle = RepeatStyle.Vertical)
    @Override
    ImageResource leftBorder();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Override
    ImageResource rightBorder();

    @Override
    @ImageOptions(repeatStyle = RepeatStyle.Both)
    ImageResource bottomLeftBorder();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Override
    ImageResource bottomBorder();

    @Override
    @ImageOptions(repeatStyle = RepeatStyle.Both)
    ImageResource bottomRightBorder();

  }

  public interface GrayWindowStyle extends FramedPanelStyle {
    String ghost();
  }

  public interface GrayHeaderStyle extends HeaderStyle {

  }

  public interface GrayHeaderResources extends HeaderResources {
    @Source({"com/sencha/gxt/theme/base/client/widget/Header.gss", "GrayWindowHeader.gss"})
    GrayHeaderStyle style();
  }

  public interface GrayWindowResources extends ContentPanelResources, ClientBundle {

    @Source({
        "com/sencha/gxt/theme/base/client/panel/ContentPanel.gss",
        "com/sencha/gxt/theme/base/client/window/Window.gss", "GrayWindow.gss"})
    @Override
    GrayWindowStyle style();

  }

  private GrayWindowStyle style;

  public GrayWindowAppearance() {
    this((GrayWindowResources) GWT.create(GrayWindowResources.class));
  }

  public GrayWindowAppearance(GrayWindowResources resources) {
    super(resources, GWT.<FramedPanelTemplate> create(FramedPanelTemplate.class), new NestedDivFrame(
        GWT.<GrayWindowDivFrameResources> create(GrayWindowDivFrameResources.class)));

    this.style = resources.style();
  }

  @Override
  public HeaderDefaultAppearance getHeaderAppearance() {
    return new HeaderDefaultAppearance(GWT.<GrayHeaderResources> create(GrayHeaderResources.class));
  }

  @Override
  public String ghostClass() {
    return style.ghost();
  }
}
