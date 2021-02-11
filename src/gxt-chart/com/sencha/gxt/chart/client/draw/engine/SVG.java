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
package com.sencha.gxt.chart.client.draw.engine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.JavaScriptException;
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.dom.client.Element;
import com.google.gwt.dom.client.Node;
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.RootPanel;
import com.sencha.gxt.chart.client.draw.Color;
import com.sencha.gxt.chart.client.draw.DrawComponent;
import com.sencha.gxt.chart.client.draw.Gradient;
import com.sencha.gxt.chart.client.draw.Matrix;
import com.sencha.gxt.chart.client.draw.Rotation;
import com.sencha.gxt.chart.client.draw.Scaling;
import com.sencha.gxt.chart.client.draw.Stop;
import com.sencha.gxt.chart.client.draw.Translation;
import com.sencha.gxt.chart.client.draw.path.PathSprite;
import com.sencha.gxt.chart.client.draw.sprite.CircleSprite;
import com.sencha.gxt.chart.client.draw.sprite.EllipseSprite;
import com.sencha.gxt.chart.client.draw.sprite.ImageSprite;
import com.sencha.gxt.chart.client.draw.sprite.RectangleSprite;
import com.sencha.gxt.chart.client.draw.sprite.Sprite;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite.TextAnchor;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite.TextBaseline;
import com.sencha.gxt.core.client.Style.HideMode;
import com.sencha.gxt.core.client.dom.XDOM;
import com.sencha.gxt.core.client.dom.XElement;
import com.sencha.gxt.core.client.util.PreciseRectangle;

/**
 * Provides specific methods to draw with SVG.
 */
public class SVG extends DomSurface {
  /**
   * {@link JavaScriptObject} representing bounding box results of an
   * {@link SVG} text element.
   */
  public static final class TextBBox extends JavaScriptObject {

    protected TextBBox() {
    }

    /**
     * Returns the height of the SVG text element
     * 
     * @return the height of the SVG text element
     */
    public native double getHeight() /*-{
			return this.height;
    }-*/;

    /**
     * Returns the width of the SVG text element
     * 
     * @return the width of the SVG text element
     */
    public native double getWidth() /*-{
			return this.width;
    }-*/;

    /**
     * Returns the x-coordinate of the SVG text element
     * 
     * @return the x-coordinate of the SVG text element
     */
    public native double getX() /*-{
			return this.x;
    }-*/;

    /**
     * Returns the y-coordinate of the SVG text element
     * 
     * @return the y-coordinate of the SVG text element
     */
    public native double getY() /*-{
			return this.y;
    }-*/;

  }

  private Element defs;
  private Map<Gradient, String> gradientIds = new HashMap<Gradient, String>();
  protected Map<Sprite, XElement> clipElements = new HashMap<Sprite, XElement>();

  @Override
  public void addGradient(Gradient gradient) {
    Element gradientElement;
    double radAngle = Math.toRadians(gradient.getAngle());
    double[] vector = {0, 0, Math.cos(radAngle), Math.sin(radAngle)};
    double temp = Math.max(Math.abs(vector[2]), Math.abs(vector[3]));
    if (temp == 0) temp = 1;
    double max = 1 / temp;
    vector[2] *= max;
    vector[3] *= max;
    if (vector[2] < 0) {
      vector[0] = -vector[2];
      vector[2] = 0;
    }
    if (vector[3] < 0) {
      vector[1] = -vector[3];
      vector[3] = 0;
    }
    gradientElement = this.createSVGElement("linearGradient");
    gradientElement.setAttribute("x1", String.valueOf(vector[0]));
    gradientElement.setAttribute("y1", String.valueOf(vector[1]));
    gradientElement.setAttribute("x2", String.valueOf(vector[2]));
    gradientElement.setAttribute("y2", String.valueOf(vector[3]));
    String id = XDOM.getUniqueId();
    gradientIds.put(gradient, id);
    gradientElement.setId(id);
    getDefinitions().appendChild(gradientElement);
    for (int i = 0; i < gradient.getStops().size(); i++) {
      Stop stop = gradient.getStops().get(i);
      Element stopEl = createSVGElement("stop");
      stopEl.setAttribute("offset", String.valueOf(stop.getOffset()) + "%");
      stopEl.setAttribute("stop-color", stop.getColor().getColor());
      stopEl.setAttribute("stop-opacity", String.valueOf(stop.getOpacity()));
      gradientElement.appendChild(stopEl);
    }

  }

