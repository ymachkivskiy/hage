package org.hage.example;

import org.hage.platform.component.structure.connections.RelativePosition;

import java.util.Random;

public class MigrationCheckComponent {
    private final Random random = new Random();


    public boolean shouldPerformMigrationWithAge(int age) {
        return age % 3 == 0 && random.nextBoolean();
    }

    public RelativePosition randomRelativePosition() {
        return RelativePosition.values()[random.nextInt(RelativePosition.values().length)];
    }

}
