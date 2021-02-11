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
import com.google.gwt.dom.client.Document;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.GXTLogConfiguration;
import com.sencha.gxt.core.client.Style.Side;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.util.Size;
import com.sencha.gxt.widget.core.client.ComponentHelper;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.ContentPanel.ContentPanelAppearance;
import com.sencha.gxt.widget.core.client.event.BeforeExpandEvent;
import com.sencha.gxt.widget.core.client.event.BeforeExpandEvent.BeforeExpandHandler;
import com.sencha.gxt.widget.core.client.event.ExpandEvent;
import com.sencha.gxt.widget.core.client.event.ExpandEvent.ExpandHandler;

import java.util.logging.Logger;

/**
 * A layout container that manages multiple content panels in an expandable
 * accordion style such that only one panel can be expanded at any given time
 * (by default). Each panel has built-in support for expanding and collapsing.
 * 
 * Note: Only {@link ContentPanel}'s and subclasses may be used in an accordion
 * layout container. Each content panel should be created with
 * {@link AccordionLayoutAppearance} to produce the expected accordion panel
 * look and feel. The <code>AccordionLayoutAppearance</code> must be created
 * using the {@link GWT#create(Class)} method to enable GWT class substitution,
 * as illustrated in the following code snippet.
 * <p />
 * Code snippet:
 * 
 * <pre>
    AccordionLayoutContainer con = new AccordionLayoutContainer();
    AccordionLayoutAppearance appearance = GWT.<AccordionLayoutAppearance> create(AccordionLayoutAppearance.class);

    ContentPanel cp = new ContentPanel(appearance);
    cp.setHeading("Accordion Item 1");
    cp.add(new Label("Accordion Content 1"));
    con.add(cp);
    con.setActiveWidget(cp);

    cp = new ContentPanel(appearance);
    cp.setHeading("Accordion Item 2");
    cp.add(new Label("Accordion Content 2"));
    con.add(cp);
  
    cp = new ContentPanel(appearance);
    cp.setHeading("Accordion Item 3");
    cp.add(new Label("Accordion Content 3"));
    con.add(cp);
    
    Viewport v = new Viewport();
    v.add(con);
    RootPanel.get().add(v);
 * </pre>
 */
public class AccordionLayoutContainer extends InsertResizeContainer implements HasActiveWidget {

  /**
   * AccordionLayoutContainer appearance. An implementation of this class is
   * provided by GWT deferred binding when an instance of the class is created
   * using {@link GWT#create(Class)}.
   */
  public interface AccordionLayoutAppearance extends ContentPanelAppearance {

  }

  /**
   * Defines the expand modes.
   */
  public enum ExpandMode {
    /**
     * Only one panel may be expanded at a time.
     */
    SINGLE,
    /**
     * Only open panel may be expanded and the size of the expanded panel is set
     * to match available container height.
     */
    SINGLE_FILL,
    /**
     * Multiple panels may be expanded at the same time.
     */
    MULTI
  }

  private class Handler implements BeforeExpandHandler, ExpandHandler {

    @Override
    public void onBeforeExpand(BeforeExpandEvent event) {
      AccordionLayoutContainer.this.onBeforeExpand(event);
    }

    @Override
    public void onExpand(ExpandEvent event) {
      AccordionLayoutContainer.this.onExpand(event);
    }

  }

  private ContentPanel activeWidget;
  private boolean activeOnTop;
  private boolean hideCollapseTool;
  private boolean titleCollapse = true;
  private ExpandMode expandMode = ExpandMode.SINGLE_FILL;
  private Handler handler = new Handler();

  private static Logger logger = Logger.getLogger(AccordionLayoutContainer.class.getName());

  /**
   * Creates a new accordion layout container.
   */
  public AccordionLayoutContainer() {
    setElement(Document.get().createDivElement());
  }

  /**
   * Returns true if the active item if first.
   * 
   * @return the active on top state
   */
  public boolean getActiveOnTop() {
    return activeOnTop;
  }

  @Override
  public Widget getActiveWidget() {
    return activeWidget;
  }

  /**
   * Returns the expand mode.
   * 
   * @return the expand mode
   */
  public ExpandMode getExpandMode() {
    return expandMode;
  }

  /**
   * Returns true if the collapse tool is hidden.
   * 
   * @return the hide collapse tool state
   */
  public boolean isHideCollapseTool() {
    return hideCollapseTool;
  }

  /**
   * Returns true if title collapse is enabled.
   * 
   * @return the title collapse state
   */
  public boolean isTitleCollapse() {
    return titleCollapse;
  }

  /**
   * True to swap the position of each panel as it is expanded so that it
   * becomes the first item in the container, false to keep the panels in the
   * rendered order (defaults to false).
   * 
   * @param activeOnTop true to keep the active item on top
   */
  public void setActiveOnTop(boolean activeOnTop) {
    this.activeOnTop = activeOnTop;
  }

