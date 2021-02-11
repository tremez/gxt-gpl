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
package com.sencha.gxt.widget.core.client.menu;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.EventTarget;
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.dom.client.NodeList;
import com.google.gwt.dom.client.Style.Display;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.logical.shared.BeforeSelectionEvent;
import com.google.gwt.event.logical.shared.BeforeSelectionHandler;
import com.google.gwt.event.logical.shared.HasBeforeSelectionHandlers;
import com.google.gwt.event.logical.shared.HasSelectionHandlers;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.Event.NativePreviewEvent;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.impl.FocusImpl;
import com.sencha.gxt.core.client.GXT;
import com.sencha.gxt.core.client.Style;
import com.sencha.gxt.core.client.Style.Anchor;
import com.sencha.gxt.core.client.Style.AnchorAlignment;
import com.sencha.gxt.core.client.Style.HideMode;
import com.sencha.gxt.core.client.Style.Side;
import com.sencha.gxt.core.client.dom.XDOM;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.dom.XWindow;
import com.sencha.gxt.core.client.gestures.ScrollGestureRecognizer;
import com.sencha.gxt.core.client.gestures.ScrollGestureRecognizer.ScrollDirection;
import com.sencha.gxt.core.client.gestures.TapGestureRecognizer;
import com.sencha.gxt.core.client.gestures.TouchData;
import com.sencha.gxt.core.client.resources.CommonStyles;
import com.sencha.gxt.core.client.util.BaseEventPreview;
import com.sencha.gxt.core.client.util.ClickRepeater;
import com.sencha.gxt.core.client.util.ClickRepeaterEvent;
import com.sencha.gxt.core.client.util.ClickRepeaterEvent.ClickRepeaterHandler;
import com.sencha.gxt.core.client.util.KeyNav;
import com.sencha.gxt.core.client.util.Point;
import com.sencha.gxt.widget.core.client.Component;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.InsertContainer;
import com.sencha.gxt.widget.core.client.event.BeforeHideEvent;
import com.sencha.gxt.widget.core.client.event.BeforeShowEvent;
import com.sencha.gxt.widget.core.client.event.HideEvent;
import com.sencha.gxt.widget.core.client.event.MaximizeEvent;
import com.sencha.gxt.widget.core.client.event.MaximizeEvent.MaximizeHandler;
import com.sencha.gxt.widget.core.client.event.MinimizeEvent;
import com.sencha.gxt.widget.core.client.event.MinimizeEvent.MinimizeHandler;
import com.sencha.gxt.widget.core.client.event.ShowEvent;
import com.sencha.gxt.widget.core.client.form.Field;

/**
 * A menu widget.
 */
public class Menu extends InsertContainer implements HasBeforeSelectionHandlers<Item>, HasSelectionHandlers<Item> {

  public static interface MenuAppearance {

    void applyDateMenu(XElement element);

    XElement createItem(XElement parent, String childId, boolean needsIndent);

    XElement getBottomScroller(XElement parent);

    XElement getGroup(XElement element, String id, String group);

    NodeList<Element> getGroups(XElement element);

    XElement getMenuList(XElement element);

    NodeList<Element> getScrollers(XElement element);

    XElement getTopScroller(XElement parent);

    boolean hasScrollers(XElement parent);

    String noSeparatorClass();

    void onScrollerOut(XElement target);

    String plainClass();

    void render(SafeHtmlBuilder builder);
  }

  private final MenuAppearance appearance;
  protected BaseEventPreview eventPreview;
  protected KeyNav keyNav;
  protected Item parentItem;
  protected boolean plain;
  protected boolean showSeparator = true;
  protected Item activeItem; 

  private int activeMax;
  private boolean constrainViewport = true;
  private String defaultAlign = "tl-bl?";
  private boolean enableScrolling = true;
  private boolean focusOnShow = true;
  private int maxHeight = Style.DEFAULT;
  private int minWidth = 120;
  private Element onHideFocusElement;
  private int scrollerHeight = 8;
  private int scrollIncrement = 24;
  private boolean showing;
  private String subMenuAlign = "tl-tr?";
  private XElement ul;

  public Menu() {
    this(GWT.<MenuAppearance>create(MenuAppearance.class));
  }

