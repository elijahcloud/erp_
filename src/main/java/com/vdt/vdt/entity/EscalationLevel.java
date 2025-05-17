package com.vdt.vdt.entity;


public enum EscalationLevel {
    LEVEL_1,
    LEVEL_2,
    LEVEL_3;

    public EscalationLevel next() {
        EscalationLevel[] levels = EscalationLevel.values();
        int ordinal = this.ordinal();
        if (ordinal < levels.length - 1) {
            return levels[ordinal + 1];
        } else {
            return this;
        }
    }

    }
