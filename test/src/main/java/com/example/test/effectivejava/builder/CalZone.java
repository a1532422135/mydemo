package com.example.test.effectivejava.builder;

/**
 * @author Administrator
 */
public class CalZone extends Pizza {
    private final boolean sauceInside;

    public static class Builder extends Pizza.Builder<Builder> {
        private boolean sauceInside = false;

        public Builder(boolean sauceInside){
            this.sauceInside = sauceInside;
        }

        @Override
        public CalZone build() {
            return new CalZone(this);
        }

        @Override
        protected Builder self() {
            return this;
        }
    }

    private CalZone(Builder builder) {
        super(builder);
        sauceInside = builder.sauceInside;
    }
}
