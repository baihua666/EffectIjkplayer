precision highp float;
varying   highp vec2 vTexCoord;
uniform   lowp  sampler2D sTexture;
void main()
{
    lowp    vec3 rgb = texture2D(sTexture, vTexCoord).rgb;
    gl_FragColor = vec4(0.299*rgb.r+0.587*rgb.g+0.114*rgb.b);
}