  @Override
  public void draw() {
    super.draw();
    if (surfaceElement == null) {
      surfaceElement = this.createSVGElement("svg");
      surfaceElement.setAttribute("xmlns", "http://www.w3.org/2000/svg");
      surfaceElement.setAttribute("version", "1.1");
      surfaceElement.setAttribute("width", String.valueOf(width));
      surfaceElement.setAttribute("height", String.valueOf(height));
      //Ensure SVG element follows the spec http://www.w3.org/TR/SVG/styling.html#UAStyleSheet
      // e.g. IE9
      surfaceElement.getStyle().setOverflow(Overflow.HIDDEN);
      Element bgRect = this.createSVGElement("rect");
      bgRect.setAttribute("width", "100%");
      bgRect.setAttribute("height", "100%");
      bgRect.setAttribute("fill", "#000");
      bgRect.setAttribute("stroke", "none");
      bgRect.setAttribute("opacity", "0");

      surfaceElement.appendChild(getDefinitions());
      surfaceElement.appendChild(bgRect);
      container.appendChild(surfaceElement);
    }

    renderAll();
  }

  @Override
  public void deleteSprite(Sprite sprite) {
    super.deleteSprite(sprite);
    clipElements.remove(sprite);
  }

  @Override
  public void renderSprite(Sprite sprite) {
    if (surfaceElement == null) {
      return;
    }
    if (getElement(sprite) == null) {
      createSprite(sprite);
    }
    if (!sprite.isDirty()) {
      return;
    }
    if (sprite.isZIndexDirty()) {
      applyZIndex(sprite);
    }

    applyAttributes(sprite);
    if (sprite.isTransformDirty()) {
      transform(sprite);
    }
    sprite.clearDirtyFlags();
  }

  /**
   * Associates the given sprite with the given clip element.
   * 
   * @param sprite the sprite
   * @param element the clip element
   */
  protected void setClipElement(Sprite sprite, XElement element) {
    clipElements.put(sprite, element);
  }

  @Override
  public void setCursor(Sprite sprite, String property) {
    XElement element = getElement(sprite);
    if (element != null) {
      element.getStyle().setProperty("cursor", property);
    }
  }

  @Override
  public void setViewBox(double x, double y, double width, double height) {
    surfaceElement.setAttribute(
        "viewBox",
        new StringBuilder().append(x).append(", ").append(y).append(", ").append(width).append(", ").append(height).toString());
  }

  @Override
  protected PreciseRectangle getBBoxText(TextSprite sprite) {
    PreciseRectangle bbox = new PreciseRectangle();
    XElement element = getElement(sprite);

    if (element != null) {
      try {
        TextBBox box = getTextBBox(element);
        bbox.setX(box.getX());
        bbox.setY(box.getY());
        bbox.setWidth(box.getWidth());
        bbox.setHeight(box.getHeight());
      } catch (JavaScriptException e) {
        //ignore, some browsers return 0s without exception (webkit), some blow up (gecko)
      }
      // If we read out a zero, but the sprite doesn't have blank text, we're probably
      // drawing while hidemode=hidden - try again with offsets
      if (bbox.equals(new PreciseRectangle()) && !"".equals(sprite.getText())) {
        DrawComponent hidden = getHiddenSVG();

        TextSprite copy = sprite.copy();
        hidden.addSprite(copy);
        copy.redraw();

        try {
          TextBBox box = getTextBBox(((DomSurface)hidden.getSurface()).getElement(copy));
          bbox.setX(box.getX());
          bbox.setY(box.getY());
          bbox.setWidth(box.getWidth());
          bbox.setHeight(box.getHeight());
        } catch (JavaScriptException e2) {
          // leave the bbox zeroed out if the second try with getTextBBox returns an error
        }
      }
    }
    return bbox;
  }

  /**
   * Returns the clip element associated with the given sprite.
   * 
   * @param sprite the sprite
   * @return the clip element
   */
  protected XElement getClipElement(Sprite sprite) {
    return clipElements.get(sprite);
  }

