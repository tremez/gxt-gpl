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
import com.google.gwt.dom.client.NativeEvent;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.GXT;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.util.Util;
import com.sencha.gxt.widget.core.client.event.BeforeCheckChangeEvent;
import com.sencha.gxt.widget.core.client.event.BeforeCheckChangeEvent.BeforeCheckChangeHandler;
import com.sencha.gxt.widget.core.client.event.BeforeCheckChangeEvent.HasBeforeCheckChangeHandlers;
import com.sencha.gxt.widget.core.client.event.CheckChangeEvent;
import com.sencha.gxt.widget.core.client.event.CheckChangeEvent.CheckChangeHandler;
import com.sencha.gxt.widget.core.client.event.CheckChangeEvent.HasCheckChangeHandlers;
import com.sencha.gxt.widget.core.client.tree.Tree.CheckState;

/**
 * Adds a menu item that contains a checkbox by default, but can also be part of a radio group.
 */
public class CheckMenuItem extends MenuItem implements HasBeforeCheckChangeHandlers<CheckMenuItem>,
    HasCheckChangeHandlers<CheckMenuItem> {

  public interface CheckMenuItemAppearance extends MenuItemAppearance {

    /**
     * @param parent The element representing the check menu item
     * @param state Whether to apply or remove the style
     */
    void applyChecked(XElement parent, boolean state);

    XElement getCheckIcon(XElement parent);

    ImageResource checked();

    ImageResource unchecked();

    ImageResource radio();

  }

  private final CheckMenuItemAppearance appearance;
  private boolean checked;
  private String group, groupTitle;

  /**
   * Creates a new check menu item.
   * 
   */
  public CheckMenuItem() {
    this(GWT.<CheckMenuItemAppearance>create(CheckMenuItemAppearance.class));
  }

  public CheckMenuItem(CheckMenuItemAppearance appearance) {
    super(appearance);
    this.appearance = appearance;

    hideOnClick = true;
    canActivate = true;

    // force render the checkbox
    setChecked(false);
  }

  /**
   * Creates a new check menu item.
   * 
   * @param text the text
   */
  public CheckMenuItem(String text) {
    this();
    setText(text);

    // force render
    // TODO appearance pattern not properly applied
    // check images should come from appearance
    setChecked(false);
  }

  @Override
  public HandlerRegistration addBeforeCheckChangeHandler(BeforeCheckChangeHandler<CheckMenuItem> handler) {
    return addHandler(handler, BeforeCheckChangeEvent.getType());
  }

  @Override
  public HandlerRegistration addCheckChangeHandler(CheckChangeHandler<CheckMenuItem> handler) {
    return addHandler(handler, CheckChangeEvent.getType());
  }

  public CheckMenuItemAppearance getCheckMenuItemAppearance() {
    return appearance;
  }

  /**
   * Returns the ARIA group title.
   * 
   * @return the group title
   */
  public String getAriaGroupTitle() {
    return groupTitle;
  }

  /**
   * Returns the group name.
   * 
   * @return the name
   */
  public String getGroup() {
    return group;
  }

  /**
   * Returns true if the item is checked.
   * 
   * @return the checked state
   */
  public boolean isChecked() {
    return checked;
  }

  /**
   * Sets the title attribute on the group container element. Only applies to radio check items when ARIA is enabled.
   * 
   * @param title the title
   */
  public void setAriaGroupTitle(String title) {
    this.groupTitle = title;
  }

  /**
   * Set the checked state of this item. If the item is in a group of CheckMenuItems, then it will toggle them as
   * radios.
   * 
   * @param checked the new checked state
   */
  public void setChecked(boolean checked) {
    setChecked(checked, false);

    toggleRadios();
  }

  /**
   * Set the checked state of this item.
   * 
   * @param state the new checked state
   * @param suppressEvent true to prevent the CheckChange event from firing
   */
  public void setChecked(boolean state, boolean suppressEvent) {
    if (suppressEvent
        || fireCancellableEvent(new BeforeCheckChangeEvent<CheckMenuItem>(this, state ? CheckState.CHECKED
            : CheckState.UNCHECKED))) {
      if (getGroup() == null) {
        setIcon(state ? appearance.checked() : appearance.unchecked());
        appearance.applyChecked(getElement(), state);
      } else {
        setIcon(state ? appearance.radio() : null);
      }
      checked = state;

      if (!suppressEvent) {
        fireEvent(new CheckChangeEvent<CheckMenuItem>(this, state ? CheckState.CHECKED : CheckState.UNCHECKED));
      }
    }
  }

  /**
   * All check items with the same group name will automatically be grouped into a single-select radio button group
   * (defaults to null).
   * 
   * @param group the group
   */
  public void setGroup(String group) {
    this.group = group;
    setChecked(checked, true);
  }

  protected void onClick(NativeEvent ce) {
    if (!disabled) {
      /* On touch devices, if a submenu is present on a checkbox menuItem, we want to make sure the user tapped the
       * checkbox icon (if one exists) before calling setChecked.
       *
       * On desktop, if a submenu is present, hovering the menuItem will open the submenu and clicking anywhere in the
       * menuItem will call setChecked.  Unfortunately, since touch uses click to both select an item and display the
       * submenu, opening the submenu will also call setChecked, which is not what we want to happen.
       */
      if (GXT.isTouch() && subMenu != null) {
        XElement target = ce.getEventTarget().cast();
        XElement checkIcon = appearance.getCheckIcon(getElement());
        if (checkIcon == null || checkIcon.isOrHasChild(target)) {
          setChecked(!checked);
        }
      } else {
        setChecked(!checked);
      }
    }
    super.onClick(ce);
  }

  protected void toggleRadios() {
    if (getGroup() != null && getParent() instanceof HasWidgets) {
      for (Widget w : ((HasWidgets) getParent())) {
        if (w instanceof CheckMenuItem) {
          CheckMenuItem check = (CheckMenuItem) w;
          if (check != this && Util.equalWithNull(group, check.getGroup()) && check.isChecked()) {
            check.setChecked(false, false);
          }
        }
      }
    }
  }

}