  public Menu(MenuAppearance appearance) {
    this.appearance = appearance;

    SafeHtmlBuilder builder = new SafeHtmlBuilder();
    appearance.render(builder);

    setElement((Element) XDOM.create(builder.toSafeHtml()));
    getElement().makePositionable(true);

    ul = appearance.getMenuList(getElement());

    monitorWindowResize = true;
    shim = true;
    setShadow(true);
    setDeferHeight(true);

    eventPreview = new BaseEventPreview() {
      @Override
      protected boolean onPreview(NativePreviewEvent pe) {
        Menu.this.onPreviewEvent(pe);
        return super.onPreview(pe);
      }

      @Override
      protected void onPreviewKeyPress(NativePreviewEvent pe) {
        super.onPreviewKeyPress(pe);
        onEscape(pe);
      }
    };

    // add menu to ignore list
    eventPreview.getIgnoreList().add(getElement());
    eventPreview.setAutoHide(false);

    getElement().setTabIndex(0);
    getElement().setAttribute("hideFocus", "true");
    getElement().addClassName(CommonStyles.get().ignore());

    keyNav = new KeyNav(this) {
      public void onDown(NativeEvent evt) {
        onKeyDown(evt);
      }

      public void onEnter(NativeEvent evt) {
        onKeyEnter(evt);
      }

      public void onLeft(NativeEvent evt) {
        onKeyLeft(evt);
      }

      public void onRight(NativeEvent evt) {
        onKeyRight(evt);
      }

      public void onUp(NativeEvent evt) {
        onKeyUp(evt);
      }

      @Override
      public void onTab(NativeEvent evt) {
        if (evt.getShiftKey()) {
          onKeyUp(evt);
        } else {
          onKeyDown(evt);
        }
      }
    };

    sinkEvents(Event.MOUSEEVENTS | Event.ONCLICK | Event.ONMOUSEWHEEL);

    // observe tap clicks on sub menu items in particular
    addGestureRecognizer(new TapGestureRecognizer() {

      @Override
      protected void onTap(TouchData touchData) {
        super.onTap(touchData);
        Menu.this.onTap(touchData);
      }

      @Override
      protected void handlePreventDefault(NativeEvent event) {
        // allow events to flow for menu items to use (such as ComboBox, Filters, etc)
      }
    });
  }

  @Override
  public HandlerRegistration addBeforeSelectionHandler(BeforeSelectionHandler<Item> handler) {
    return addHandler(handler, BeforeSelectionEvent.getType());
  }

  /**
   * Fires when the {@link Menu} is maximized.
   *
   * @param handler sets the {@link MaximizeHandler}
   * @return the {@link HandlerRegistration}.
   */
  public HandlerRegistration addMaximizeHandler(MaximizeHandler handler) {
    return addHandler(handler, MaximizeEvent.getType());
  }

  /**
   * Fires when the {@link Menu} is minimized.
   *
   * @param handler sets the {@link MinimizeHandler}.
   * @return the {@link HandlerRegistration}.
   */
  public HandlerRegistration addMinimizeHandler(MinimizeHandler handler) {
    return addHandler(handler, MinimizeEvent.getType());
  }

  @Override
  public HandlerRegistration addSelectionHandler(SelectionHandler<Item> handler) {
    return addHandler(handler, SelectionEvent.getType());
  }

  /**
   * Returns the appearance object for this instance
   *
   * @return the appearance impl used by this component
   */
  public MenuAppearance getAppearance() {
    return appearance;
  }

  /**
   * Returns the default alignment.
   *
   * @return the default align
   */
  public String getDefaultAlign() {
    return defaultAlign;
  }

  /**
   * Returns the max height of the menu or -1 if not set.
   *
   * @return the max height in pixels
   */
  public int getMaxHeight() {
    return maxHeight;
  }

  /**
   * Returns the menu's minimum width.
   *
   * @return the width
   */
  public int getMinWidth() {
    return minWidth;
  }

  /**
   * Returns the menu's parent item.
   *
   * @return the parent item
   */
  public Item getParentItem() {
    return parentItem;
  }

  /**
   * Returns the sub menu alignment.
   *
   * @return the alignment
   */
  public String getSubMenuAlign() {
    return subMenuAlign;
  }