  private static DrawComponent hiddenTextSpriteMeasuring;
  /** 
   * Returns a hidden draw component that is hidden with offsets. Do not retain an instance, it will be removed from
   * the dom after this event loop.
   */
  private DrawComponent getHiddenSVG() {
    if (hiddenTextSpriteMeasuring == null) {
      hiddenTextSpriteMeasuring = new DrawComponent();
      hiddenTextSpriteMeasuring.addStyleName(HideMode.OFFSETS.value());
      RootPanel.get().add(hiddenTextSpriteMeasuring);
      hiddenTextSpriteMeasuring.redrawSurfaceForced();
      Scheduler.get().scheduleFinally(new ScheduledCommand() {
        @Override
        public void execute() {
          assert hiddenTextSpriteMeasuring != null : "More than one finally was invoked.";
          hiddenTextSpriteMeasuring.removeFromParent();
          hiddenTextSpriteMeasuring = null;
        }
      });
    }
    return hiddenTextSpriteMeasuring;
  }

  /**
   * Applies the attributes of the given sprite to its SVG element.
   * 
   * @param sprite the sprite to have its attributes set
   */
  private void applyAttributes(Sprite sprite) {
    XElement element = getElement(sprite);

    if (sprite instanceof PathSprite) {
      PathSprite path = (PathSprite) sprite;
      if (path.size() > 0) {
        element.setAttribute("d", path.toString());
      }
      if (path.isStrokeLineCapDirty()) {
        setAttribute(element, "stroke-linecap", path.getStrokeLineCap());
      }
      if (path.isStrokeLineJoinDirty()) {
        setAttribute(element, "stroke-linejoin", path.getStrokeLineJoin());
      }
      if (path.isMiterLimitDirty()) {
        setAttribute(element, "stroke-miterlimit", path.getMiterLimit());
      }
    } else if (sprite instanceof TextSprite) {
      TextSprite text = (TextSprite) sprite;
      if (text.isTextDirty() || text.isXDirty()) {
        tuneText(text);
      }
      if (text.isFontSizeDirty()) {
        if (text.getFontSize() > 0) {
          element.getStyle().setFontSize(text.getFontSize(), Unit.PX);
        } else {
          element.getStyle().clearFontSize();
        }
      }
      if (text.isFontStyleDirty()) {
        if (text.getFontStyle() != null) {
          element.getStyle().setFontStyle(text.getFontStyle());
        } else {
          element.getStyle().clearFontStyle();
        }
      }
      if (text.isFontWeightDirty()) {
        if (text.getFontWeight() != null) {
          element.getStyle().setFontWeight(text.getFontWeight());
        } else {
          element.getStyle().clearFontWeight();
        }
      }
      if (text.isFontDirty()) {
        setAttribute(element, "font-family", text.getFont());
      }
      if (text.isTextAnchorDirty()) {
        if (text.getTextAnchor() == TextAnchor.START) {
          element.setAttribute("text-anchor", "start");
        } else if (text.getTextAnchor() == TextAnchor.MIDDLE) {
          element.setAttribute("text-anchor", "middle");
        } else if (text.getTextAnchor() == TextAnchor.END) {
          element.setAttribute("text-anchor", "end");
        } else {
          element.removeAttribute("text-anchor");
        }
      }
      if (text.isXDirty()) {
        setAttribute(element, "x", text.getX());
      }
      if (text.isYDirty()) {
        setAttribute(element, "y", text.getY());
      }
    } else if (sprite instanceof RectangleSprite) {
      RectangleSprite rect = (RectangleSprite) sprite;
      if (rect.isXDirty()) {
        setAttribute(element, "x", rect.getX());
      }
      if (rect.isYDirty()) {
        setAttribute(element, "y", rect.getY());
      }
      if (rect.isWidthDirty()) {
        setAttribute(element, "width", rect.getWidth());

      }
      if (rect.isHeightDirty()) {
        setAttribute(element, "height", rect.getHeight());
      }
      if (rect.isRadiusDirty()) {
        setAttribute(element, "rx", rect.getRadius());
        setAttribute(element, "ry", rect.getRadius());

      }
    } else if (sprite instanceof CircleSprite) {
      CircleSprite circle = (CircleSprite) sprite;
      if (circle.isCenterXDirty()) {
        setAttribute(element, "cx", circle.getCenterX());
      }
      if (circle.isCenterYDirty()) {
        setAttribute(element, "cy", circle.getCenterY());
      }
      if (circle.isRadiusDirty()) {
        setAttribute(element, "r", circle.getRadius());
      }
    } else if (sprite instanceof EllipseSprite) {
      EllipseSprite ellipse = (EllipseSprite) sprite;
      if (ellipse.isCenterXDirty()) {
        setAttribute(element, "cx", ellipse.getCenterX());
      }
      if (ellipse.isCenterYDirty()) {
        setAttribute(element, "cy", ellipse.getCenterY());
      }
      if (ellipse.isRadiusXDirty()) {
        setAttribute(element, "rx", ellipse.getRadiusX());
      }
      if (ellipse.isRadiusYDirty()) {
        setAttribute(element, "ry", ellipse.getRadiusY());
      }
    } else if (sprite instanceof ImageSprite) {
      ImageSprite image = (ImageSprite) sprite;
      if (image.isResourceDirty()) {
        if (image.getResource() != null) {
          element.setAttributeNS("http://www.w3.org/1999/xlink", "href", image.getResource().getSafeUri().asString());
        } else {
          element.removeAttribute("href");
        }
      }
      if (image.isXDirty()) {
        setAttribute(element, "x", image.getX());
      }
      if (image.isYDirty()) {
        setAttribute(element, "y", image.getY());
      }
      if (image.isHeightDirty()) {
        setAttribute(element, "height", image.getHeight() + "px");
      }
      if (image.isWidthDirty()) {
        setAttribute(element, "width", image.getWidth() + "px");
      }
    }

    if (sprite.isStrokeDirty()) {
      setColorAttribute(element, "stroke", sprite.getStroke());
    }
    if (sprite.isStrokeWidthDirty()) {
      setAttribute(element, "stroke-width", sprite.getStrokeWidth());
    }
    if (sprite.isFillDirty()) {
      setColorAttribute(element, "fill", sprite.getFill());
    }
    if (sprite.isFillOpacityDirty()) {
      setAttribute(element, "fill-opacity", sprite.getFillOpacity());
    }
    if (sprite.isStrokeOpacityDirty()) {
      setAttribute(element, "stroke-opacity", sprite.getStrokeOpacity());
    }
    if (sprite.isOpacityDirty()) {
      setAttribute(element, "opacity", sprite.getOpacity());
    }
    String id = spriteIds.get(sprite);
    if (id != null) {
      element.setId(id);
    }

    // Hide or show the sprite
    if (sprite.isHiddenDirty()) {
      if (sprite.isHidden()) {
        element.setAttribute("visibility", "hidden");
      } else {
        element.removeAttribute("visibility");
      }
    }

    // Apply clip rectangle to the sprite
    if (sprite.isClipRectangleDirty()) {
      if (sprite.getClipRectangle() != null) {
        applyClip(sprite);
      } else {
        removeClip(sprite);
      }
    }
  }

