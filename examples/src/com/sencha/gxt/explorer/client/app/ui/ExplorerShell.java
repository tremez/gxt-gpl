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
package com.sencha.gxt.explorer.client.app.ui;

import java.util.Arrays;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Float;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.CssResource;
import com.google.gwt.resources.client.CssResource.NotStrict;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.resources.client.ImageResource.ImageOptions;
import com.google.gwt.resources.client.ImageResource.RepeatStyle;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.AcceptsOneWidget;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.google.inject.Inject;
import com.sencha.gxt.cell.core.client.form.ComboBoxCell.TriggerAction;
import com.sencha.gxt.core.client.BindingPropertySet;
import com.sencha.gxt.core.client.BindingPropertySet.PropertyName;
import com.sencha.gxt.core.client.util.Margins;
import com.sencha.gxt.data.shared.ListStore;
import com.sencha.gxt.data.shared.ModelKeyProvider;
import com.sencha.gxt.data.shared.StringLabelProvider;
import com.sencha.gxt.state.client.BorderLayoutStateHandler;
import com.sencha.gxt.widget.core.client.ContentPanel;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer;
import com.sencha.gxt.widget.core.client.container.BorderLayoutContainer.BorderLayoutData;
import com.sencha.gxt.widget.core.client.container.BoxLayoutContainer.BoxLayoutData;
import com.sencha.gxt.widget.core.client.container.HBoxLayoutContainer;
import com.sencha.gxt.widget.core.client.container.HBoxLayoutContainer.HBoxLayoutAlign;
import com.sencha.gxt.widget.core.client.container.MarginData;
import com.sencha.gxt.widget.core.client.container.SimpleContainer;
import com.sencha.gxt.widget.core.client.form.ComboBox;

public class ExplorerShell implements IsWidget {

  private BorderLayoutContainer borderLayoutContainer = new BorderLayoutContainer();

  public enum Theme {
    BLUE("Blue Theme"), GRAY("Gray Theme"), NEPTUNE("Neptune Theme"), TRITON("Triton Theme");

    private final String value;

    private Theme(String value) {
      this.value = value;
    }

    public String value() {
      return value;
    }

    public boolean isActive() {
      ActiveTheme theme = GWT.create(ActiveTheme.class);
      switch (this) {
        case BLUE:
          return theme.isBlue();
        case GRAY:
          return theme.isGray();
        case NEPTUNE:
          return theme.isNeptune();
        case TRITON:
          return theme.isTriton();
      }
      return false;
    }

    @Override
    public String toString() {
      return value();
    }
  }

  @PropertyName("gxt.theme")
  public interface ActiveTheme extends BindingPropertySet {
    @PropertyValue(value = "gray", warn = false)
    boolean isGray();

    @PropertyValue(value = "blue", warn = false)
    boolean isBlue();

    @PropertyValue(value = "neptune", warn = false)
    boolean isNeptune();

    @PropertyValue(value = "triton", warn = false)
    boolean isTriton();
  }

  public interface Resources extends ClientBundle {
    @NotStrict
    @Source("explorerStyles.gss")
    CssResource explorerStyles();

    @Source("hd-bg.gif")
    @ImageOptions(repeatStyle = RepeatStyle.Horizontal)
    ImageResource classicBg();

    @ImageOptions(repeatStyle = RepeatStyle.Both)
    @Source("square.gif")
    ImageResource squareBg();
  }

