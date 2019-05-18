#include <stdio.h>
#include <gtk/gtk.h>
#include <glib.h>
#include <libgen.h>
#include <stdbool.h>
#include <wait.h>

#include "pam.h"
#include "display_manager.h"

#define ENTER_KEY 65293
#define ESC_KEY 65307
#define UI_FILE "gui.ui"
#define WINDOW_ID "window"
#define USERNAME_ID "username_text_entry"
#define PASSWORD_ID "password_text_entry"
#define STATUS_ID "status_label"

#define DISPLAY ":1"
#define VT "vt01"

static GtkEntry *user_text_field;
static GtkEntry *pass_text_field;
static GtkLabel *status_label;

static gboolean login_func(void *data) {
    GtkWidget *widget = GTK_WIDGET(data);
    const gchar *username = gtk_entry_get_text(user_text_field);
    const gchar *password = gtk_entry_get_text(pass_text_field);

    gtk_label_set_text(status_label, "Logging in...");
    while (gtk_events_pending()) gtk_main_iteration();
    pid_t child_pid;
    if (login(username, password, &child_pid)) {
        gtk_widget_hide(widget);
        while (gtk_events_pending()) gtk_main_iteration();

        // Wait for child process to finish (wait for logout)
        int status;
        waitpid(child_pid, &status, 0);
        gtk_widget_show(widget);
        while (gtk_events_pending()) gtk_main_iteration();

        gtk_label_set_text(status_label, "");
        while (gtk_events_pending()) gtk_main_iteration();

        logout();
    } else {
        gtk_label_set_text(status_label, "Login error");
        while (gtk_events_pending()) gtk_main_iteration();
    }
    gtk_entry_set_text(pass_text_field, "");
    while (gtk_events_pending()) gtk_main_iteration();

    return FALSE;
}

static gboolean key_event(GtkWidget *widget, GdkEventKey *event) {
    if (event->keyval == ENTER_KEY) {
        // ref. https://stackoverflow.com/questions/30607429/gtk3-and-multithreading-replacing-deprecated-functions
        // 'thread and mutex' approach looks good, but does not work (thread blocked)
        gdk_threads_add_idle(login_func, (void *) widget);
    } else if (event->keyval == ESC_KEY) {
        gtk_main_quit();
    }
    return FALSE;
}

static bool testing = false;

int main(int argc, char *argv[]) {
    const char *display = DISPLAY;
    const char *vt = VT;

    if (argc == 3) {
        display = argv[1];
        vt = argv[2];
    }
    if (!testing) {
        signal(SIGSEGV, sig_handler);
        signal(SIGTRAP, sig_handler);
        start_x_server(display, vt);
    }
    setenv("DISPLAY", display, true);

    gtk_init(&argc, &argv);

    char ui_file_path[256];
    if (readlink("/proc/self/exe", ui_file_path, sizeof(ui_file_path)) == -1) {
        printf("Error: could not get location of binary");
        exit(1);
    }

    dirname(ui_file_path);
    strcat(ui_file_path, "/" UI_FILE);

    GtkBuilder *builder = gtk_builder_new_from_file(ui_file_path);
    GtkWidget *window = GTK_WIDGET(gtk_builder_get_object(builder, WINDOW_ID));
    user_text_field = GTK_ENTRY(gtk_builder_get_object(builder, USERNAME_ID));
    pass_text_field = GTK_ENTRY(gtk_builder_get_object(builder, PASSWORD_ID));
    status_label = GTK_LABEL(gtk_builder_get_object(builder, STATUS_ID));

    // Make full screen
    GdkScreen *screen = gdk_screen_get_default();
    gint height = gdk_screen_get_height(screen);
    gint width = gdk_screen_get_width(screen);
    gtk_widget_set_size_request(GTK_WIDGET(window), width, height);
    gtk_widget_show(window);

    g_signal_connect(window, "key-release-event", G_CALLBACK(key_event), NULL);
    g_signal_connect(window, "destroy", G_CALLBACK(gtk_main_quit), NULL);
    gtk_main();

    stop_x_server();

    return 0;
}
