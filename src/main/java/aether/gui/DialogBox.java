package aether.gui;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;

/** Displays one user or Aether message as a chat bubble with a speaker badge. */
public class DialogBox extends HBox {
    private static final double MAX_MESSAGE_WIDTH = 390;

    private DialogBox(String text, String speaker, boolean isUser) {
        Label message = new Label(text);
        Label avatar = new Label(speaker);

        message.setWrapText(true);
        message.setMaxWidth(MAX_MESSAGE_WIDTH);
        HBox.setHgrow(message, Priority.SOMETIMES);

        getStyleClass().add("dialog-box");
        message.getStyleClass().add(isUser ? "user-message" : "aether-message");
        avatar.getStyleClass().add(isUser ? "user-avatar" : "aether-avatar");
        setAlignment(isUser ? Pos.TOP_RIGHT : Pos.TOP_LEFT);

        if (isUser) {
            getChildren().addAll(message, avatar);
        } else {
            getChildren().addAll(avatar, message);
        }
    }

    /**
     * Creates a right-aligned dialog box for a command entered by the user.
     *
     * @param text the command to display
     * @return a user dialog box
     */
    public static DialogBox getUserDialog(String text) {
        return new DialogBox(text, "YOU", true);
    }

    /**
     * Creates a left-aligned dialog box for Aether's response.
     *
     * @param text the response to display
     * @return an Aether dialog box
     */
    public static DialogBox getAetherDialog(String text) {
        return new DialogBox(text, "AE", false);
    }
}
