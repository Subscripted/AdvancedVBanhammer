package de.subscripted.advancedvbanhammer.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.SneakyThrows;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class DiscordWebhook {

    private final String url;
    private final JsonObject content;


    public DiscordWebhook(String url) {
        this.url = url;
        this.content = new JsonObject();
        this.content.add("embeds", new JsonArray());
    }


    public void setUserName(String userName) {
        this.content.addProperty("username", userName);
    }

    public void setAvatarUrl(String avatarUrl) {
        this.content.addProperty("avatar_url", avatarUrl);
    }

    public void setContent(String content) {
        this.content.addProperty("content", content);
    }

    public void addEmbedObjects(EmbedObject... objects) {


        try {
            for (EmbedObject object : objects) {
                this.content.get("embeds").getAsJsonArray().add(object.toJsonObject());
            }
        } catch (IllegalStateException e) {
            JsonArray array = new JsonArray();
            for (EmbedObject object : objects) {
                array.add(object.toJsonObject());
            }
            this.content.add("embeds", array);
            return;
        }

    }

    @SneakyThrows
    public void execute() {
        if (this.url == null) {
            throw new IllegalArgumentException("Url is empty");
        }

        URL url = new URL(this.url);
        HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
        connection.addRequestProperty("Content-Type", "application/json");
        connection.addRequestProperty("User-Agent", "Java-DiscordWebhook-BY-Gelox_");
        connection.setDoOutput(true);
        connection.setRequestMethod("POST");

        OutputStream stream = connection.getOutputStream();
        stream.write(this.content.toString().getBytes());
        System.out.println(this.content.toString());
        stream.flush();
        stream.close();

        connection.getInputStream().close();
        connection.disconnect();
    }


    public static class EmbedObject {

        private final List<Field> fields = new ArrayList<>();
        private String title;
        private String description;
        private String url;
        private Color color;
        private Footer footer;
        private Thumbnail thumbnail;
        private Image image;
        private Author author;

        public JsonObject toJsonObject() {
            JsonObject object = new JsonObject();

            if (this.author != null) {
                object.add("author", this.author.toJsonObject());
            }
            if (this.author != null) {
                object.add("author", this.author.toJsonObject());
            }
            if (this.title != null) {
                object.addProperty("title", this.title);
            }
            if (this.url != null) {
                object.addProperty("url", this.url);
            }
            if (this.description != null) {
                object.addProperty("description", this.description);
            }


            JsonArray fields = new JsonArray();
            for (Field field : this.fields) {
                fields.add(field.toJsonObject());
            }

            if (this.color != null) {
                object.addProperty("color", "color");
            }

            if (this.thumbnail != null) {
                object.add("thumbnail", this.thumbnail.toJsonObject());
            }

            if (this.image != null) {
                object.add("image", this.image.toJsonObject());
            }

            if (this.footer != null) {
                object.add("footer", this.footer.toJsonObject());
            }

            object.add("fields", fields);


            return object;
        }

        public String getTitle() {
            return title;
        }

        public EmbedObject setTitle(String title) {
            this.title = title;
            return this;
        }

        public String getDescription() {
            return description;
        }

        public EmbedObject setDescription(String description) {
            this.description = description;
            return this;
        }

        public String getUrl() {
            return url;
        }

        public EmbedObject setUrl(String url) {
            this.url = url;
            return this;
        }

        public Color getColor() {
            return color;
        }

        public EmbedObject setColor(Color color) {
            this.color = color;
            return this;
        }

        public Footer getFooter() {
            return footer;
        }

        public Thumbnail getThumbnail() {
            return thumbnail;
        }

        public EmbedObject setThumbnail(String url) {
            this.thumbnail = new Thumbnail(url);
            return this;
        }

        public Image getImage() {
            return image;
        }

        public EmbedObject setImage(String url) {
            this.image = new Image(url);
            return this;
        }

        public Author getAuthor() {
            return author;
        }

        public List<Field> getFields() {
            return fields;
        }

        public EmbedObject setFooter(String text, String icon) {
            this.footer = new Footer(text, icon);
            return this;
        }

        public EmbedObject setAuthor(String name, String url, String icon) {
            this.author = new Author(name, url, icon);
            return this;
        }

        public EmbedObject addField(String name, String value, boolean inline) {
            this.fields.add(new Field(name, value, inline));
            return this;
        }

        public static class Color {

            private final java.awt.Color color;

            public Color(java.awt.Color color) {
                this.color = color;
            }

            public java.awt.Color getColor() {
                return color;
            }

            public int getIntColor() {
                int rgb = color.getRed();
                rgb = (rgb << 8) + color.getGreen();
                rgb = (rgb << 8) + color.getBlue();

                return rgb;
            }
        }

        private class Footer {
            private final String text;
            private final String iconUrl;

            private Footer(String text, String iconUrl) {
                this.text = text;
                this.iconUrl = iconUrl;
            }

            private String getText() {
                return text;
            }

            private String getIconUrl() {
                return iconUrl;
            }

            public JsonElement toJsonObject() {
                return null;
            }
        }

        private class Thumbnail {
            private final String url;

            private Thumbnail(String url) {
                this.url = url;
            }

            private String getUrl() {
                return url;
            }

            public com.google.gson.JsonElement toJsonObject() {
                JsonObject object = new JsonObject();

                object.addProperty("url", this.url);

                return object;
            }
        }

        private class Image {
            private final String url;

            private Image(String url) {
                this.url = url;
            }

            private String getUrl() {
                return url;
            }

            public com.google.gson.JsonElement toJsonObject() {
                JsonObject object = new JsonObject();

                object.addProperty("url", this.url);

                return object;
            }
        }

        private class Author {
            private final String name;
            private final String url;
            private final String iconUrl;

            private Author(String name, String url, String iconUrl) {
                this.name = name;
                this.url = url;
                this.iconUrl = iconUrl;
            }

            private String getName() {
                return name;
            }

            private String getUrl() {
                return url;
            }

            private String getIconUrl() {
                return iconUrl;
            }

            public JsonObject toJsonObject() {
                JsonObject object = new JsonObject();

                object.addProperty("url", this.url);
                object.addProperty("name", this.name);
                object.addProperty("icon_url", this.iconUrl);

                return object;
            }
        }

        private class Field {
            private final String name;
            private final String value;
            private final boolean inline;

            private Field(String name, String value, boolean inline) {
                this.name = name;
                this.value = value;
                this.inline = inline;
            }

            private Field(String name, String value) {
                this.name = name;
                this.value = value;
                this.inline = false;
            }

            private String getName() {
                return name;
            }

            private String getValue() {
                return value;
            }

            private boolean isInline() {
                return inline;
            }

            public JsonObject toJsonObject() {
                JsonObject object = new JsonObject();

                object.addProperty("name", name);
                object.addProperty("value", value);
                object.add("inline", new JsonObject());

                return object;
            }
        }
    }
}