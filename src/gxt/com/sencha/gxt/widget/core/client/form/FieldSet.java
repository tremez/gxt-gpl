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
package com.sencha.gxt.widget.core.client.form;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.sencha.gxt.core.client.Style.Side;
import com.sencha.gxt.core.client.dom.XDOM;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.util.Size;
import com.sencha.gxt.core.client.util.TextMetrics;
import com.sencha.gxt.widget.core.client.Collapsible;
import com.sencha.gxt.widget.core.client.ComponentHelper;
import com.sencha.gxt.widget.core.client.button.IconButton.IconConfig;
import com.sencha.gxt.widget.core.client.button.ToolButton;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.event.BeforeCollapseEvent;
import com.sencha.gxt.widget.core.client.event.BeforeCollapseEvent.BeforeCollapseHandler;
import com.sencha.gxt.widget.core.client.event.BeforeCollapseEvent.HasBeforeCollapseHandlers;
import com.sencha.gxt.widget.core.client.event.BeforeExpandEvent;
import com.sencha.gxt.widget.core.client.event.BeforeExpandEvent.BeforeExpandHandler;
import com.sencha.gxt.widget.core.client.event.BeforeExpandEvent.HasBeforeExpandHandlers;
import com.sencha.gxt.widget.core.client.event.CollapseEvent;
import com.sencha.gxt.widget.core.client.event.CollapseEvent.CollapseHandler;
import com.sencha.gxt.widget.core.client.event.CollapseEvent.HasCollapseHandlers;
import com.sencha.gxt.widget.core.client.event.ExpandEvent;
import com.sencha.gxt.widget.core.client.event.ExpandEvent.ExpandHandler;
import com.sencha.gxt.widget.core.client.event.ExpandEvent.HasExpandHandlers;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.event.SelectEvent.SelectHandler;

/**
 * A simple container that wraps its content in a HTML fieldset. FieldSet support collapsing which can be enabled using
 * {@link #setCollapsible(boolean)}.
 */
