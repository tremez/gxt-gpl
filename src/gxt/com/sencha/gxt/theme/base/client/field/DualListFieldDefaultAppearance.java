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
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import com.sencha.gxt.core.client.resources.StyleInjectorHelper;
import com.sencha.gxt.widget.core.client.button.IconButton.IconConfig;
import com.sencha.gxt.widget.core.client.form.DualListField.DualListFieldAppearance;

public class DualListFieldDefaultAppearance implements DualListFieldAppearance {

  public interface DualListFieldResources extends ClientBundle {
    @Source("DualListField.gss")
    DualListFieldStyle css();
    
    @Source("up2.gif")
    @ImageOptions(repeatStyle = RepeatStyle.None)
    ImageResource up();

    @Source("doubleright2.gif")
    @ImageOptions(repeatStyle = RepeatStyle.None)
    ImageResource allRight();
    
    @Source("left2.gif")
    @ImageOptions(repeatStyle = RepeatStyle.None)
    ImageResource left();
    
    @Source("right2.gif")
    @ImageOptions(repeatStyle = RepeatStyle.None)
    ImageResource right();
    
    @Source("doubleleft2.gif")
    @ImageOptions(repeatStyle = RepeatStyle.None)
    ImageResource allLeft();
    
    @Source("down2.gif")
    @ImageOptions(repeatStyle = RepeatStyle.None)
    ImageResource down();
  }
  
  public interface DualListFieldStyle extends CssResource {
    String up();
    
    String allRight();
    
    String right();
    
    String left();
    
    String allLeft();
    
    String down();
  }
  
  private final DualListFieldResources resources;
  private final DualListFieldStyle style;
  
  public DualListFieldDefaultAppearance() {
    this(GWT.<DualListFieldResources>create(DualListFieldResources.class));
  }
  
  public DualListFieldDefaultAppearance(DualListFieldResources resources) {
    this.resources = resources;
    this.style = this.resources.css();
    
    StyleInjectorHelper.ensureInjected(style, true);
  }
  
  @Override
  public IconConfig up() {
    return new IconConfig(style.up());
  }

  @Override
  public IconConfig allRight() {
    return new IconConfig(style.allRight());
  }

  @Override
  public IconConfig right() {
    return new IconConfig(style.right());
  }

  @Override
  public IconConfig left() {
    return new IconConfig(style.left());
  }

  @Override
  public IconConfig allLeft() {
    return new IconConfig(style.allLeft());
  }

  @Override
  public IconConfig down() {
    return new IconConfig(style.down());
  }

}
