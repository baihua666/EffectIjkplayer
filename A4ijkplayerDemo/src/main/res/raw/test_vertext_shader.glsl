attribute vec4 aPosition;
varying highp vec2 vTexCoord;
attribute highp vec2 aTexCoord;

void main(void) {
    vTexCoord = vec2(aTexCoord.x,1.0-aTexCoord.y);
    gl_Position = aPosition;
}