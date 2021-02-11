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

import com.google.gwt.dom.client.Document;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import com.google.gwt.uibinder.client.UiChild;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.GXT;
import com.sencha.gxt.core.client.dom.XElement;

/**
 * A base class for HTML layout containers. Provides behavior common to all HTML
 * layout containers, including the attachment of of the widget element to the
 * container's parent element. For a code snippet that illustrates the use of
 * this class, see {@link HtmlLayoutContainer}.
 */
public abstract class AbstractHtmlLayoutContainer extends Container {

  /**
   * Specifies HTML layout parameters, such as the mapping of each child to a
   * corresponding selector in the HTML template. For a code snippet that
   * illustrates the use of this class, see {@link HtmlLayoutContainer}.
   */
  public static class HtmlData extends MarginData {

    private String selector;

    /**
     * Creates an HTML layout parameter with the specified selector value.
     * 
     * @param selector identifies the element in the HTML template to which the
     *          associated widget is attached. If more than one element matches
     *          the selector, the first matching element is selected.
     */
    public HtmlData(String selector) {
      this.selector = selector;
    }

    /**
     * Returns the selector that identifies the element in the HTML template to
     * which the associated widget is attached.
     * 
     * @return the selector for the widget
     */
    public String getSelector() {
      return selector;
    }

    /**
     * Sets the selector that identifies the element in the HTML template to
     * which the associated widget is attached.
     * 
     * @param selector identifies the element in the HTML template to which the
     *          associated widget is attached. If more than one element matches
     *          the selector, the first matching element is selected.
     */
    public void setSelector(String selector) {
      this.selector = selector;
    }
  }

  private SafeHtml html = SafeHtmlUtils.EMPTY_SAFE_HTML;

  protected AbstractHtmlLayoutContainer() {
    setElement(Document.get().createDivElement());
  }

  /**
   * Adds a widget to the HTML layout container with the specified layout
   * parameters.
   * 
   * @param child the widget to add to the layout container
   * @param layoutData the parameters that describe how to lay out the widget
   */
  @UiChild(tagname = "child")
  public void add(IsWidget child, HtmlData layoutData) {
    if (child != null) {
      child.asWidget().setLayoutData(layoutData);
    }
    super.add(child);
  }

  @Override
  protected void doPhysicalAttach(Widget child, int beforeIndex) {
    Object layoutData = child.getLayoutData();
    if (layoutData instanceof HtmlData) {
      String selector = ((HtmlData) layoutData).getSelector();

      //Workaround for EXTGWT-2454
      if (selector.startsWith("#") && getElement().getOffsetParent() == null) {
        selector = "//" + selector;
      }

      XElement c = getContainerTarget().child(selector);
      if (c != null) {
        c.appendChild(child.getElement());
        return;
      }
    }
    super.doPhysicalAttach(child, beforeIndex);
  }

  protected SafeHtml getHTML() {
    return html;
  }

  @Override
  protected void onAfterFirstAttach() {
    super.onAfterFirstAttach();
    if (GXT.isIE8()) {
      getElement().repaint();
    }
  }

  protected void setHTML(SafeHtml html) {
    this.html = html;
    for (Widget w : this) {
      doPhysicalDetach(w);
    }
    getContainerTarget().setInnerSafeHtml(html);

    int i = 0;
    for (Widget w : this) {
      doPhysicalAttach(w, i++);
    }
  }

}