  /**
   * Hides the menu.
   */
  public void hide() {
    hide(false);
  }

  /**
   * Hides this menu and optionally all parent menus
   *
   * @param deep true to close all parent menus
   */
  public void hide(final boolean deep) {
    if (showing) {
      if (fireCancellableEvent(new BeforeHideEvent())) {

        if (activeItem != null) {
          activeItem.deactivate();
          activeItem = null;
        }

        onHide();

        Scheduler.get().scheduleDeferred(new ScheduledCommand() {
          @Override
          public void execute() {
            RootPanel.get().remove(Menu.this);
            // if document.activeElement is null or the body element, focus on onHideFocusElement
            if (onHideFocusElement != null &&
              (XDOM.getActiveElement() == null
                || XElement.as(XDOM.getActiveElement()).isOrHasChild(XElement.as(Document.get().getBody())) // IE8 sets activeElement to HTML
              )) {
              // EXTGWT-4175: for touch we don't want to focus on the input every time we hide - keyboard will pop up
              if (!GXT.isTouch()) {
                FocusImpl.getFocusImplForPanel().focus(onHideFocusElement);
              }
            }
          }
        });

        eventPreview.remove();
        showing = false;
        hidden = true;
        fireEvent(new HideEvent());
        if (deep && parentItem != null && parentItem.getParent() instanceof Menu) {
          ((Menu) parentItem.getParent()).hide(true);
        }

      }
    }
  }

  /**
   * Returns true if constrain to viewport is enabled.
   *
   * @return the constrain to viewport state
   */
  public boolean isConstrainViewport() {
    return constrainViewport;
  }

  /**
   * Returns true if vertical scrolling is enabled.
   *
   * @return true for scrolling
   */
  public boolean isEnableScrolling() {
    return enableScrolling;
  }

  /**
   * Returns true if the menu will be focused when displayed.
   *
   * @return true if focused
   */
  public boolean isFocusOnShow() {
    return focusOnShow;
  }

  @Override
  public boolean isVisible() {
    return showing;
  }

  @Override
  public void onBrowserEvent(Event event) {
    super.onBrowserEvent(event);
    switch (event.getTypeInt()) {
      case Event.ONCLICK:
        onClick(event);
        break;
      case Event.ONMOUSEMOVE:
        onMouseMove(event);
        break;
      case Event.ONMOUSEOUT:
        onMouseOut(event);
        break;
      case Event.ONMOUSEOVER:
        onMouseOver(event);
        break;
      case Event.ONMOUSEWHEEL:
        if (enableScrolling) {
          // Windows and Chrome sensitivity isn't reflected through this call
          boolean top = event.getMouseWheelVelocityY() < 0;
          if (GXT.isIE() || GXT.isChrome()) {
            top = getWheelDelta(event) > 0;
          }
          scrollMenu(top);
        }
    }
  }

  /**
   * Sets the active item. The widget must be of type <code>Item</code> to be activated. All other types are ignored.
   *
   * @param widget     the widget to set active
   * @param autoExpand true to auto expand the item
   */
  public void setActiveItem(Widget widget, boolean autoExpand) {
    if (widget == null) {
      deactivateActiveItem();
      return;
    }
    if (widget instanceof Item) {
      Item item = (Item) widget;
      if (item != activeItem) {
        deactivateActiveItem();

        this.activeItem = item;
        item.activate(autoExpand);
        item.getElement().scrollIntoView(ul, false);
        focus();

      } else if (autoExpand) {
        item.expandMenu(autoExpand);
      }
    }
  }

  /**
   * Sets whether the menu should be constrained to the viewport when shown. Only applies when using
   * {@link #showAt(int, int)}.
   *
   * @param constrainViewport true to constrain
   */
  public void setConstrainViewport(boolean constrainViewport) {
    this.constrainViewport = constrainViewport;
  }

  /**
   * Sets the default {@link XElement#alignTo} anchor position value for this menu relative to its element of origin
   * (defaults to "tl-bl?").
   *
   * @param defaultAlign the default align
   */
  public void setDefaultAlign(String defaultAlign) {
    this.defaultAlign = defaultAlign;
  }

