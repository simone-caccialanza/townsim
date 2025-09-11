package org.townsimulator.components;

import jecs.core.ComponentBase;
import jecs.core.annotation.JecsComponent;
import org.annotationlib.annotations.LogField;

import static org.townsimulator.utils.Constants.JOB_COMPONENT_TABLE_INDEX;

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

        @SuppressWarnings("unchecked")
        @Override
        public Component castToChild(ComponentBase componentBase) {
            return (Component) componentBase;
        }

        public static int tableIndex() {
            return JOB_COMPONENT_TABLE_INDEX;
        }

        @Override
        public int compareTo(ComponentBase o) {
            return Component.class.getName().compareTo(o.castToChild(o).getClass().getName());
        }
    }
}
