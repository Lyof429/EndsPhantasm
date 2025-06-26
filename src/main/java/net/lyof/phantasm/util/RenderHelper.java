package net.lyof.phantasm.util;

import it.unimi.dsi.fastutil.Hash;
import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenCustomHashMap;
import net.lyof.phantasm.Phantasm;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class RenderHelper {
    public static void renderFace(MatrixStack matrices, VertexConsumer vertices, int light,
                           Point p1, Point p2, Point p3, Point p4) {

        matrices.push();

        MatrixStack.Entry entry = matrices.peek();
        Matrix4f position = entry.getPositionMatrix();
        Matrix3f normal = entry.getNormalMatrix();


        // Front face
        vertices.vertex(position, p1.x, p1.y, p1.z)
                .color(255, 255, 255, 255)
                .texture(p1.u, p1.v)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(normal, 1, 1, 1)
                .next();
        vertices.vertex(position, p2.x, p2.y, p2.z)
                .color(255, 255, 255, 255)
                .texture(p2.u, p2.v)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(normal, 1, 1, 1)
                .next();
        vertices.vertex(position, p3.x, p3.y, p3.z)
                .color(255, 255, 255, 255)
                .texture(p3.u, p3.v)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(normal, 1, 1, 1)
                .next();
        vertices.vertex(position, p4.x, p4.y, p4.z)
                .color(255, 255, 255, 255)
                .texture(p4.u, p4.v)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(normal, 1, 1, 1)
                .next();


        matrices.multiply(p1.getRotation(p2),
                (p1.x + p2.x + p3.x + p4.x) / 4,
                (p1.y + p2.y + p3.y + p4.y) / 4,
                (p1.z + p2.z + p3.z + p4.z) / 4);

        entry = matrices.peek();
        position = entry.getPositionMatrix();
        normal = entry.getNormalMatrix();

        // Back face
        vertices.vertex(position, p1.x, p1.y, p1.z)
                .color(255, 255, 255, 255)
                .texture(p1.u, p1.v)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(normal, 1, 1, 1)
                .next();
        vertices.vertex(position, p2.x, p2.y, p2.z)
                .color(255, 255, 255, 255)
                .texture(p2.u, p2.v)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(normal, 1, 1, 1)
                .next();
        vertices.vertex(position, p3.x, p3.y, p3.z)
                .color(255, 255, 255, 255)
                .texture(p3.u, p3.v)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(normal, 1, 1, 1)
                .next();
        vertices.vertex(position, p4.x, p4.y, p4.z)
                .color(255, 255, 255, 255)
                .texture(p4.u, p4.v)
                .overlay(OverlayTexture.DEFAULT_UV)
                .light(light)
                .normal(normal, 1, 1, 1)
                .next();

        matrices.pop();
    }

    public static void renderCube(MatrixStack matrices, VertexConsumerProvider vertexConsumers, Identifier texture, int light,
                                  float x0, float x1, float y0, float y1, float z0, float z1) {
        renderCube(matrices, vertexConsumers.getBuffer(RenderLayer.getEntitySolid(texture)), light,
                x0, x1, y0, y1, z0, z1);
    }

    public static void renderCube(MatrixStack matrices, VertexConsumer vertices, int light,
                                  float x0, float x1, float y0, float y1, float z0, float z1) {
        renderCube(matrices, vertices, light,
                x0, x1, y0, y1, z0, z1, false);
    }

    public static void renderCube(MatrixStack matrices, VertexConsumer vertices, int light,
                           float x0, float x1, float y0, float y1, float z0, float z1, boolean scaleTexture) {

        float dx = Math.abs(x1 - x0), dy = Math.abs(y1 - y0), dz = Math.abs(z1 - z0);
        if (scaleTexture) { dx = 1; dy = 1; dz = 1; }

        renderFace(matrices, vertices, light,
                Point.of(x0, y0, z0, dx, dy),
                Point.of(x0, y1, z0, dx, 0),
                Point.of(x1, y1, z0, 0, 0), 
                Point.of(x1, y0, z0, 0, dy));
                //y0, y1, x0, x1, z0, z0);
        renderFace(matrices, vertices, light,
                Point.of(x1, y0, z0, dz, dy),
                Point.of(x1, y1, z0, dz, 0),
                Point.of(x1, y1, z1, 0, 0),
                Point.of(x1, y0, z1, 0, dy));
                //y0, y1, x1, x1, z0, z1);
        renderFace(matrices, vertices, light,
                Point.of(x0, y0, z1, dx, dy),
                Point.of(x0, y1, z1, dx, 0),
                Point.of(x1, y1, z1, 0, 0),
                Point.of(x1, y0, z1, 0, dy));
                //y0, y1, x1, x0, z1, z1);
        renderFace(matrices, vertices, light,
                Point.of(x0, y0, z0, dz, dy),
                Point.of(x0, y1, z0, dz, 0),
                Point.of(x0, y1, z1, 0, 0),
                Point.of(x0, y0, z1, 0, dy));
                //y0, y1, x0, x0, z1, z0);
        renderFace(matrices, vertices, light,
                Point.of(x0, y0, z0, 0, dz),
                Point.of(x1, y0, z0, dx, dz),
                Point.of(x1, y0, z1, dx, 0),
                Point.of(x0, y0, z1, 0, 0));
        renderFace(matrices, vertices, light,
                Point.of(x0, y1, z0, 0, dz),
                Point.of(x1, y1, z0, dx, dz),
                Point.of(x1, y1, z1, dx, 0),
                Point.of(x0, y1, z1, 0, 0));
    }


    public static class Point {
        public float x, y, z, u, v;
        private static final Map<String, Quaternionf> rotationCache = new HashMap<>();
        private static final Map<String, Point> instanceCache = new HashMap<>();

        public static Point of(float x, float y, float z, float u, float v) {
            String key = x + " " + y + " " + z + " " + u + " " + v;
            if (instanceCache.containsKey(key)) return instanceCache.get(key);

            Point p = new Point();
            p.x = x; p.y = y; p.z = z; p.u = u; p.v = v;
            instanceCache.put(key, p);
            return p;
        }

        public Quaternionf getRotation(Point other) {
            String key = this.x + " " + this.y + " " + this.z + "/" + other.x + " " + other.y + " " + other.z;
            if (rotationCache.containsKey(key)) return rotationCache.get(key);

            rotationCache.put(key, RotationAxis.of(new Vector3f(this.x - other.x, this.y - other.y, this.z - other.z))
                    .rotationDegrees(180));
            return rotationCache.get(key);
        }
    }
}
