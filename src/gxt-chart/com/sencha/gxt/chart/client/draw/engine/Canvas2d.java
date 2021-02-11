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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

import com.google.gwt.canvas.dom.client.CanvasGradient;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.Context2d.LineCap;
import com.google.gwt.canvas.dom.client.Context2d.LineJoin;
import com.google.gwt.canvas.dom.client.Context2d.TextAlign;
import com.google.gwt.canvas.dom.client.Context2d.TextBaseline;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.canvas.dom.client.FillStrokeStyle;
import com.google.gwt.canvas.dom.client.TextMetrics;
import com.google.gwt.dom.client.CanvasElement;
import com.google.gwt.dom.client.Document;
import com.google.gwt.dom.client.ImageElement;
import com.sencha.gxt.chart.client.draw.Color;
import com.sencha.gxt.chart.client.draw.Gradient;
import com.sencha.gxt.chart.client.draw.Matrix;
import com.sencha.gxt.chart.client.draw.Stop;
import com.sencha.gxt.chart.client.draw.Surface;
import com.sencha.gxt.chart.client.draw.path.ClosePath;
import com.sencha.gxt.chart.client.draw.path.CurveTo;
import com.sencha.gxt.chart.client.draw.path.CurveToQuadratic;
import com.sencha.gxt.chart.client.draw.path.CurveToQuadraticSmooth;
import com.sencha.gxt.chart.client.draw.path.CurveToSmooth;
import com.sencha.gxt.chart.client.draw.path.EllipticalArc;
import com.sencha.gxt.chart.client.draw.path.LineTo;
import com.sencha.gxt.chart.client.draw.path.LineToHorizontal;
import com.sencha.gxt.chart.client.draw.path.LineToVertical;
import com.sencha.gxt.chart.client.draw.path.MoveTo;
import com.sencha.gxt.chart.client.draw.path.PathCommand;
import com.sencha.gxt.chart.client.draw.path.PathSprite;
import com.sencha.gxt.chart.client.draw.sprite.CircleSprite;
import com.sencha.gxt.chart.client.draw.sprite.EllipseSprite;
import com.sencha.gxt.chart.client.draw.sprite.ImageSprite;
import com.sencha.gxt.chart.client.draw.sprite.RectangleSprite;
import com.sencha.gxt.chart.client.draw.sprite.Sprite;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite;
import com.sencha.gxt.chart.client.draw.sprite.TextSprite.TextAnchor;
import com.sencha.gxt.core.client.util.PrecisePoint;
import com.sencha.gxt.core.client.util.PreciseRectangle;

/**
 * Canvas-based implementation of the Surface api, using the 2-d context (as opposed to webgl, etc). EXPERIMENTAL,
 * DISABLED BY DEFAULT, AND NOT OFFICIALLY SUPPORTED. This implementation may in some cases be faster than the built-in
 * SVG or VML implementations, and may be configured as an official implementation in the future, but for now purely
 * experimental. Use at your own risk.
 *
 * Known caveats of using Canvas over a DOM-based implementation like SVG or VML:
 * <ul>
 *   <li>
 *     Font details aren't managed by the DOM, so TextSprites must have a font size and family set to avoid the browser
 *     default.
 *   </li>
 *   <li>
 *     Any change requires a full repaint of the entire canvas, presently this is implmenent as a deferred call to wait
 *     for any other changes within the current event loop.
 *   </li>
 * </ul>
 *
 *
 * Known bugs and missing features:
 * <ul>
 *   <li>Gradients are partially implemented, and do not correctly handle the angle property</li>
 *   <li>Sprite.setCursor is ignored</li>
 *   <li>TextSprite.setFontStyle and setFontWeight are ignored</li>
 *   <li>TextSprite.getBBox() may not be correct</li>
 *   <li>High density displays (i.e. 'retina') are not correctly supported</li>
 * </ul>
 */
