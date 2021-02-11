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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.GXTLogConfiguration;
import com.sencha.gxt.core.client.Style;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.resources.CommonStyles;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.core.client.util.Size;
import com.sencha.gxt.core.client.util.Util;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.button.ButtonGroup;
import com.sencha.gxt.widget.core.client.button.SplitButton;
import com.sencha.gxt.widget.core.client.button.TextButton;
import com.sencha.gxt.widget.core.client.button.ToggleButton;
import com.sencha.gxt.widget.core.client.event.BeforeShowEvent;
import com.sencha.gxt.widget.core.client.event.BeforeShowEvent.BeforeShowHandler;
import com.sencha.gxt.widget.core.client.event.CheckChangeEvent;
import com.sencha.gxt.widget.core.client.event.CheckChangeEvent.CheckChangeHandler;
import com.sencha.gxt.widget.core.client.event.OverflowEvent;
import com.sencha.gxt.widget.core.client.event.OverflowEvent.HasOverflowHandlers;
import com.sencha.gxt.widget.core.client.event.OverflowEvent.OverflowHandler;
import com.sencha.gxt.widget.core.client.event.SelectEvent;
import com.sencha.gxt.widget.core.client.menu.CheckMenuItem;
import com.sencha.gxt.widget.core.client.menu.HeaderMenuItem;
import com.sencha.gxt.widget.core.client.menu.Item;
import com.sencha.gxt.widget.core.client.menu.Menu;
import com.sencha.gxt.widget.core.client.menu.MenuItem;
import com.sencha.gxt.widget.core.client.menu.SeparatorMenuItem;
import com.sencha.gxt.widget.core.client.toolbar.FillToolItem;
import com.sencha.gxt.widget.core.client.toolbar.SeparatorToolItem;
import com.sencha.gxt.widget.core.client.toolbar.ToolBar;

/**
 * A layout container for horizontal rows of widgets. Provides support for automatic overflow (i.e. when there are too
 * many widgets to display in the available width -- see {@link #setEnableOverflow(boolean)}).
 * 
 * <p/>
 * Code Snippet:
 * 
 * <pre>
    HBoxLayoutContainer c = new HBoxLayoutContainer();
    c.setHBoxLayoutAlign(HBoxLayoutAlign.TOP);
    BoxLayoutData layoutData = new BoxLayoutData(new Margins(5, 0, 0, 5));
    c.add(new TextButton("Button 1"), layoutData);
    c.add(new TextButton("Button 2"), layoutData);
    c.add(new TextButton("Button 3"), layoutData);
    Viewport v = new Viewport();
    v.add(c);
    RootPanel.get().add(v);
 * </pre>
 * 
 * @see ToolBar
 */
public class HBoxLayoutContainer extends BoxLayoutContainer implements HasOverflowHandlers {

  /**
   * The vertical alignment of the horizontal widgets.
   */
  public enum HBoxLayoutAlign {
    /**
     * Children are aligned horizontally at the <b>bottom</b> side of the container.
     */
    BOTTOM,
    /**
     * Children are aligned horizontally at the <b>mid-height</b> of the container.
     */
    MIDDLE,
    /**
     * Children are stretched vertically to fill the height of the container.
     */
    STRETCH,
    /**
     * Children heights are set the size of the largest child's height.
     */
    STRETCHMAX,
    /**
     * Children are aligned horizontally at the <b>top</b> side of the container.
     */
    TOP
  }

  public interface HBoxLayoutContainerAppearance extends BoxLayoutContainerAppearance {

    ImageResource moreIcon();

    String moreButtonStyle();
  }

  protected List<Widget> hiddens = new ArrayList<Widget>();
  protected boolean hasOverflow;
  protected TextButton more;
  protected Menu moreMenu;

  private boolean enableOverflow = true;
  private HBoxLayoutAlign hBoxLayoutAlign;
  private int triggerWidth = 35;
  private Map<Widget, Integer> widthMap = new HashMap<Widget, Integer>();
  private Map<Widget, Integer> loopWidthMap = new HashMap<Widget, Integer>();
  private Map<Widget, Integer> loopHeightMap = new HashMap<Widget, Integer>();

  private static Logger logger = Logger.getLogger(HBoxLayoutContainer.class.getName());

  /**
   * Creates a new HBoxlayout.
   */
  public HBoxLayoutContainer() {
    this(HBoxLayoutAlign.TOP);
  }

