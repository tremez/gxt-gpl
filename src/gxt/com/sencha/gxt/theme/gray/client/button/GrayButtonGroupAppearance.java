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
package com.sencha.gxt.theme.gray.client.button;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import com.sencha.gxt.theme.base.client.button.ButtonGroupBaseAppearance;
import com.sencha.gxt.theme.base.client.button.ButtonGroupBaseTableFrameResources;
import com.sencha.gxt.theme.base.client.frame.TableFrame;

public class GrayButtonGroupAppearance extends ButtonGroupBaseAppearance {

  public interface GrayButtonGroupTableFrameResources extends ButtonGroupBaseTableFrameResources {

    @Source({
        "com/sencha/gxt/theme/base/client/frame/TableFrame.gss",
        "com/sencha/gxt/theme/base/client/button/ButtonGroupTableFrame.gss"})
    @Override
    ButtonGroupTableFrameStyle style();

    @Source("com/sencha/gxt/core/public/clear.gif")
    @ImageOptions(repeatStyle = RepeatStyle.Both)
    ImageResource background();

    @Source("groupTopLeftBorder.gif")
    @ImageOptions(repeatStyle = RepeatStyle.Vertical)
    @Override
    ImageResource topLeftBorder();

    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    @Source("groupTopBorder.gif")
    @Override
    ImageResource topBorder();

    @Override
    @Source("groupTopRightBorder.gif")
    ImageResource topRightBorder();

    @ImageOptions(repeatStyle = RepeatStyle.Vertical)
    @Source("groupLeftBorder.gif")
    @Override
    ImageResource leftBorder();

    @ImageOptions(repeatStyle = RepeatStyle.Vertical)
    @Source("groupRightBorder.gif")
    @Override
    ImageResource rightBorder();

    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    @Source("groupBottomBorder.gif")
    @Override
    ImageResource bottomBorder();

    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    @Source("groupTopNoHeadBorder.gif")
    ImageResource topNoHeadBorder();
  }

  public interface GrayButtonGroupResources extends ButtonGroupResources {
    @Source({"com/sencha/gxt/theme/base/client/button/ButtonGroup.gss", "GrayButtonGroup.gss"})
    ButtonGroupStyle css();
  }

  public GrayButtonGroupAppearance() {
    this(GWT.<GrayButtonGroupResources> create(GrayButtonGroupResources.class));
  }

  public GrayButtonGroupAppearance(GrayButtonGroupResources resources) {
    super(resources, GWT.<GroupTemplate> create(GroupTemplate.class), new TableFrame(
        GWT.<GrayButtonGroupTableFrameResources> create(GrayButtonGroupTableFrameResources.class)));
  }

}