  /**
   * True to enable vertical scrolling of the children in the menu (defaults to true).
   *
   * @param enableScrolling true to for scrolling
   */
  public void setEnableScrolling(boolean enableScrolling) {
    this.enableScrolling = enableScrolling;
  }

  /**
   * True to set the focus on the menu when it is displayed.
   *
   * @param focusOnShow true to focus
   */
  public void setFocusOnShow(boolean focusOnShow) {
    this.focusOnShow = focusOnShow;
  }

  /**
   * Sets the max height of the menu (defaults to -1). Only applies when {@link #setEnableScrolling(boolean)} is set to
   * true.
   *
   * @param maxHeight the max height
   */
  public void setMaxHeight(int maxHeight) {
    this.maxHeight = maxHeight;
  }

  /**
   * Sets he minimum width of the menu in pixels (defaults to 120).
   *
   * @param minWidth the minimum width
   */
  public void setMinWidth(int minWidth) {
    this.minWidth = minWidth;
  }

  /**
   * Sets the element that should receive focus onHide unless the onHide was triggered by
   * focusing on a different element. This is typically set on the top-level menu rather than
   * a sub menu
   *
   * @param onHideFocusElement the element to focus on during onHide
   */
  public void setOnHideFocusElement(Element onHideFocusElement) {
    this.onHideFocusElement = onHideFocusElement;
  }

  /**
   * The {@link XElement#alignTo} anchor position value to use for submenus of this menu (defaults to "tl-tr-?").
   *
   * @param subMenuAlign the sub alignment
   */
  public void setSubMenuAlign(String subMenuAlign) {
    this.subMenuAlign = subMenuAlign;
  }

  /**
   * Displays this menu relative to another element.
   *
   * @param elem      the element to align to
   * @param alignment the {@link XElement#alignTo} anchor position to use in aligning to the element (defaults to
   *                  defaultAlign)
   */
  public void show(Element elem, AnchorAlignment alignment) {
    show(elem, alignment, 0, 0);
  }

  /**
   * Displays this menu relative to another element.
   *
   * @param elem      the element to align to
   * @param alignment the {@link XElement#alignTo} anchor position to use in aligning to the element (defaults to
   *                  defaultAlign)
   * @param offsetX   X offset
   * @param offsetY   Y offset
   */
  public void show(Element elem, AnchorAlignment alignment, int offsetX, int offsetY) {
    if (!fireCancellableEvent(new BeforeShowEvent())) {
      return;
    }

    getElement().makePositionable(true);
    RootPanel.get().add(this);

    onShow();
    getElement().updateZIndex(0);

    showing = true;
    doAutoSize();

    getElement().alignTo(elem, alignment, offsetX, offsetY);

    if (enableScrolling) {
      constrainScroll(getElement().getY());
    }
    showElement();

    eventPreview.add();

    if (focusOnShow) {
      focus();
    }
    fireEvent(new ShowEvent());
  }

  /**
   * Displays the element only.
   * <p/>
   * Override this method to provide an animation when showing the menu.
   * <pre><code>
   * Menu menu1 = new Menu() {
   *   @Override
   *   protected void showElement() {
   *     getElement().<FxElement> cast().slideIn(Direction.DOWN);
   *   }
   * };
   * </code></pre>
   */
  protected void showElement() {
    getElement().show();
  }

  /**
   * Displays this menu relative to the widget using the default alignment.
   *
   * @param widget the align widget
   */
  public void show(Widget widget) {
    show(widget.getElement(), new AnchorAlignment(Anchor.TOP_LEFT, Anchor.BOTTOM_LEFT));
  }

  /**
   * Displays this menu at a specific xy position.
   *
   * @param x the x coordinate
   * @param y the y coordinate
   */
  public void showAt(int x, int y) {
    if (!fireCancellableEvent(new BeforeShowEvent())) {
      return;
    }

    getElement().makePositionable(true);
    RootPanel.get().add(this);

    onShow();
    getElement().updateZIndex(0);

    showing = true;
    doAutoSize();

    if (constrainViewport) {
      Point p = getElement().adjustForConstraints(new Point(x, y));
      x = p.getX();
      y = p.getY();
    }
    setPagePosition(x + XDOM.getBodyScrollLeft(), y + XDOM.getBodyScrollTop());
    if (enableScrolling) {
      constrainScroll(y);
    }

    showElement();
    eventPreview.add();

    if (focusOnShow) {
      focus();
    }

    fireEvent(new ShowEvent());
  }