  /**
   * Creates a new HBoxlayout.
   *
   * @param appearance the hbox appearance
   */
  public HBoxLayoutContainer(HBoxLayoutContainerAppearance appearance) {
    this(HBoxLayoutAlign.TOP, appearance);
  }

  /**
   * Creates a new HBoxlayout.
   *
   * @param align the horizontal alignment
   */
  public HBoxLayoutContainer(HBoxLayoutAlign align) {
    this(align, GWT.<HBoxLayoutContainerAppearance>create(HBoxLayoutContainerAppearance.class));
  }

  protected HBoxLayoutContainer(HBoxLayoutAlign align, HBoxLayoutContainerAppearance appearance) {
    super(appearance);
    setHBoxLayoutAlign(align);
  }

  @Override
  public HandlerRegistration addOverflowHandler(OverflowHandler handler) {
    return addHandler(handler, OverflowEvent.getType());
  }

  /**
   * Returns the horizontal layout appearance.
   *
   * @return the appearance
   */
  @Override
  public HBoxLayoutContainerAppearance getAppearance() {
    return (HBoxLayoutContainerAppearance) super.getAppearance();
  }

  /**
   * Returns the horizontal alignment.
   * 
   * @return the horizontal alignment
   */
  public HBoxLayoutAlign getHBoxLayoutAlign() {
    return hBoxLayoutAlign;
  }

  /**
   * Returns true if overflow is enabled.
   * 
   * @return the overflow state
   */
  public boolean isEnableOverflow() {
    return enableOverflow;
  }

  /**
   * True to show a drop down icon when the available width is less than the required width (defaults to true).
   * 
   * @param enableOverflow true to enable overflow support
   */
  public void setEnableOverflow(boolean enableOverflow) {
    this.enableOverflow = enableOverflow;
  }

  /**
   * Sets the vertical alignment for child items (defaults to TOP).
   * 
   * @param hBoxLayoutAlign the vertical alignment
   */
  public void setHBoxLayoutAlign(HBoxLayoutAlign hBoxLayoutAlign) {
    this.hBoxLayoutAlign = hBoxLayoutAlign;
  }

  protected void addWidgetToMenu(Menu menu, Widget w) {
    // TODO do we really want all these types referenced here?
    if (w instanceof SeparatorToolItem) {
      menu.add(new SeparatorMenuItem());

    } else if (w instanceof SplitButton) {
      final SplitButton sb = (SplitButton) w;
      MenuItem item = new MenuItem(sb.getText(), sb.getIcon());
      item.setEnabled(sb.isEnabled());
      item.setItemId(sb.getItemId());
      if (sb.getData("gxt-menutext") != null) {
        item.setText(sb.getData("gxt-menutext").toString());
      }
      if (sb.getMenu() != null) {
        item.setSubMenu(sb.getMenu());
      }

      item.addSelectionHandler(new SelectionHandler<Item>() {

        @Override
        public void onSelection(SelectionEvent<Item> event) {
          sb.fireEvent(new SelectEvent());
        }
      });
      menu.add(item);

    } else if (w instanceof TextButton) {
      final TextButton b = (TextButton) w;

      MenuItem item = new MenuItem(b.getText(), b.getIcon());
      item.setItemId(b.getItemId());

      if (b.getData("gxt-menutext") != null) {
        item.setText(b.getData("gxt-menutext").toString());
      }
      if (b.getMenu() != null) {
        item.setHideOnClick(false);
        item.setSubMenu(b.getMenu());
      }
      item.setEnabled(b.isEnabled());

      item.addSelectionHandler(new SelectionHandler<Item>() {

        @Override
        public void onSelection(SelectionEvent<Item> event) {
          b.fireEvent(new SelectEvent());
        }
      });

      menu.add(item);
    } else if (w instanceof ButtonGroup) {
      ButtonGroup g = (ButtonGroup) w;
      g.setItemId(g.getItemId());
      menu.add(new SeparatorMenuItem());
      menu.add(new HeaderMenuItem(g.getHeading()));

      Widget con = g.getWidget();
      if (con != null && con instanceof HasWidgets) {
        HasWidgets widgets = (HasWidgets) con;
        Iterator<Widget> it = widgets.iterator();
        while (it.hasNext()) {
          addWidgetToMenu(menu, it.next());
        }
      }

      menu.add(new SeparatorMenuItem());
    } else if (w instanceof ToggleButton) {
      final ToggleButton b = (ToggleButton) w;

      final CheckMenuItem item = new CheckMenuItem(b.getText());
      item.setItemId(b.getItemId());
      item.setChecked(b.getValue());

      if (b.getData("gxt-menutext") != null) {
        item.setText(b.getData("gxt-menutext").toString());
      }

      item.setEnabled(b.isEnabled());

      item.addCheckChangeHandler(new CheckChangeHandler<CheckMenuItem>() {

        @Override
        public void onCheckChange(CheckChangeEvent<CheckMenuItem> event) {
          // must pass true to cause value change event to fire
          b.setValue(event.getItem().isChecked(), true);
          b.fireEvent(new SelectEvent());
        }
      });

      menu.add(item);

    }

    if (menu.getWidgetCount() > 0) {
      if (menu.getWidget(0) instanceof SeparatorMenuItem) {
        menu.remove(menu.getWidget(0));
      }
      if (menu.getWidgetCount() > 0) {
        if (menu.getWidget(menu.getWidgetCount() - 1) instanceof SeparatorMenuItem) {
          menu.remove(menu.getWidget(menu.getWidgetCount() - 1));
        }
      }
    }
  }

