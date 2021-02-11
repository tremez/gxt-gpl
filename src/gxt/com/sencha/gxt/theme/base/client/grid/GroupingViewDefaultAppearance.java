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
package com.sencha.gxt.theme.base.client.grid;

import com.google.gwt.core.client.GWT;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource.Import;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.safehtml.shared.SafeHtml;
import com.sencha.gxt.core.client.XTemplates;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.resources.StyleInjectorHelper;
import com.sencha.gxt.widget.core.client.grid.GridView.GridStateStyles;
import com.sencha.gxt.widget.core.client.grid.GroupingView;
import com.sencha.gxt.widget.core.client.grid.GroupingView.GroupingData;
import com.sencha.gxt.widget.core.client.grid.GroupingView.GroupingViewAppearance;

public class GroupingViewDefaultAppearance implements GroupingViewAppearance {

  public interface DefaultHeaderTemplate extends XTemplates, GroupHeaderTemplate<Object> {
    @Override
    @XTemplate("{groupInfo.value}")
    public SafeHtml renderGroupHeader(GroupingData<Object> groupInfo);
  }

  public interface GroupHeaderTemplate<M> {
    SafeHtml renderGroupHeader(GroupingData<M> groupInfo);
  }

  public interface GroupingViewResources extends ClientBundle {

    ImageResource groupBy();

    @Import(GridStateStyles.class)
    @Source("GroupingView.gss")
    GroupingViewStyle style();
  }

  public interface GroupingViewStyle extends GroupingView.GroupingViewStyle {

  }

  public interface GroupTemplate<M> {
    SafeHtml renderGroup(GroupingView.GroupingViewStyle style, SafeHtml groupHeader, SafeHtml rows, SafeHtml groupSummary);
  }

  private final GroupingViewResources resources;
  private GroupingView.GroupingViewStyle style;
  private GroupHeaderTemplate<?> headerTemplate = GWT.create(DefaultHeaderTemplate.class);

  public GroupingViewDefaultAppearance() {
    this(GWT.<GroupingViewResources> create(GroupingViewResources.class));
  }

  public GroupingViewDefaultAppearance(GroupingViewResources resources) {
    this.resources = resources;
    this.style = this.resources.style();

    StyleInjectorHelper.ensureInjected(style, true);
  }

  @Override
  public XElement findHead(XElement element) {
    return element.findParent("." + style.groupHead(), 10);
  }

  @Override
  public XElement getGroup(XElement head) {
    return head.getParentElement().cast();
  }

  @Override
  public ImageResource groupByIcon() {
    return resources.groupBy();
  }

  @Override
  public boolean isCollapsed(XElement group) {
    return group.hasClassName(style.groupCollapsed());
  }

  @Override
  public void onGroupExpand(XElement group, boolean expanded) {
    group.setClassName(style.groupCollapsed(), !expanded);
    assert group.getNextSiblingElement() != null;
    group.getNextSiblingElement().<XElement>cast().setClassName(style.bodyCollapsed(), !expanded);
  }

  @SuppressWarnings({"unchecked", "rawtypes"})
  @Override
  public SafeHtml renderGroupHeader(GroupingData<?> groupInfo) {
    return headerTemplate.renderGroupHeader((GroupingData) groupInfo);
  }

  public void setHeaderTemplate(GroupHeaderTemplate<?> headerTemplate) {
    this.headerTemplate = headerTemplate;
  }

  @Override
  public GroupingView.GroupingViewStyle style() {
    return style;
  }

}
