precision highp float;
varying   highp vec2 vTexCoord;
attribute highp vec4 aPosition;
attribute highp vec2 aTexCoord;

void main()
{
    vTexCoord = vec2(aTexCoord.x,1.0-aTexCoord.y);
    gl_Position = aPosition;
}