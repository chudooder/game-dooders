package net;

import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.newdawn.slick.Color;

import chu.engine.Entity;
import chu.engine.anim.Renderer;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.triangulate.ConformingDelaunayTriangulationBuilder;
import com.vividsolutions.jts.triangulate.DelaunayTriangulationBuilder;
import com.vividsolutions.jts.triangulate.quadedge.QuadEdgeSubdivision;
import com.vividsolutions.jts.triangulate.quadedge.Vertex;

public class VisibilityRenderer {

	public static Map<Long, Geometry> shadows = new HashMap<>();

	public static void addShadow(long frame, Geometry shadow) {
		Geometry g = shadows.get(frame);
		if (g != null) {
			Geometry intersect = g.intersection(shadow);
			shadows.put(frame, intersect);
		} else {
			shadows.put(frame, shadow);
		}

	}

	public static void render(long frame) {
		Geometry geom = shadows.get(frame);
		if (geom != null) {
			DelaunayTriangulationBuilder b =
					new DelaunayTriangulationBuilder();

			b.setSites(geom);
			GeometryCollection list = (GeometryCollection) b
					.getTriangles(new GeometryFactory());

			for (int i = 0; i < list.getNumGeometries(); i++) {
				Geometry g = list.getGeometryN(i);
				Coordinate[] c = g.getCoordinates();
				Renderer.drawTriangle(c[0].x, c[0].y, c[1].x, c[1].y, c[2].x,
						c[2].y, Entity.RENDER_PRIORITY_SHADOW, Color.black);
				Renderer.drawLine(c[0].x, c[0].y, c[1].x, c[1].y, 1,
						Entity.RENDER_PRIORITY_HUD, Color.red, Color.red);
				Renderer.drawLine(c[2].x, c[2].y, c[1].x, c[1].y, 1,
						Entity.RENDER_PRIORITY_HUD, Color.red, Color.red);
				Renderer.drawLine(c[2].x, c[2].y, c[0].x, c[0].y, 1,
						Entity.RENDER_PRIORITY_HUD, Color.red, Color.red);
			}

		}
	}

}
