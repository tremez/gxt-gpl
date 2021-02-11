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
package com.sencha.gxt.widget.core.client.button;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.uibinder.client.UiConstructor;
import com.google.gwt.user.client.Event;
import com.sencha.gxt.core.client.dom.XDOM;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.gestures.TapGestureRecognizer;
import com.sencha.gxt.core.client.gestures.TouchData;
import com.sencha.gxt.core.client.resources.CommonStyles;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.event.BeforeSelectEvent;
import com.sencha.gxt.widget.core.client.event.BeforeSelectEvent.BeforeSelectHandler;
import com.sencha.gxt.widget.core.client.event.BeforeSelectEvent.HasBeforeSelectHandlers;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.HasSelectHandlers;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

/**
 * A simple css styled button with 3 states: normal, over, and disabled.
 * 
 * <p />
 * Note: To change the icon style after construction use
 * {@link #changeStyle(String)}.
 */
public class IconButton extends Component implements HasBeforeSelectHandlers, HasSelectHandlers {

  public interface IconButtonAppearance {
    XElement getIconElem(XElement parent);

    void render(SafeHtmlBuilder sb);
  }

  public static class IconConfig {

    private String style, overStyle, disableStyle;

    public IconConfig(String style) {
      this(style, style + "Over");
    }

    public IconConfig(String style, String overStyle) {
      this(style, overStyle, style + "Disabled");
    }

    @UiConstructor
    public IconConfig(String style, String overStyle, String disableStyle) {
      this.style = style;
      this.overStyle = overStyle;
      this.disableStyle = disableStyle;
    }

    public String getDisableStyle() {
      return disableStyle;
    }

    public String getOverStyle() {
      return overStyle;
    }

    public String getStyle() {
      return style;
    }

    public void setDisableStyle(String disableStyle) {
      this.disableStyle = disableStyle;
    }

    public void setOverStyle(String overStyle) {
      this.overStyle = overStyle;
    }

    public void setStyle(String style) {
      this.style = style;
    }

  }

  protected IconConfig config;
  protected boolean cancelBubble = true;
  private final IconButtonAppearance appearance;

  /**
   * Creates a new icon button.
   * 
   * @param appearance the icon button appearance
   * @param config the icon configuration
   */
  public IconButton(IconButtonAppearance appearance, IconConfig config) {
    this.config = config;

    this.appearance = appearance;

    SafeHtmlBuilder sb = new SafeHtmlBuilder();
    this.appearance.render(sb);

    setElement((Element) XDOM.create(sb.toSafeHtml()));

    // mark element to not start drags
    addStyleName(CommonStyles.get().nodrag());

    addStyleName(config.style);
    sinkEvents(Event.ONCLICK | Event.MOUSEEVENTS | Event.FOCUSEVENTS | Event.ONKEYUP);

    addGestureRecognizer(new TapGestureRecognizer() {

      @Override
      protected void onTap(TouchData touchData) {
        super.onTap(touchData);
        onClick(touchData.getLastNativeEvent().<Event> cast());
      }
    });
  }

  /**
   * Creates a new icon button.
   * 
   * @param config the icon configuration
   */
  @UiConstructor
  public IconButton(IconConfig config) {
    this(GWT.<IconButtonAppearance> create(IconButtonAppearance.class), config);
  }

  /**
   * Creates a new icon button. The 'over' style and 'disabled' style names
   * determined by adding 'Over' and 'Disabled' to the base style name.
   * 
   * @param style the base style
   */
  public IconButton(String style) {
    this(GWT.<IconButtonAppearance> create(IconButtonAppearance.class), new IconConfig(style));
  }

  /**
   * Creates a new icon button. The 'over' style and 'disabled' style names
   * determined by adding '-over' and '-disabled' to the base style name.
   * 
   * @param style the base style
   * @param handler the select handler
   */
  public IconButton(String style, SelectHandler handler) {
    this(style);
    addSelectHandler(handler);
  }

  @Override
  public HandlerRegistration addBeforeSelectHandler(BeforeSelectHandler handler) {
    return addHandler(handler, BeforeSelectEvent.getType());
  }

  @Override
  public HandlerRegistration addSelectHandler(SelectHandler handler) {
    return addHandler(handler, SelectEvent.getType());
  }

  /**
   * Changes the icon style.
   * 
   * @param config the config object
   */
  public void changeStyle(IconConfig config) {
    removeStyleName(this.config.style);
    removeStyleName(this.config.overStyle);
    removeStyleName(this.config.disableStyle);
    addStyleName(config.style);
    this.config = config;
  }

  /**
   * Changes the icon style.
   * 
   * @param style the new icon style
   */
  public void changeStyle(String style) {
    removeStyleName(config.style);
    removeStyleName(config.overStyle);
    removeStyleName(config.disableStyle);
    addStyleName(style);
    this.config = new IconConfig(style);
  }

  public IconButtonAppearance getAppearance() {
    return appearance;
  }

  @Override
  public void onBrowserEvent(Event event) {
    super.onBrowserEvent(event);
    switch (event.getTypeInt()) {
      case Event.ONMOUSEOVER:
        if (isEnabled()) {
          addStyleName(config.overStyle);
        }
        break;
      case Event.ONMOUSEOUT:
        removeStyleName(config.overStyle);
        break;
      case Event.ONCLICK:
        onClick(event);
        break;
      case Event.ONFOCUS:
        onFocus(event);
        break;
      case Event.ONBLUR:
        onBlur(event);
        break;
      case Event.ONKEYUP:
        onKeyPress(event);
        break;
    }
  }

  protected void onClick(Event e) {
    if (e != null && cancelBubble) {
      e.stopPropagation();
    }
    if (isEnabled() && fireCancellableEvent(new BeforeSelectEvent())) {
      fireEvent(new SelectEvent());
    }

    removeStyleName(config.overStyle);
  }

  @Override
  protected void onDisable() {
    super.onDisable();
    addStyleName(config.disableStyle);
  }

  @Override
  protected void onEnable() {
    super.onEnable();
    removeStyleName(config.disableStyle);
  }

  protected void onKeyPress(Event ce) {
    int code = ce.getKeyCode();
    if (code == KeyCodes.KEY_ENTER || code == 32) {
      onClick(ce);
    }
  }

}
