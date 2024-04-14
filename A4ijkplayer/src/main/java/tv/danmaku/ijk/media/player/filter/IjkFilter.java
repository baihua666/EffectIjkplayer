package tv.danmaku.ijk.media.player.filter;

public interface IjkFilter {
    void onCreated();
    void onSizeChanged(int width, int height);
    int onDrawFrame(int textureId);
    void onTexcoords(float[] texcoords);
    void onVertices(float[] vertices);
    void onRelease();

    boolean enable();
}