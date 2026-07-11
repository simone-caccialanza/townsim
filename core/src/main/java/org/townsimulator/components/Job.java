package org.townsimulator.components;

import jecs.core.ComponentBase;
import jecs.core.annotation.JecsComponent;
import org.annotationlib.annotations.LogField;


public final class Job {

    public enum JobType {
        DOCTOR,
        ENGINEER,
        PRINCIPLE,
        PROFESSOR,
        SOLDIER,
        BUSINESSMAN,
        ARCHITECT,
        CLEANER,
        FIREMAN,
        POLICEMAN,
        TEACHER,
    }

    @JecsComponent
    public static final class Component extends ComponentBase {
        @LogField
        public JobType jobType;

        public Component(JobType jobType) {
            this.jobType = jobType;
        }

        @Override
        public void reset() {
            jobType = null;
        }
    }
}