  protected void constrainScroll(int y) {
    ul.getStyle().setProperty("height", "auto");
    int full = ul.getOffsetHeight();

    int max = maxHeight != Style.DEFAULT ? maxHeight : (XDOM.getViewHeight(false) - y);
    if (full > max && max > 0) {
      activeMax = max - 10 - scrollerHeight * 2;
      ul.setHeight(activeMax, true);
      createScrollers();
    } else {
      ul.setHeight(full, true);
      NodeList<Element> nodes = appearance.getScrollers(getElement());
      for (int i = 0; i < nodes.getLength(); i++) {
        nodes.getItem(i).getStyle().setDisplay(Display.NONE);
      }
    }
    ul.setScrollTop(0);
  }

  protected void createScrollers() {
    if (appearance.hasScrollers(getElement())) {
      // Scrollers already created
      return;
    }

    ClickRepeaterHandler handler = new ClickRepeaterHandler() {
      @Override
      public void onClick(ClickRepeaterEvent event) {
        onScroll(event);
      }
    };

    XElement topScroller = appearance.getTopScroller(getElement());
    new ClickRepeater(this, topScroller).addClickHandler(handler);

    XElement bottomScroller = appearance.getBottomScroller(getElement());
    new ClickRepeater(this, bottomScroller).addClickHandler(handler);

    addGestureRecognizer(new ScrollGestureRecognizer(ul, ScrollDirection.VERTICAL));
  }

  protected void deactivateActiveItem() {
    if (activeItem != null) {
      activeItem.deactivate();
      activeItem = null;
    }
  }

  protected void doAutoSize() {
    // has a width been set?
    String w = getElement().getStyle().getWidth();
    if (showing && "".equals(w)) {
      int width = getContainerTarget().getOffsetWidth() + getElement().getFrameWidth(Side.LEFT, Side.RIGHT);
      getElement().setWidth(Math.max(width, minWidth), true);
    }
  }

  @Override
  protected void doPhysicalAttach(Widget child, int beforeIndex) {
    boolean needsIndent = (child instanceof Field) || (child instanceof ContentPanel)
      || (child instanceof AdapterMenuItem && ((AdapterMenuItem) child).isNeedsIndent());
    XElement div = appearance.createItem(getElement(), child.getElement().getId(), needsIndent);

    div.appendChild(child.getElement());
    if (child instanceof Component) {
      Component c = (Component) child;
      if (!c.isEnabled()) {
        c.disable();
      }
    }
    getContainerTarget().insertChild(div, beforeIndex);

  }

  @Override
  protected void doPhysicalDetach(Widget child) {
    Element parentElement = child.getElement().getParentElement();
    if (parentElement != null) {
      parentElement.removeFromParent();
    }
    super.doPhysicalDetach(child);
  }

  @Override
  protected XElement getContainerTarget() {
    return ul;
  }

  @Override
  protected void onAfterFirstAttach() {
    super.onAfterFirstAttach();
    if (plain) {
      getElement().addClassName(appearance.plainClass());
    }
    if (!showSeparator) {
      getElement().addClassName(appearance.noSeparatorClass());
    }
  }

  /**
   * Provides information about external events occurring in the DOM, allow the menu to decide to hide if necessary
   */
  protected void onPreviewEvent(NativePreviewEvent pe) {
    int type = pe.getTypeInt();
    switch (type) {
      case Event.ONMOUSEDOWN:
      case Event.ONMOUSEWHEEL:
      case Event.ONSCROLL:
      case Event.ONKEYPRESS:
      case Event.ONTOUCHSTART:
        XElement target = pe.getNativeEvent().getEventTarget().cast();

        // ignore targets within a parent with x-ignore, such as the listview in
        // a combo
        if (target.findParent(".x-ignore", 10) != null) {
          return;
        }

        // is the target part of the sub menu chain, if yes, dont out hide
        Item active = activeItem != null ? activeItem : getParentItem();
        while (active != null) {
          if (active instanceof MenuItem) {
            MenuItem menuItem = (MenuItem) active;
            if (menuItem.getParent().getElement().isOrHasChild(target)) {
              return;
            }
            if (menuItem.getSubMenu() != null && menuItem.getSubMenu().isVisible()) {
              if (menuItem.getSubMenu().getElement().isOrHasChild(target)) {
                return;
              }
              active = menuItem.getSubMenu().activeItem;
            } else {
              active = null;
            }
          } else {
            active = null;
          }
        }

        if (!getElement().isOrHasChild(target)) {
          hide(true);
          return;
        }
        break;
    }
  }

