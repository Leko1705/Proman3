package proman.view;

import javax.swing.*;
import java.util.Objects;

public interface Tab {

    String getTitle();

    default Icon getIcon(){
        return null;
    }

    default boolean isCloseable(){
        return false;
    }


    static Tab simpleTab(String title){
        Objects.requireNonNull(title);
        return new Builder(title);
    }


    class Builder implements Tab {

        private final String title;
        private Icon icon = null;
        private boolean closeable = false;

        public Builder(String title) {
            this.title = Objects.requireNonNull(title);
        }

        @Override
        public String getTitle() {
            return title;
        }

        @Override
        public Icon getIcon() {
            return icon;
        }

        public Builder setIcon(Icon icon) {
            this.icon = icon;
            return this;
        }

        @Override
        public boolean isCloseable() {
            return closeable;
        }

        public Builder setCloseable(boolean closeable) {
            this.closeable = closeable;
            return this;
        }
    }

}