  /**
   * Applies the clipping rectangle of the given sprite
   * 
   * @param sprite the sprite to apply its clipping rectangle
   */
  private void applyClip(Sprite sprite) {
    PreciseRectangle rect = sprite.getClipRectangle();
    XElement clipPath = getClipElement(sprite);

    if (clipPath != null) {
      Element clipParent = clipPath.getParentElement();
      clipParent.getParentElement().removeChild(clipParent);
    }

    XElement clipElement = createSVGElement("clipPath");
    clipPath = createSVGElement("rect");
    clipPath.setAttribute("x", String.valueOf(rect.getX()));
    clipPath.setAttribute("y", String.valueOf(rect.getY()));
    clipPath.setAttribute("width", String.valueOf(rect.getWidth()));
    clipPath.setAttribute("height", String.valueOf(rect.getHeight()));
    clipElement.appendChild(clipPath);
    getDefinitions().appendChild(clipElement);
    getElement(sprite).setAttribute("clip-path", "url(#" + clipElement.getId() + ")");
    setClipElement(sprite, clipElement);
  }

  /**
   * Insert or move a given {@link Sprite}'s element to the correct place in the DOM list for its zIndex.
   *
   * This should only need to be run once per set of changes, provided sprites are only rendered *after* all
   * z-index changes.
   * 
   * @param sprite - the {@link Sprite} to insert into the dom
   */
  private void applyZIndex(Sprite sprite) {
    int index = sprites.indexOf(sprite);
    int zIndex = sprite.getZIndex();
    int leftZIndex = Integer.MIN_VALUE;
    int rightZIndex = Integer.MAX_VALUE;
    if (index >= 1) {
      leftZIndex = sprites.get(index - 1).getZIndex();
    }
    if (index < sprites.size() - 1) {
      rightZIndex = sprites.get(index + 1).getZIndex();
    }

    if (leftZIndex > zIndex || rightZIndex < zIndex) {
      // if it doesnt match either the item on the right or the left, something may be out of order, rebuild it all
      Collections.sort(sprites, zIndexComparator());

      for (int i = 0; i < sprites.size(); i++) {
        XElement elt = getElement(sprites.get(i));
        if (elt != null) {
          getSurfaceElement().appendChild(elt);
        }
      }
    } else {
      // if no reordering is needed this pass but we have a detached element, insert it into the correct position
      XElement elt = getElement(sprite);
      if (elt != null && elt.getParentElement() == null) {
        // need to insert in the correct position in the dom, three possible cases, the end, the beginning, and the middle
        if (index == sprites.size() - 1) {// fast case, last element
          getSurfaceElement().appendChild(elt);
        } else if (index == 0) {// base case, first element
          getSurfaceElement().insertChild(elt, 2);
        } else {// otherwise, make sure prev element was inserted, then insert right after it
          Sprite neighborSprite = sprites.get(index - 1);
          Node prevNeighbor = getElement(neighborSprite);
          if (prevNeighbor == null) {
            // Force element creation - this will almost certainly invoke this method recursively, and will eventually
            // insert at beginning, then other nodes in the stack will be appended after it.
            // This is not presently a concern for three main reasons: first, this is how the code has worked for a long
            // time, typically items will be drawn in order anyway, and typically items are inserted in small batches
            // when sprite.redraw() is used individually.
            neighborSprite.redraw();
            prevNeighbor = getElement(neighborSprite);
            assert prevNeighbor != null;
          }
          assert prevNeighbor.getParentElement() == getSurfaceElement() : prevNeighbor.getParentElement();
          getSurfaceElement().insertAfter(elt, prevNeighbor);
        }
      }
    }


  }