public class FieldSet extends SimpleContainer implements HasBeforeExpandHandlers, HasExpandHandlers,
    HasBeforeCollapseHandlers, HasCollapseHandlers, Collapsible {

  public interface FieldSetAppearance {
    XElement getContainerTarget(XElement parent);

    XElement getHeadingElement(XElement parent);

    XElement getToolElement(XElement parent);

    void onCollapse(XElement parent, boolean collapse);

    void render(SafeHtmlBuilder sb);

    IconConfig collapseIcon();

    IconConfig expandIcon();
  }

  private final FieldSetAppearance appearance;
  private boolean collapsed, collapsible;
  private ToolButton collapseButton;
  private SafeHtml heading = SafeHtmlUtils.EMPTY_SAFE_HTML;

  /**
   * Creates a new field set.
   */
  public FieldSet() {
    this(GWT.<FieldSetAppearance> create(FieldSetAppearance.class));
  }

  /**
   * Creates a new field set.
   * 
   * @param appearance the field set appearance
   */
  public FieldSet(FieldSetAppearance appearance) {
    super(true);

    this.appearance = appearance;

    SafeHtmlBuilder builder = new SafeHtmlBuilder();
    this.appearance.render(builder);

    setElement((Element) XDOM.create(builder.toSafeHtml()));
  }

  @Override
  public HandlerRegistration addBeforeCollapseHandler(BeforeCollapseHandler handler) {
    return addHandler(handler, BeforeCollapseEvent.getType());
  }

  @Override
  public HandlerRegistration addBeforeExpandHandler(BeforeExpandHandler handler) {
    return addHandler(handler, BeforeExpandEvent.getType());
  }

  @Override
  public HandlerRegistration addCollapseHandler(CollapseHandler handler) {
    return addHandler(handler, CollapseEvent.getType());
  }

  @Override
  public HandlerRegistration addExpandHandler(ExpandHandler handler) {
    return addHandler(handler, ExpandEvent.getType());
  }

  @Override
  public void collapse() {
    assert collapsible : "Calling collapse on non-collapsible field set";
    if (collapsible) {
      if (fireCancellableEvent(new BeforeCollapseEvent())) {
        this.collapsed = true;
        appearance.onCollapse(getElement(), true);
        // the widget has been collapsed, show the expand icon
        getCollapseButton().changeStyle(appearance.expandIcon());
        fireEvent(new CollapseEvent());
      }
    }
  }

  @Override
  public void expand() {
    assert collapsible : "Calling expand on non-collapsible field set";
    if (collapsible) {
      if (fireCancellableEvent(new BeforeExpandEvent())) {
        this.collapsed = false;
        appearance.onCollapse(getElement(), false);
        // the widget has been expanded, show the collapse icon
        getCollapseButton().changeStyle(appearance.collapseIcon());
        fireEvent(new ExpandEvent());
      }
    }
  }

  public FieldSetAppearance getAppearance() {
    return appearance;
  }

  /**
   * Returns the collapse button.
   * 
   * @return the collapse button or null if field set not collapsible
   */
  public ToolButton getCollapseButton() {
    if (collapseButton == null) {
      collapseButton = new ToolButton(appearance.collapseIcon());
      collapseButton.addSelectHandler(new SelectHandler() {

        @Override
        public void onSelect(SelectEvent event) {
          setExpanded(!isExpanded());
        }
      });
      if (isAttached()) {
        ComponentHelper.doAttach(collapseButton);
      }
    }
    return collapseButton;
  }

  /**
   * Returns true if the fieldset is collapsible.
   * 
   * @return true if collapsible
   */
  public boolean isCollapsible() {
    return collapsible;
  }

  @Override
  public boolean isExpanded() {
    return !collapsed;
  }

  /**
   * Sets whether the fieldset is collapsible (defaults to false, pre-render). This method only configures the field set
   * to be collapsible and does not change the expand / collapse state. Use {@link #setExpanded(boolean)},
   * {@link #expand()}, and {@link #collapse()} to expand and collapse the field set.
   * 
   * @param collapsible true to enable collapsing
   */
  public void setCollapsible(boolean collapsible) {
    assertPreRender();
    this.collapsible = collapsible;
  }

  /**
   * Convenience method to expand / collapse the field set by invoking {@link #expand()} or {@link #collapse()}.
   * 
   * @param expand true to expand the field set, otherwise collapse
   */
  public void setExpanded(boolean expand) {
    if (expand) {
      expand();
    } else {
      collapse();
    }
  }

  /**
   * Returns the heading html.
   *
   * @return the heading html
   */
  public SafeHtml getHeading() {
    return heading;
  }

  /**
   * Sets the heading html.
   *
   * @param html the heading html
   */
  public void setHeading(SafeHtml html) {
    this.heading = html;
    getAppearance().getHeadingElement(getElement()).setInnerSafeHtml(html);
  }

  /**
   * Sets the heading text.
   *
   * Text that contains reserved html characters will be escaped.
   *
   * @param text the text
   */
  public void setHeading(String text) {
    setHeading(SafeHtmlUtils.fromString(text));
  }

  @Override
  protected Size adjustSize(Size size) {
    int width = size.getWidth();
    if (width != -1 && width > 50) {
      width -= getContainerTarget().getMargins(Side.LEFT, Side.RIGHT);
      size.setWidth(width);
    }
    return size;
  }

  @Override
  protected void doAttachChildren() {
    super.doAttachChildren();
    ComponentHelper.doAttach(collapseButton);
  }

  @Override
  protected void doDetachChildren() {
    super.doDetachChildren();
    ComponentHelper.doDetach(collapseButton);
  }

  @Override
  protected XElement getContainerTarget() {
    return appearance.getContainerTarget(getElement());
  }

  @Override
  protected void notifyHide() {
    if (!collapsed) {
      super.notifyHide();
    }
  }

  @Override
  protected void notifyShow() {
    if (!collapsed) {
      super.notifyShow();
    }
  }

  @Override
  protected void onAfterFirstAttach() {
    super.onAfterFirstAttach();
    if (collapsible) {
      // make sure we create
      getCollapseButton();
      appearance.getToolElement(getElement()).appendChild(collapseButton.getElement());
    }
  }

  @Override
  protected void onResize(int width, int height) {
    int tw = width - getElement().getFrameWidth(Side.LEFT, Side.RIGHT);
    getContainerTarget().setWidth(tw, true);

    // measure legend width
    XElement legend = getElement().selectNode("legend");

    TextMetrics.get().bind(legend);

    int legendWidth = TextMetrics.get().getWidth(getAppearance().getHeadingElement(getElement()).getInnerText());

    if (legendWidth > tw) {
      legend.setWidth(tw - 5);
    } else {
      legend.getStyle().clearWidth();
    }

    if (isAutoHeight()) {
      getContainerTarget().getStyle().clearHeight();
    } else {
      int adj = getElement().getFrameWidth(Side.TOP, Side.BOTTOM);
      adj += appearance.getHeadingElement(getElement()).getOffsetHeight();
      getContainerTarget().setHeight(height - adj, true);
    }
    super.onResize(width, height);
  }

}
