package proman.view;

import proman.filesys.Resources;

import javax.swing.*;

public interface AllIcons {

    interface Navigation {
        Icon threeDotsVertical = Resources.loadResourceIcon("three-dots-vertical.png");
        Icon threeDotsHorizontal = Resources.loadResourceIcon("three-dots-horizontal.png");
        Icon menuArrowDown = Resources.loadResourceIcon("menu-arrow-down.png");
        Icon menuArrowRight = Resources.loadResourceIcon("menu-arrow-right.png");
        Icon menu = Resources.loadResourceIcon("menu.png");
    }

    interface ContextAction {
        Icon warning = Resources.loadResourceIcon("warning.png");
        Icon error = Resources.loadResourceIcon("error.png");
    }

}
