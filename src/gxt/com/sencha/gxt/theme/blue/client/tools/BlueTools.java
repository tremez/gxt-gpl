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
package com.sencha.gxt.theme.blue.client.tools;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.sencha.gxt.widget.core.client.button.Tools;

public class BlueTools extends Tools {

  public interface BlueToolResources extends ToolResources, ClientBundle {

    @Source("com/sencha/gxt/theme/base/client/tool/BaseTools.gss")
    ToolStyle style();
    
    @Override
    ImageResource closeIcon();

    @Override
    ImageResource closeOverIcon();

    @Override
    ImageResource collapseIcon();

    @Override
    ImageResource collapseOverIcon();

    @Override
    ImageResource doubleDownIcon();

    @Override
    ImageResource doubleDownOverIcon();

    @Override
    ImageResource doubleLeftIcon();

    @Override
    ImageResource doubleLeftOverIcon();

    @Override
    ImageResource doubleRightIcon();

    @Override
    ImageResource doubleRightOverIcon();

    @Override
    ImageResource doubleUpIcon();

    @Override
    ImageResource doubleUpOverIcon();

    @Override
    ImageResource downIcon();

    @Override
    ImageResource downOverIcon();

    @Override
    ImageResource expandIcon();

    @Override
    ImageResource expandOverIcon();

    @Override
    ImageResource gearIcon();

    @Override
    ImageResource gearOverIcon();

    @Override
    ImageResource leftIcon();

    @Override
    ImageResource leftOverIcon();

    @Override
    ImageResource maximizeIcon();

    @Override
    ImageResource maximizeOverIcon();

    @Override
    ImageResource minimizeIcon();

    @Override
    ImageResource minimizeOverIcon();

    @Override
    ImageResource minusIcon();

    @Override
    ImageResource minusOverIcon();

    @Override
    ImageResource pinIcon();

    @Override
    ImageResource pinOverIcon();

    @Override
    ImageResource unpinIcon();

    @Override
    ImageResource unpinOverIcon();

    @Override
    ImageResource plusIcon();

    @Override
    ImageResource plusOverIcon();

    @Override
    ImageResource printIcon();

    @Override
    ImageResource printOverIcon();

    @Override
    ImageResource questionIcon();

    @Override
    ImageResource questionOverIcon();

    @Override
    ImageResource refreshIcon();

    @Override
    ImageResource refreshOverIcon();

    @Override
    ImageResource restoreIcon();

    @Override
    ImageResource restoreOverIcon();

    @Override
    ImageResource rightIcon();

    @Override
    ImageResource rightOverIcon();

    @Override
    ImageResource saveIcon();

    @Override
    ImageResource saveOverIcon();

    @Override
    ImageResource searchIcon();

    @Override
    ImageResource searchOverIcon();

    @Override
    ImageResource upIcon();

    @Override
    ImageResource upOverIcon();
  }

  public BlueTools() {
    super(GWT.<ToolResources> create(BlueToolResources.class));
  }

}