  /**
   * On a tap, either open a submenu or select and hide the menu.
   */
  protected void onTap(TouchData touchData) {
    Event event = touchData.getLastNativeEvent().cast();

    Widget itemWidget = findWidget((Element) event.getEventTarget().cast());

    if (itemWidget instanceof MenuItem) {
      final MenuItem menuItem = (MenuItem) itemWidget;

      // Has a submenu, so don't hide menu on tap
      if (menuItem.hasSubMenu() && menuItem.isCanActivate() && menuItem.isEnabled()) {
        final boolean hideOnClick = menuItem.isHideOnClick();
        menuItem.setHideOnClick(false);
        setActiveItem(menuItem, true);
        onClick(event); // still want to call click even if menuItem has a submenu

        // wait a moment after submenu renders
        Scheduler.get().scheduleFinally(new Command() {
          @Override
          public void execute() {
            menuItem.setHideOnClick(hideOnClick);
          }
        });
      } else {
        // Is an item, so close on tap, this is going to be a selection
        if (activeItem != menuItem && menuItem.isCanActivate() && menuItem.isEnabled()) {
          final boolean hideOnClick = menuItem.isHideOnClick();
          menuItem.setHideOnClick(true);
          // process the tap as a click
          onClick(event);
          menuItem.setHideOnClick(hideOnClick);
        }
      }
    }
  }

  protected void onClick(Event ce) {
    Widget item = findWidget((Element) ce.getEventTarget().cast());
    if (item != null && item instanceof Item) {
      ((Item) item).onClick(ce);
    }
  }

  protected void onEscape(NativePreviewEvent pe) {
    if (pe.getNativeEvent().getKeyCode() == KeyCodes.KEY_ESCAPE) {
      if (activeItem != null && !activeItem.onEscape()) {
        return;
      }
      pe.getNativeEvent().preventDefault();
      pe.getNativeEvent().stopPropagation();
      hide(false);
    }
  }

  @Override
  protected void onHide() {
    super.onHide();
    deactivateActiveItem();
  }

  protected void onKeyDown(NativeEvent evt) {
    evt.stopPropagation();
    evt.preventDefault();
    if (tryActivate(getWidgetIndex(activeItem) + 1, 1) == null) {
      tryActivate(0, 1);
    }
  }

  protected void onKeyEnter(NativeEvent evt) {
    if (activeItem != null) {
      evt.stopPropagation();
      activeItem.onClick(evt);
    }
  }

  protected void onKeyLeft(NativeEvent evt) {
    hide();
    if (parentItem != null && parentItem.getParent() instanceof Menu) {
      ((Menu) parentItem.getParent()).focus();
    } else {
      Menu menu = Menu.this;
      while (menu.parentItem != null && menu.parentItem.getParent() instanceof Menu) {
        menu = (Menu) menu.parentItem.getParent();
      }
      menu.fireEvent(new MinimizeEvent());
    }
  }

  protected void onKeyRight(NativeEvent evt) {
    if (activeItem != null) {
      activeItem.expandMenu(true);
    }
    if (activeItem instanceof MenuItem) {
      MenuItem mi = (MenuItem) activeItem;
      if (mi.subMenu != null && mi.subMenu.isVisible()) {
        return;
      }
    }
    Menu menu = Menu.this;
    while (menu.parentItem != null && menu.parentItem.getParent() instanceof Menu) {
      menu = (Menu) menu.parentItem.getParent();
    }
    menu.fireEvent(new MaximizeEvent());
  }