  protected void clearMenu() {
    moreMenu.clear();
  }

  @Override
  protected void doLayout() {
    Size size = getElement().getStyleSize();

    if (GXTLogConfiguration.loggingIsEnabled()) {
      logger.finest(getId() + " doLayout  size: " + size);
    }

    if ((size.getHeight() == 0 && size.getWidth() == 0) || size.getWidth() == 0) {
      return;
    }

    int w = size.getWidth() - getScrollOffset();
    int h = size.getHeight();

    int styleHeight = Util.parseInt(getElement().getStyle().getProperty("height"), Style.DEFAULT);
    int styleWidth = Util.parseInt(getElement().getStyle().getProperty("width"), Style.DEFAULT);

    boolean findWidth = styleWidth == -1;
    boolean findHeight = styleHeight == -1;

    if (GXTLogConfiguration.loggingIsEnabled()) {
      logger.finest(getId() + " findWidth: " + findWidth + " findHeight: " + findHeight);
    }

    int calculateWidth = 0;

    int maxWidgetHeight = 0;
    int maxMarginTop = 0;
    int maxMarginBottom = 0;

    for (int i = 0, len = getWidgetCount(); i < len; i++) {
      Widget widget = getWidget(i);

      BoxLayoutData layoutData = null;
      Object d = widget.getLayoutData();
      if (d instanceof BoxLayoutData) {
        layoutData = (BoxLayoutData) d;
      } else {
        layoutData = new BoxLayoutData();
        widget.setLayoutData(layoutData);
      }

      Margins cm = layoutData.getMargins();
      if (cm == null) {
        cm = new Margins(0);
        layoutData.setMargins(cm);
      }
    }

    if (findWidth || findHeight) {
      for (int i = 0, len = getWidgetCount(); i < len; i++) {
        Widget widget = getWidget(i);

        if (!widget.isVisible()) {
          continue;
        }

        BoxLayoutData layoutData = (BoxLayoutData) widget.getLayoutData();
        Margins cm = layoutData.getMargins();

        calculateWidth += widget.getOffsetWidth();
        maxWidgetHeight = Math.max(maxWidgetHeight, widget.getOffsetHeight());

        calculateWidth += (cm.getLeft() + cm.getRight());
        maxMarginTop = Math.max(maxMarginTop, cm.getTop());
        maxMarginBottom = Math.max(maxMarginBottom, cm.getBottom());
      }
      maxWidgetHeight += (maxMarginTop + maxMarginBottom);

      if (findWidth) {
        w = calculateWidth;
      }

      if (findHeight) {
        h = maxWidgetHeight;
      }
    }

    int pl = 0;
    int pt = 0;
    int pb = 0;
    int pr = 0;
    if (getPadding() != null) {
      pl = getPadding().getLeft();
      pt = getPadding().getTop();
      pb = getPadding().getBottom();
      pr = getPadding().getRight();
    }

    if (findHeight) {
      h += pt + pb;
    }
    if (findWidth) {
      w += pl + pr;
    }

    int stretchHeight = h - pt - pb;
    int totalFlex = 0;
    int totalWidth = 0;
    int maxHeight = 0;

    for (int i = 0, len = getWidgetCount(); i < len; i++) {
      Widget widget = getWidget(i);

      widget.addStyleName(CommonStyles.get().positionable());
      widget.getElement().getStyle().setMargin(0, Unit.PX);

      if (!widget.isVisible()) {
        continue;
      }

      if (widget == more) {
        triggerWidth = widget.getOffsetWidth() + 10;
      }

      BoxLayoutData layoutData = (BoxLayoutData) widget.getLayoutData();
      Margins cm = layoutData.getMargins();

      // TODO strange issue where getOffsetWidth call in 2nd loop is returning smaller number than actual offset
      // when packing CENTER or END so we cache the offsetWidth for use in 2nd loop
      // with buttons, the button is word wrapping causing the button to be narrower and taller
      int ww = widget.getOffsetWidth();
      loopWidthMap.put(widget, ww);
      loopHeightMap.put(widget, widget.getOffsetHeight());

      totalFlex += layoutData.getFlex();
      totalWidth += (ww + cm.getLeft() + cm.getRight());
      maxHeight = Math.max(maxHeight, widget.getOffsetHeight() + cm.getTop() + cm.getBottom());
    }

    int innerCtHeight = maxHeight + pt + pb;

    if (hBoxLayoutAlign.equals(HBoxLayoutAlign.STRETCH)) {
      getContainerTarget().setSize(w, h, true);
    } else if (hBoxLayoutAlign.equals(HBoxLayoutAlign.MIDDLE) || hBoxLayoutAlign.equals(HBoxLayoutAlign.BOTTOM)) {
      getContainerTarget().setSize(w, h = Math.max(h, innerCtHeight), true);
    } else {
      getContainerTarget().setSize(w, innerCtHeight, true);
    }

    int extraWidth = w - totalWidth - pl - pr;
    int allocated = 0;
    int componentWidth, componentHeight, componentTop;
    int availableHeight = h - pt - pb;

    if (getPack().equals(BoxLayoutPack.CENTER)) {
      pl += extraWidth / 2;
    } else if (getPack().equals(BoxLayoutPack.END)) {
      pl += extraWidth;
    }

    for (int i = 0, len = getWidgetCount(); i < len; i++) {
      Widget widget = getWidget(i);

      if (!widget.isVisible()) {
        continue;
      }

      BoxLayoutData layoutData = (BoxLayoutData) widget.getLayoutData();
      Margins cm = layoutData.getMargins();

      componentWidth = loopWidthMap.remove(widget);
      componentHeight = loopHeightMap.remove(widget);

      pl += cm.getLeft();

      pl = Math.max(0, pl);
      if (hBoxLayoutAlign.equals(HBoxLayoutAlign.MIDDLE)) {
        int diff = availableHeight - (componentHeight + cm.getTop() + cm.getBottom());
        if (diff == 0) {
          componentTop = pt + cm.getTop();
        } else {
          componentTop = pt + cm.getTop() + (diff / 2);
        }
      } else {
        if (hBoxLayoutAlign.equals(HBoxLayoutAlign.BOTTOM)) {
          componentTop = h - (pb + cm.getBottom() + componentHeight);
        } else {
          componentTop = pt + cm.getTop();
        }

      }

      boolean component = widget instanceof Component;
      Component c = null;
      if (component) {
        c = (Component) widget;
      }

      int width = -1;
      if (component) {
        c.setPosition(pl, componentTop);
      } else {
        XElement.as(widget.getElement()).setLeftTop(pl, componentTop);
      }

      if (getPack().equals(BoxLayoutPack.START) && layoutData.getFlex() > 0) {
        int add = (int) Math.floor(extraWidth * (layoutData.getFlex() / totalFlex));
        allocated += add;
        if (isAdjustForFlexRemainder() && i == getWidgetCount() - 1) {
          add += (extraWidth - allocated);
        }

        componentWidth += add;
        width = componentWidth;
      }
      if (hBoxLayoutAlign.equals(HBoxLayoutAlign.STRETCH)) {
        applyLayout(
            widget,
            width,
            Util.constrain(stretchHeight - cm.getTop() - cm.getBottom(), layoutData.getMinSize(),
                layoutData.getMaxSize()));
      } else if (hBoxLayoutAlign.equals(HBoxLayoutAlign.STRETCHMAX)) {
        applyLayout(widget, width,
            Util.constrain(maxHeight - cm.getTop() - cm.getBottom(), layoutData.getMinSize(), layoutData.getMaxSize()));
      } else if (width > 0) {
        applyLayout(widget, width, -1);
      }
      pl += componentWidth + cm.getRight();
    }

    // do we need overflow
    if (enableOverflow) {
      int runningWidth = 0;
      for (int i = 0, len = getWidgetCount(); i < len; i++) {
        Widget widget = getWidget(i);

        if (widget == more) {
          continue;
        }

        BoxLayoutData layoutData = null;
        Object d = widget.getLayoutData();
        if (d instanceof BoxLayoutData) {
          layoutData = (BoxLayoutData) d;
        } else {
          layoutData = new BoxLayoutData();
        }
        Margins cm = layoutData.getMargins();
        if (cm == null) {
          cm = new Margins(0);
        }
        runningWidth += getWidgetWidth(widget);
        runningWidth += cm.getLeft();
        runningWidth += cm.getRight();
      }

      if (runningWidth > w) {
        hasOverflow = true;
        onOverflow();
      } else {
        hasOverflow = false;
        if (more != null && more.getParent() == this) {
          onUnoverflow();
        }

      }
    }
  }

