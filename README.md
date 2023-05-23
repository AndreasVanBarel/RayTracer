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

## Example renders

For more examples, see the above mentioned report.