  @Override
  public void setActiveWidget(Widget widget) {
    if (activeWidget != widget) {
      if (activeWidget != null && expandMode != ExpandMode.MULTI) {
        activeWidget.collapse();
      }

      activeWidget = (ContentPanel) widget;

      if (!activeWidget.isExpanded()) {
        activeWidget.expand();
      } else {
        if (activeOnTop) {
          getContainerTarget().insertChild(activeWidget.getElement(), 0);
        }
      }
    }
  }

  /**
   * Sets the expand mode (defaults to {@link ExpandMode#SINGLE_FILL}).
   * 
   * @param expandMode the mode
   */
  public void setExpandMode(ExpandMode expandMode) {
    this.expandMode = expandMode;
  }

  /**
   * True to hide the contained panels' collapse/expand toggle buttons, false to
   * display them (defaults to false). When set to true, {@link #titleCollapse}
   * should be true also.
   * 
   * @param hideCollapseTool true to hide the collapse tool
   */
  public void setHideCollapseTool(boolean hideCollapseTool) {
    this.hideCollapseTool = hideCollapseTool;
  }

  /**
   * True to allow expand/collapse of each contained panel by clicking anywhere
   * on the title bar, false to allow expand/collapse only when the toggle tool
   * button is clicked (defaults to true). When set to false,
   * {@link #hideCollapseTool} should be false also.
   * 
   * @param titleCollapse true for title collapse
   */
  public void setTitleCollapse(boolean titleCollapse) {
    this.titleCollapse = titleCollapse;
  }

  @Override
  protected void doLayout() {
    if (activeWidget != null) {
      if (GXTLogConfiguration.loggingIsEnabled()) {
        logger.finest("doLayout");
      }
      Size size = getContainerTarget().getStyleSize();

      switch (expandMode) {
        case MULTI:
        case SINGLE:
          for (int i = 0, len = getWidgetCount(); i < len; i++) {
            ContentPanel c = (ContentPanel) getWidget(i);
            if (expandMode == ExpandMode.SINGLE && c != activeWidget) {
              c.collapse();
            }
            int horizontalPadding = getContainerTarget().getPadding(Side.LEFT, Side.RIGHT);
            applyLayout(c, size.getWidth() - horizontalPadding, -1);
          }
          break;
        case SINGLE_FILL:
          if (activeWidget.isExpanded()) {
            int hh = 0;
            for (int i = 0, len = getWidgetCount(); i < len; i++) {
              ContentPanel c = (ContentPanel) getWidget(i);

              if (c != activeWidget) {
                c.collapse();
                hh += c.getAppearance().getHeaderSize(c.getElement()).getHeight();
                applyLayout(c, size.getWidth(), -1);
              }
              // take vertical padding into account as well
              hh += c.getElement() != null ? c.getElement().getPadding(Side.TOP, Side.BOTTOM) : 0;
            }
            XElement activeElement = activeWidget.getElement();
            int horizontalPadding = activeElement != null ? activeElement.getPadding(Side.LEFT, Side.RIGHT) : 0;
            activeWidget.setPixelSize(size.getWidth(), size.getHeight() - hh);
            // need to set body size as well, otherwise it will extend past the parents width
            activeWidget.getBody().setWidth(size.getWidth() - horizontalPadding);
          } else {
            for (int i = 0, len = getWidgetCount(); i < len; i++) {
              ContentPanel c = (ContentPanel) getWidget(i);
              if (c != activeWidget) {
                c.collapse();
              }
              applyLayout(c, size.getWidth(), -1);
            }
          }

      }

    } else {
      for (int i = 0, len = getWidgetCount(); i < len; i++) {
        ContentPanel c = (ContentPanel) getWidget(i);
        if (c != activeWidget) {
          c.collapse();
        }
      }
    }
  }

  protected void onBeforeExpand(BeforeExpandEvent event) {
    if (activeWidget != null && (activeWidget != event.getSource()) && expandMode != ExpandMode.MULTI) {
      activeWidget.collapse();
    }

    activeWidget = (ContentPanel) event.getSource();

    if (activeOnTop) {
      getContainerTarget().insertChild(activeWidget.getElement(), 0);
    }
  }

  protected void onExpand(ExpandEvent event) {
    forceLayout();
  }

  @Override
  protected void onInsert(int index, Widget child) {
    super.onInsert(index, child);
    assert child instanceof ContentPanel : "AccordionLayoutContainer must be ContentPanels";

    ContentPanel cp = (ContentPanel) child;
    cp.addBeforeExpandHandler(handler);
    cp.addExpandHandler(handler);
    cp.setCollapsible(true);
    cp.setHideCollapseTool(hideCollapseTool);
    cp.setAnimCollapse(false);
    cp.setTitleCollapse(titleCollapse);
  }

  protected void onRemove(Widget child) {
    super.onRemove(child);

    ContentPanel cp = (ContentPanel) child;
    ComponentHelper.removeHandler(cp, BeforeExpandEvent.getType(), handler);
    ComponentHelper.removeHandler(cp, ExpandEvent.getType(), handler);
  }

}
