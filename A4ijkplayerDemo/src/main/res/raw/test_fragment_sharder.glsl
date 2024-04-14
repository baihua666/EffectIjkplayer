//https://jameshfisher.com/2020/08/11/production-ready-green-screen-in-the-browser/

precision mediump float;

varying   highp vec2 vTexCoord;

uniform sampler2D tex;

//uniform vec3 keyColor = vec3(0.449,0.805,0.414);

//uniform float similarity;
//uniform float smoothness;
//uniform float spill;



// From https://github.com/libretro/glsl-shaders/blob/master/nnedi3/shaders/rgb-to-yuv.glsl
vec2 RGBtoUV(vec3 rgb) {
    return vec2(
    rgb.r * -0.169 + rgb.g * -0.331 + rgb.b *  0.5    + 0.5,
    rgb.r *  0.5   + rgb.g * -0.419 + rgb.b * -0.081  + 0.5
    );
}


vec4 ProcessChromaKey(vec2 texCoord) {
    vec4 rgba = texture2D(tex, texCoord);
// todo,debug
    vec3 keyColor = vec3(0.0,1.0,0.0);
    float similarity = 0.4;
    float smoothness = 0.08;
    float spill = 0.1;
//    vec3 keyColor = vec3(115,206,106);
    float chromaDist = distance(RGBtoUV(texture2D(tex, texCoord).rgb), RGBtoUV(keyColor));
//    return vec4(1.0 *chromaDist,1.0 *chromaDist,1.0 *chromaDist,1.0);

    float baseMask = chromaDist - similarity;
    float fullMask = pow(clamp(baseMask / smoothness, 0., 1.), 1.5);
    rgba.a = fullMask;

    float spillVal = pow(clamp(baseMask / spill, 0., 1.), 1.5);
    float desat = clamp(rgba.r * 0.2126 + rgba.g * 0.7152 + rgba.b * 0.0722, 0., 1.);
    rgba.rgb = mix(vec3(desat, desat, desat), rgba.rgb, spillVal);

    return rgba;
}

void main(void) {
    gl_FragColor = ProcessChromaKey(vTexCoord);
}