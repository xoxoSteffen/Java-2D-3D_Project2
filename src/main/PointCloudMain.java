package main;

import reconstruction.PointCloudApplication;

public class PointCloudMain {

    public static void main(String[] args) {
        try {
            PointCloudApplication app =
                    new PointCloudApplication(
                            "Point Cloud Viewer",
                            800,
                            600,
                            "xyz/bunny.xyz",
                            "shaders/Phong.vsh",
                            "shaders/Phong.fsh"
                    );

            app.mainLoop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
