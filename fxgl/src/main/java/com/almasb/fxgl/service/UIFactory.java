/*
 * The MIT License (MIT)
 *
 * FXGL - JavaFX Game Library
 *
 * Copyright (c) 2015-2017 AlmasB (almaslvl@gmail.com)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.almasb.fxgl.service;

import com.almasb.fxgl.animation.AnimatedPoint2D;
import com.almasb.fxgl.animation.AnimatedValue;
import com.almasb.fxgl.animation.Animation;
import com.almasb.fxgl.app.FXGL;
import com.almasb.fxgl.util.EmptyRunnable;
import javafx.animation.FadeTransition;
import javafx.collections.ObservableList;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

/**
 * Factory service for creating UI controls.
 * Used to unify the look across FXGL.
 *
 * @author Almas Baimagambetov (almaslvl@gmail.com)
 */
public interface UIFactory {

    /**
     * @param size font size
     * @return main UI font with given size
     */
    Font newFont(double size);

    default Text newText(String message) {
        return newText(message, Color.WHITE, 18);
    }

    default Text newText(String message, double fontSize) {
        return newText(message, Color.WHITE, fontSize);
    }

    default Text newText(String message, Color textColor, double fontSize) {
        Text text = new Text(message);
        text.setFill(textColor);
        text.setFont(newFont(fontSize));
        return text;
    }

    default void centerTextX(Text text, double minX, double maxX) {
        text.setTranslateX((minX + maxX) / 2 - text.getLayoutBounds().getWidth() / 2);
    }

    default void centerTextY(Text text, double minY, double maxY) {
        text.setTranslateY((minY + maxY) / 2 - text.getLayoutBounds().getHeight() / 2);
    }

    default void centerText(Text text) {
        centerText(text, FXGL.getAppWidth() / 2, FXGL.getAppHeight() / 2);
    }

    default void centerText(Text text, double x, double y) {
        text.setTranslateX(x - text.getLayoutBounds().getWidth() / 2);
        text.setTranslateY(y - text.getLayoutBounds().getHeight() / 2);
    }

    /**
     * Binds text to application center, i.e. text stays
     * centered regardless of content size.
     *
     * @param text UI object
     */
    default void centerTextBind(Text text) {
        centerTextBind(text, FXGL.getAppWidth() / 2, FXGL.getAppHeight() / 2);
    }

    /**
     * Binds text to given center point, i.e. text stays
     * centered regardless of content size.
     *
     * @param text UI object
     */
    default void centerTextBind(Text text, double x, double y) {
        text.layoutBoundsProperty().addListener((o, old, bounds) -> {
            text.setTranslateX(x - bounds.getWidth() / 2);
            text.setTranslateY(y - bounds.getHeight() / 2);
        });
    }

    Button newButton(String text);

    <T> ChoiceBox<T> newChoiceBox(ObservableList<T> items);

    <T> ChoiceBox<T> newChoiceBox();

    CheckBox newCheckBox();

    <T> Spinner<T> newSpinner(ObservableList<T> items);

    <T> ListView<T> newListView(ObservableList<T> items);

    <T> ListView<T> newListView();

//    default FadeTransition fadeIn(Node node, Duration duration) {
//        return fadeIn(node, duration, () -> {});
//    }
//
//    default FadeTransition fadeIn(Node node, Duration duration, Runnable onFinishedAction) {
//        FadeTransition ft = new FadeTransition(duration, node);
//        ft.setFromValue(0);
//        ft.setToValue(1);
//        ft.setOnFinished(e -> onFinishedAction.run());
//        ft.play();
//        return ft;
//    }

    default FadeTransition fadeOut(Node node, Duration duration) {
        return fadeOut(node, duration, () -> {});
    }

    default FadeTransition fadeOut(Node node, Duration duration, Runnable onFinishedAction) {
        FadeTransition ft = new FadeTransition(duration, node);
        ft.setFromValue(1);
        ft.setToValue(0);
        ft.setOnFinished(e -> onFinishedAction.run());
        ft.play();
        return ft;
    }

    default FadeTransition fadeInOut(Node node, Duration duration) {
        return fadeInOut(node, duration, () -> {});
    }

    default FadeTransition fadeInOut(Node node, Duration duration, Runnable onFinishedAction) {
        FadeTransition ft = new FadeTransition(duration, node);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.setCycleCount(2);
        ft.setAutoReverse(true);
        ft.setOnFinished(e -> onFinishedAction.run());
        ft.play();
        return ft;
    }

    default FadeTransition fadeOutIn(Node node, Duration duration) {
        return fadeOutIn(node, duration, () -> {});
    }

    default FadeTransition fadeOutIn(Node node, Duration duration, Runnable onFinishedAction) {
        FadeTransition ft = new FadeTransition(duration, node);
        ft.setFromValue(1);
        ft.setToValue(0);
        ft.setCycleCount(2);
        ft.setAutoReverse(true);
        ft.setOnFinished(e -> onFinishedAction.run());
        ft.play();
        return ft;
    }

    default Animation<?> translate(Node node, Point2D from, Point2D to, Duration duration) {
        return translate(node, from, to, Duration.ZERO, duration);
    }

    default Animation<?> translate(Node node, Point2D from, Point2D to, Duration delay, Duration duration) {
        return new Animation<Point2D>(delay, duration, 1, new AnimatedPoint2D(from, to)) {

            @Override
            public void onProgress(Point2D value) {
                node.setTranslateX(value.getX());
                node.setTranslateY(value.getY());
            }
        };
    }

    default Animation<?> fadeIn(Node node, Duration duration) {
        return fadeIn(node, duration, EmptyRunnable.INSTANCE);
    }

    default Animation<?> fadeIn(Node node, Duration duration, Runnable onFinishedAction) {
        return fadeIn(node, Duration.ZERO, duration, onFinishedAction);
    }

    default Animation<?> fadeIn(Node node, Duration delay, Duration duration) {
        return fadeIn(node, delay, duration, EmptyRunnable.INSTANCE);
    }

    default Animation<?> fadeIn(Node node, Duration delay, Duration duration, Runnable onFinishedAction) {
        Animation<?> anim = new Animation<Double>(delay, duration, 1, new AnimatedValue<>(0.0, 1.0)) {
            @Override
            public void onProgress(Double value) {
                node.setOpacity(value);
            }
        };
        anim.setOnFinished(onFinishedAction);
        return anim;
    }
}