public class Canvas2d extends Surface {
  /** experimental flag to try only redrawing changed content and colliding bboxes */
  private static final boolean REDRAW_ALL = true;
  private Map<Sprite, PreciseRectangle> renderedBbox = REDRAW_ALL ? new HashMap<Sprite, PreciseRectangle>() : null;
  private PreciseRectangle viewbox;

  @Override
  public void draw() {
    super.draw();
    if (surfaceElement == null) {
      surfaceElement = Document.get().createCanvasElement().cast();
      getCanvas().setWidth(width);
      getCanvas().setHeight(height);

      container.appendChild(surfaceElement);
    }
    getContext().clearRect(0, 0, width, height);

    //TODO only sort, clear, re-render modified sprites (where they were, where they are)
    //as a test to see if that gives us a bump in speed
    Collections.sort(sprites, zIndexComparator());

    //collect viewbox
    if (component.isViewBox()) {
      int temp = sprites.indexOf(backgroundSprite);
      sprites.remove(temp);
      PreciseRectangle bbox = sprites.getBBox();
      sprites.add(temp, backgroundSprite);

      setViewBox(bbox.getX(), bbox.getY(), bbox.getWidth(), bbox.getHeight());
      backgroundSprite.setWidth(bbox.getWidth());
      backgroundSprite.setHeight(bbox.getHeight());
    }

    appendAll();
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

  @Override
  protected void renderAll() {
    throw new UnsupportedOperationException("When drawing with canvas either use appendAll() or draw()");
  }

  protected void appendAll() {
    for (Sprite s : sprites) {
      append(s);
    }
  }

  @Override
  public void renderSprite(Sprite sprite) {
    if (surfaceElement == null) {
      return;
    }

    if (!sprite.isDirty()) {
      return;
    }
    if (REDRAW_ALL) {
      component.redrawSurface();
    } else {
      //clear this item's bbox, both old and new
      PreciseRectangle oldBbox = renderedBbox.get(sprite);
      PreciseRectangle newBbox = sprite.getBBox();

      Context2d ctx = getContext();
      //TODO invert this logic to cut two lines of code, and see about shorting this out better
      if (oldBbox != null && (oldBbox.contains(newBbox.getX(), newBbox.getY()) && oldBbox.contains(newBbox.getX() + newBbox.getWidth(), newBbox.getY() + newBbox.getHeight()))) {
        ctx.clearRect(oldBbox.getX(), oldBbox.getY(), oldBbox.getWidth(), oldBbox.getHeight());
      } else if (oldBbox == null || (newBbox.contains(oldBbox.getX(), oldBbox.getY()) && newBbox.contains(oldBbox.getX() + oldBbox.getWidth(), oldBbox.getY() + oldBbox.getHeight()))) {
        //TODO if null, only clear if something is above the new sprite...
        ctx.clearRect(newBbox.getX(), newBbox.getY(), newBbox.getWidth(), newBbox.getHeight());
      } else {
        ctx.clearRect(oldBbox.getX(), oldBbox.getY(), oldBbox.getWidth(), oldBbox.getHeight());
        ctx.clearRect(newBbox.getX(), newBbox.getY(), newBbox.getWidth(), newBbox.getHeight());
      }

      //in order, re-render all items that intersect the bbox, ending with the element itself
      //-first, find all sprites in the old bbox, or the new bbox
      //-sort both, merge the lists, removing duplicates (this is slightly smaller than the union)
      List<Sprite> oldBoxSprites = getIntersectingSprites(oldBbox);
      List<Sprite> newBoxSprites = getIntersectingSprites(oldBbox);

      //could be a list, but needs to avoid dups
      LinkedHashSet<Sprite> sprites = new LinkedHashSet<Sprite>(oldBoxSprites);
      sprites.addAll(newBoxSprites);
      Collections.sort(newBoxSprites, zIndexComparator());

      for (Sprite s : sprites) {
        //TODO artificial clip to limit to what has been cleared
        append(s);
      }
    }
  }

  private List<Sprite> getIntersectingSprites(PreciseRectangle bbox) {
    List<Sprite> sprites = new ArrayList<Sprite>();
    for (Sprite s : this.sprites) {
      PreciseRectangle spriteRect = s.getBBox();
      if (intersectHelper(bbox, spriteRect) || intersectHelper(spriteRect, bbox)) {
        sprites.add(s);
      }
    }
    return sprites;
  }

  private boolean intersectHelper(PreciseRectangle rect1, PreciseRectangle rect2) {
    return rect2.contains(rect1.getX(), rect1.getY()) ||
        rect2.contains(rect1.getX(), rect1.getY() + rect1.getHeight()) ||
        rect2.contains(rect1.getX() + rect1.getWidth(), rect1.getY()) ||
        rect2.contains(rect1.getX() + rect1.getWidth(), rect1.getY() + rect1.getHeight());
  }

  /**
   * In the Canvas2d class, this method does more or less what renderSprite does in SVG and VML - it
   * actually renders the sprite to the dom.
   * @param sprite the sprite to draw
   */
  protected void append(Sprite sprite) {
    if (sprite.isHidden() || sprite.getOpacity() == 0) {
      return;
    }
    Context2d ctx = getContext();
    ctx.save();
    //set global stuff, fill, stroke, clip, etc

    //clip - deal with translation or normal rectangle
    if (sprite.getClipRectangle() != null) {
      PreciseRectangle clip = sprite.getClipRectangle();
      if (sprite.getScaling() != null || sprite.getTranslation() != null || sprite.getRotation() != null) {
        PathSprite transPath = new PathSprite(new RectangleSprite(clip));
        transPath = transPath.map(sprite.transformMatrix());
        appendPath(ctx, transPath);
      } else {
        ctx.beginPath();
        ctx.rect(clip.getX(), clip.getY(), clip.getWidth(), clip.getHeight());
        ctx.closePath();
      }
      ctx.clip();
    }

    if (sprite.getScaling() != null || sprite.getTranslation() != null || sprite.getRotation() != null
            || (component.isViewBox() && viewbox != null)) {
      Matrix matrix = sprite.transformMatrix();
      if (matrix != null) {
        //TODO consider replacing this transform call with three distinct calls to translate/scale/rotate if cheaper
        ctx.transform(matrix.get(0, 0), matrix.get(1, 0),
            matrix.get(0, 1), matrix.get(1, 1),
            matrix.get(0, 2), matrix.get(1, 2));
      }
      if (component.isViewBox() && viewbox != null) {
        double size = Math.min(getWidth() / viewbox.getWidth(), getHeight() / viewbox.getHeight());

        ctx.scale(size, size);
        ctx.translate(-viewbox.getX(), -viewbox.getY());
      }
    }

    //TODO see about caching colors via the dirty flag? If we don't use a color/gradient for a pass or three, dump it
    double opacity = Double.isNaN(sprite.getOpacity()) ? 1.0 : sprite.getOpacity();
    PreciseRectangle untransformedBbox = sprite.getPathSprite().dimensions();
    if (sprite.getStroke() != null && sprite.getStroke() != Color.NONE && sprite.getStrokeWidth() != 0) {
      ctx.setLineWidth(Double.isNaN(sprite.getStrokeWidth()) ? 1.0 : sprite.getStrokeWidth());
      ctx.setStrokeStyle(getColor(sprite.getStroke(), untransformedBbox));//TODO read bbox from cache
    }
    if (sprite.getFill() != null && sprite.getFill() != Color.NONE) {
      ctx.setFillStyle(getColor(sprite.getFill(), untransformedBbox));//TODO read bbox from cache
    }

    if (sprite instanceof PathSprite) {
      appendPath(ctx, (PathSprite)sprite);
    } else if (sprite instanceof TextSprite) {
      TextSprite text = (TextSprite) sprite;
      //TODO style and weight
      ctx.setFont(text.getFontSize() + "px " + text.getFont());
      ctx.setTextAlign(getTextAlign(text.getTextAnchor()));
      ctx.setTextBaseline(getTextBaseline(text.getTextBaseline()));
      ctx.fillText(text.getText(), text.getX(), text.getY());
    } else if (sprite instanceof RectangleSprite) {
      RectangleSprite rect = (RectangleSprite) sprite;
      if (Double.isNaN(rect.getRadius()) || rect.getRadius() == 0) {
        if (sprite.getFill() != null && sprite.getFill() != Color.NONE) {
          ctx.setGlobalAlpha(Double.isNaN(sprite.getFillOpacity()) ? opacity : opacity * sprite.getFillOpacity());
          ctx.fillRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
        }
        if (sprite.getStroke() != null && sprite.getStroke() != Color.NONE && sprite.getStrokeWidth() != 0) {
          ctx.setGlobalAlpha(Double.isNaN(sprite.getStrokeOpacity()) ? opacity : opacity * sprite.getStrokeOpacity());
          ctx.strokeRect(rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
        }
      } else {
        appendPath(ctx, rect.getPathSprite());
      }
    } else if (sprite instanceof CircleSprite) {
      CircleSprite circle = (CircleSprite) sprite;
      ctx.beginPath();
      ctx.arc(circle.getCenterX(), circle.getCenterY(), circle.getRadius(), 0, 2 * Math.PI);
      ctx.closePath();
      if (sprite.getFill() != null && sprite.getFill() != Color.NONE) {
        ctx.setGlobalAlpha(Double.isNaN(sprite.getFillOpacity()) ? opacity : opacity * sprite.getFillOpacity());
        ctx.fill();
      }
      if (sprite.getStroke() != null && sprite.getStroke() != Color.NONE && sprite.getStrokeWidth() != 0) {
        ctx.setGlobalAlpha(Double.isNaN(sprite.getStrokeOpacity()) ? opacity : opacity * sprite.getStrokeOpacity());
        ctx.stroke();
      }
    } else if (sprite instanceof EllipseSprite) {
      appendPath(ctx, sprite.getPathSprite());
    } else if (sprite instanceof ImageSprite) {
      ImageSprite image = (ImageSprite) sprite;
      ImageElement elt = Document.get().createImageElement();
      elt.setSrc(image.getResource().getSafeUri().asString());
      ctx.drawImage(elt, image.getX(), image.getY(), image.getWidth(), image.getHeight());
    }

    ctx.restore();

    if (!REDRAW_ALL) {
      renderedBbox.put(sprite, getBBox(sprite));
    }

    sprite.clearDirtyFlags();
  }

  private TextBaseline getTextBaseline(TextSprite.TextBaseline baseline) {
    switch (baseline) {
      case BOTTOM:
        return TextBaseline.BOTTOM;
      case MIDDLE:
        return TextBaseline.MIDDLE;
      case TOP:
      default:
        return TextBaseline.TOP;
    }
  }

  private TextAlign getTextAlign(TextAnchor anchor) {
    switch (anchor) {
      case END:
        return TextAlign.END;
      case MIDDLE:
        return TextAlign.CENTER;
      case START:
      default:
        return TextAlign.START;
    }
  }

  private String getColorString(Color color, double opacity) {
    assert color != null;
    if (opacity == 1) {
      return color.getColor();
    }
    return color.getColor();

  }
  private FillStrokeStyle getColor(Color color, PreciseRectangle bbox) {
    if (color instanceof Gradient) {
      return makeGradient((Gradient) color, bbox);
    }
    return CssColor.make(getColorString(color, 1));
  }

  protected void appendPath(Context2d ctx, PathSprite sprite) {
    ctx.beginPath();
    sprite.toAbsolute();
//    sprite = sprite.copy().toCurve();

    PrecisePoint currentPoint = new PrecisePoint();
    PrecisePoint movePoint = new PrecisePoint();
    PrecisePoint curvePoint = new PrecisePoint();
    PrecisePoint quadraticPoint = new PrecisePoint();

    appendPathCommands(ctx, sprite.getCommands(), currentPoint, movePoint, curvePoint, quadraticPoint);

    double opacity = Double.isNaN(sprite.getOpacity()) ? 1.0 : sprite.getOpacity();
    if (sprite.getFill() != null && sprite.getFill() != Color.NONE) {
      ctx.setGlobalAlpha(Double.isNaN(sprite.getFillOpacity()) ? opacity : opacity * sprite.getFillOpacity());
      ctx.fill();
    }
    if (sprite.getStroke() != null && sprite.getStroke() != Color.NONE && sprite.getStrokeWidth() != 0) {
      ctx.setLineCap(sprite.getStrokeLineCap() == null ? LineCap.BUTT : sprite.getStrokeLineCap());
      ctx.setLineJoin(sprite.getStrokeLineJoin() == null ? LineJoin.MITER : sprite.getStrokeLineJoin());
      ctx.setMiterLimit(sprite.getMiterLimit() == Double.NaN ? 4 : sprite.getMiterLimit());
      ctx.setGlobalAlpha(Double.isNaN(sprite.getStrokeOpacity()) ? opacity : opacity * sprite.getStrokeOpacity());
      ctx.stroke();
    }
  }

  private PathCommand appendPathCommands(Context2d ctx, List<PathCommand> commands, PrecisePoint currentPoint, PrecisePoint movePoint, PrecisePoint curvePoint, PrecisePoint quadraticPoint) {
    PathCommand last = null;
    for (PathCommand cmd : commands) {
      last = cmd;
      if (cmd instanceof MoveTo) {
        MoveTo move = (MoveTo) cmd;

        quadraticPoint.setX(currentPoint.getX());
        quadraticPoint.setY(currentPoint.getY());
        movePoint.setX(move.getX());
        movePoint.setY(move.getY());
        currentPoint.setX(move.getX());
        currentPoint.setY(move.getY());
        curvePoint.setX(move.getX());
        curvePoint.setY(move.getY());

        ctx.moveTo(move.getX(), move.getY());
      } else if (cmd instanceof LineTo) {
        LineTo line = (LineTo) cmd;

        quadraticPoint.setX(currentPoint.getX());
        quadraticPoint.setY(currentPoint.getY());
        currentPoint.setX(line.getX());
        currentPoint.setY(line.getY());
        curvePoint.setX(line.getX());
        curvePoint.setY(line.getY());

        ctx.lineTo(line.getX(), line.getY());
      } else if (cmd instanceof CurveTo) {
        CurveTo curve = (CurveTo) cmd;

        quadraticPoint.setX(currentPoint.getX());
        quadraticPoint.setY(currentPoint.getY());
        currentPoint.setX(curve.getX());
        currentPoint.setY(curve.getY());
        curvePoint.setX(curve.getX2());
        curvePoint.setY(curve.getY2());

        ctx.bezierCurveTo(curve.getX1(), curve.getY1(), curve.getX2(), curve.getY2(), curve.getX(), curve.getY());
      } else if (cmd instanceof CurveToQuadratic) {
        CurveToQuadratic curve = (CurveToQuadratic) cmd;
        double ax = 2.0 * curve.getX1() / 3.0;
        double ay = 2.0 * curve.getY1() / 3.0;

        quadraticPoint.setX(curve.getX1());
        quadraticPoint.setY(curve.getY1());
        currentPoint.setX(curve.getX() / 3.0 + ax);
        currentPoint.setY(curve.getY() / 3.0 + ay);
        curvePoint.setX(curve.getX1());
        curvePoint.setY(curve.getY1());

        ctx.quadraticCurveTo(curve.getX1(), curve.getY1(), curve.getX(), curve.getY());
      } else if (cmd instanceof ClosePath) {
        quadraticPoint.setX(currentPoint.getX());
        quadraticPoint.setY(currentPoint.getY());

        ctx.closePath();
      } else {
        assert cmd instanceof EllipticalArc
                || cmd instanceof CurveToQuadraticSmooth
                || cmd instanceof CurveToSmooth
                || cmd instanceof LineToHorizontal
                || cmd instanceof LineToVertical
                : cmd.getClass() + " is not yet implemented";
        last = appendPathCommands(ctx, cmd.toCurve(currentPoint, movePoint, curvePoint, quadraticPoint), currentPoint, movePoint, curvePoint, quadraticPoint);
        CurveTo curve = (CurveTo) last;
        currentPoint.setX(curve.getX());
        currentPoint.setY(curve.getY());
        curvePoint.setX(curve.getX2());
        curvePoint.setY(curve.getY2());
      }

    }
    return last;
  }

  @Override
  public void setCursor(Sprite sprite, String property) {
    // TODO 
  }

  @Override
  public void setViewBox(double x, double y, double width, double height) {
    viewbox = new PreciseRectangle();
    double relativeHeight = this.height / height;
    double relativeWidth = this.width / width;
    if (width * relativeHeight < this.width) {
      x -= (this.width - width * relativeHeight) / 2.0 / relativeHeight;
    }
    if (height * relativeWidth < this.height) {
      y -= (this.height - height * relativeWidth) / 2.0 / relativeWidth;
    }

    viewbox.setX(x);
    viewbox.setY(y);
    viewbox.setHeight(height);
    viewbox.setWidth(width);
  }

  @Override
  protected PreciseRectangle getBBoxText(TextSprite sprite) {
    Context2d ctx = getContext();
    ctx.setFont(sprite.getFontSize() + "px " + sprite.getFont());
    TextMetrics text = ctx.measureText(sprite.getText());

    //TODO real height
    return new PreciseRectangle(sprite.getX(), sprite.getY(), text.getWidth(), sprite.getFontSize() * 4/3);
  }
  protected CanvasElement getCanvas() {
    return getSurfaceElement().cast();
  }
  protected Context2d getContext() {
    return getCanvas().getContext2d();
  }

  @Override
  public void setWidth(int width) {
    this.width = width;
    if (getCanvas() != null && getCanvas().getWidth() != width) {
      getCanvas().setWidth(width);
    }
  }
  @Override
  public void setHeight(int height) {
    this.height = height;
    if (getCanvas() != null && getCanvas().getHeight() != height) {
      getCanvas().setHeight(height);
    }
  }



  protected CanvasGradient makeGradient(Gradient gradient, PreciseRectangle bbox) {
    // emulating http://www.w3.org/TR/SVG/pservers.html#LinearGradientElementGradientUnitsAttribute = objectBoundingBox
    double radAngle = Math.toRadians(gradient.getAngle());
    double[] vector = {0, 0, Math.cos(radAngle) * bbox.getHeight(), Math.sin(radAngle) * bbox.getWidth()};
    @SuppressWarnings("unused")
    double temp = Math.max(Math.abs(vector[2]), Math.abs(vector[3]));

    if (vector[2] < 0) {
      vector[0] = -vector[2];
      vector[2] = 0;
    }
    if (vector[3] < 0) {
      vector[1] = -vector[3];
      vector[3] = 0;
    }

    CanvasGradient g = getContext().createLinearGradient(
            bbox.getX() + vector[0],
            bbox.getY() + vector[1],
            bbox.getX() + vector[2],
            bbox.getY() + vector[3]);


    for (Stop s : gradient.getStops()) {
      g.addColorStop(((double) s.getOffset())/100.0, getColorString(s.getColor(), s.getOpacity()));
    }
    return g;
  }
}