  /**
   * Generates an SVG element for the given sprite and inserts it into the DOM
   * based on its z-index.
   * 
   * @param sprite the sprite to have its element generated
   * @return the generated element
   */
  private Element createSprite(Sprite sprite) {
    // Create svg element and append to the DOM.
    final XElement element;
    if (sprite instanceof CircleSprite) {
      element = createSVGElement("circle");
    } else if (sprite instanceof EllipseSprite) {
      element = createSVGElement("ellipse");
    } else if (sprite instanceof ImageSprite) {
      element = createSVGElement("image");
    } else if (sprite instanceof PathSprite) {
      element = createSVGElement("path");
    } else if (sprite instanceof RectangleSprite) {
      element = createSVGElement("rect");
    } else if (sprite instanceof TextSprite) {
      element = createSVGElement("text");
    } else {
      element = null;
      assert false : "unsupported sprite type " + sprite.getClass();
    }
    setElement(sprite, element);
    element.getStyle().setProperty("webkitTapHighlightColor", "rgba(0,0,0,0)");
    applyZIndex(sprite); // performs the insertion
    return element;
  }

  /**
   * Creates an SVG element using the give type.
   * 
   * @param type the type of element to create
   * @return the created SVG element
   */
  private XElement createSVGElement(String type) {
    return XElement.as(XDOM.createElementNS("http://www.w3.org/2000/svg", type));
  }

  /**
   * Returns the definitions element of the SVG element. If not already created
   * it will be instantiated.
   * 
   * @return the definitions element of the SVG element
   */
  private Element getDefinitions() {
    if (defs == null) {
      defs = createSVGElement("defs");
    }
    return defs;
  }

  /**
   * Returns the {@link TextBBox} for the given text element.
   * 
   * @param text the text element to get the bounding box
   * @return the bounding box
   */
  private native TextBBox getTextBBox(Element text) throws JavaScriptException /*-{
		return text.getBBox();
  }-*/;

