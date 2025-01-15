package com.worldplay.utility.builds;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BuildManager {
    private static final CopyOnWriteArrayList<PlaceBuild> builds = new CopyOnWriteArrayList<>();



    public static void addPlaceBuild(PlaceBuild placeBuild) {
        builds.add(placeBuild);
        System.out.println(builds);
    }

    public static void tick() {
        try {
            // Ваш код здесь
            if (!builds.isEmpty()) {
                for(PlaceBuild build: builds) {
                    build.place();

                    if(build.isPlace()) {
                        builds.remove(build);
                    }
                }
            }
        } catch (Exception e) {
            // Логируем ошибку, но не даем задаче упасть
            e.printStackTrace();
            System.err.println("Ошибка в методе tick: " + e.getMessage());
        }
    }


    private static final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    public static Runnable safeRunnable(Runnable task) {
        return () -> {
            try {
                task.run();
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Ошибка в задаче: " + e.getMessage());
            }
        };
    }

    public static void start() {
        executor.scheduleAtFixedRate(safeRunnable(BuildManager::tick), 0, 5, TimeUnit.MILLISECONDS);
    }

}
