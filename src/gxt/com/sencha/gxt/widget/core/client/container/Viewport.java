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
package com.sencha.gxt.widget.core.client.container;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Window;
import com.sencha.gxt.core.client.dom.XDOM;

/**
 * A LayoutContainer that fills the browser window and monitors window resizing.
 * Viewports are best used for applications that will fill the browser without
 * window scrolling. Children of the viewport can allow scrolling.</p>
 * 
 * <h3>Example</h3>
 * 
 * <pre>
  public void onModuleLoad() {
    Viewport viewport = new Viewport();
    viewport.setWidget(new ContentPanel(), new MarginData(10));
    RootPanel.get().add(viewport);
  }
 * </pre>
 * 
 * <p/>
 * The viewport is not added to the root panel automatically. Is is not
 * necessary to call {@link #forceLayout()} after adding the viewport to the
 * RootPanel. Layout will be called after being added to the root panel.
 */
public class Viewport extends SimpleContainer {

  @SuppressWarnings("javadoc")
  public interface ViewportAppearance {

    public void render(SafeHtmlBuilder sb);

  }

  private boolean enableScroll;

  /**
   * Creates a viewport layout container with the default appearance.
   */
  public Viewport() {
    this(GWT.<ViewportAppearance> create(ViewportAppearance.class));
  }

  /**
   * Creates a viewport layout container with the specified appearance.
   * 
   * @param appearance the appearance of the viewport layout container
   */
  public Viewport(ViewportAppearance appearance) {
    super(true);
    SafeHtmlBuilder sb = new SafeHtmlBuilder();
    appearance.render(sb);
    setElement((Element) XDOM.create(sb.toSafeHtml()));
    monitorWindowResize = true;
    getFocusSupport().setIgnore(false);
    setPixelSize(Window.getClientWidth(), Window.getClientHeight());
  }

  /**
   * Returns true if window scrolling is enabled.
   * 
   * @return true if window scrolling is enabled
   */
  public boolean getEnableScroll() {
    return enableScroll;
  }

  /**
   * Sets whether window scrolling is enabled.
   * 
   * @param enableScroll true to enable window scrolling
   */
  public void setEnableScroll(boolean enableScroll) {
    this.enableScroll = enableScroll;
    Window.enableScrolling(enableScroll);
  }

  @Override
  protected void onAttach() {
    setEnableScroll(enableScroll);
    super.onAttach();
    setPixelSize(Window.getClientWidth(), Window.getClientHeight());
  }

  @Override
  protected void onWindowResize(int width, int height) {
    setPixelSize(width, height);
  }

}
