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
package com.sencha.gxt.widget.core.client.form.error;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.editor.client.EditorError;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.event.logical.shared.HasResizeHandlers;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.sencha.gxt.core.client.Style.Anchor;
import com.sencha.gxt.core.client.Style.AnchorAlignment;
import com.sencha.gxt.core.client.Style.HideMode;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.shared.event.GroupingHandlerRegistration;
import com.sencha.gxt.widget.core.client.ComponentHelper;
import com.sencha.gxt.widget.core.client.WidgetComponent;
import com.sencha.gxt.widget.core.client.tips.Tip.TipAppearance;
import com.sencha.gxt.widget.core.client.tips.ToolTip;

public class SideErrorHandler implements ErrorHandler {

  /**
   * Marker interface to indicate that we want a slightly different appearance than usual, to indicate that this is an
   * error, and not help text.
   */
  public interface SideErrorTooltipAppearance extends TipAppearance {

  }

  public interface SideErrorResources extends ClientBundle {

    @Source("exclamation.gif")
    ImageResource errorIcon();
  }

  private class Handler implements AttachEvent.Handler, ResizeHandler {
    @Override
    public void onAttachOrDetach(AttachEvent event) {
      if (event.isAttached()) {
        doAttach();
      } else {
        doDetach();
      }
    }
    @Override
    public void onResize(ResizeEvent event) {
      adjustSize();
    }
  }

  protected Widget target;
  protected WidgetComponent errorIcon;
  protected final SideErrorResources resources;
  protected ToolTip tip;

  private List<EditorError> errors;
  private boolean showingError;
  private boolean adjustTargetWidth = true;
  private int originalWidth = -1;

  private GroupingHandlerRegistration handlers = new GroupingHandlerRegistration();

  public SideErrorHandler(Widget target) {
    this.target = target;

    Handler handler = new Handler();
    handlers.add(target.addAttachHandler(handler));

    if (target.isAttached()) {
      doAttach();
    }

    if (target instanceof HasResizeHandlers) {
      handlers.add(((HasResizeHandlers) target).addResizeHandler(handler));
    }

    resources = GWT.create(SideErrorResources.class);
  }

  @Override
  public void clearInvalid() {
    assert handlers != null : "The error handler has already been removed from the field, please create a fresh instance rather than reusing an old one";
    this.errors = null;
    if (errorIcon != null) {
      restoreSize();

      ComponentHelper.doDetach(errorIcon);
      errorIcon.hide();
      target.getElement().setAttribute("aria-describedby", "");
      showingError = false;
    }
  }

  /**
   * Returns {@code true} if the target width is adjusted.
   * 
   * @return the target width resize state
   */
  public boolean isAdjustTargetWidth() {
    return adjustTargetWidth;
  }

  @Override
  public void markInvalid(List<EditorError> errors) {
    assert handlers != null : "The error handler has already been removed from the field, please create a fresh instance rather than reusing an old one";
    if (errors.size() == 0) {
      clearInvalid();
      return;
    }

    this.errors = errors;
    if (!target.isAttached()) {
      // follow up later, after attached
      return;
    }

    String error = errors.get(0).getMessage();

    if (showingError && tip != null) {
      tip.getToolTipConfig().setBody(error);
      tip.update(tip.getToolTipConfig());
      return;
    }

    showingError = true;

    if (errorIcon == null) {
      errorIcon = new WidgetComponent(new Image(resources.errorIcon()));
      errorIcon.setHideMode(HideMode.VISIBILITY);
      errorIcon.hide();

      Element p = target.getElement().getParentElement();
      p.appendChild(errorIcon.getElement());

      errorIcon.getElement().setDisplayed(true);
      errorIcon.getElement().makePositionable(true);

    } else if (!errorIcon.getElement().isConnected()) {
      errorIcon.setHideMode(HideMode.VISIBILITY);
      errorIcon.hide();
      Element p = target.getElement().getParentElement();
      p.appendChild(errorIcon.getElement());
    }

    if (tip == null) {
      tip = new ToolTip(errorIcon, GWT.<SideErrorTooltipAppearance> create(SideErrorTooltipAppearance.class));
    }

    if (!errorIcon.isAttached()) {
      ComponentHelper.doAttach(errorIcon);
    }

    adjustSize();

    errorIcon.getElement().setVisibility(false);
    errorIcon.show();
    alignErrorIcon();

    // needed to prevent flickering
    Scheduler.get().scheduleDeferred(new ScheduledCommand() {
      @Override
      public void execute() {
        if (errorIcon.isAttached()) {
          errorIcon.show();
          alignErrorIcon();
          errorIcon.getElement().setVisibility(true);
        }
      }
    });

    tip.getToolTipConfig().setBody(error);
    tip.update(tip.getToolTipConfig());
  }

  /**
   * Adjusts the size of the widget to account for the side icon, if visible. May be called multiple times, whether or
   * not an error is displayed, and must be called again when size changes to account for that change.
   */
  protected void adjustSize() {
    // Only run if showing a tooltip
    if (!this.showingError) {
      return;
    }
    if (adjustTargetWidth) {
      int w = target.getElement().<XElement> cast().getStyleSize().getWidth();

      if (w != -1) {
        originalWidth = w;

        // we're currently adjusting, so ignore any incoming attempts to adjust
        this.adjustTargetWidth = false;
        target.setWidth(w - 18 + "px");
        this.adjustTargetWidth = true;
      }
    }
    alignErrorIcon();
  }

  /**
   * Restores the widget to its original size. Need only be called when in the process of getting rid of an error, when
   * showingError is still true.
   */
  protected void restoreSize() {
    if (showingError && adjustTargetWidth) {
      this.adjustTargetWidth = false;
      target.setWidth(originalWidth + "px");
      this.adjustTargetWidth = true;
    }
  }

  @Override
  public void release() {
    handlers.removeHandler();
    handlers = null;
  }

  /**
   * True to adjust the target width when an error is displayed (defaults to true).
   *
   * @param adjustTargetWidth true to adjust target width
   */
  public void setAdjustTargetWidth(boolean adjustTargetWidth) {
    this.adjustTargetWidth = adjustTargetWidth;
  }

  protected void alignErrorIcon() {
    Scheduler.get().scheduleDeferred(new ScheduledCommand() {
      @Override
      public void execute() {
        if (!showingError || !target.isAttached()) {
          return;
        }
        assert errorIcon.isAttached() : "errorIcon not attached";
        Element input = null;// target.getElement().<XElement> cast().selectNode("input");
        if (input == null) {
          input = target.getElement();
        }
        errorIcon.getElement().alignTo(input, new AnchorAlignment(Anchor.TOP_LEFT, Anchor.TOP_RIGHT, false), 2, 3);
      }
    });
  }

  protected void doAttach() {
    if (!showingError && errors != null) {
      markInvalid(errors);
    }
    ComponentHelper.doAttach(errorIcon);
  }

  protected void doDetach() {
    ComponentHelper.doDetach(errorIcon);
  }

}
