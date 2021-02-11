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
package com.sencha.gxt.theme.base.client.slider;

import com.google.gwt.cell.client.Cell.Context;
import com.google.gwt.dom.client.Element;
import com.google.gwt.resources.client.CssResource;
import com.sencha.gxt.cell.core.client.SliderCell.SliderAppearance;
import com.sencha.gxt.core.client.resources.StyleInjectorHelper;

public abstract class SliderBaseAppearance implements SliderAppearance {

  public interface SliderResources {

    SliderStyle style();

  }

  public interface SliderStyle extends CssResource {
    public String drag();

    public String end();

    public String focus();

    public String inner();

    public String over();

    public String slider();

    public String thumb();

    public int halfThumb();
  }

  private final SliderStyle style;

  public SliderBaseAppearance(SliderResources resources) {
    style = resources.style();

    StyleInjectorHelper.ensureInjected(this.style, true);
  }


  @Override
  public void onEmpty(Element parent, boolean empty) {
    // Not possible to "empty" a slider
  }

  @Override
  public void onFocus(Element parent, boolean focus) {
    // No visible effect of focussing
  }

  @Override
  public void onMouseDown(Context context, Element parent) {
    getThumb(parent).addClassName(style.drag());
  }

  @Override
  public void onMouseOut(Context context, Element parent) {
    getThumb(parent).removeClassName(style.over());
  }

  @Override
  public void onMouseOver(Context context, Element parent) {
    getThumb(parent).addClassName(style.over());
  }

  @Override
  public void onMouseUp(Context context, Element parent) {
    getThumb(parent).removeClassName(style.drag());
  }

  @Override
  public void onValid(Element parent, boolean valid) {
    // Always valid
  }

  @Override
  public void setReadOnly(Element parent, boolean readonly) {
    // TODO Not currently disableable
  }

  protected Element getEndEl(Element parent) {
    return parent.getFirstChildElement().getFirstChildElement();
  }

  protected Element getInnerEl(Element parent) {
    return parent.getFirstChildElement().getFirstChildElement().getFirstChildElement();
  }

  @Override
  public Element getThumb(Element parent) {
    return parent.getFirstChildElement().getFirstChildElement().getFirstChildElement().getFirstChildElement();
  }

}
