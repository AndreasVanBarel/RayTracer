# Java Ray Tracer

This repository contains a CPU based Java raytracer to render 3d graphics. It was created in the context of the Computer Graphics Project course by prof. P. Dutré at KU Leuven in Spring 2015. It was the only project to receive 19/20, the top score for that year. The accompanying report is included in [English](https://github.com/AndreasVanBarel/RayTracer/blob/main/Report.pdf) and [Dutch](https://github.com/AndreasVanBarel/RayTracer/blob/main/Verslag.pdf).

## Usage

Clone the repository. The entry point `main()` is located in `src/main/Renderer.java`. In that same file, the line `int scene_id = 22;` determines which scene will be rendered. The properties of the scene (the objects and light sources etc) for each `scene_id` can be found and modified in `src/scene/SceneGenerator.java`. The properties of the camera (such as depth of field parameters) can be found and modified in `src/CameraGenerator.java`. Anti-aliasing is implemented as doing `AA` random samples in each pixel, where `AA` can be set in `src/main/Renderer.java` with `public static final int AA = 10;`.

## Features

Please consult the above mentioned report for a detailed description of features and accompanying rendered figures. A quick overview:

- Analytic shapes: spheres, boxes, cylinders, planes, and triangles
- Loading triangle meshes from .obj files with normal maps and uv maps
- Generation of a bounding volume hierarchy to speed up the ray tracing 
- Materials system, with support for several BRDF (Bidirectional Reflectance Distribution Function) models
- Materials can be textured, reflective, refractive, or glossy
- Environment maps (both per object or for the entire scene)
- Multiple light sources in a single scene
- Several light source types: directional light sources, point light sources, and area light sources
- Anti-aliasing (AA)
- Depth of field (DOF)
- Efficient implementation. Each render takes at most a few seconds.

## Example renders

For more examples, see the above mentioned report, or better yet, clone the repo and make your own!

#### Glossy sphere with area lights
<p align="center">
  <img src="https://github.com/AndreasVanBarel/RayTracer/blob/main/renders/glossy_sphere.png" width="500" title="Glossy sphere">
</p>
<!---![Glossy sphere](https://github.com/AndreasVanBarel/RayTracer/blob/main/renders/glossy_sphere.png?raw=true)-->

#### Refractive sphere
<p align="center">
  <img src="https://github.com/AndreasVanBarel/RayTracer/blob/main/renders/refractive_sphere.png" width="500" title="Refractive sphere">
</p>
<!---![Refractive sphere](https://github.com/AndreasVanBarel/RayTracer/blob/main/renders/refractive_sphere.png?raw=true)-->

#### Depth of field 
<p align="center">
  <img src="https://github.com/AndreasVanBarel/RayTracer/blob/main/renders/DOF.png" width="500" title="Depth of field">
</p>
<!---![Depth of field](https://github.com/AndreasVanBarel/RayTracer/blob/main/renders/DOF.png?raw=true)-->

#### Area light with refractive Stanford bunny
<p align="center">
  <img src="https://github.com/AndreasVanBarel/RayTracer/blob/main/renders/area_light.png" width="500" title="Stanford bunny">
</p>
<!---![Stanford bunny](https://github.com/AndreasVanBarel/RayTracer/blob/main/renders/area_light.png?raw=true)-->

#### Bounding box hierarchy false color image. 
The whiter, the more bounding boxes were traversed in the ray tracing of that pixel.
<p align="center">
  <img src="https://github.com/AndreasVanBarel/RayTracer/blob/main/renders/BBH.png" width="500" title="Bounding box false color">
</p>
<!---![Bounding box hierarchy](https://github.com/AndreasVanBarel/RayTracer/blob/main/renders/BBH.png?raw=true)-->

#### An emergent Sierpinski-like fractal 
Obtained using four spheres aranged as a tetraeder, i.e., all touching, with each having a non-physical material that amplifies the ray on each bounce instead of diminishing it. The back sphere is black, the other three have the three primary colors.
<p align="center">
  <img src="https://github.com/AndreasVanBarel/RayTracer/blob/main/renders/sierpinski.png" width="500" title="Emergent Sierpinski-like fractal">
</p>
<!---![Sierpinski](https://github.com/AndreasVanBarel/RayTracer/blob/main/renders/sierpinski.png?raw=true)-->

Giving the spheres a more physical material can help to understand the fractal:
<p align="center">
  <img src="https://github.com/AndreasVanBarel/RayTracer/blob/main/renders/sierpinski_explanation.png" width="500" title="Sierpinski explanation">
</p>
<!---![Sierpinski explanation](https://github.com/AndreasVanBarel/RayTracer/blob/main/renders/sierpinski_explanation.png?raw=true)-->