  protected int getWidgetWidth(Widget widget) {
    Integer w = widthMap.get(widget);
    if (w != null) {
      return w;
    } else {
      return widget.getOffsetWidth();
    }
  }

  protected void hideComponent(Widget w) {
    widthMap.put(w, w.getOffsetWidth());
    hiddens.add(w);
    w.setVisible(false);
  }

  protected void initMore() {
    if (more == null) {
      moreMenu = new Menu();
      moreMenu.addBeforeShowHandler(new BeforeShowHandler() {

        @Override
        public void onBeforeShow(BeforeShowEvent event) {
          clearMenu();

          for (int i = 0, len = getWidgetCount(); i < len; i++) {
            Widget w = getWidget(i);
            if (isHidden(w) && w != more) {
              addWidgetToMenu(moreMenu, w);
            }
          }

          HBoxLayoutContainer.this.fireEvent(new OverflowEvent(moreMenu));
        }
      });

      more = new TextButton();
      more.addStyleName("x-toolbar-more");
      more.addStyleName(getAppearance().moreButtonStyle());
      more.setData("x-ignore-width", true);
      more.setData("gxt-more", "true");
      more.setIcon(getAppearance().moreIcon());
      more.setMenu(moreMenu);
    }

    if (more.getParent() != this) {
      add(more);
    }
  }

