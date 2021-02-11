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
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import com.sencha.gxt.widget.core.client.form.StoreFilterField.StoreFilterFieldAppearance;

public class StoreFilterFieldDefaultAppearance extends TriggerFieldDefaultAppearance implements StoreFilterFieldAppearance {

  public interface StoreFilterFieldResources extends TriggerFieldResources {

    @Source({"ValueBaseField.gss", "TextField.gss", "TriggerField.gss", "StoreFilterField.gss"})
    StoreFilterFieldStyle css();

    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    ImageResource textBackground();

    @Source("clearTrigger.gif")
    ImageResource triggerArrow();

    @Source("clearTriggerOver.gif")
    ImageResource triggerArrowOver();

    @Source("clearTriggerClick.gif")
    ImageResource triggerArrowClick();

    @Source("clearTriggerFocus.gif")
    ImageResource triggerArrowFocus();

  }
  
  public interface StoreFilterFieldStyle extends TriggerFieldStyle {
    
  }

  public StoreFilterFieldDefaultAppearance() {
    this(GWT.<StoreFilterFieldResources> create(StoreFilterFieldResources.class));
  }

  public StoreFilterFieldDefaultAppearance(StoreFilterFieldResources resources) {
    super(resources);
  }
}