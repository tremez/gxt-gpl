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
package com.sencha.gxt.theme.gray.client.tabs;

import static com.google.gwt.resources.client.ImageResource.RepeatStyle.Both;
import static com.google.gwt.resources.client.ImageResource.RepeatStyle.Horizontal;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.sencha.gxt.theme.base.client.tabs.TabPanelBaseAppearance;

public class GrayTabPanelAppearance extends TabPanelBaseAppearance {

  public interface GrayTabPanelResources extends TabPanelResources, ClientBundle {

    // Prevent sprite sheet inclusion to allow background positioning in IE 6-7
    @ImageOptions(repeatStyle = Both)
    ImageResource bottomInactiveLeftBackground();

    // Prevent sprite sheet inclusion to allow background positioning in IE 6-7
    @ImageOptions(repeatStyle = Both)
    ImageResource bottomInactiveRightBackground();

    // Prevent sprite sheet inclusion to allow background positioning in IE 6-7
    @ImageOptions(repeatStyle = Both)
    ImageResource bottomLeftBackground();

    // Prevent sprite sheet inclusion to allow background positioning in IE 6-7
    @ImageOptions(repeatStyle = Both)
    ImageResource bottomRightBackground();

    ImageResource scrollerLeft();

    ImageResource scrollerLeftOver();

    ImageResource scrollerRight();

    ImageResource scrollerRightOver();

    @Source({"com/sencha/gxt/theme/base/client/tabs/TabPanel.gss", "GrayTabPanel.gss"})
    GrayTabPanelStyle style();

    @ImageOptions(repeatStyle = Horizontal)
    ImageResource tabCenter();

    @ImageOptions(repeatStyle = Horizontal)
    ImageResource tabCenterActive();

    @ImageOptions(repeatStyle = Horizontal)
    ImageResource tabCenterOver();

    ImageResource tabClose();

    ImageResource tabLeft();

    ImageResource tabLeftActive();

    ImageResource tabLeftOver();

    // Prevent sprite sheet inclusion to allow background positioning in IE 6-7
    @ImageOptions(repeatStyle = Both)
    ImageResource tabRight();

    // Prevent sprite sheet inclusion to allow background positioning in IE 6-7
    @ImageOptions(repeatStyle = Both)
    ImageResource tabRightActive();

    // Prevent sprite sheet inclusion to allow background positioning in IE 6-7
    @ImageOptions(repeatStyle = Both)
    ImageResource tabRightOver();

    @ImageOptions(repeatStyle = Horizontal)
    ImageResource tabStripBackground();

    @ImageOptions(repeatStyle = Horizontal)
    ImageResource tabStripBottomBackground();

  }

  public interface GrayTabPanelStyle extends TabPanelStyle {
  }

  public GrayTabPanelAppearance() {
    this(GWT.<GrayTabPanelResources> create(GrayTabPanelResources.class), GWT.<Template> create(Template.class),
        GWT.<ItemTemplate> create(ItemTemplate.class));
  }

  public GrayTabPanelAppearance(GrayTabPanelResources resources, Template template, ItemTemplate itemTemplate) {
    super(resources, template, itemTemplate);
  }

}