  protected boolean isHidden(Widget w) {
    return hiddens != null && hiddens.contains(w);
  }

  @Override
  protected void onInsert(int index, Widget child) {
    super.onInsert(index, child);
    child.addStyleName(CommonStyles.get().floatLeft());
  }

  @Override
  protected void onRemove(Widget child) {
    super.onRemove(child);
    child.removeStyleName(CommonStyles.get().floatLeft());
  }

  protected void onOverflow() {
    Size size = getElement().getStyleSize();
    final int w = size.getWidth() - getScrollOffset() - triggerWidth;

    boolean change = false;

    int loopWidth = 0;
    for (int i = 0; i < getWidgetCount(); i++) {
      Widget widget = getWidget(i);
      if (widget == more) continue;

      if (!(widget instanceof FillToolItem)) {
        loopWidth += getWidgetWidth(widget);
        BoxLayoutData data = (BoxLayoutData) widget.getLayoutData();
        if (data != null && data.getMargins() != null) {
          loopWidth += (data.getMargins().getLeft() + data.getMargins().getRight());
        }
        if (loopWidth >= w) {
          if (!isHidden(widget)) {
            change = true;
            hideComponent(widget);
          }
        } else {
          if (isHidden(widget)) {
            change = true;
            unhideComponent(widget);
          }
        }
      }
    }

    if (hiddens != null && hiddens.size() > 0) {
      initMore();
    }

    if (change) {
      Scheduler.get().scheduleDeferred(new ScheduledCommand() {
        @Override
        public void execute() {
          forceLayout();
        }
      });
    }
  }

  protected void onUnoverflow() {
    if (more != null) {
      Scheduler.get().scheduleDeferred(new ScheduledCommand() {

        @Override
        public void execute() {
          for (int i = 0; i < getWidgetCount(); i++) {
            Widget widget = getWidget(i);
            unhideComponent(widget);
          }
          remove(more);
          forceLayout();
        }
      });
    }
  }

  protected void unhideComponent(Widget w) {
    if (hiddens.remove(w)) {
      widthMap.remove(w);
      w.setVisible(true);
    }
  }
}