  private void removeClip(Sprite sprite) {
    XElement clipElement = getClipElement(sprite);
    setClipElement(sprite, null);
    getElement(sprite).removeAttribute("clip-path");
    getDefinitions().removeChild(clipElement);
  }

  private void setAttribute(XElement element, String attribute, double value) {
    if (!(Double.isNaN(value))) {
      element.setAttribute(attribute, String.valueOf(value));
    } else {
      element.removeAttribute(attribute);
    }
  }

  private void setAttribute(XElement element, String attribute, Object value) {
    if (value != null) {
      element.setAttribute(attribute, value.toString());
    } else {
      element.removeAttribute(attribute);
    }
  }

  private void setColorAttribute(XElement element, String attribute, Color value) {
    if (value != null) {
      if (value instanceof Gradient) {
        element.setAttribute(attribute, "url(#" + gradientIds.get(value) + ")");
      } else {
        element.setAttribute(attribute, value.getColor());
      }
    } else {
      element.removeAttribute(attribute);
    }
  }

  /**
   * Divides up the text of the given {@link TextSprite}. Tspans are created
   * based on the new line character. The resultant tspans are added to the text
   * element.
   * 
   * @param sprite the sprite to have its text used
   * @return the generated tspans
   */
  private List<Element> setText(TextSprite sprite) {
    List<Element> tspans = new ArrayList<Element>();
    XElement spriteElement = getElement(sprite);

    while (spriteElement.hasChildNodes()) {
      spriteElement.removeChild(spriteElement.getFirstChild());
    }

    String parentX = String.valueOf(sprite.getX());

    // Wrap each row into tspan to emulate rows
    String[] texts = sprite.getText().split("\n");
    for (int i = 0; i < texts.length; i++) {
      if (texts[i] != null) {
        Element tspan = createSVGElement("tspan");
        tspan.appendChild(XDOM.createTextNode(texts[i]));
        tspan.setAttribute("x", parentX);
        spriteElement.appendChild(tspan);
        tspans.add(tspan);
      }
    }

    return tspans;
  }

  /**
   * Applies the transformations of the given sprite to its SVG element. The
   * transformations applied include {@link Rotation}, {@link Scaling} and
   * {@link Translation}.
   * 
   * @param sprite the sprite to have its transformations applied
   */
  private void transform(Sprite sprite) {
    Matrix matrix = sprite.transformMatrix();
    getElement(sprite).setAttribute(
        "transform",
        new StringBuilder().append("matrix( ").append(matrix.get(0, 0)).append(", ").append(matrix.get(1, 0)).append(
            ", ").append(matrix.get(0, 1)).append(", ").append(matrix.get(1, 1)).append(", ").append(matrix.get(0, 2)).append(
            ", ").append(matrix.get(1, 2)).append(")").toString());
  }

  /**
   * Wrap SVG text inside a tspan to allow for line wrapping. In addition this
   * normalizes the baseline for text the vertical middle of the text to be the
   * same as VML.
   * 
   * @param sprite - the sprite to have its text tuned
   */
  private void tuneText(TextSprite sprite) {
    List<Element> tspans = new ArrayList<Element>();

    if (sprite.getText() != null) {
      tspans = setText(sprite);
    }

    // Normalize baseline via a DY shift of first tspan.
    if (tspans.size() > 0) {
      double height = sprite.getFontSize();
      double initialHeight = height;
      if (sprite.getTextBaseline() == TextBaseline.BOTTOM) {
        double totalHeight = height * (tspans.size() - 1) * 1.2 + height;
        initialHeight = height - (totalHeight);
      } else if (sprite.getTextBaseline() == TextBaseline.MIDDLE) {
        double totalHeight = height * (tspans.size() - 1) * 1.2 + height;
        initialHeight = height - (totalHeight / 2.0);
      }
      for (int i = 0; i < tspans.size(); i++) {
        tspans.get(i).setAttribute("dy", String.valueOf(i != 0 ? height * 1.2 : initialHeight));
      }
    }
  }

  /**
   * Generates a {@link Comparator} for use in sorting sprites by their z-index.
   * 
   * @return the generated comparator
   */
  private Comparator<Sprite> zIndexComparator() {
    return new Comparator<Sprite>() {
      @Override
      public int compare(Sprite o1, Sprite o2) {
        return o1.getZIndex() - o2.getZIndex();
      }
    };
  }
}