  protected void onKeyUp(NativeEvent evt) {
    evt.stopPropagation();
    evt.preventDefault();
    if (tryActivate(getWidgetIndex(activeItem) - 1, -1) == null) {
      tryActivate(getWidgetCount() - 1, -1);
    }
  }

  protected void onMouseMove(Event ce) {
    Widget c = findWidget((Element) ce.getEventTarget().cast());
    if (c != null && c instanceof Item) {
      Item item = (Item) c;
      if (activeItem != item && item.canActivate && item.isEnabled()) {
        setActiveItem(item, true);
      }
    }
  }

  protected void onMouseOut(Event ce) {
    EventTarget to = ce.getRelatedEventTarget();
    if (activeItem != null && (to == null || (Element.is(to) && !activeItem.getElement().isOrHasChild(Element.as(to))))
      && activeItem.shouldDeactivate(ce)) {
      if (to != null && Element.is(to)) {
        XElement xto = to.cast();
        if (xto.findParent("." + CommonStyles.get().ignore(), 3) != null) {
          return;
        }
      }
      deactivateActiveItem();
    }
  }

  protected void onMouseOver(Event ce) {
    EventTarget from = ce.getRelatedEventTarget();
    if (from == null || (Element.is(from) && !getElement().isOrHasChild(Element.as(from)))) {
      Widget c = findWidget((Element) ce.getEventTarget().cast());
      if (c != null && c instanceof Item) {
        Item item = (Item) c;
        if (activeItem != item && item.canActivate && item.isEnabled()) {
          setActiveItem(item, true);
        }
      }
    }
  }

  protected void onScroll(ClickRepeaterEvent ce) {
    XElement target = ce.getSource().getEl();
    boolean top = appearance.getTopScroller(getElement()).equals(target);
    scrollMenu(top);

    if (top ? ul.getScrollTop() <= 0 : ul.getScrollTop() + activeMax >= ul.getPropertyInt("scrollHeight")) {
      appearance.onScrollerOut(target);
    }
  }

  @Override
  protected void onWidgetHide(Widget widget) {
    super.onWidgetHide(widget);
    Element p = widget.getElement().getParentElement();
    if (widget instanceof Component) {
      p.addClassName(((Component) widget).getHideMode().value());
    } else {
      p.addClassName(HideMode.DISPLAY.value());
    }
  }

  @Override
  protected void onWidgetShow(Widget widget) {
    super.onWidgetShow(widget);
    Element p = widget.getElement().getParentElement();
    if (widget instanceof Component) {
      p.removeClassName(((Component) widget).getHideMode().value());
    } else {
      p.removeClassName(HideMode.DISPLAY.value());
    }
  }

  @Override
  protected void onWindowResize(int width, int height) {
    boolean doHide = true;
    if (GXT.isTouch()) {
      // should we prevent hide?
      // If resize occurs due to focusing an input and bringing up
      // virtual Keyboard, let's not hide
      Element activeElement = XDOM.getActiveElement();
      if (activeElement.hasTagName("INPUT")) {
        doHide = false;
      }

      /* on touch devices, a window resize can mean only one of two things: either there was a change in orientation or
       * the virtual keyboard was displayed/hidden.  If we determine that the last resize was not an orientation change,
       * we can assume it was the virtual keyboard - in which case, we do not want to  hide the menu.
       */
      if (!XWindow.isLastResizeOrientationChange()) {
        doHide = false;
      }
    }

    super.onWindowResize(width, height);
    if (doHide) {
      hide(true);
    }
  }

  protected void scrollMenu(boolean top) {
    ul.setScrollTop(ul.getScrollTop() + scrollIncrement * (top ? -1 : 1));
  }

  protected Item tryActivate(int start, int step) {
    for (int i = start, len = getWidgetCount(); i >= 0 && i < len; i += step) {
      Widget w = getWidget(i);
      if (w instanceof Item) {
        Item item = (Item) w;
        if (item.canActivate && item.isEnabled()) {
          setActiveItem(item, false);
          return item;
        }
      }
    }
    return null;
  }

  /**
   * Returns the IE wheelDelta on mouse scrolling.
   */
  private native int getWheelDelta(Event event) /*-{
    return event.wheelDelta;
  }-*/;

}
