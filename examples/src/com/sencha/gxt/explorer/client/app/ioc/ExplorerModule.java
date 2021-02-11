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
package com.sencha.gxt.explorer.client.app.ioc;

import com.google.gwt.activity.shared.ActivityMapper;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.shared.EventBus;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.inject.client.AbstractGinModule;
import com.google.gwt.place.shared.PlaceController;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.sencha.gxt.explorer.client.app.mvp.ExplorerActivityMapper;
import com.sencha.gxt.explorer.client.app.ui.ExampleDetailView;
import com.sencha.gxt.explorer.client.app.ui.ExampleDetailViewImpl;
import com.sencha.gxt.explorer.client.app.ui.ExampleListView;
import com.sencha.gxt.explorer.client.app.ui.ExampleListViewImpl;
import com.sencha.gxt.explorer.client.model.ExampleModel;
import com.sencha.gxt.explorer.shared.ExplorerRequestFactory;


public class ExplorerModule extends AbstractGinModule  {

  @Override
  protected void configure() {
    bind(EventBus.class).to(SimpleEventBus.class).in(Singleton.class);

    bind(ActivityMapper.class).to(ExplorerActivityMapper.class);

    // bind up some data
    bind(ExampleModel.class).in(Singleton.class);

    // View implementation binding - if we build a mobile explorer, this would
    // be broken out into another module so it could be rebound
    bind(ExampleListView.class).to(ExampleListViewImpl.class).in(Singleton.class);
    bind(ExampleDetailView.class).to(ExampleDetailViewImpl.class).in(Singleton.class);
  }
  
  @SuppressWarnings("deprecation")
  @Provides
  @Singleton
  PlaceController providePlaceController(EventBus eventBus) {
    return new PlaceController(eventBus);
  }
  
  @Provides
  @Singleton
  ExplorerRequestFactory provideRequestFactory(EventBus eventBus) {
    ExplorerRequestFactory rf = GWT.create(ExplorerRequestFactory.class);
    rf.initialize(eventBus);
    return rf;
  }


}