  @Inject
  public ExplorerShell(ExampleListView listView, ExampleDetailView detailView) {
    Resources resource = GWT.create(Resources.class);
    resource.explorerStyles().ensureInjected();

    BorderLayoutData northData = new BorderLayoutData(42);

    SimpleContainer themeComboContainer = new SimpleContainer();
    themeComboContainer.getElement().getStyle().setMargin(3, Unit.PX);
    themeComboContainer.setResize(false);
    themeComboContainer.add(getThemeCombo());

    HTML title = new HTML("Sencha GXT Explorer");
    title.addStyleName("demo-title");

    HTML links = new HTML("<a href=\"http://docs.sencha.com/gxt/latest/\" target=\"_blank\">GXT Documentation</a>");
    links.addStyleName("demo-links");

    BoxLayoutData spacerFlex = new BoxLayoutData();
    spacerFlex.setFlex(1);

    HBoxLayoutContainer menuContainer = new HBoxLayoutContainer(HBoxLayoutAlign.MIDDLE);
    menuContainer.setStateful(false);
    menuContainer.add(title, new BoxLayoutData(new Margins(0, 15, 0, 10)));
    menuContainer.add(links, new BoxLayoutData(new Margins(0, 10, 0, 0)));
    menuContainer.add(new SimpleContainer(), spacerFlex);
    menuContainer.add(themeComboContainer, new BoxLayoutData(new Margins(0, 10, 0, 0)));

    menuContainer.setId("demo-header");
    menuContainer.addStyleName("x-small-editor");


    BorderLayoutData westData = new BorderLayoutData(260);
    westData.setMargins(Theme.NEPTUNE.isActive() || Theme.TRITON.isActive() ? new Margins(0) : new Margins(5, 0, 5, 5));
    westData.setCollapsible(true);
    westData.setCollapseHeaderVisible(true);

    ContentPanel westContainer = new ContentPanel();
    westContainer.setHeading("Navigation");
    westContainer.add(listView.asWidget());

    MarginData centerData = new MarginData();
    centerData.setMargins(Theme.TRITON.isActive() ? new Margins(0, 0, 0, 10) : Theme.NEPTUNE.isActive() ? new Margins(0, 0, 0, 8) : new Margins(5));

    SimpleContainer centerContainer = new SimpleContainer();

    centerContainer.add(detailView.asWidget());

    borderLayoutContainer.setNorthWidget(menuContainer, northData);
    borderLayoutContainer.setWestWidget(westContainer, westData);
    borderLayoutContainer.setCenterWidget(centerContainer, centerData);

    // State Manager, init state
    borderLayoutContainer.setStateful(true);
    borderLayoutContainer.setStateId("explorerLayout");

    // State Manager, load previous state
    BorderLayoutStateHandler state = new BorderLayoutStateHandler(borderLayoutContainer);
    state.loadState();
  }

  private ComboBox<Theme> getThemeCombo() {
    ListStore<Theme> colors = new ListStore<Theme>(new ModelKeyProvider<Theme>() {
      @Override
      public String getKey(Theme item) {
        return item.name();
      }
    });
    colors.addAll(Arrays.asList(Theme.values()));

    ComboBox<Theme> themeCombo = new ComboBox<Theme>(colors, new StringLabelProvider<Theme>());
    themeCombo.setTriggerAction(TriggerAction.ALL);
    themeCombo.setForceSelection(true);
    themeCombo.setEditable(false);
    themeCombo.getElement().getStyle().setFloat(Float.RIGHT);
    themeCombo.setWidth(125);
    themeCombo.setValue(Theme.GRAY.isActive() ? Theme.GRAY : Theme.BLUE.isActive() ? Theme.BLUE : Theme.NEPTUNE.isActive() ? Theme.NEPTUNE : Theme.TRITON);
    themeCombo.addSelectionHandler(new SelectionHandler<ExplorerShell.Theme>() {
      @Override
      public void onSelection(SelectionEvent<Theme> event) {
        switch (event.getSelectedItem()) {
          case BLUE:
            Window.Location.assign(GWT.getHostPageBaseURL() + "explorer-blue.html" + Window.Location.getHash());
            break;
          case GRAY:
            Window.Location.assign(GWT.getHostPageBaseURL() + "explorer-gray.html" + Window.Location.getHash());
            break;
          case NEPTUNE:
            Window.Location.assign(GWT.getHostPageBaseURL() + "explorer-neptune.html" + Window.Location.getHash());
            break;
          case TRITON:
            Window.Location.assign(GWT.getHostPageBaseURL() + "index.html" + Window.Location.getHash());
            break;
          default:
            assert false : "Unsupported theme enum";
        }
      }
    });

    return themeCombo;
  }

  @Override
  public Widget asWidget() {
    return borderLayoutContainer;
  }

  public AcceptsOneWidget getDisplay() {
    return borderLayoutContainer;
  }
}
